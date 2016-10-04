package com.questbase.app.di.modules;

import com.annimon.stream.Optional;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.questbase.app.CommonUtils;
import com.questbase.app.dao.auth.AuthDao;
import com.questbase.app.net.RestApi;
import com.questbase.app.utils.Auth;
import com.questbase.app.utils.AuthUtils;

import java.nio.charset.Charset;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;
import okhttp3.HttpUrl;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.logging.HttpLoggingInterceptor;
import okio.Buffer;
import okio.BufferedSink;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava.RxJavaCallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

@Module
public class RestModule {

    public static final String BASE_URL = CommonUtils.HOST_PREFIX + "/";
    private static final String DATE_FORMAT = "yyyy-MM-dd'T'HH:mm:ssZ";

    @Singleton
    @Provides
    RestApi provideRestApi(AuthDao authDao) {
        HttpLoggingInterceptor httpLogger = new HttpLoggingInterceptor();
        httpLogger.setLevel(HttpLoggingInterceptor.Level.BODY);
        OkHttpClient client = new OkHttpClient.Builder()
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
                    Optional<Auth> auth = authDao.getAuth();
                    if (!auth.isPresent()) {
                        throw new RuntimeException("User must be authorized");
                    }

                    String dataToSign = "/" + request.url().toString().replace(BASE_URL, "") + postData;
                    String signature = AuthUtils.packSignature(AuthUtils.calculateSignature(dataToSign, auth.get().token));

                    HttpUrl url = request.url().newBuilder()
                            .addQueryParameter("tid", auth.get().tokenId)
                            .addQueryParameter("sgn", signature).build();

                    if (request.method().equals("POST")) {
                        request = request.newBuilder()
                                .url(url)
                                .addHeader("Content-Type", "application/json")
                                .post(RequestBody.create(MediaType.parse("application/json"), postData.getBytes()))
                                .build();
                    } else if (request.method().equals("GET")) {
                        request = request.newBuilder()
                                .url(url)
                                .addHeader("Content-Type", "application/json")
                                .get()
                                .build();
                    }
                    return chain.proceed(request);
                })
                .build();

        Gson gson = new GsonBuilder()
                .setDateFormat(DATE_FORMAT)
                .create();

        return new Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create(gson))
                .addCallAdapterFactory(RxJavaCallAdapterFactory.create())
                .client(client)
                .build().create(RestApi.class);
    }
}