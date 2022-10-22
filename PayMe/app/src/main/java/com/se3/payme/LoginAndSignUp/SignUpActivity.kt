package com.se3.payme.LoginAndSignUp

import android.content.DialogInterface
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.util.Patterns
import android.view.View
import android.widget.ProgressBar
import android.widget.TextView
import com.google.android.material.button.MaterialButton
import com.google.android.material.textfield.TextInputEditText
import com.se3.payme.ApiClient
import com.se3.payme.Data.MyData
import com.se3.payme.R
import com.se3.payme.models.JwtRequest
import com.se3.payme.models.RegistrationRequest
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class SignUpActivity : AppCompatActivity() {
    private lateinit var txtSignIn:TextView
    private lateinit var edtSignUpFullName:TextInputEditText
    private lateinit var edtSignUpEmail:TextInputEditText
    private lateinit var edtSignUpMobile:TextInputEditText
    private lateinit var edtSignUpPassword:TextInputEditText
    private lateinit var progressBar:ProgressBar
    private lateinit var submitButton:MaterialButton
    private lateinit var edtSignUpPin:TextInputEditText



    private  val scope = CoroutineScope(Dispatchers.Main)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_sign_up)

        txtSignIn = findViewById(R.id.txtSignIn)
        edtSignUpFullName = findViewById(R.id.edtSignUpFullName)
        edtSignUpEmail = findViewById(R.id.edtSignUpEmail)
        edtSignUpMobile = findViewById(R.id.edtSignUpMobile)
        edtSignUpPassword = findViewById(R.id.edtSignUpPassword)
        edtSignUpPin = findViewById(R.id.edtSignUpPin)
        submitButton = findViewById<MaterialButton>(R.id.btnSignUp)
        progressBar = findViewById(R.id.progressSignUp  )



        txtSignIn.setOnClickListener {
            val intent:Intent = Intent(this,LoginActivity::class.java)
            startActivity(intent)
            finish()
        }
        submitButton.setOnClickListener {
            authenticate()
        }
    }


    private fun authenticate() {
        val emailS = edtSignUpEmail.text.toString()
        val passS = edtSignUpPassword.text.toString()
        val fullName = edtSignUpFullName.text.toString()
        val phone = edtSignUpMobile.text.toString()
        val pin = edtSignUpPin.text.toString()



        if (isEmailValid(emailS) && passS.length != 0 && pin.length <=5 && pin.length!=0 && phone.length!=0) {

            progressBar.visibility = View.VISIBLE

            val registrationRequest: RegistrationRequest = RegistrationRequest(firstName = fullName, lastName = fullName, email = emailS, tel = phone, password = passS, pin = pin)
            executeCall(registrationRequest)


        } else {
            val dlgAlert: android.app.AlertDialog.Builder = android.app.AlertDialog.Builder(this)

            dlgAlert.setMessage("Please fill the form correctly")
            dlgAlert.setTitle("Error Message...")
            dlgAlert.setPositiveButton("OK", null)
            dlgAlert.setCancelable(true)
            dlgAlert.create().show()

            dlgAlert.setPositiveButton("Ok",
                DialogInterface.OnClickListener { dialog, which -> })

        }
    }
    fun isEmailValid(email: CharSequence?): Boolean {
        return Patterns.EMAIL_ADDRESS.matcher(email).matches()
    }
    private  fun executeCall(jwtRequest: RegistrationRequest){
        scope.launch {
            try {
                val response = ApiClient.apiService.register(jwtRequest)

                if (response.isSuccessful && response.body() != null) {
                    val content = response.body()
                    Log.d("Tag", "content ${response.body()}")
                    progressBar.visibility = View.GONE

                    val dlgAlert: android.app.AlertDialog.Builder =
                        android.app.AlertDialog.Builder(this@SignUpActivity)

                    dlgAlert.setMessage("Please verify your mail and validate your account")
                    dlgAlert.setTitle("Mail Sends")
                    dlgAlert.setPositiveButton("OK", null)
                    dlgAlert.setCancelable(true)
                    dlgAlert.create().show()

                    dlgAlert.setPositiveButton("Ok",
                        DialogInterface.OnClickListener { dialog, which -> startActivity(Intent(this@SignUpActivity,LoginActivity::class.java)) })
                    progressBar.visibility = View.GONE

                } else {
                    val dlgAlert: android.app.AlertDialog.Builder =
                        android.app.AlertDialog.Builder(this@SignUpActivity)

                    dlgAlert.setMessage("Please verify your mail and validate your account")
                    dlgAlert.setTitle("Mail Sends")
                    dlgAlert.setPositiveButton("OK", null)
                    dlgAlert.setCancelable(true)
                    dlgAlert.create().show()

                    dlgAlert.setPositiveButton("Ok",
                        DialogInterface.OnClickListener { dialog, which -> startActivity(Intent(this@SignUpActivity,LoginActivity::class.java)) })

                    progressBar.visibility = View.GONE

                }
            } catch (e: Exception) {
                Log.d("Tag", "In the catch ${e.message}")

                val dlgAlert: android.app.AlertDialog.Builder =
                    android.app.AlertDialog.Builder(this@SignUpActivity)

                dlgAlert.setMessage("${e.message.toString()}")
                dlgAlert.setTitle("Error Message...")
                dlgAlert.setPositiveButton("OK", null)
                dlgAlert.setCancelable(true)
                dlgAlert.create().show()

                dlgAlert.setPositiveButton("Ok",
                    DialogInterface.OnClickListener { dialog, which -> })
                progressBar.visibility = View.GONE



            }



        }

    }

}