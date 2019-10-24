package com.example.loodos.presenter;

import android.util.Log;

import com.example.loodos.model.Details;
import com.example.loodos.service.APIClient;
import com.example.loodos.service.APInterface;
import com.example.loodos.utils.CommonUtils;
import com.example.loodos.ınterface.PresenterImpl;
import com.example.loodos.ınterface.ViewImpl;

import org.json.JSONException;
import org.json.JSONObject;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class DetailsPresenter implements PresenterImpl.DetailsPresenterImpl {

    ViewImpl.DetailsImpl detailsView;


    public DetailsPresenter(ViewImpl.DetailsImpl detailsView) {
        this.detailsView = detailsView;
    }

    @Override
    public void showMovieDetails(String query) {

        APInterface apInterface = APIClient.getClient().create(APInterface.class);

        Call<Details> call = apInterface.movieDetails(query, CommonUtils.API_KEY);
        call.enqueue(new Callback<Details>() {
            @Override
            public void onResponse(Call<Details> call, Response<Details> response) {

                JSONObject movieDetailObject =new JSONObject();

                try {
                    movieDetailObject.put("Title",response.body().getTitle());
                    movieDetailObject.put("Year",response.body().getYear());
                    movieDetailObject.put("Released",response.body().getReleased());
                    movieDetailObject.put("Director",response.body().getDirector());
                    movieDetailObject.put("Plot",response.body().getPlot());
                    movieDetailObject.put("Language",response.body().getLanguage());
                    movieDetailObject.put("Runtime",response.body().getRuntime());

                } catch (JSONException e) {
                    e.printStackTrace();
                }

                detailsView.showMovieDetails(movieDetailObject);
            }

            @Override
            public void onFailure(Call<Details> call, Throwable t) {
                detailsView.onError();
            }
        });
    }
}
