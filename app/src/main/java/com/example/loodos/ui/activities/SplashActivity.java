package com.example.loodos.ui.activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.loodos.BuildConfig;
import com.example.loodos.R;
import com.example.loodos.utils.CommonUtils;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.remoteconfig.FirebaseRemoteConfig;
import com.google.firebase.remoteconfig.FirebaseRemoteConfigSettings;

public class SplashActivity extends AppCompatActivity {

    private final int SPLASH_TIME_OUT = 3000;

    private static final String TITLE = "title";
    private static final String COLOR = "background_color";
    private static final String TITLE_COLOR = "title_color";

    private FirebaseRemoteConfig mRemoteConfig;
    private RelativeLayout rlSplashActivity;
    private LinearLayout llSplashNetworkError;
    private TextView title;
    private TextView txtNetworkError;
    private Button btntryAgain;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        hideSystemUI();
        init();

        if (CommonUtils.isNetworkAvailable(getApplicationContext())) {

            rlSplashActivity.setVisibility(View.VISIBLE);
            llSplashNetworkError.setVisibility(View.GONE);
            showSplashScreen();

        } else {

            rlSplashActivity.setVisibility(View.GONE);
            llSplashNetworkError.setVisibility(View.VISIBLE);
            showNetworkError();
        }

    }

    private void init() {

        llSplashNetworkError = findViewById(R.id.ll_splashNetworkError);
        rlSplashActivity = findViewById(R.id.ll_SplashActivity);
        title = findViewById(R.id.txt_loodos);
        txtNetworkError = findViewById(R.id.txt_network_error);
        btntryAgain = findViewById(R.id.btn_try_again);
    }

    private void fetchFirebase() {

        long cacheExpiration = 2000;

        if (mRemoteConfig.getInfo().getConfigSettings().isDeveloperModeEnabled()) {
            cacheExpiration = 0;
        }

        mRemoteConfig.fetch(cacheExpiration)
                .addOnCompleteListener(this, new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful()) {

                            mRemoteConfig.activateFetched();

                            rlSplashActivity.setBackgroundColor(Color.parseColor(mRemoteConfig.getString(COLOR)));
                            title.setTextColor(Color.parseColor(mRemoteConfig.getString(TITLE_COLOR)));
                            title.setText(mRemoteConfig.getString(TITLE));

                            new Handler().postDelayed(new Runnable() {
                                @Override
                                public void run() {

                                    Intent mainIntent = new Intent(getApplicationContext(), MainActivity.class);
                                    startActivity(mainIntent);
                                    finish();
                                }

                            }, SPLASH_TIME_OUT);


                        } else {
                            Toast.makeText(SplashActivity.this, getString(R.string.fetch_failed), Toast.LENGTH_SHORT).show();
                        }

                    }
                });
    }

    private void showSplashScreen() {

        mRemoteConfig = FirebaseRemoteConfig.getInstance();
        FirebaseRemoteConfigSettings configSettings = new FirebaseRemoteConfigSettings.Builder()
                .setDeveloperModeEnabled(BuildConfig.DEBUG)
                .build();

        mRemoteConfig.setConfigSettings(configSettings);
        mRemoteConfig.setDefaults(R.xml.remote_config_default);

        fetchFirebase();
    }

    private void showNetworkError() {

        txtNetworkError.setText(getString(R.string.network_error_text, "\n"));
        btntryAgain.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getApplicationContext(), SplashActivity.class));
            }
        });
    }

    private void hideSystemUI() {
        getWindow().getDecorView().setSystemUiVisibility(
                View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                        | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                        | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                        | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION // hide nav bar
                        | View.SYSTEM_UI_FLAG_FULLSCREEN // hide status bar
                        | View.SYSTEM_UI_FLAG_IMMERSIVE);
    }
}
