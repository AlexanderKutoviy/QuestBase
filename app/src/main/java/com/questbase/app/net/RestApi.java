package com.questbase.app.net;

import com.questbase.app.domain.Form;
import com.questbase.app.domain.FormResource;
import com.questbase.app.domain.ProfileResponse;
import com.questbase.app.net.body.ResourceRequest;
import com.questbase.app.net.entity.Category;
import com.questbase.app.net.entity.VersionedForm;
import com.questbase.app.net.entity.transactions.TransactionsResponseDto;

import java.util.List;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Url;
import rx.Observable;

public interface RestApi {

    /**
     * Makes POST request and returns response with profile data
     *
     * @return users profile from server
     * @author OlxeandrKutoviy
     */
    @POST("device/v1/profile/get")
    Observable<ProfileResponse> getProfile();

    /**
     * Makes POST request and returns all users transactions
     *
     * @return users transactionc history
     * @author OlxeandrKutoviy
     */
    @POST("device/v1/payout/payout-history")
    Observable<TransactionsResponseDto> getTransactions();

    /**
     * Makes POST request and gets all categories of quests
     *
     * @return all quests categories
     * @author OlxeandrKutoviy
     */
    @POST("device/v1/form/get-categories")
    Observable<List<Category>> getCategories();

    /**
     * Makes POST request with body (category) and returns last versions of quests
     *
     * @param category to take quests
     * @return last versions of quests
     * @author OlxeandrKutoviy
     */
    @POST("device/v1/form/list-by-category")
    Observable<List<VersionedForm>> getVersionedForms(@Body Category category);

    /**
     * Make POST request with body (form) and returns JSON with quests description
     *
     * @param form (quest) to take descriptor
     * @return quest description
     * @author OlxeandrKutoviy
     */
    @POST("device/v1/form/get-form")
    Observable<Form> getFormDescriptor(@Body Form form);

    /**
     * Returns all resources form current quest(images etc. )
     *
     * @return resources for current quest
     * @params one item from list of resources o current quest
     * @author DenisShtanko
     */
    @POST("device/v1/dbForm/resources")
    Call<ResponseBody> getResources(@Body FormResource formResource);

    /**
     * @return binary data
     * @author DenisShtanko
     */
    @POST("device/v1/form/get-resource")
    Call<ResponseBody> loadFiles(@Body ResourceRequest body);

    /**
     * @return compressed files
     * @author DenisShtanko
     */
    @GET
    Call<ResponseBody> loadFormattedFiles(@Url String url);

    /**
     * @return users avatar
     * @author DenisShtanko
     */
    @GET
    Call<ResponseBody> loadUsersAvatar(@Url String url);
}