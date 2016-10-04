package com.questbase.servertests;

import com.annimon.stream.Optional;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.questbase.app.BuildConfig;
import com.questbase.app.domain.ProfileResponse;
import com.questbase.app.net.RestApi;
import com.questbase.app.net.body.PayoutRequest;
import com.questbase.app.net.entity.PersonalResult;
import com.questbase.app.net.entity.transactions.TransactionEventsContainerDto;
import com.questbase.app.net.entity.transactions.TransactionsResponseDto;
import com.questbase.app.net.objects.PayoutResponse;
import com.questbase.app.utils.Auth;
import com.questbase.app.utils.AuthUtils;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
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

import static org.junit.Assert.*;

@RunWith(RobolectricGradleTestRunner.class)
@Config(constants = BuildConfig.class, sdk = 19, manifest = "src/main/AndroidManifest.xml")
public class ServerTest {

    RestApi api;
    TestApi testApi;
    private final String PHONE_NUMBER = "380978213323";
    private final String TARGET = "phone";
    private final String PAYOUT_AMOUNT = "50";

    @Before
    public void setup() throws Exception {
        String BASE_URL = "https://therespo.com/";
//        String BASE_URL = "https://note.therespo.dev/";
//        String BASE_URL = "https://dev.therespo.com/";
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

        testApi = new Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create(gson))
                .addCallAdapterFactory(RxJavaCallAdapterFactory.create())
                .client(client)
                .build().create(TestApi.class);
    }

    @Test
    public void payoutFailTest() {
        TransactionsResponseDto before = api.getTransactions().toBlocking().first();

        testApi.setBalance(0).toBlocking().first();
        PayoutResponse res = api.performPayout(new PayoutRequest(PHONE_NUMBER, TARGET, PAYOUT_AMOUNT)).toBlocking().first();
        assertNotNull(res.error);
        assertNull(res.success);

        TransactionsResponseDto after = api.getTransactions().toBlocking().first();

        assertEquals(before.events, after.events.subList(1, after.events.size()));
    }

    @Test
    public void payoutOkTest() {
        TransactionsResponseDto beforePayout = api.getTransactions().toBlocking().first();

        testApi.setBalance(60).toBlocking().first();
        PayoutResponse res = api.performPayout(new PayoutRequest(PHONE_NUMBER, TARGET, PAYOUT_AMOUNT)).toBlocking().first();
        assertNull(res.error);
        assertNotNull(res.success);

        TransactionsResponseDto afterPayout = api.getTransactions().toBlocking().first();
        assertEquals(beforePayout, afterPayout.events.subList(1, afterPayout.events.size()));

        TransactionEventsContainerDto last = afterPayout.events.get(0);
        assertEquals("Confirmed", last.event.state);

        testApi.banPayouts().toBlocking().first();

        TransactionsResponseDto after2 = api.getTransactions().toBlocking().first();
        TransactionEventsContainerDto last2 = after2.events.get(0);
        assertEquals("PayError", last2.event.state);
    }

    @Test
    public void balanceTest() {
        {
            testApi.setBalance(0).toBlocking().first();
            ProfileResponse profile = api.getProfile().toBlocking().first();
            assertEquals(0.0, profile.balance, 0.00001);
        }

        {
            testApi.setBalance(5.0).toBlocking().first();
            ProfileResponse profile = api.getProfile().toBlocking().first();
            assertEquals(5.0, profile.balance, 0.00001);
        }
    }

    @Test
    public void statisticsRestApiManualTest() {
        PersonalResult personalResult = api.getRespondentsChartData().toBlocking().first();
        System.out.println(personalResult);
    }
}
