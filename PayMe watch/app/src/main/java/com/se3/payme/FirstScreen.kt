package com.se3.payme

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import com.se3.payme.LoginAndSignUp.LoginActivity

class FirstScreen : AppCompatActivity() {
//    override fun getActionBar(): ActionBar? {
//        false
//    }
    override fun onCreate(savedInstanceState: Bundle?) {
    supportActionBar?.hide()

        actionBar?.hide()
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_first_screen)

    Handler().postDelayed({
        val intent = Intent(this, LoginActivity::class.java)
        startActivity(intent)
        finish()
    },2500)

    }
}