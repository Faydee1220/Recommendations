package com.rq.recommendations.api;

import com.rq.recommendations.model.ActiveListings;

import retrofit2.Callback;
import retrofit2.http.GET;
import retrofit2.http.Query;

/**
 * Created by Faydee on 2018/3/6.
 */

public interface Api {

    @GET("listings/active")
    void activeListings(@Query("includes") String includes,
                       Callback<ActiveListings> callback);
}
