package com.se3.payme

//import androidx.navigation.ui.setupWithNavController
import android.content.DialogInterface
import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.setupWithNavController
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.se3.payme.LoginAndSignUp.LoginActivity


class MainActivity : AppCompatActivity() {
    var handler: Handler? = null
    var r: Runnable? = null
    private lateinit var bottomNavigationView : BottomNavigationView
//    private lateinit var navController  : NavController
    override fun onCreate(savedInstanceState: Bundle?) {
    supportActionBar?.hide()

        super.onCreate(savedInstanceState)

        setContentView(R.layout.activity_main)


        bottomNavigationView= findViewById(R.id.bottom_nav_view)
        val navHost = supportFragmentManager.findFragmentById(R.id.nav_host_frag) as NavHostFragment
        val navController = navHost.navController
        bottomNavigationView.setupWithNavController(navController)
    handler = Handler()

    r = Runnable { // TODO Auto-generated method stub

//       val  intent = Intent(this,LoginActivity::class.java)
//        startActivity(intent)
//        finish()


        val dlgAlert: android.app.AlertDialog.Builder = android.app.AlertDialog.Builder(this)

        dlgAlert.setMessage("You made 5min with interacting with the app , you will need to authenticate back")
        dlgAlert.setTitle("Time Out")
        dlgAlert.setPositiveButton("OK", null)
        dlgAlert.setCancelable(true)
        dlgAlert.create().show()

        dlgAlert.setPositiveButton("Ok",
            DialogInterface.OnClickListener { dialog, which -> })
    }
    }
    override fun onUserInteraction() {
        // TODO Auto-generated method stub
        super.onUserInteraction()
        stopHandler() //stop first and then start
        startHandler()
    }

    fun stopHandler() {
        handler!!.removeCallbacks(r!!)
    }

    fun startHandler() {
        handler!!.postDelayed(r!!, (5 * 60 * 1000).toLong()) //for 5 minutes
    }
}