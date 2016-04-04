package com.chernyee.cssquare.Qod;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

/**
 * Created by Issac on 4/3/2016.
 */
public interface QodApi {

    @GET("qod.json")
    Call<QodData> getQodApi(@Query("category") String category);


}
