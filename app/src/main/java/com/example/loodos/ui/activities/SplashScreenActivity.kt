package com.example.loodos.ui.activities

import androidx.appcompat.app.AppCompatActivity

import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.os.Handler
import android.view.View
import android.widget.Button
import android.widget.LinearLayout
import android.widget.RelativeLayout
import android.widget.TextView
import android.widget.Toast

import com.example.loodos.BuildConfig
import com.example.loodos.R
import com.example.loodos.utils.CommonUtils
import com.google.android.gms.tasks.OnCompleteListener
import com.google.android.gms.tasks.Task
import com.google.firebase.remoteconfig.FirebaseRemoteConfig
import com.google.firebase.remoteconfig.FirebaseRemoteConfigSettings

class SplashScreenActivity : AppCompatActivity() {

    private val SPLASH_TIME_OUT = 3000

    private var mRemoteConfig: FirebaseRemoteConfig? = null
    private var rlSplashActivity: RelativeLayout? = null
    private var llSplashNetworkError: LinearLayout? = null
    private var title: TextView? = null
    private var txtNetworkError: TextView? = null
    private var btntryAgain: Button? = null


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash_screen)

        hideSystemUI()
        init()

        if (CommonUtils.isNetworkAvailable(applicationContext)) {

            rlSplashActivity!!.visibility = View.VISIBLE
            llSplashNetworkError!!.visibility = View.GONE
            showSplashScreen()

        } else {

            rlSplashActivity!!.visibility = View.GONE
            llSplashNetworkError!!.visibility = View.VISIBLE
            showNetworkError()
        }

    }

    private fun init() {

        llSplashNetworkError = findViewById(R.id.ll_splashNetworkError)
        rlSplashActivity = findViewById(R.id.ll_SplashActivity)
        title = findViewById(R.id.txt_loodos)
        txtNetworkError = findViewById(R.id.txt_network_error)
        btntryAgain = findViewById(R.id.btn_try_again)
    }

    private fun fetchFirebase() {

        var cacheExpiration: Long = 2000

        if (mRemoteConfig!!.info.configSettings.isDeveloperModeEnabled) {
            cacheExpiration = 0
        }

        mRemoteConfig!!.fetch(cacheExpiration)
                .addOnCompleteListener(this) { task ->
                    if (task.isSuccessful) {

                        mRemoteConfig!!.activateFetched()

                        rlSplashActivity!!.setBackgroundColor(Color.parseColor(mRemoteConfig!!.getString(COLOR)))
                        title!!.setTextColor(Color.parseColor(mRemoteConfig!!.getString(TITLE_COLOR)))
                        title!!.text = mRemoteConfig!!.getString(TITLE)

                        Handler().postDelayed({
                            val mainIntent = Intent(applicationContext, MainActivity::class.java)
                            startActivity(mainIntent)
                            finish()
                        }, SPLASH_TIME_OUT.toLong())


                    } else {
                        Toast.makeText(this@SplashScreenActivity, getString(R.string.fetch_failed), Toast.LENGTH_SHORT).show()
                    }
                }
    }

    private fun showSplashScreen() {

        mRemoteConfig = FirebaseRemoteConfig.getInstance()
        val configSettings = FirebaseRemoteConfigSettings.Builder()
                .setDeveloperModeEnabled(BuildConfig.DEBUG)
                .build()

        mRemoteConfig!!.setConfigSettings(configSettings)
        mRemoteConfig!!.setDefaults(R.xml.remote_config_default)

        fetchFirebase()
    }

    private fun showNetworkError() {

        txtNetworkError!!.text = getString(R.string.network_error_text, "\n")
        btntryAgain!!.setOnClickListener { startActivity(Intent(applicationContext, SplashScreenActivity::class.java)) }
    }

    private fun hideSystemUI() {
        window.decorView.systemUiVisibility = (View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                or View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                or View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                or View.SYSTEM_UI_FLAG_HIDE_NAVIGATION // hide nav bar
                or View.SYSTEM_UI_FLAG_FULLSCREEN // hide status bar
                or View.SYSTEM_UI_FLAG_IMMERSIVE)
    }

    companion object {

        private val TITLE = "title"
        private val COLOR = "background_color"
        private val TITLE_COLOR = "title_color"
    }
}