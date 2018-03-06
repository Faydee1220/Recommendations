package com.rq.recommendations.api;

import com.rq.recommendations.model.ActiveListings;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.http.GET;
import retrofit2.http.Path;
import retrofit2.http.Query;

/**
 * Created by Faydee on 2018/3/6.
 */

public interface Api {

//    @GET("listings/active")
//    void activeListings(@Query("includes") String includes,
//                       Callback<ActiveListings> callback);

    @GET("listings/active")
    Call<List<ActiveListings>> activeListings(
            @Query("includes") String includes,
            Callback<ActiveListings> callback);
}

//public interface GitHubService {
//    @GET("users/{user}/repos")
//    Call<List<Repo>> listRepos(@Path("user") String user);
//}