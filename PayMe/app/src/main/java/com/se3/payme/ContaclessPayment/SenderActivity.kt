package com.se3.payme.ContaclessPayment

import android.content.Context
import android.content.DialogInterface
import androidx.appcompat.app.AppCompatActivity
import com.se3.payme.ContaclessPayment.OutcomingNfcManager.NfcActivity
import android.widget.TextView
import android.nfc.NfcAdapter
import com.se3.payme.ContaclessPayment.OutcomingNfcManager
import android.os.Bundle
import com.se3.payme.R
import android.widget.Toast
import android.content.Intent
import android.media.MediaPlayer
import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.ImageView
import com.pubnub.api.PNConfiguration
import com.pubnub.api.PubNub
import com.pubnub.api.callbacks.SubscribeCallback
import com.pubnub.api.models.consumer.PNStatus
import com.pubnub.api.models.consumer.pubsub.PNMessageResult
import com.pubnub.api.models.consumer.pubsub.PNPresenceEventResult
import com.se3.payme.ApiClient
import com.se3.payme.Data.MyData
import com.se3.payme.models.Users
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.util.*

class SenderActivity : AppCompatActivity(), NfcActivity {
    //    private TextView tvOutcomingMessage;
    //    private EditText etOutcomingMessage;
    private lateinit var pubnub: PubNub

//    private val btnSetOutcomingMessage: Button? = null
    private lateinit var textNFCMessage: TextView
    private lateinit  var success: ImageView
    private lateinit  var nfcImage: ImageView
    private lateinit  var token: TextView
    private lateinit  var nfcAdapter: NfcAdapter
    private val scope = CoroutineScope(context = Dispatchers.Main)

    private lateinit  var outcomingNfccallback: OutcomingNfcManager
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_sender)
        textNFCMessage = findViewById(R.id.tv_out_label)
        success = findViewById(R.id.success1)
        nfcImage = findViewById(R.id.nfcImage)
        token = findViewById(R.id.token)
        if (!isNfcSupported) {
            Toast.makeText(this, "Nfc is not supported on this device", Toast.LENGTH_SHORT).show()
            finish()
        }
        if (!nfcAdapter!!.isEnabled) {
            Toast.makeText(
                this,
                "NFC disabled on this device. Turn on to proceed",
                Toast.LENGTH_SHORT
            ).show()
        }
        initViews()
        initPubNub()

        // encapsulate sending logic in a separate class
        outcomingNfccallback = OutcomingNfcManager(this)
        nfcAdapter!!.setOnNdefPushCompleteCallback(outcomingNfccallback, this)
        nfcAdapter!!.setNdefPushMessageCallback(outcomingNfccallback, this)
    }

    private fun initViews() {
//        this.tvOutcomingMessage = findViewById(R.id.tv_out_message);
//        this.etOutcomingMessage = findViewById(R.id.et_message);
//        this.btnSetOutcomingMessage = findViewById(R.id.btn_set_out_message);
//        this.btnSetOutcomingMessage.setOnClickListener((v) -> setOutGoingMessage());
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
                runOnUiThread {
                    try {

                        if(msg.contains(MyData.user.qrCode.toString())){


//                            success.visibility = View.VISIBLE
                            Log.d("TAGVERIFY","Here is the message ${msg}")
                            getUserDetail(this@SenderActivity,msg)


                        }else{

                        }
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

    private fun getUserDetail(context: Context,msg:String){
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
                    MediaPlayer.create(this@SenderActivity,R.raw.ios).start()

                    textNFCMessage.text = msg.substring(msg.lastIndexOf(',') + 1);
                    nfcImage.visibility= View.GONE
                    success.visibility=View.VISIBLE
//                    progressBar.visibility = View.GONE
                }else{
                    val dlgAlert: android.app.AlertDialog.Builder =
                        android.app.AlertDialog.Builder(context)

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
                    android.app.AlertDialog.Builder(context)

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

    override fun onNewIntent(intent: Intent) {
        super.onNewIntent(intent)
        setIntent(intent)
    }

    private val isNfcSupported: Boolean
        private get() {
            nfcAdapter = NfcAdapter.getDefaultAdapter(this)
            return nfcAdapter != null
        }

    private fun setOutGoingMessage() {
        val outMessage = MyData.user.qrCode
        //        this.tvOutcomingMessage.setText(outMessage);
    }

    override fun getOutcomingMessage(): String {
        return MyData.user.qrCode.toString()
    }

    override fun signalResult() {
        // this will be triggered when NFC message is sent to a device.
        // should be triggered on UI thread. We specify it explicitly
        // cause onNdefPushComplete is called from the Binder thread
        nfcImage!!.visibility = View.GONE
        textNFCMessage!!.visibility = View.GONE
        success!!.visibility = View.VISIBLE
        token!!.visibility = View.VISIBLE
        runOnUiThread {
            Toast.makeText(
                this@SenderActivity,
                R.string.message_beaming_complete,
                Toast.LENGTH_SHORT
            ).show()
        }
    }
}