package com.muryno.cardfinder.view.activity

import android.content.Context
import android.content.DialogInterface
import android.media.MediaPlayer
import android.opengl.Visibility
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.ProgressBar
import android.widget.RelativeLayout
import android.widget.TextView
import androidx.appcompat.app.AlertDialog
import androidx.constraintlayout.widget.ConstraintLayout
import com.google.android.material.textfield.TextInputEditText
import com.muryno.cardfinder.model.entity.CardDetails
import com.muryno.cardfinder.view.base.BaseActivity
import com.se3.payme.ApiClient
import com.se3.payme.Data.MyData
import com.se3.payme.R
import com.se3.payme.models.BankCard
import com.se3.payme.models.FunderTransferByQrCode
import com.se3.payme.models.LinkBankRequest
import com.se3.payme.models.Users
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch


class CardDetailActivity : BaseActivity() {
private lateinit var cancl:ImageView
private lateinit var crd_brand:TextView
    private lateinit var crd_type:TextView
    private lateinit var bank:TextView
    private lateinit var country:TextView
    private lateinit var currency:TextView
    private lateinit var continueNext:TextView
    private lateinit var layout:RelativeLayout
    private lateinit var layoutCon:ConstraintLayout

    private val scope = CoroutineScope(context = Dispatchers.Main)
    private
    var cardDetails : CardDetails? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(com.se3.payme.R.layout.activity_card_detail)
        cancl = findViewById(com.se3.payme.R.id.cancl)
        layout = findViewById(R.id.relativeLay)
        crd_brand = findViewById(com.se3.payme.R.id.crd_brand)
        crd_type = findViewById(com.se3.payme.R.id.crd_type)
        layoutCon = findViewById(R.id.relativeCon)
        continueNext = findViewById(com.se3.payme.R.id.continue_button)
        bank = findViewById(com.se3.payme.R.id.bank)
        country = findViewById(com.se3.payme.R.id.country)
        currency = findViewById(com.se3.payme.R.id.currency)
        continueNext.setOnClickListener {
//            val intent = Intent(this,ValidateAccount::class.java)
//            startActivity(intent)
            showAlertWithTextInputLayout(this)
        }

        if (intent != null) {
            cardDetails = intent.getSerializableExtra("data") as CardDetails
            if (cardDetails == null) {
                finish()

                //if the data is null, it should close the page, nothing to display
            }

            displayData(cardDetails)
        }

        cancl.setOnClickListener { finish() }
    }
    private fun showAlertWithTextInputLayout(context: Context) {
        val builder = AlertDialog.Builder(context)
        builder.setTitle("Link Card")

        val viewInflated: View = LayoutInflater.from(context)
            .inflate(com.se3.payme.R.layout.validate_card, getView() as ViewGroup?, false)
        builder.setView(viewInflated)
        val ccv = viewInflated.findViewById<TextInputEditText>(R.id.ccv)
        val pin = viewInflated.findViewById<TextInputEditText>(R.id.pin)


        builder.setPositiveButton(
            "Link"
        ) { dialog, which ->
            Log.d("BankName","= ${bank.text.toString()}")
            if (ccv.text.toString().isNotEmpty() && pin.text.toString().isNotEmpty()) {
                val linkBankRequest: LinkBankRequest = LinkBankRequest(
                    MyData.user.email,
                        number = MyData.bankCard,
                        ccv = ccv.text.toString(),
                        cardType = crd_type.text.toString(),
                         name =  bank.text.toString()

                )

                executeCall(linkBankRequest,ccv,pin)
            }else{
                                val dlgAlert: android.app.AlertDialog.Builder =
                    android.app.AlertDialog.Builder(this)
                dlgAlert.setTitle("Error")
                    dlgAlert.setMessage("Fill all the field")
                dlgAlert.setPositiveButton("OK", null)
                dlgAlert.setCancelable(true)
                dlgAlert.create().show()

                dlgAlert.setPositiveButton("Ok",
                    DialogInterface.OnClickListener { dialog, which -> })
            }
//
//            dialog.dismiss()
        }
        builder.setNegativeButton(
            android.R.string.cancel
        ) { dialog, which -> dialog.cancel() }

        builder.show()

    }

    private  fun executeCall(linkBankRequest: LinkBankRequest,cvv:TextInputEditText,pin:TextInputEditText) {
        scope.launch {
            try {

                val header = mutableMapOf<String, String>()
                val tokenn = MyData.token.toString()
                header["Authorization"] = tokenn
                val response =
                    ApiClient.apiService.linkBank(headers = header, linkBankRequest)

                if (response.isSuccessful && response.body() != null) {
                    val content = response.body()
                    Log.d("Tag", "content ${response.body()}")
//                progressBar.visibility = View.GONE

                    layout.visibility = View.GONE
                    layoutCon.visibility = View.VISIBLE


//                val dlgAlert: android.app.AlertDialog.Builder =
//                    android.app.AlertDialog.Builder(activity)
//                dlgAlert.setIcon(R.drawable.ic_tick_24)
//                dlgAlert.setPositiveButton("OK", null)
//                dlgAlert.setCancelable(true)
//                dlgAlert.create().show()
//
//                dlgAlert.setPositiveButton("Ok",
//                    DialogInterface.OnClickListener { dialog, which -> })
//                    blueSuccess.visibility = View.VISIBLE
                    MediaPlayer.create(this@CardDetailActivity, R.raw.ios).start()
                    getUserDetail()

                } else {
                    val dlgAlert: android.app.AlertDialog.Builder =
                        android.app.AlertDialog.Builder(this@CardDetailActivity)

                    dlgAlert.setMessage(response.message().toString())
                    dlgAlert.setTitle("Error Message...")
                    dlgAlert.setPositiveButton("OK", null)
                    dlgAlert.setCancelable(true)
                    dlgAlert.create().show()

                    dlgAlert.setPositiveButton("Ok",
                        DialogInterface.OnClickListener { dialog, which -> })


                }
            } catch (e: Exception) {
                Log.d("Tag", "In the catch ${e.message}")

                val dlgAlert: android.app.AlertDialog.Builder =
                    android.app.AlertDialog.Builder(this@CardDetailActivity)

                dlgAlert.setMessage("Please verify f if you have internet connection ${e.message}")
                dlgAlert.setTitle("Error Message...")
                dlgAlert.setPositiveButton("OK", null)
                dlgAlert.setCancelable(true)
                dlgAlert.create().show()

                dlgAlert.setPositiveButton("Ok",
                    DialogInterface.OnClickListener { dialog, which -> })


            }


        }
//
    }
    private fun getUserDetail(){
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
                    MyData.user = Users(content!!.usersId,content!!.firstName,content!!.lastName,content.email,content.password,content.accountNumber,content.tel,content.locked,content.enabled,content.balance,content.qrCode,content.pin,content.friends,
                        content.transactions,content.postProjects,content.bankCards
                    )
                    MyData.token = tokenn
//                    progressBar.visibility = View.GONE
                }else{
                    val dlgAlert: android.app.AlertDialog.Builder =
                        android.app.AlertDialog.Builder(this@CardDetailActivity)

                    dlgAlert.setMessage("${response.body()}")
                    dlgAlert.setTitle("Error Message...")
                    dlgAlert.setPositiveButton("OK", null)
                    dlgAlert.setCancelable(true)
                    dlgAlert.create().show()

                    dlgAlert.setPositiveButton("Ok",
                        DialogInterface.OnClickListener { dialog, which -> })
//                    progressBar.visibility = View.GONE
                }
            }catch (e:Exception){
                val dlgAlert: android.app.AlertDialog.Builder =
                    android.app.AlertDialog.Builder(this@CardDetailActivity)

                dlgAlert.setMessage("Payment successful but could not update your data")
                dlgAlert.setTitle("Update Error..")
                dlgAlert.setPositiveButton("OK", null)
                dlgAlert.setCancelable(true)
                dlgAlert.create().show()

                dlgAlert.setPositiveButton("Ok",
                    DialogInterface.OnClickListener { dialog, which -> })
//                progressBar.visibility = View.GONE
            }
        }

    }

    private fun displayData(cardDetails : CardDetails?){
        crd_brand.text = cardDetails?.brand ?: "Not available"
        crd_type.text = cardDetails?.type ?: "Not available"
        bank.text = cardDetails?.bank?.name ?: "Not available"
        country.text = cardDetails?.country?.name ?: "Not available"
        currency.text = cardDetails?.country?.currency  ?: "Not available"
    }
}
