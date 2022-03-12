package com.example.sosudatech

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.content.SharedPreferences




class SplashActivity : AppCompatActivity() {
    var splashDuration: Long? = 50
    var isSession:Boolean? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash)
        val prefs = getSharedPreferences(CommonClass.Common.MY_PREFS_NAME, MODE_PRIVATE)
        isSession = prefs.getBoolean(CommonClass.Common.isSession,false)

        initUI()
    }
    fun initUI() {

        val handler = Handler()
        handler.postDelayed({

            if (isSession!!) {
                val openMainActivity = Intent(this@SplashActivity, DetailsActivity::class.java)
                startActivity(openMainActivity)
                finish()
            } else  {
                val openMainActivity = Intent(this@SplashActivity, MainActivity::class.java)
                startActivity(openMainActivity)
                finish()
            }


        }, splashDuration!!)

    }
}