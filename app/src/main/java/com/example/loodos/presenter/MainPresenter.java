package com.example.loodos.presenter;

import android.widget.Toast;

import com.example.loodos.model.Result;
import com.example.loodos.model.Search;
import com.example.loodos.service.APIClient;
import com.example.loodos.service.APInterface;
import com.example.loodos.utils.CommonUtils;
import com.example.loodos.ınterface.ViewImpl;
import com.example.loodos.ınterface.PresenterImpl;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainPresenter implements PresenterImpl.MainPresenterImpl {

    ViewImpl.MainImpl mainView;
    ArrayList<Search> movies = new ArrayList<>();

    public MainPresenter(ViewImpl.MainImpl mainView) {
        this.mainView = mainView;
    }


    @Override
    public void searchList(String query) {

        movies.clear();

        APInterface apInterface = APIClient.getClient().create(APInterface.class);

        Call<Result> call = apInterface.searchMovieList(query, CommonUtils.API_KEY);
        call.enqueue(new Callback<Result>() {
            @Override
            public void onResponse(Call<Result> call, Response<Result> response) {

                if (response.body().getResponse().equals("True")) {

                    movies = response.body().getSearch();
                    mainView.hideGIF();
                    mainView.showSearchList(movies);

                } else {
                    mainView.emptyGIF();
                }


            }

            @Override
            public void onFailure(Call<Result> call, Throwable t) {
                mainView.onError();
            }
        });
    }
}
