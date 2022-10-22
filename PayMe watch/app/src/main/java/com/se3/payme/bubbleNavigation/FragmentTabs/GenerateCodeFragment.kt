package com.se3.payme.bubbleNavigation.FragmentTabs

import android.app.Notification
import android.app.PendingIntent
import android.content.Context
import android.content.DialogInterface
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.Color
import android.media.MediaPlayer
import android.os.Bundle
import android.os.CountDownTimer
import android.os.Handler
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.core.app.NotificationCompat
import androidx.fragment.app.Fragment
import com.google.zxing.BarcodeFormat
import com.google.zxing.MultiFormatWriter
import com.google.zxing.WriterException
import com.pubnub.api.PNConfiguration
import com.pubnub.api.PubNub
import com.pubnub.api.callbacks.SubscribeCallback
import com.pubnub.api.models.consumer.PNStatus
import com.pubnub.api.models.consumer.pubsub.PNMessageResult
import com.pubnub.api.models.consumer.pubsub.PNPresenceEventResult
import com.se3.payme.ApiClient
import com.se3.payme.Data.MyData
import com.se3.payme.MainActivity
import com.se3.payme.R
import com.se3.payme.models.Users
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import okhttp3.internal.notify
import java.util.*


class GenerateCodeFragment : Fragment() {
    private  val TAG = "GenerateCode"
    private lateinit var pubnub: PubNub
    private lateinit var textSnd:TextView
    private lateinit var countDown:TextView
    private val scope = CoroutineScope(context = Dispatchers.Main)
    private lateinit var bitmap: Bitmap
private  lateinit var imageQr:ImageView
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val  view = inflater.inflate(R.layout.fragment_generate_code, container, false)
         imageQr = view.findViewById<ImageView>(R.id.QrCodeImage)
        textSnd = view.findViewById(R.id.textSnd)
        var text = MyData.user?.qrCode.toString()
        bitmap = generateQRCode(text)
        imageQr.setImageBitmap(bitmap)
        countDown = view.findViewById(R.id.countDown)
        initPubNub(view)
        return view
    }
    private fun generateQRCode(text: String): Bitmap {
        val width = 1300
        val height = 1300
        val bitmap = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888)
        val codeWriter = MultiFormatWriter()
        try {
            val bitMatrix =
                codeWriter.encode(text, BarcodeFormat.QR_CODE, width, height)
            for (x in 0 until width) {
                for (y in 0 until height) {
                    val color = if (bitMatrix[x, y]) Color.BLACK else Color.WHITE
                    bitmap.setPixel(x, y, color)
                }
            }
        } catch (e: WriterException) {

            Log.d(TAG, "generateQRCode: ${e.message}")

        }
        return bitmap
    }
    private fun createNotificationChannels(view: View) {
        val ctx = view.context
        val intent = Intent(ctx, MainActivity::class.java)
        val contentIntent =
            PendingIntent.getActivity(ctx, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT)

        val b = NotificationCompat.Builder(ctx)

        b.setAutoCancel(true)
            .setDefaults(Notification.DEFAULT_ALL)
            .setWhen(System.currentTimeMillis())
            .setSmallIcon(R.drawable.done_icon)
            .setTicker("Hearty365")
            .setContentTitle("Default notification")
            .setContentText("Hi ${MyData.user.firstName} you just receive some money from a friend")
            .setDefaults(Notification.DEFAULT_LIGHTS or Notification.DEFAULT_SOUND)
            .setContentIntent(contentIntent)
            .setContentInfo("Info")


        ctx.getSystemService(Context.NOTIFICATION_SERVICE).notify()
    }
    fun initPubNub(view: View) {
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
                Log.d("TAGMSG","$msg")
                scope.launch {
                    try {
                        if(msg.contains(MyData.user.qrCode.toString())){

                            textSnd.text = msg.substring(msg.lastIndexOf(',') + 1);
                            imageQr.setImageResource(R.drawable.success)
                            Log.d("TAGVERIFY","Here is the message  ${msg}")
                            MediaPlayer.create(view.context,R.raw.ios).start()

                            getUserDetail(view.context)
                            object : CountDownTimer(10000, 1000) {
                                override fun onTick(millisUntilFinished: Long) {
                                    countDown.setText("seconds remaining: " + millisUntilFinished / 1000)
                                    // logic to set the EditText could go here
                                }

                                override fun onFinish() {
                                    countDown.text=""
                                    imageQr.setImageBitmap(bitmap)
                                    textSnd.text = ""

                                }
                            }.start()
                            createNotificationChannels(view)

                        }else{
                            Log.d("TAGVERIFY","Nothing was scanned")

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
                    MyData.user = Users(content!!.usersId,content!!.firstName,content!!.lastName,content.email,content.password,content.accountNumber,content.tel,content.locked,content.enabled,content.balance,content.qrCode,content.pin,
                        content.friends,content.transactions,content.postProjects,content.bankCards
                    )
                    MyData.token = tokenn
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
}