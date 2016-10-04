package com.questbase.app.obsolete;

import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.JsonParser;
import com.questbase.app.controller.files.FilesController;

import org.mozilla.javascript.Context;
import org.mozilla.javascript.Function;
import org.mozilla.javascript.ImporterTopLevel;
import org.mozilla.javascript.NativeJSON;
import org.mozilla.javascript.NativeObject;
import org.mozilla.javascript.Scriptable;
import org.mozilla.javascript.ScriptableObject;

import java.io.File;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import rx.Observable;
import rx.Scheduler;
import rx.schedulers.Schedulers;
import rx.subjects.ReplaySubject;

public class ScriptManager {

    private static final String RHINO_RUNNER_THREAD_GROUP = "RhinoRunnerThreadGroup";
    private static final String RHINO_RUNNER_THREAD = "RhinoRunnerThread";
    private static final String ARGUMENTS = "arguments";
    private static final long THREAD_STACK_SIZE = 1024 * 1024;
    private Context rhino;
    private ScriptableObject sharedScope;
    private Gson gson = new Gson();
    private Function loadFunction;

    private ExecutorService executor = Executors.newSingleThreadExecutor(runnable -> {
        ThreadGroup group = new ThreadGroup(RHINO_RUNNER_THREAD_GROUP);
        return new Thread(group, runnable, RHINO_RUNNER_THREAD, THREAD_STACK_SIZE);
    });

    private Scheduler scheduler = Schedulers.from(executor);

    private enum FunctionName {
        PRINT("print"),
        LOAD("load"),
        REQUIRE("require"),
        SET_STATE("setState"),
        GET_STATE("getState"),
        SET_ANSWER("setAnswer"),
        GET_ANSWER("getAnswer"),
        NEXT_QUESTION("nextQuestion"),
        LAST_ANSWERED_QUESTION("lastAnsweredQuestion"),
        ANALYSE("analyse"),
        PROGRESS("getProgress");

        String name;

        FunctionName(String name) {
            this.name = name;
        }
    }

    private enum FunctionTag {
        LOADER("loader"),
        STATE("state"),
        ANSWER("answer");

        String name;

        FunctionTag(String name) {
            this.name = name;
        }
    }

    public ScriptManager(final FilesController filesController) {
        init(filesController);
    }

    private Observable<Void> init(final FilesController filesController) {
        ReplaySubject<Void> result = ReplaySubject.create();
        executor.execute(() -> {
            try {
                rhino = Context.enter();
                // turn off optimization to make Rhino Android compatible
                rhino.setOptimizationLevel(-1);
                String requireJsSource = filesController.loadFileByPath("js/libs/r.js");
                // add browser support and importer objects
                final JsRuntimeSupport browserSupport = new JsRuntimeSupport(filesController);
                sharedScope = rhino.initStandardObjects(browserSupport, true);
                final ImporterTopLevel importer = new ImporterTopLevel(rhino);
                rhino.initStandardObjects(importer);

                // inject custom functions to js context
                String[] names = {FunctionName.PRINT.name, FunctionName.LOAD.name};
                sharedScope.defineFunctionProperties(names, sharedScope.getClass(), ScriptableObject.DONTENUM);
                Scriptable argsObj = rhino.newArray(sharedScope, new Object[]{});
                sharedScope.defineProperty(ARGUMENTS, argsObj, ScriptableObject.DONTENUM);

                // include requireJs module
                rhino.evaluateString(sharedScope, requireJsSource, FunctionName.REQUIRE.name, 1, null);
                result.onNext(null);
                result.onCompleted();
            } catch (Throwable e) {
                result.onError(e);
                result.onCompleted();
                throw new RuntimeException("Execution error while init ScriptManager " + e);
            }
        });
        return result;
    }

    /**
     * DbForm is loaded not directly, but using a kind of 'loader'
     */
    public Observable<Function> loadForm(FilesController filesController, final int id, String libVersion) {
        ReplaySubject<Function> loadedForm = ReplaySubject.create();
        executor.execute(() -> {
            try {
                // dbForm home directory
                File formDir = filesController.getFormDirectory(id);
                if (loadFunction == null) {
                    // source code of loader, customized to load the dbForm we need
                    String loaderJsSource = String.format(filesController.loadFileByPath("js/loader_pattern.js"),
                            id, formDir.getAbsoluteFile().toString(), filesController.getLibPath(libVersion));
                    // ok, do load the dbForm by evaluating the loader
                    loadFunction = (Function) rhino.evaluateString(sharedScope, loaderJsSource, FunctionTag.LOADER.name, 1, null);
                }
                String path = filesController.getLibPath(libVersion);
                Function modelFactory = (Function) loadFunction.call(rhino, sharedScope, null,
                        new Object[]{id, formDir.getAbsoluteFile().toString(), path});
                loadedForm.onNext(modelFactory);
                loadedForm.onCompleted();
            } catch (Throwable e) {
                loadedForm.onError(e);
                loadedForm.onCompleted();
                throw new RuntimeException("Execution error while loaded form " + id + " " + e);
            }
        });
        return loadedForm;
    }

    public Observable<Model> getModel(final FilesController filesController, int formId, String libVersion) {
        return loadForm(filesController, formId, libVersion).observeOn(scheduler).map(
                f -> new Model((NativeObject) f.call(rhino, sharedScope, null, new Object[]{})));
    }

    public class Model {
        NativeObject obj;

        Model(NativeObject obj) {
            this.obj = obj;
        }

        public Observable<Model> setState(String state) {
            return Observable.from(new Object[]{1})
                    .observeOn(scheduler)
                    .flatMap(v -> runModelFunction(obj, FunctionName.SET_STATE.name,
                            new Object[]{rhino.evaluateString(sharedScope, "(" + state + ")", FunctionTag.STATE.name, 1, null)}
                    ))
                    .map(res -> Model.this);
        }

        public Observable<Model> setAnswer(String questionId, JsonElement ans) {
            return Observable.from(new Object[]{1})
                    .observeOn(scheduler)
                    .flatMap(v -> runModelFunction(obj, FunctionName.SET_ANSWER.name, new Object[]{
                            questionId, rhino.evaluateString(sharedScope, "(" + gson.toJson(ans) + ")", FunctionTag.ANSWER.name, 1, null)
                    }))
                    .map(res -> Model.this);
        }

        public Observable<String> getState() {
            return runModelFunction(obj, FunctionName.GET_STATE.name)
                    .map(ScriptManager.this::stringify);
        }

        public Observable<JsonElement> getAnswer(String questionId) {
            return runModelFunction(obj, FunctionName.GET_ANSWER.name, new Object[]{questionId})
                    .map(res -> new JsonParser().parse((stringify(res))));
        }

        public Observable<JsonElement> getNextQuestion() {
            return runModelFunction(obj, FunctionName.NEXT_QUESTION.name)
                    .map(res -> new JsonParser().parse((stringify(res))));
        }

        public Observable<JsonElement> getLastAnsweredQuestion() {
            return runModelFunction(obj, FunctionName.LAST_ANSWERED_QUESTION.name)
                    .map(res -> new JsonParser().parse((stringify(res))));
        }

        public Observable<JsonElement> analyse() { //TODO: RESPO-658 Fix method analyse()
            return runModelFunction(obj, FunctionName.ANALYSE.name)
                    .map(res -> new JsonParser().parse((stringify(res))));
        }

        public Observable<Double> getProgress() {
            return runModelFunction(obj, FunctionName.PROGRESS.name)
                    .map(res -> (Double) res);
        }
    }

    private Observable<Object> runModelFunction(NativeObject nativeObject, String func) {
        return runModelFunction(nativeObject, func, new Object[]{});
    }

    private Observable<Object> runModelFunction(NativeObject nativeObject, String functionName, Object[] params) {
        ReplaySubject<Object> result = ReplaySubject.create();
        executor.execute(() -> {
            try {
                Object funcResult = ((Function) nativeObject.getPrototype().get(functionName, nativeObject)).call(rhino, sharedScope, nativeObject, params);
                result.onNext(funcResult);
                result.onCompleted();
            } catch (Throwable e) {
                result.onError(e);
                result.onCompleted();
                throw new RuntimeException(e);
            }
        });
        return result.observeOn(scheduler);
    }

    private String stringify(Object value) {
        if (value instanceof String) {
            return (String) value;
        } else {
            return NativeJSON.stringify(rhino, sharedScope, value, null, null).toString();
        }
    }
}
