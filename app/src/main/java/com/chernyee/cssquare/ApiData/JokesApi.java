package com.chernyee.cssquare.ApiData;

import retrofit2.Call;
import retrofit2.http.GET;

/**
 * Created by Issac on 4/6/2016.
 */
public interface JokesApi {

    @GET("jokes.json")
    Call<JokesData> getJokesApi();
}
