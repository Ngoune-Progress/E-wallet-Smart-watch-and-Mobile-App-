package com.se3.payme.bubbleNavigation

import android.content.DialogInterface
import android.media.AudioManager
import android.media.ToneGenerator
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.ProgressBar
import android.widget.TextView
import androidx.fragment.app.Fragment
import com.pubnub.api.PNConfiguration
import com.pubnub.api.PubNub
import com.pubnub.api.callbacks.PNCallback
import com.pubnub.api.callbacks.SubscribeCallback
import com.pubnub.api.models.consumer.PNPublishResult
import com.pubnub.api.models.consumer.PNStatus
import com.pubnub.api.models.consumer.pubsub.PNMessageResult
import com.pubnub.api.models.consumer.pubsub.PNPresenceEventResult
import com.se3.payme.ApiClient
import com.se3.payme.Data.MyData
import com.se3.payme.R
import com.se3.payme.models.FundTransferRequest
import com.se3.payme.models.Users
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.util.*


class LoadPayment : Fragment() {
    private val scope = CoroutineScope(context = Dispatchers.Main)
    private lateinit var progressBar:ProgressBar
    private lateinit var blueSuccess:ImageView
    private lateinit var text:TextView
    var pubnub: PubNub? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_load_payment, container, false)
        progressBar = view.findViewById(R.id.loadpayment)
        text = view.findViewById(R.id.payText)
        blueSuccess = view.findViewById(R.id.blueSucces)
        val funderTransfer = FundTransferRequest(MyData.user.email.toString(),MyData.toAccount.toString(),
            MyData.fundTransfer!!,MyData.ref!!)
        initPubNub()

        executeCall(funderTransfer,view)
        return  view
    }
    fun initPubNub() {
        val pnConfiguration = PNConfiguration()
        pnConfiguration.publishKey = "pub-c-e6f1d7b9-bddd-470a-9821-4d300a13abc6" // REPLACE with your pub key
        pnConfiguration.subscribeKey = "sub-c-238756a8-f160-4f38-8aed-3dbbab8ea21c" // REPLACE with your sub key
        pnConfiguration.isSecure = true
        pubnub = PubNub(pnConfiguration)

        // Listen to messages that arrive on the channel
        pubnub!!.addListener(object : SubscribeCallback() {
            override fun status(pub: PubNub, status: PNStatus) {}
            override fun message(pub: PubNub, message: PNMessageResult) {
                // Replace double quotes with a blank space
                val msg = message.message.toString().replace("\"", "")
//                textView = findViewById<View>(R.id.animalSound)
                scope.launch {
                    try {
                        // Display the message on the app
//                        textView.setText(msg)
                    } catch (e: java.lang.Exception) {
                        println("Error")
                        e.printStackTrace()
                    }
                }
            }

            override fun presence(pub: PubNub, presence: PNPresenceEventResult) {}
        })

        // Subscribe to the global channel
        pubnub!!.subscribe()
            .channels(Arrays.asList("global_channel"))
            .execute()
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
                    MyData.user = Users(content!!.usersId,content!!.firstName,content!!.lastName,content.email,content.password,content.accountNumber,content.tel,content.locked,content.enabled,content.balance,content.qrCode,content.pin,content.friends,
                        content.transactions,content.postProjects,content.bankCards
                    )
                    MyData.token = tokenn
                    progressBar.visibility = View.GONE
                }else{
                    val dlgAlert: android.app.AlertDialog.Builder =
                        android.app.AlertDialog.Builder(view.context)

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
                    android.app.AlertDialog.Builder(view.context)

                dlgAlert.setMessage("Payment successful but could not update your data")
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
    fun publishMessage(animal_sound: String?) {
        // Publish message to the global chanel
        pubnub?.publish()
            ?.message(animal_sound)




            ?.channel("global_channel")
            ?.async(object : PNCallback<PNPublishResult?>() {
                override fun onResponse(result: PNPublishResult?, status: PNStatus) {
                    // status.isError() to see if error happened and print status code if error
                    if (status.isError) {
                        println("pub status code: " + status.statusCode)
                    }
                }
            })
    }
    private fun getUserDetailsss(emaill:String,token:String?){
        scope.launch {
            try {
                val header = mutableMapOf<String,String>()
                val tokenn = ""+token
                header["Authorization"]=tokenn
                val response = ApiClient.apiService.findUserByEmail(headers =header ,emaill)
                if (response.isSuccessful && response.body()!=null){
                    val content = response.body()
//                    MyData.user = Users(content!!.usersId,content!!.firstName,content!!.lastName,content.email,content.password,content.accountNumber,content.tel,content.locked,content.enabled,content.balance,content.qrCode,content.pin,content.friends,
//                        content.transactions,content.postProjects,content.bankCards
//                    )
//                    MyData.token = tokenn
//                    progressBar.visibility = View.GONE
//                    next()
                    publishMessage("${content?.qrCode},Received ${MyData.fundTransfer}XAF from ${MyData.user.firstName}");

                }else{
                    val dlgAlert: android.app.AlertDialog.Builder =
                        android.app.AlertDialog.Builder(activity)

                    dlgAlert.setMessage("fool}")
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
                    android.app.AlertDialog.Builder(activity)

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
    private  fun executeCall(funderTransfer: FundTransferRequest,view: View){
        scope.launch {
            try {
                val header = mutableMapOf<String,String>()
                val tokenn = MyData.token.toString()
                header["Authorization"]=tokenn
                val response = ApiClient.apiService.fundTransfer(headers=header,funderTransfer)

                if (response.isSuccessful && response.body() != null) {
                    val content = response.body()
                    Log.d("Tag", "content ${response.body()}")
//                progressBar.visibility = View.GONE
                    progressBar.visibility = View.GONE
//                    val dlgAlert: android.app.AlertDialog.Builder =
//                        android.app.AlertDialog.Builder(activity)
//                    dlgAlert.setIcon(R.drawable.ic_tick_24)
//                    dlgAlert.setPositiveButton("OK", null)
//                    dlgAlert.setCancelable(true)
//                    dlgAlert.create().show()
//
//                    dlgAlert.setPositiveButton("Ok",

//                        DialogInterface.OnClickListener { dialog, which -> })

                    val toneGen1 = ToneGenerator(AudioManager.STREAM_MUSIC, 100)
                    toneGen1.startTone(ToneGenerator.TONE_CDMA_PIP, 150)
                    text.text = "Done"
                    blueSuccess.visibility = View.VISIBLE

                    getUserDetailsss(funderTransfer.toAccount,MyData.token.toString())
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