package com.example.loodos;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.loodos.ui.activities.MainActivity;
import com.example.loodos.utils.CommonUtils;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.remoteconfig.FirebaseRemoteConfig;
import com.google.firebase.remoteconfig.FirebaseRemoteConfigSettings;

public class SplashActivity extends AppCompatActivity {

    private final int SPLASH_DISPLAY_LENGTH = 3000;

    private static final String TITLE = "title";
    private static final String COLOR = "background_color";
    private static final String TITLE_COLOR = "title_color";

    private FirebaseRemoteConfig mRemoteConfig;
    private LinearLayout llSplashActivity;
    private TextView title;

    private TextView txtNetworkError;
    private Button btntryAgain;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


        if (CommonUtils.isNetworkAvailable(getApplicationContext())) {

            setContentView(R.layout.activity_splash);

            llSplashActivity = findViewById(R.id.ll_SplashActivity);
            title = findViewById(R.id.txt_loodos);

            mRemoteConfig = FirebaseRemoteConfig.getInstance();
            FirebaseRemoteConfigSettings configSettings = new FirebaseRemoteConfigSettings.Builder()
                    .setDeveloperModeEnabled(BuildConfig.DEBUG)
                    .build();

            mRemoteConfig.setConfigSettings(configSettings);
            mRemoteConfig.setDefaults(R.xml.remote_config_default);

            fetch();

        } else {

            setContentView(R.layout.activity_splash_network_error);

            txtNetworkError=findViewById(R.id.txt_network_error);
            txtNetworkError.setText(getString(R.string.network_error_text,"\n"));

            btntryAgain=findViewById(R.id.btn_try_again);
            btntryAgain.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    startActivity(new Intent(getApplicationContext(), SplashActivity.class));
                }
            });

        }


    }

    private void fetch() {

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

                            llSplashActivity.setBackgroundColor(Color.parseColor(mRemoteConfig.getString(COLOR)));
                            title.setTextColor(Color.parseColor(mRemoteConfig.getString(TITLE_COLOR)));
                            title.setText(mRemoteConfig.getString(TITLE));

                            new Handler().postDelayed(new Runnable() {
                                @Override
                                public void run() {

                                    Intent mainIntent = new Intent(getApplicationContext(), MainActivity.class);
                                    startActivity(mainIntent);
                                    finish();
                                }

                                }, SPLASH_DISPLAY_LENGTH);


                        } else {
                            Toast.makeText(SplashActivity.this, getString(R.string.fetch_failed), Toast.LENGTH_SHORT).show();
                        }

                    }
                });
    }
}
