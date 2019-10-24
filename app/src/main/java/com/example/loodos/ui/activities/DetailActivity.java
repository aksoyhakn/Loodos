package com.example.loodos.ui.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.util.Log;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.loodos.R;
import com.example.loodos.presenter.DetailsPresenter;
import com.example.loodos.ınterface.PresenterImpl;
import com.example.loodos.ınterface.ViewImpl;

import org.json.JSONException;
import org.json.JSONObject;

public class DetailActivity extends AppCompatActivity implements ViewImpl.DetailsImpl {

    private PresenterImpl.DetailsPresenterImpl detailsPresenter;
    public static final String MOVIE_POSTER_URL = "movie_poster_url";
    public static final String MOVİE_TITLE = "movie_title";
    private ImageView imgPoster;
    private TextView txtMovieTitle, txtMovieYear, txtMovieReleased, txtMovieDirector, txtMoviePlot,txtMovieLanguage;

    @SuppressLint("NewApi")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);

        detailsPresenter = new DetailsPresenter(this);

        final String moviePosterURL = getIntent().getStringExtra(MOVIE_POSTER_URL);
        final String movieTitle = getIntent().getStringExtra(MOVİE_TITLE);

        imgPoster = findViewById(R.id.img_movie_poster);

        txtMovieTitle = findViewById(R.id.txt_movie_title);
        txtMovieYear = findViewById(R.id.txt_movie_year);
        txtMovieReleased = findViewById(R.id.txt_movie_released);
        txtMovieDirector = findViewById(R.id.txt_movie_director);
        txtMoviePlot = findViewById(R.id.txt_movie_plot);
        txtMovieLanguage = findViewById(R.id.txt_movie_language);


        txtMovieTitle.setText(movieTitle);

        if (!moviePosterURL.equals("N/A")) {
            Glide.with(this).load(moviePosterURL).into(imgPoster);
        } else {
            imgPoster.setImageResource(R.drawable.empty_poster);
        }

        detailsPresenter.showMovieDetails(movieTitle);

    }


    @Override
    public void showMovieDetails(JSONObject movieObject) {

        try {

            txtMovieTitle.setText(movieObject.getString("Title"));
            txtMovieYear.setText(movieObject.getString("Year"));
            txtMovieReleased.setText(movieObject.getString("Released"));
            txtMovieDirector.setText(movieObject.getString("Director"));
            txtMoviePlot.setText(movieObject.getString("Plot"));
            txtMovieLanguage.setText(movieObject.getString("Language"));

        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onError() {
        Toast.makeText(getApplicationContext(), getString(R.string.error), Toast.LENGTH_LONG).show();
    }
}
