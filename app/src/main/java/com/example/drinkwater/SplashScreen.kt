package com.example.drinkwater

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.WindowManager

class SplashScreen : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash_screen)
        startActivity(Intent(this, MainActivity::class.java))

        // we used the postDelayed(Runnable, time) method
        // to send a message with a delayed time.
        //Normal Handler is deprecated , so we have to change the code little bit

        // Handler().postDelayed({
//        Handler(Looper.getMainLooper()).postDelayed({
        //          val intent = Intent(this, MainActivity::class.java)
        //        startActivity(intent)
        //      finish()
        //}, 3000) // 3000 is the delayed time in milliseconds.
        //}

    }
}