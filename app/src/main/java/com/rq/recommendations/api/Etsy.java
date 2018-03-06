package com.rq.recommendations.api;

import com.rq.recommendations.model.ActiveListings;
import retrofit.Callback;
import retrofit.RequestInterceptor;
import retrofit.RestAdapter;

/**
 * Created by Faydee on 2018/3/5.
 */

public class Etsy {
    private static final String API_KEY = "39vwei0gy6ug13pebvlgol5w";
    private static final String API_URL = "https://openapi.etsy.com/v2";

    private static Api getApi() {
        return new RestAdapter.Builder()
                .setEndpoint(API_URL)
                .setRequestInterceptor(getInterceptor())
                .build()
                .create(Api.class);
    }

    private static RequestInterceptor getInterceptor() {
        return new RequestInterceptor() {
            @Override
            public void intercept(RequestFacade request) {
                request.addEncodedQueryParam("api_key", API_KEY);
            }
        };
    }

    public static void getActiveListing(Callback<ActiveListings> callback) {
        getApi().activeListings("Shop,Images", callback);
    }
}
