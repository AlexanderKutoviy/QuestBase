package com.questbase.unittestssuite;

import android.app.Activity;

import com.annimon.stream.Optional;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonPrimitive;
import com.questbase.app.BuildConfig;
import com.questbase.app.controller.files.DefaultFilesController;
import com.questbase.app.controller.files.FilesController;
import com.questbase.app.net.RestApi;
import com.questbase.app.obsolete.ScriptManager;
import com.questbase.app.utils.Auth;
import com.questbase.app.utils.AuthUtils;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.Robolectric;
import org.robolectric.RobolectricGradleTestRunner;
import org.robolectric.annotation.Config;

import java.nio.charset.Charset;
import java.security.SecureRandom;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;

import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.SSLContext;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;

import okhttp3.HttpUrl;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;
import okhttp3.logging.HttpLoggingInterceptor;
import okio.Buffer;
import okio.BufferedSink;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava.RxJavaCallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

@RunWith(RobolectricGradleTestRunner.class)
@Config(constants = BuildConfig.class, sdk = 19, manifest = "src/main/AndroidManifest.xml")
public class RhinoTest {

    private RestApi api;
    private String libVersion = "582a669e03bd4384ad593651e0d0d01dc3297d00";

    @Before
    public void setup() throws Exception {
        String BASE_URL = "https://dev.therespo.com/";
        Optional<Auth> auth = Optional.of(new Auth("10", "this-is-really-test-token--trust-me-on-this", "129"));
        HttpLoggingInterceptor httpLogger = new HttpLoggingInterceptor();
        httpLogger.setLevel(HttpLoggingInterceptor.Level.BODY);

        // DO NOT USE TrustAll IN PRODUCTION. FOR TEST PURPOSES ONLY
        final TrustManager[] trustAllCerts = new TrustManager[]{
                new X509TrustManager() {
                    @Override
                    public void checkClientTrusted(java.security.cert.X509Certificate[] chain, String authType) throws CertificateException {
                    }

                    @Override
                    public void checkServerTrusted(java.security.cert.X509Certificate[] chain, String authType) throws CertificateException {
                    }

                    @Override
                    public java.security.cert.X509Certificate[] getAcceptedIssuers() {
                        return new X509Certificate[]{};
                    }
                }
        };
        SSLContext sslContext = SSLContext.getInstance("TLS");
        sslContext.init(null, trustAllCerts, new SecureRandom());

        // DO NOT USE TrustAll IN PRODUCTION. FOR TEST PURPOSES ONLY
        HostnameVerifier hostnameVerifier = (hostname, session) -> true;

        OkHttpClient client = new OkHttpClient.Builder()
                .sslSocketFactory(sslContext.getSocketFactory())
                .hostnameVerifier(hostnameVerifier)
                .addInterceptor(httpLogger)
                .addInterceptor(chain -> {
                    Request request = chain.request();
                    BufferedSink buf = new Buffer();
                    if (request.body() != null) {
                        request.body().writeTo(buf);
                    }
                    String postData = buf.buffer().readString(Charset.forName("UTF-8"));
                    if ("".equals(postData)) {
                        postData = "{}";
                    }
                    if (!auth.isPresent()) {
                        throw new RuntimeException("User must be authorized");
                    }

                    String dataToSign = "/" + request.url().toString().replace(BASE_URL, "") + postData;
                    String signature = AuthUtils.packSignature(AuthUtils.calculateSignature(dataToSign, auth.get().token));

                    if (request.method().equals("POST")) {
                        HttpUrl url = request.url().newBuilder()
                                .addQueryParameter("tid", auth.get().tokenId)
                                .addQueryParameter("sgn", signature).build();
                        request = request.newBuilder()
                                .url(url)
                                .addHeader("Content-Type", "application/json")
                                .post(RequestBody.create(MediaType.parse("application/json"), postData.getBytes()))
                                .build();
                    }
                    System.out.println("[REQ] " + request + "\n> " + postData);
                    Response response = chain.proceed(request);
                    System.out.println("[RES] \n> " + response.peekBody(999999).string());
                    return response;
                })
                .build();

        Gson gson = new GsonBuilder()
                .setDateFormat("yyyy-MM-dd HH:mm:ss Z")
                .create();

        api = new Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create(gson))
                .addCallAdapterFactory(RxJavaCallAdapterFactory.create())
                .client(client)
                .build().create(RestApi.class);
    }

    @Test
    public void testSetAndGetAnswer() throws Throwable {
        FilesController filesController = new DefaultFilesController(Robolectric.buildActivity(Activity.class).create().get(), api);
        ScriptManager scriptManager = new ScriptManager(filesController);
        initManager(scriptManager, filesController);
        Gson gson = new Gson();
        String state = "{}";
        ScriptManager.Model model = createModel(scriptManager, filesController, 217, state);
        model.setAnswer("q-0", new JsonPrimitive("answer1")).toBlocking().first();
        state = model.getState().toBlocking().first();

        Assert.assertEquals("\"answer1\"", gson.toJson(createModel(scriptManager, filesController, 217, state).getAnswer("q-0").toBlocking().first()));
    }


    @Test
    public void testSetAndGetState() {
        FilesController filesController = new DefaultFilesController(Robolectric.buildActivity(Activity.class).create().get(), api);
        ScriptManager scriptManager = new ScriptManager(filesController);
        initManager(scriptManager, filesController);
        String state = "{}";
        ScriptManager.Model model = scriptManager.getModel(filesController, 217, libVersion).toBlocking().first();
        model.setState(state).toBlocking().first();
        state = model.getState().toBlocking().first();
        Assert.assertEquals("{\"answers\":{},\"pastQuestions\":[]}", state);
    }

    @Test
    public void testDifferentForm() {
        FilesController filesController = new DefaultFilesController(Robolectric.buildActivity(Activity.class).create().get(), api);
        ScriptManager scriptManager = new ScriptManager(filesController);
        ScriptManager.Model firstTestModel = createModel(scriptManager, filesController, 215);
        ScriptManager.Model secondTestModel = createModel(scriptManager, filesController, 216);
        Assert.assertNotEquals(firstTestModel.getNextQuestion().toBlocking().first().toString(), secondTestModel.getNextQuestion().toBlocking().first().toString());
    }

    private void initManager(ScriptManager scriptManager, FilesController filesController) {
        scriptManager.loadForm(filesController, 217, libVersion).toBlocking().first();
        scriptManager.loadForm(filesController, 216, libVersion).toBlocking().first();
        scriptManager.loadForm(filesController, 215, libVersion).toBlocking().first();
        scriptManager.loadForm(filesController, 217, libVersion).toBlocking().first();
        scriptManager.loadForm(filesController, 217, libVersion).toBlocking().first();
    }

    private ScriptManager.Model createModel(ScriptManager scriptManager, FilesController filesController, int formId, String state) {
        ScriptManager.Model model = scriptManager.getModel(filesController, 217, libVersion).toBlocking().first();
        model.setState(state).toBlocking().first();
        return model;
    }

    private ScriptManager.Model createModel(ScriptManager scriptManager, FilesController filesController, int formId) {
        return scriptManager.getModel(filesController, formId, libVersion).toBlocking().first();
    }
}
