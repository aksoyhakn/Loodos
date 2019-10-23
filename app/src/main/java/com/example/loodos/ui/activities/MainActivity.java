package com.example.loodos.ui.activities;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Point;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;

import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import com.airbnb.lottie.LottieAnimationView;
import com.example.loodos.R;
import com.example.loodos.pojo.Result;
import com.example.loodos.pojo.Search;
import com.example.loodos.presenter.MainPresenter;
import com.example.loodos.service.APIClient;
import com.example.loodos.service.APInterface;
import com.example.loodos.ui.adapter.MainAdapter;
import com.example.loodos.ınterface.PresenterImpl;
import com.example.loodos.ınterface.ViewImpl;
import com.ferfalk.simplesearchview.SimpleSearchView;
import com.ferfalk.simplesearchview.utils.DimensUtils;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainActivity extends AppCompatActivity implements ViewImpl.MainImpl {

    private PresenterImpl.MainPresenterImpl mainPresenter;
    private LottieAnimationView lottieAnimationView;
    private Toolbar toolbar;
    private SimpleSearchView searchView;
    private RecyclerView recyclerView;


    @SuppressLint("NewApi")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mainPresenter = new MainPresenter(this);

        lottieAnimationView = findViewById(R.id.animation_view);
        searchView = findViewById(R.id.searchView);
        recyclerView = findViewById(R.id.rv_mainActivity);


        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        searchView.setOnQueryTextListener(new SimpleSearchView.OnQueryTextListener() {
            @RequiresApi(api = Build.VERSION_CODES.KITKAT)
            @Override
            public boolean onQueryTextSubmit(String query) {

                mainPresenter.searchList(query);

                return false;
            }

            @RequiresApi(api = Build.VERSION_CODES.KITKAT)
            @Override
            public boolean onQueryTextChange(String newText) {

                return false;
            }

            @Override
            public boolean onQueryTextCleared() {
                return false;
            }
        });


    }


    private void setupSearchView(Menu menu) {
        MenuItem item = menu.findItem(R.id.action_search);
        searchView.setMenuItem(item);
        searchView.setHint(getString(R.string.search_hint));

        Point revealCenter = searchView.getRevealAnimationCenter();
        revealCenter.x -= DimensUtils.convertDpToPx(40, getApplicationContext());
    }

    @Override
    public void showProgress() {
        lottieAnimationView.setVisibility(View.VISIBLE);
    }

    @Override
    public void hideProgress() {
        lottieAnimationView.setVisibility(View.GONE);
    }

    @Override
    public void showSearchList(ArrayList<Search> searchArrayList) {

        recyclerView.setHasFixedSize(true);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getApplicationContext());
        recyclerView.setLayoutManager(linearLayoutManager);
        MainAdapter mainAdapter = new MainAdapter(getApplicationContext(), searchArrayList);
        recyclerView.setAdapter(mainAdapter);
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_search, menu);
        setupSearchView(menu);
        return super.onCreateOptionsMenu(menu);
    }

}
