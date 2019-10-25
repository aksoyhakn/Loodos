package com.example.loodos.ui.activities;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.ActivityOptionsCompat;
import androidx.core.view.ViewCompat;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Point;
import android.os.Build;
import android.os.Bundle;
import android.text.Html;
import android.transition.Fade;
import android.transition.Slide;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.Menu;

import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.airbnb.lottie.LottieAnimationView;
import com.bumptech.glide.Glide;
import com.example.loodos.R;
import com.example.loodos.model.Details;
import com.example.loodos.model.Result;
import com.example.loodos.model.Search;
import com.example.loodos.presenter.MainPresenter;
import com.example.loodos.service.APIClient;
import com.example.loodos.service.APInterface;
import com.example.loodos.ui.adapter.MainAdapter;
import com.example.loodos.utils.CommonUtils;
import com.example.loodos.ınterface.ItemClickListenerImpl;
import com.example.loodos.ınterface.PresenterImpl;
import com.example.loodos.ınterface.ViewImpl;
import com.ferfalk.simplesearchview.SimpleSearchView;
import com.ferfalk.simplesearchview.utils.DimensUtils;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainActivity extends AppCompatActivity implements ViewImpl.MainImpl {

    private PresenterImpl.MainPresenterImpl mainPresenter;
    private LottieAnimationView lottieAnimationView;
    private ImageView gifLoading;
    private Toolbar toolbar;
    private TextView txtNotFind;
    private SimpleSearchView searchView;
    private RecyclerView recyclerView;


    @SuppressLint("NewApi")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        init();
        setSupportActionBar(toolbar);

        mainPresenter = new MainPresenter(this);

        Glide.with(this).load(R.drawable.loodos_loading).into(gifLoading);


        searchView.setOnQueryTextListener(new SimpleSearchView.OnQueryTextListener() {
            @RequiresApi(api = Build.VERSION_CODES.KITKAT)
            @Override
            public boolean onQueryTextSubmit(String query) {

                recyclerView.setVisibility(View.VISIBLE);
                mainPresenter.searchList(query);

                CommonUtils.hideSoftKeyboard(MainActivity.this);

                return false;
            }

            @RequiresApi(api = Build.VERSION_CODES.KITKAT)
            @Override
            public boolean onQueryTextChange(String newText) {

                if (newText.length() == 0) {
                    showGIF();
                }

                return false;
            }

            @Override
            public boolean onQueryTextCleared() {
                return false;
            }
        });


    }


    @Override
    public void init() {

        toolbar = findViewById(R.id.toolbar);
        lottieAnimationView = findViewById(R.id.anim_search_file);
        gifLoading = findViewById(R.id.gif_loading);
        searchView = findViewById(R.id.searchView);
        recyclerView = findViewById(R.id.rv_mainActivity);
        txtNotFind = findViewById(R.id.txt_not_find);
    }

    @Override
    public void showGIF() {

        gifLoading.setVisibility(View.VISIBLE);
        recyclerView.setVisibility(View.GONE);
        lottieAnimationView.setVisibility(View.GONE);
    }

    @Override
    public void hideGIF() {
        recyclerView.setVisibility(View.VISIBLE);
        gifLoading.setVisibility(View.GONE);
        lottieAnimationView.setVisibility(View.GONE);
    }

    @Override
    public void emptyGIF() {

        lottieAnimationView.setVisibility(View.VISIBLE);
        gifLoading.setVisibility(View.GONE);
        txtNotFind.setVisibility(View.VISIBLE);
        recyclerView.setVisibility(View.GONE);

    }

    @Override
    public void onError() {
        Toast.makeText(getApplicationContext(), getString(R.string.error), Toast.LENGTH_LONG).show();
    }


    @Override
    public void showSearchList(ArrayList<Search> searchArrayList) {

        recyclerView.setHasFixedSize(true);

        GridLayoutManager mGridLayoutManager = new GridLayoutManager(MainActivity.this, 2);
        recyclerView.setLayoutManager(mGridLayoutManager);
        MainAdapter mainAdapter = new MainAdapter(getApplicationContext(), searchArrayList, new ItemClickListenerImpl() {
            @SuppressLint("NewApi")
            @Override
            public void onItemClick(RecyclerView.ViewHolder holder, int position, ImageView imageView) {

                Search search = searchArrayList.get(position);
                imageView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {

                        Intent intent = new Intent(MainActivity.this, DetailActivity.class);
                        intent.putExtra(DetailActivity.MOVIE_POSTER_URL, search.getPoster());
                        intent.putExtra(DetailActivity.MOVİE_TITLE, search.getTitle());

                        ActivityOptionsCompat options = ActivityOptionsCompat.makeSceneTransitionAnimation(MainActivity.this,
                                imageView, "example_transition");
                        startActivity(intent, options.toBundle());

                    }
                });


            }
        });
        recyclerView.setAdapter(mainAdapter);
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_search, menu);
        setupSearchView(menu);
        return super.onCreateOptionsMenu(menu);
    }

    private void setupSearchView(Menu menu) {
        MenuItem item = menu.findItem(R.id.action_search);
        searchView.setMenuItem(item);
        searchView.setHint(getString(R.string.search_hint));

        Point revealCenter = searchView.getRevealAnimationCenter();
        revealCenter.x -= DimensUtils.convertDpToPx(40, getApplicationContext());
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            showCloseAppDialog();
        }
        return super.onKeyDown(keyCode, event);
    }

    private void showCloseAppDialog() {

        AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);

        builder.setMessage(getString(R.string.login_logout));
        builder.setPositiveButton(Html.fromHtml("<font color='#d40000'>" + getString(R.string.ok) + "</font>"), new DialogInterface.OnClickListener() {

            public void onClick(DialogInterface dialog, int which) {
                finishAffinity();
                dialog.dismiss();
            }
        });

        builder.setNegativeButton(Html.fromHtml("<font color='#55574a'>" + getString(R.string.cancel) + "</font>"), new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {

                dialog.cancel();

            }

        });

        AlertDialog alert = builder.show();
    }

}
