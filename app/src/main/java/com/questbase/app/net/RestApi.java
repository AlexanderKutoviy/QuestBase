package com.questbase.app.net;

import com.google.gson.JsonObject;
import com.questbase.app.domain.Form;
import com.questbase.app.domain.FormResource;
import com.questbase.app.domain.ProfileResponse;
import com.questbase.app.net.body.FormPostResponse;
import com.questbase.app.net.body.PhoneNumberRequest;
import com.questbase.app.net.body.PayoutRequest;
import com.questbase.app.net.body.ResourceRequest;
import com.questbase.app.net.body.TestSessionRequestDto;
import com.questbase.app.net.entity.Category;
import com.questbase.app.net.entity.PersonalResult;
import com.questbase.app.net.entity.VersionedForm;
import com.questbase.app.net.entity.statistics.PersonalStatsContainer;
import com.questbase.app.net.entity.transactions.TransactionsResponseDto;
import com.questbase.app.net.objects.PayoutResponse;
import com.questbase.app.net.objects.PhoneAddingResponse;
import com.questbase.app.net.objects.TestSessionResponseDto;

import java.util.List;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Url;
import rx.Observable;

public interface RestApi {

    @POST("device/v1/profile/get")
    Observable<ProfileResponse> getProfile();

    @POST("device/v1/payout/payout-history")
    Observable<TransactionsResponseDto> getTransactions();

    @POST("device/v1/form/get-categories")
    Observable<List<Category>> getCategories();

    @POST("device/v1/form/list-by-category")
    Observable<List<VersionedForm>> getVersionedForms(@Body Category category);

    @POST("device/v1/form/get-form")
    Observable<Form> getFormDescriptor(@Body Form form);

    @POST("device/v1/dbForm/resources")
    Call<ResponseBody> getResources(@Body FormResource formResource);

    @POST("device/v1/form/get-resource")
    Call<ResponseBody> loadFiles(@Body ResourceRequest body);

    @GET
    Call<ResponseBody> loadFormattedFiles(@Url String url);

    @GET
    Call<ResponseBody> loadUsersAvatar(@Url String url);

    @POST("device/v1/stat/general")
    Observable<PersonalResult> getRespondentsChartData();

    @POST("device/v1/form/result")
    Observable<PersonalStatsContainer> getStatisticsChartData(@Body Form form);

    @POST("device/v1/test/get-five")
    Observable<String> mineSomeRespos();

    @POST("device/v1/payout/create")
    Observable<PayoutResponse> performPayout(@Body PayoutRequest body);

    @POST("device/v1/dbForm/result")
    Observable<List<JsonObject>> postResponse(@Body FormPostResponse body);

    @POST("device/v1/profile/set")
    Observable<PhoneAddingResponse> setPhoneNumber(@Body PhoneNumberRequest body);

    @POST("device/v1/form/submit-state")
    Observable<TestSessionResponseDto> postTestSession(@Body TestSessionRequestDto body);
}