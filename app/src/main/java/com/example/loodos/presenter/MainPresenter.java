package com.example.loodos.presenter;

import android.util.Log;

import com.example.loodos.pojo.Result;
import com.example.loodos.pojo.Search;
import com.example.loodos.service.APIClient;
import com.example.loodos.service.APInterface;
import com.example.loodos.utils.CommonUtils;
import com.example.loodos.ınterface.ViewImpl;
import com.example.loodos.ınterface.PresenterImpl;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainPresenter implements PresenterImpl.MainPresenterImpl {

    ViewImpl.MainImpl mainView;

    public MainPresenter(ViewImpl.MainImpl mainView) {
        this.mainView = mainView;
    }


    @Override
    public void searchList(String query) {

        APInterface apInterface = APIClient.getClient().create(APInterface.class);

        mainView.showProgress();

        Call<Result> call = apInterface.searchMovieList(query, CommonUtils.API_KEY);
        call.enqueue(new Callback<Result>() {
            @Override
            public void onResponse(Call<Result> call, Response<Result> response) {
                ArrayList<Search> movies = response.body().getSearch();

                mainView.hideProgress();
                mainView.showSearchList(movies);

            }

            @Override
            public void onFailure(Call<Result> call, Throwable t) {

            }
        });
    }
}
