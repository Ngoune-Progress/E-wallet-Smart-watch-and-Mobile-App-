package com.se3.payme.ContaclessPayment

import android.R
import android.app.PendingIntent
import android.content.Context
import android.content.DialogInterface
import android.content.Intent
import android.content.IntentFilter
import android.content.IntentFilter.MalformedMimeTypeException
import android.media.MediaPlayer
import android.nfc.NdefMessage
import android.nfc.NfcAdapter
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.ImageView
import android.widget.ProgressBar
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.constraintlayout.widget.ConstraintLayout
import com.google.android.material.snackbar.Snackbar
import com.pubnub.api.PNConfiguration
import com.pubnub.api.PubNub
import com.pubnub.api.callbacks.PNCallback
import com.pubnub.api.callbacks.SubscribeCallback
import com.pubnub.api.models.consumer.PNPublishResult
import com.pubnub.api.models.consumer.PNStatus
import com.pubnub.api.models.consumer.pubsub.PNMessageResult
import com.pubnub.api.models.consumer.pubsub.PNPresenceEventResult
import com.qusion.lib_pindotview.PinDotView
import com.se3.payme.ApiClient
import com.se3.payme.Data.MyData
import com.se3.payme.models.FunderTransferByQrCode
import com.se3.payme.models.Users
import kotlinx.coroutines.*
import java.util.*


class ReceiverActivity : AppCompatActivity() {
    var pubnub: PubNub? = null

    private val scope = CoroutineScope(context = Dispatchers.Main)
        private lateinit var progressBar:ProgressBar
        private lateinit var imageNf:ImageView
        private lateinit var text:TextView
        private lateinit var succes:ImageView
        private lateinit var nfcFrame:ConstraintLayout
    private lateinit var nfcFrame2:ConstraintLayout
    private lateinit var pinDotView: PinDotView


    private var tvIncomingMessage: TextView? = null
    private var nfcAdapter: NfcAdapter? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(com.se3.payme.R.layout.activity_receiver)
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

    // need to check NfcAdapter for nullability. Null means no NFC support on the device
    private val isNfcSupported: Boolean
        private get() {
            nfcAdapter = NfcAdapter.getDefaultAdapter(this)

            return nfcAdapter != null
        }

    private fun initViews() {
        tvIncomingMessage = findViewById(com.se3.payme.R.id.tv_out_label1)
        progressBar = findViewById(com.se3.payme.R.id.progressBarP)
        imageNf = findViewById(com.se3.payme.R.id.nfcImage1)
        text = findViewById(com.se3.payme.R.id.tv_out_label1)
        succes = findViewById(com.se3.payme.R.id.success1)
        nfcFrame = findViewById(com.se3.payme.R.id.nfcFrame)
        nfcFrame2 = findViewById(com.se3.payme.R.id.nfcFrame2)
        pinDotView = findViewById(com.se3.payme.R.id.nfcPinView)


    }

    override fun onNewIntent(intent: Intent) {
        // also reading NFC message from here in case this activity is already started in order
        // not to start another instance of this activity
        super.onNewIntent(intent)
        receiveMessageFromDevice(intent)
    }

    override fun onResume() {
        super.onResume()

        // foreground dispatch should be enabled here, as onResume is the guaranteed place where app
        // is in the foreground
        enableForegroundDispatch(this, nfcAdapter)
        receiveMessageFromDevice(intent)
    }

    override fun onPause() {
        super.onPause()
        disableForegroundDispatch(this, nfcAdapter)
    }

    private fun receiveMessageFromDevice(intent: Intent) {
        val action = intent.action
        if (NfcAdapter.ACTION_NDEF_DISCOVERED == action) {
            val parcelables = intent.getParcelableArrayExtra(NfcAdapter.EXTRA_NDEF_MESSAGES)
            val inNdefMessage = parcelables!![0] as NdefMessage
            val inNdefRecords = inNdefMessage.records
            val ndefRecord_0 = inNdefRecords[0]
            val inMessage = String(ndefRecord_0.payload)
            tvIncomingMessage!!.text = inMessage
//            imageNf.visibility = View.GONE
            text.visibility = View.GONE
            progressBar.visibility = View.VISIBLE


            val funderTransferByQrCode = FunderTransferByQrCode(MyData.user?.email,inMessage,MyData.fundTransfer)
            onQrResult(inMessage)
        }
    }

    private fun onQrResult(contents: String) {
        if (contents==null){
//            showToast("Empty Qr Result")
        }
//            saveToDataBase(contents)

//            showToast("The is a result ${contents}")

        nfcFrame.visibility = View.GONE
        if(contents == MyData.user.qrCode){
            val dlgAlert: android.app.AlertDialog.Builder = android.app.AlertDialog.Builder(this)

            dlgAlert.setMessage("It is impossible for you to send fund to your own account")
            dlgAlert.setTitle("Bad Request")
            dlgAlert.setPositiveButton("OK", null)
            dlgAlert.setCancelable(true)
            dlgAlert.create().show()

            dlgAlert.setPositiveButton("Ok",
                DialogInterface.OnClickListener { dialog, which -> })
            progressBar.visibility =View.GONE
            nfcFrame.visibility = View.VISIBLE
        }else {
            nfcFrame.visibility = View.GONE
            nfcFrame2.visibility = View.VISIBLE

            pinDotView.setOnCompletedListener { pin ->
                if(pin!=MyData.user.pin.toString()) {
                    GlobalScope.launch {
                        delay(1000)
                        withContext(Dispatchers.Main) {
                            pinDotView.showErrorAnimation(clearPin = true)
                        }
                    }
                }
                else{
                    nfcFrame2.visibility = View.GONE

                    progressBar.visibility = View.VISIBLE

                    val funderTransferByQrCode = FunderTransferByQrCode(MyData.user?.email,contents,MyData.fundTransfer)
                    executeCall(funderTransferByQrCode)
                }
                //Snackbar.make(pinDotView, pin, Snackbar.LENGTH_SHORT).show()
            }

            pinDotView.setOnBiometricsButtonClickedListener {
                Snackbar.make(pinDotView, "BIOMETRICS", Snackbar.LENGTH_SHORT).show()
            }

            pinDotView.setOnForgotButtonClickedListener {
                Snackbar.make(pinDotView, "FORGOT", Snackbar.LENGTH_SHORT).show()
            }

        }

    }
    private  fun executeCall(funderTransferByQrCode: FunderTransferByQrCode){
        scope.launch {
            try {
                val header = mutableMapOf<String,String>()
                val tokenn = MyData.token.toString()
                header["Authorization"]=tokenn
                val response = ApiClient.apiService.fundTransferByQrCode(headers=header,funderTransferByQrCode)

                if (response.isSuccessful && response.body() != null) {
                    val content = response.body()
//                    Log.d("Tag", "content ${response.body()}")
////                progressBar.visibility = View.GONE

                    getUserDetail(applicationContext)
//                    succes.visibility = View.VISIBLE


//                    val dlgAlert: android.app.AlertDialog.Builder =
//                        android.app.AlertDialog.Builder(applicationContext)
//                    dlgAlert.setIcon(R.drawable.ic_tick_24)
//                    dlgAlert.setPositiveButton("OK", null)
//                    dlgAlert.setCancelable(true)
                    publishMessage("${funderTransferByQrCode.toAccountQrCode},Received ${MyData.fundTransfer.toString()}XAF from ${MyData.user.firstName.toString()}");

//                    dlgAlert.create().show()
//
//                    dlgAlert.setPositiveButton("Ok",
//                        DialogInterface.OnClickListener { dialog, which -> })


                } else {
                    val dlgAlert: android.app.AlertDialog.Builder =
                        android.app.AlertDialog.Builder(applicationContext)

                    dlgAlert.setMessage("Insufficient Fund")
                    dlgAlert.setTitle("Error Message...")
                    dlgAlert.setPositiveButton("OK", null)
                    dlgAlert.setCancelable(true)
                    dlgAlert.create().show()

                    dlgAlert.setPositiveButton("Ok",
                        DialogInterface.OnClickListener { dialog, which -> })
                    progressBar.visibility = View.GONE
                    imageNf.visibility = View.VISIBLE
                    text.visibility = View.VISIBLE



                }
            } catch (e: Exception) {
                Log.d("Tag", "In the catch ${e.message}")

                val dlgAlert: android.app.AlertDialog.Builder =
                    android.app.AlertDialog.Builder(applicationContext)

                dlgAlert.setMessage("Please verify if you have internet connection")
                dlgAlert.setTitle("Error Message...")
                dlgAlert.setPositiveButton("OK", null)
                dlgAlert.setCancelable(true)
                dlgAlert.create().show()

                dlgAlert.setPositiveButton("Ok",
                    DialogInterface.OnClickListener { dialog, which -> })
                progressBar.visibility = View.GONE
                imageNf.visibility = View.VISIBLE
                text.visibility = View.VISIBLE



            }



        }

    }
    private fun getUserDetail(context: Context){
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
                    progressBar.visibility = View.GONE
                    nfcFrame.visibility=View.VISIBLE
                    imageNf.setImageResource(com.se3.payme.R.drawable.success)
                    MediaPlayer.create(this@ReceiverActivity, com.se3.payme.R.raw.ios).start()
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
                    progressBar.visibility = View.GONE
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
    // Foreground dispatch holds the highest priority for capturing NFC intents
    // then go activities with these intent filters:
    // 1) ACTION_NDEF_DISCOVERED
    // 2) ACTION_TECH_DISCOVERED
    // 3) ACTION_TAG_DISCOVERED
    // always try to match the one with the highest priority, cause ACTION_TAG_DISCOVERED is the most
    // general case and might be intercepted by some other apps installed on your device as well
    // When several apps can match the same intent Android OS will bring up an app chooser dialog
    // which is undesirable, because user will most likely have to move his device from the tag or another
    // NFC device thus breaking a connection, as it's a short range
    fun enableForegroundDispatch(activity: AppCompatActivity, adapter: NfcAdapter?) {

        // here we are setting up receiving activity for a foreground dispatch
        // thus if activity is already started it will take precedence over any other activity or app
        // with the same intent filters
        val intent = Intent(activity.applicationContext, activity.javaClass)
        intent.flags = Intent.FLAG_ACTIVITY_SINGLE_TOP

        //
        val pendingIntent = PendingIntent.getActivity(activity.applicationContext, 0, intent, 0)
        val filters = arrayOfNulls<IntentFilter>(1)
        val techList = arrayOf<Array<String>>()
        filters[0] = IntentFilter()
        filters[0]!!.addAction(NfcAdapter.ACTION_NDEF_DISCOVERED)
        filters[0]!!.addCategory(Intent.CATEGORY_DEFAULT)
        try {
            filters[0]!!.addDataType(MIME_TEXT_PLAIN)
        } catch (ex: MalformedMimeTypeException) {
            throw RuntimeException("Check your MIME type")
        }
        adapter!!.enableForegroundDispatch(activity, pendingIntent, filters, techList)
    }

    fun disableForegroundDispatch(activity: AppCompatActivity?, adapter: NfcAdapter?) {
        adapter!!.disableForegroundDispatch(activity)
    }

    companion object {
        const val MIME_TEXT_PLAIN = "text/plain"
    }
}