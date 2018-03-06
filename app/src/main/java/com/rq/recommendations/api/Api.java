package com.rq.recommendations.api;

import com.rq.recommendations.model.ActiveListings;

import retrofit.Callback;
import retrofit.http.GET;
import retrofit.http.Query;

/**
 * Created by Faydee on 2018/3/6.
 */

public interface Api {

    @GET("/listings/active")
    void activeListings(@Query("includes") String includes,
                       Callback<ActiveListings> callback);

}
