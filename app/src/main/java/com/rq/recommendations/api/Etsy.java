package com.rq.recommendations.api;

import com.rq.recommendations.model.ActiveListings;

import java.io.IOException;

import okhttp3.HttpUrl;
import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import retrofit2.Callback;
import retrofit2.Retrofit;

/**
 * Created by Faydee on 2018/3/5.
 */

public class Etsy {
    private static final String API_KEY = "39vwei0gy6ug13pebvlgol5w";
    private static final String API_URL = "https://openapi.etsy.com/v2/";

    private static Api getApi() {
        OkHttpClient client = new OkHttpClient();

        client.interceptors().add(new Interceptor() {
            @Override
            public Response intercept(Chain chain) throws IOException {
                Request request = chain.request();
                HttpUrl url = request.url().newBuilder().addQueryParameter("api_key", API_KEY).build();
                request = request.newBuilder().url(url).build();
                return chain.proceed(request);
            }
        });

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(API_URL)
                .client(client)
                .build();
        Api api = retrofit.create(Api.class);
        return api;
    }

    private static OkHttpClient getClient() {
        OkHttpClient client = new OkHttpClient();

        client.interceptors().add(new Interceptor() {
            @Override
            public Response intercept(Chain chain) throws IOException {
                Request request = chain.request();
                HttpUrl url = request.url().newBuilder().addQueryParameter("api_key",API_KEY).build();
                request = request.newBuilder().url(url).build();
                return chain.proceed(request);
            }
        });

        return client;
    }

    public static void getActiveListing(Callback<ActiveListings> callback) {
        getApi().activeListings("Shop,Images", callback);
    }
}
