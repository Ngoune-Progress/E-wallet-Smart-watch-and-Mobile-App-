package com.se3.payme.bubbleNavigation

import android.app.AlertDialog
import android.content.DialogInterface
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.ProgressBar
import androidx.fragment.app.Fragment
import com.se3.payme.ApiClient
import com.se3.payme.Data.MyData
import com.se3.payme.R
import com.se3.payme.models.PostProject
import com.se3.payme.models.Users
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.util.*

class PostProjectFragment : Fragment() {
    private val scope = CoroutineScope(context = Dispatchers.Main)

    private lateinit var theme:EditText
private lateinit var minAmount:EditText
private lateinit var descp : EditText
private lateinit var emailTel:EditText
private lateinit var progressBar: ProgressBar
private lateinit var post:Button
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view =  inflater.inflate(R.layout.fragment_post_project, container, false)
        init(view)
        return  view
    }
    private fun init(view: View){
        theme = view.findViewById(R.id.InvestorName)
        minAmount = view.findViewById(R.id.amountText)
        descp = view.findViewById(R.id.emailOrTel)
        emailTel = view.findViewById(R.id.emailOrTel)
        progressBar = view.findViewById(R.id.postProjectProgress)
        post = view.findViewById(R.id.postProject)
        post.setOnClickListener {
            if(theme.text.isEmpty() || minAmount.text.isEmpty() || descp.text.isEmpty() || emailTel.text.isEmpty() ){
                val dgAlert: AlertDialog.Builder = AlertDialog.Builder(view.context)
                dgAlert.setTitle("Form Error")
                dgAlert.setMessage("Please fill all the form input")
                dgAlert.setPositiveButton("ok",null)
                dgAlert.setCancelable(true)
                dgAlert.setPositiveButton("Ok",
                    DialogInterface.OnClickListener { dialog, which -> })
                dgAlert.show()

            }else{
                progressBar.visibility =View.VISIBLE
                val currentTime = Calendar.getInstance().time
                val pp = PostProject(theme = theme.text.toString(), personName = MyData.user.firstName,email = MyData.user.email!!,description = descp.text.toString(), minInvest = minAmount.text.toString(), date = currentTime.toString(), numOfInvestor = 0)
                executeCall(view,pp)
            }
        }

    }

    private fun getUserDetail(view: View){
        scope.launch {
            try {
                val header = mutableMapOf<String,String>()
                val tokenn = MyData.token!!
                header["Authorization"]=tokenn
                val response = ApiClient.apiService.findUserByEmail(headers =header ,
                    MyData.user.email!!
                )
                if (response.isSuccessful && response.body()!=null){
                    val content = response.body()
                    MyData.user = Users(content!!.usersId,content!!.firstName,content!!.lastName,content.email,content.password,content.accountNumber,content.tel,content.locked,content.enabled,content.balance,content.qrCode,content.pin,
                        content.friends,content.transactions,content.postProjects,content.bankCards
                    )
                    MyData.token = tokenn
                    progressBar.visibility = View.GONE
                    getFragmentManager()?.popBackStack()
                }else{
                    val dlgAlert: android.app.AlertDialog.Builder =
                        android.app.AlertDialog.Builder(view.context)

                    dlgAlert.setMessage("${response.body()}")
                    dlgAlert.setTitle("Error P Message...")
                    dlgAlert.setPositiveButton("OK", null)
                    dlgAlert.setCancelable(true)
                    dlgAlert.create().show()

                    dlgAlert.setPositiveButton("Ok",
                        DialogInterface.OnClickListener { dialog, which -> })
                    progressBar.visibility = View.GONE
                }
            }catch (e:Exception){
                val dlgAlert: android.app.AlertDialog.Builder =
                    android.app.AlertDialog.Builder(view.context)

                dlgAlert.setMessage("Post Save, but could not update your data due to connection error")
                dlgAlert.setTitle("Update Error..")
                dlgAlert.setPositiveButton("OK", null)
                dlgAlert.setCancelable(true)
                dlgAlert.create().show()

                dlgAlert.setPositiveButton("Ok",
                    DialogInterface.OnClickListener { dialog, which -> })
                progressBar.visibility = View.GONE
            }
        }

    }

    private  fun executeCall(view: View,postProject: PostProject){
        scope.launch {
            try {
                val header = mutableMapOf<String,String>()
                val tokenn = MyData.token.toString()
                header["Authorization"]=tokenn
                val response = ApiClient.apiService.addPost(headers=header,postProject)

                if (response.isSuccessful && response.body() != null) {
                    val content = response.body()
                    Log.d("Tag", "content ${response.body()}")
                    progressBar.visibility = View.GONE



                    getUserDetail(view)

                } else {
                    val dlgAlert: android.app.AlertDialog.Builder =
                        android.app.AlertDialog.Builder(activity)

                    dlgAlert.setMessage("Account does not exist")
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
                    android.app.AlertDialog.Builder(activity)

                dlgAlert.setMessage("Please verify  your have internet connection")
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