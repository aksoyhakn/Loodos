package com.example.loodos.ui.activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.loodos.R;
import com.example.loodos.presenter.DetailsPresenter;
import com.example.loodos.ınterface.PresenterImpl;
import com.example.loodos.ınterface.ViewImpl;
import com.google.firebase.analytics.FirebaseAnalytics;

import org.json.JSONException;
import org.json.JSONObject;

public class DetailActivity extends AppCompatActivity implements ViewImpl.DetailsImpl {

    private PresenterImpl.DetailsPresenterImpl detailsPresenter;

    public static final String MOVIE_POSTER_URL = "movie_poster_url";
    public static final String MOVİE_TITLE = "movie_title";

    private ImageView imgPoster,imgInfo;
    private TextView txtMovieTitle, txtMovieYear, txtMovieReleased, txtMovieDirector, txtMoviePlot,txtMovieLanguage;

    FirebaseAnalytics firebaseAnalytics;


    @SuppressLint("NewApi")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);

        init();

        detailsPresenter = new DetailsPresenter(this);
        firebaseAnalytics = FirebaseAnalytics.getInstance(this);

        final String moviePosterURL = getIntent().getStringExtra(MOVIE_POSTER_URL);
        final String movieTitle = getIntent().getStringExtra(MOVİE_TITLE);


        if (!moviePosterURL.equals("N/A")) {
            Glide.with(this).load(moviePosterURL).into(imgPoster);
        } else {
            imgPoster.setImageResource(R.drawable.empty_poster);
        }

        imgInfo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getApplicationContext(),QuestionActivity.class));
            }
        });

        detailsPresenter.showMovieDetails(movieTitle);

    }


    @Override
    public void init() {

        imgPoster = findViewById(R.id.img_movie_poster);
        imgInfo=findViewById(R.id.img_info);
        txtMovieTitle = findViewById(R.id.txt_movie_title);
        txtMovieYear = findViewById(R.id.txt_movie_year);
        txtMovieReleased = findViewById(R.id.txt_movie_released);
        txtMovieDirector = findViewById(R.id.txt_movie_director);
        txtMoviePlot = findViewById(R.id.txt_movie_plot);
        txtMovieLanguage = findViewById(R.id.txt_movie_language);
    }

    @Override
    public void showMovieDetails(JSONObject movieObject) {

        try {
            Bundle params = new Bundle();

            txtMovieTitle.setText(movieObject.getString("Title"));
            params.putString("movie_title", movieObject.getString("Title"));

            txtMovieYear.setText(movieObject.getString("Year"));
            params.putString("movie_year", movieObject.getString("Year"));

            txtMovieReleased.setText(movieObject.getString("Released"));
            params.putString("movie_released", movieObject.getString("Released"));

            txtMovieDirector.setText(movieObject.getString("Director"));
            params.putString("movie_director", movieObject.getString("Director"));

            txtMoviePlot.setText(movieObject.getString("Plot"));
            params.putString("movie_plot", movieObject.getString("Plot"));

            txtMovieLanguage.setText(movieObject.getString("Language"));
            params.putString("movie_language", movieObject.getString("Language"));

            firebaseAnalytics.logEvent("movie_detail", params);

        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onError() {
        Toast.makeText(getApplicationContext(), getString(R.string.error), Toast.LENGTH_LONG).show();
    }
}
