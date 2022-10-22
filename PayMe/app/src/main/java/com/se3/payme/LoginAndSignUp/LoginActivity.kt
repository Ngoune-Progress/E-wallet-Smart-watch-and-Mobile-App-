package com.se3.payme.LoginAndSignUp

import android.content.DialogInterface
import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.util.Log
import android.util.Patterns
import android.view.View
import android.widget.*
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import com.google.android.gms.tasks.OnCompleteListener
import com.google.firebase.messaging.FirebaseMessaging
import com.se3.payme.ApiClient
import com.se3.payme.Data.MyData
import com.se3.payme.MainActivity
import com.se3.payme.R
import com.se3.payme.models.JwtRequest
import com.se3.payme.models.Users
import kotlinx.coroutines.*


class LoginActivity : AppCompatActivity() {
    private lateinit var txtSignUp:TextView
    private lateinit var btnSignIn:Button
    private lateinit var email:EditText
    private lateinit var password:EditText
    private lateinit var progressBar: ProgressBar
    private  val scope = CoroutineScope(Dispatchers.Main)
    private  var token:String=""
        override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)
        txtSignUp = findViewById(R.id.txtSignUp);
            progressBar =findViewById(R.id.sigInProgress)
        btnSignIn = findViewById(R.id.btnSignIn)
        email = findViewById(R.id.edtSignInEmail)
        password = findViewById(R.id.edtSignInPassword)

                // 1
                FirebaseMessaging.getInstance().token
                    .addOnCompleteListener(OnCompleteListener { task ->
                        // 2
                        if (!task.isSuccessful) {
//                            Log.w(TAG, "getInstanceId failed", task.exception)
                            return@OnCompleteListener
                        }
                        // 3
                        val token = task.result

                        // 4
//                        val msg = getString(R.string.token_prefix, token)
//                        Log.d(TAG, msg)
//                        Toast.makeText(baseContext, token, Toast.LENGTH_LONG).show()
//                                                Log.d("TAG", token)

                    })


        txtSignUp.setOnClickListener {
             val intent:Intent =  Intent(this, SignUpActivity::class.java);
            startActivity(intent);
        }
            btnSignIn.setOnClickListener {
                authenticate()

            }
    }

    private fun authenticate() {
        val emailS = email.text.toString()
        val passS = password.text.toString()

        if (isEmailValid(emailS) && passS.length != 0) {

            progressBar.visibility = View.VISIBLE

            val jwtRequest: JwtRequest = JwtRequest(username = emailS, password = passS)
        executeCall(jwtRequest,emailS)


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

    private  fun next(){
        val intent: Intent = Intent(this, MainActivity::class.java);
        startActivity(intent)
        finish()
    }
    private fun getUserDetail(emaill:String,token:String){
        scope.launch {
            try {
                val header = mutableMapOf<String,String>()
                val tokenn = "Bearer "+token
                header["Authorization"]=tokenn
                val response = ApiClient.apiService.findUserByEmail(headers =header ,emaill)
                if (response.isSuccessful && response.body()!=null){
                    val content = response.body()
                    MyData.user = Users(content!!.usersId,content!!.firstName,content!!.lastName,content.email,content.password,content.accountNumber,content.tel,content.locked,content.enabled,content.balance,content.qrCode,content.pin,content.friends,
                        content.transactions,content.postProjects,content.bankCards
                    )
                    MyData.token = tokenn
                    progressBar.visibility = View.GONE
                    next()
                }else{
                    val dlgAlert: android.app.AlertDialog.Builder =
                        android.app.AlertDialog.Builder(this@LoginActivity)

                    dlgAlert.setMessage("${response.body()}")
                    dlgAlert.setTitle("Error Message...")
                    dlgAlert.setPositiveButton("OK", null)
                    dlgAlert.setCancelable(true)
                    dlgAlert.create().show()

                    dlgAlert.setPositiveButton("Ok",
                        DialogInterface.OnClickListener { dialog, which -> })
                    progressBar.visibility = View.GONE
                }
            }catch (e:Exception){
                val dlgAlert: android.app.AlertDialog.Builder =
                    android.app.AlertDialog.Builder(this@LoginActivity)

                dlgAlert.setMessage("${e.message}")
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
    private  fun executeCall(jwtRequest: JwtRequest,email:String){
         scope.launch {
                try {
                    val response = ApiClient.apiService.authenticate(jwtRequest)

                    if (response.isSuccessful && response.body() != null) {
                        val content = response.body()
                        Log.d("Tag", "content ${response.body()}")
                        progressBar.visibility = View.GONE
                        val tokenn = "Bearer "+ content?.token.toString()

                        MyData.token = tokenn
                        getUserDetail(email,content!!.token.toString())

                    } else {
                            val dlgAlert: android.app.AlertDialog.Builder =
                                android.app.AlertDialog.Builder(this@LoginActivity)

                            dlgAlert.setMessage("Email or Password is incorrect")
                            dlgAlert.setTitle("Error Message...")
                            dlgAlert.setPositiveButton("OK", null)
                            dlgAlert.setCancelable(true)
                            dlgAlert.create().show()

                            dlgAlert.setPositiveButton("Ok",
                                DialogInterface.OnClickListener { dialog, which -> })
                            progressBar.visibility = View.GONE



                    }
                } catch (e: Exception) {
                    Log.d("Tag", "In the catch ${e.message}")

                        val dlgAlert: android.app.AlertDialog.Builder =
                            android.app.AlertDialog.Builder(this@LoginActivity)

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

    override fun onDestroy() {
        scope.cancel()
        super.onDestroy()
    }

    fun isEmailValid(email: CharSequence?): Boolean {
        return Patterns.EMAIL_ADDRESS.matcher(email).matches()
    }
}