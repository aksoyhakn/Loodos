package com.example.loodos.service;

import com.example.loodos.model.Details;
import com.example.loodos.model.Result;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface APInterface {

    @GET("?type=movie")
    Call<Result> searchMovieList(@Query("s") String data,@Query("apikey") String apiKey);

    @GET("?plot=full")
    Call<Details> movieDetails(@Query("t") String data, @Query("apikey") String apiKey);

}
