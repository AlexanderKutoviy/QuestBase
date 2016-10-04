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

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.Field;
import retrofit2.http.Url;
import rx.Observable;

public class MockRestApi implements RestApi {

    @Override
    public Observable<ProfileResponse> getProfile() {
        return null;
    }

    @Override
    public Observable<TransactionsResponseDto> getTransactions() {
        return null;
    }

    @Override
    public Call<ResponseBody> loadUsersAvatar(@Url String url) {
        return null;
    }

    @Override
    public Observable<List<Category>> getCategories() {
        return Observable.just(Arrays.asList(new Category(1, 134, "name1"),
                new Category(2, 235235, "name2"),
                new Category(3, 122332432, "name3"),
                new Category(4, 234234, "name4"),
                new Category(6, 432442, "name5")));
    }

    @Override
    public Observable<List<VersionedForm>> getVersionedForms(Category category) {
        List<VersionedForm> versionedForms = new ArrayList<>();
        versionedForms.add(new VersionedForm(1, 111));
        versionedForms.add(new VersionedForm(2, 222));
        versionedForms.add(new VersionedForm(3, 333));
        versionedForms.add(new VersionedForm(4, 444));
        versionedForms.add(new VersionedForm(6, 555));
        return Observable.just(versionedForms);
    }

    @Override
    public Observable<Form> getFormDescriptor(@Body Form form) {
        return null;
    }

    @Override
    public Call<ResponseBody> loadFiles(@Body ResourceRequest body) {
        return null;
    }

    @Override
    public Call<ResponseBody> loadFormattedFiles(@Url String url) {
        return null;
    }

    @Override
    public Observable<PersonalResult> getRespondentsChartData() {
        throw new UnsupportedOperationException("Not implemented");
    }

    @Override
    public Observable<PersonalStatsContainer> getStatisticsChartData(@Body Form form) {
        return null;
    }

    @Override
    public Observable<String> mineSomeRespos() {
        return null;
    }

    public Call<ResponseBody> loadFormattedFiles(@Field("form") String form, @Field("path") String path, @Field("name") String name, @Field("w") String w, @Field("h") String h, @Field("crc") String crc, @Field("ext") String ext) {
        return null;
    }

    @Override
    public Call<ResponseBody> getResources(FormResource formResource) {
        return null;
    }

    @Override
    public Observable<List<JsonObject>> postResponse(@Body FormPostResponse body) {
        return null;
    }

    @Override
    public Observable<PayoutResponse> performPayout(@Body PayoutRequest body) {
        return null;
    }

    @Override
    public Observable<PhoneAddingResponse> setPhoneNumber(@Body PhoneNumberRequest body) {
        return null;
    }

    @Override
    public Observable<TestSessionResponseDto> postTestSession(@Body TestSessionRequestDto body) {
        return null;
    }
}
