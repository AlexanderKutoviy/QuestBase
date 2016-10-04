package com.questbase.servertests;

import retrofit2.http.GET;
import retrofit2.http.Query;
import rx.Observable;

public interface TestApi {
    @GET("device/test/set-balance")
    Observable<String> setBalance(@Query("newBalance") double newBalance);

    @GET("device/test/ban-payouts")
    Observable<String> banPayouts();
}
