package com.se3.payme.LoginAndSignUp

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.TextView
import com.se3.payme.R

class SignUpActivity : AppCompatActivity() {
    private lateinit var txtSignIn:TextView
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_sign_up)

        txtSignIn = findViewById(R.id.txtSignIn)
        txtSignIn.setOnClickListener {
            val intent:Intent = Intent(this,LoginActivity::class.java)
            startActivity(intent)
            finish()
        }
    }
}