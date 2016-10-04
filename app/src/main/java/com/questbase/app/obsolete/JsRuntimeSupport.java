package com.questbase.app.obsolete;

import com.annimon.stream.Stream;
import com.questbase.app.controller.files.FilesController;

import org.mozilla.javascript.Context;
import org.mozilla.javascript.Function;
import org.mozilla.javascript.Scriptable;
import org.mozilla.javascript.ScriptableObject;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStreamReader;

class JsRuntimeSupport extends ScriptableObject {
    private static final long serialVersionUID = 1L;
    private static final String ASSETS_PREFIX = "assets:";
    private final FilesController filesController;

    JsRuntimeSupport(FilesController filesController) {
        this.filesController = filesController;
    }

    @Override
    public String getClassName() {
        return JsRuntimeSupport.class.getSimpleName();
    }

    public static void print(Context cx, Scriptable thisObj, Object[] args,
                             Function funObj) {
        Stream.of(args).forEach(arg -> System.out.print(Context.toString(arg) + " "));
    }

    public static void load(Context cx, Scriptable thisObj, Object[] args,
                            Function funObj) {
        JsRuntimeSupport shell = (JsRuntimeSupport) getTopLevelScope(thisObj);
        Stream.of(args).forEach(arg -> shell.processSource(cx, Context.toString(arg)));
    }

    private void processSource(Context cx, String filename) {
        try {
            if (filename.startsWith("./form/")) {
                filename = filename.substring("./form/".length());
                String[] splitPath = filename.split("/", 2);
                int id = Integer.valueOf(splitPath[0]);
                String formDir = splitPath[1];
                filename = new File(filesController.getFormDirectory(id), formDir).getAbsolutePath();
            }
            if (filename.startsWith(ASSETS_PREFIX)) {
                cx.evaluateReader(this, new InputStreamReader(filesController.openFileByName(
                        filename.substring(ASSETS_PREFIX.length()))), filename, 1, null);
            } else {
                cx.evaluateReader(this, new InputStreamReader(
                        new FileInputStream(filename)), filename, 1, null);
            }
        } catch (Exception ex) {
            throw new RuntimeException("Failed to load: " + filename);
        }
    }
}