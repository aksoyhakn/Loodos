package com.example.loodos.service;

import android.content.Context;

import com.example.loodos.utils.CommonUtils;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class APIClient {

    private static Retrofit retrofit = null;

    public static Retrofit getClient() {
        Gson gson = new GsonBuilder().setLenient().create();
        if (retrofit==null) {
            try {
                retrofit = new Retrofit.Builder()
                        .baseUrl(CommonUtils.URL)
                        .addConverterFactory(GsonConverterFactory.create(gson))
                        .build();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return retrofit;
    }
}
