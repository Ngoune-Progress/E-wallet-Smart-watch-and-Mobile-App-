package com.se3.payme.bubbleNavigation.FragmentTabs

import android.Manifest
import android.content.DialogInterface
import android.content.pm.PackageManager
import android.media.MediaPlayer

import android.os.Build
import android.os.Bundle
import android.os.CountDownTimer
import android.os.Handler
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.core.content.PermissionChecker
import androidx.core.content.PermissionChecker.checkSelfPermission
import androidx.fragment.app.Fragment
import com.budiyev.android.codescanner.CodeScanner
import com.budiyev.android.codescanner.CodeScannerView
import com.budiyev.android.codescanner.DecodeCallback
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
import com.se3.payme.R
import com.se3.payme.Secure.SecurityPassword
import com.se3.payme.models.FunderTransferByQrCode
import com.se3.payme.models.Users
import kotlinx.coroutines.*
import me.dm7.barcodescanner.zbar.Result
import me.dm7.barcodescanner.zbar.ZBarScannerView
import java.util.*


class ScanCodeFragment : Fragment() {
        companion object {
            private const val CAMERA_PERMISSION_CODE = 100
            private const val STORAGE_PERMISSION_CODE = 101
        }

        private  lateinit var mView: View

        private lateinit var codeScanner: CodeScanner
        private val scope = CoroutineScope(context = Dispatchers.Main)
        private lateinit var blueSuccess:ImageView
        private  lateinit var  containerScanner: CodeScannerView
        private lateinit var count:TextView
        private  lateinit var  frameLayout1: ConstraintLayout
        private  lateinit var  frameLayout2: ConstraintLayout
        private lateinit var pinDotView: PinDotView


        var pubnub: PubNub? = null
        private lateinit var progressBar: ProgressBar
        override fun onCreateView(
            inflater: LayoutInflater, container: ViewGroup?,
            savedInstanceState: Bundle?
        ): View {
             mView = inflater.inflate(R.layout.fragment_scan_code, container, false)
            frameLayout1 = mView.findViewById(R.id.frame1)
            frameLayout2 = mView.findViewById(R.id.frame2)
            pinDotView = mView.findViewById(R.id.pinView1)
            blueSuccess = mView.findViewById(R.id.blueSucces1)
            count = mView.findViewById(R.id.count)

            progressBar = mView.findViewById(R.id.scanCodeProgressBar)
            initPubNub()
            return mView
        }

        override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
            if (ContextCompat.checkSelfPermission(view.context, Manifest.permission.CAMERA) == PackageManager.PERMISSION_DENIED) {
                // Requesting the permission
                ActivityCompat.requestPermissions(requireActivity(), arrayOf(Manifest.permission.CAMERA), CAMERA_PERMISSION_CODE)
            } else {
//                Toast.makeText(this@MainActivity, "Permission already granted", Toast.LENGTH_SHORT).show()
            }
            containerScanner = mView.findViewById<CodeScannerView>(R.id.containerScanner)
            val activity = requireActivity()
            codeScanner = CodeScanner(activity, containerScanner)
            codeScanner.decodeCallback = DecodeCallback {
                activity.runOnUiThread {
//                    Toast.makeText(activity, it.text, Toast.LENGTH_LONG).show()
//                                SecurityPassword()
                        onQrResult(it.text,mView)
                }
            }
            containerScanner.setOnClickListener {
                codeScanner.startPreview()
            }
        }

        override fun onResume() {
            super.onResume()
            codeScanner.startPreview()
        }

        override fun onPause() {
            codeScanner.releaseResources()
            super.onPause()
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

        private  fun executeCall(funderTransferByQrCode: FunderTransferByQrCode) {
            scope.launch {
                try {

                    val header = mutableMapOf<String, String>()
                    val tokenn = MyData.token.toString()
                    header["Authorization"] = tokenn
                    val response =
                        ApiClient.apiService.fundTransferByQrCode(headers = header, funderTransferByQrCode)

                    if (response.isSuccessful && response.body() != null) {
                        val content = response.body()
                        Log.d("Tag", "content ${response.body()}")
//                progressBar.visibility = View.GONE
                        publishMessage("${funderTransferByQrCode.toAccountQrCode},Received ${MyData.fundTransfer}XAF from ${MyData.user.firstName}");

                        progressBar.visibility = View.GONE

//                val dlgAlert: android.app.AlertDialog.Builder =
//                    android.app.AlertDialog.Builder(activity)
//                dlgAlert.setIcon(R.drawable.ic_tick_24)
//                dlgAlert.setPositiveButton("OK", null)
//                dlgAlert.setCancelable(true)
//                dlgAlert.create().show()
//
//                dlgAlert.setPositiveButton("Ok",
//                    DialogInterface.OnClickListener { dialog, which -> })
                        blueSuccess.visibility = View.VISIBLE
                        MediaPlayer.create(mView.context,R.raw.ios).start()
                        getUserDetail(mView)
                        object : CountDownTimer(10000, 1000) {
                            override fun onTick(millisUntilFinished: Long) {
                                count.setText("seconds remaining: " + millisUntilFinished / 1000)
                                // logic to set the EditText could go here
                            }

                            override fun onFinish() {
                                blueSuccess.visibility = View.GONE

                                count.text=""
                                frameLayout2.visibility = View.GONE
                                frameLayout1.visibility = View.VISIBLE
                                containerScanner.visibility = View.VISIBLE



                            }
                        }.start()


                    } else {
                        val dlgAlert: android.app.AlertDialog.Builder =
                            android.app.AlertDialog.Builder(activity)

                        dlgAlert.setMessage("QrCode is not valid")
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

                    dlgAlert.setMessage("Please verify f if you have internet connection ${e.message}")
                    dlgAlert.setTitle("Error Message...")
                    dlgAlert.setPositiveButton("OK", null)
                    dlgAlert.setCancelable(true)
                    dlgAlert.create().show()

                    dlgAlert.setPositiveButton("Ok",
                        DialogInterface.OnClickListener { dialog, which -> })
                    progressBar.visibility = View.GONE


                }


            }
//
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
        private fun onQrResult(contents: String?,view: View) {
        if (contents==null){
//            showToast("Empty Qr Result")
        }
//            saveToDataBase(contents)

//            showToast("The is a result ${contents}")

        containerScanner.visibility = View.GONE
        if(contents == MyData.user.qrCode){
            val dlgAlert: android.app.AlertDialog.Builder = android.app.AlertDialog.Builder(mView.context)

            dlgAlert.setMessage("It is impossible for you to send fund to your own account")
            dlgAlert.setTitle("Bad Request")
            dlgAlert.setPositiveButton("OK", null)
            dlgAlert.setCancelable(true)
            dlgAlert.create().show()

            dlgAlert.setPositiveButton("Ok",
                DialogInterface.OnClickListener { dialog, which -> })
            progressBar.visibility =View.GONE
            containerScanner.visibility = View.VISIBLE
        }else {
            frameLayout1.visibility = View.GONE
            frameLayout2.visibility = View.VISIBLE

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
                    frameLayout2.visibility = View.GONE

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
    }













//-------------------------------------------------------------------------------------------------------------------------

//
//class ScanCodeFragment : Fragment() ,ZBarScannerView.ResultHandler {

//    val CHANNEL_1_ID = "channel1"
//    val CHANNEL_2_ID = "channel2"
//    companion object {
//
//        fun newInstance(): ScanCodeFragment {
//            return ScanCodeFragment()
//        }
//    }
//
//    private lateinit var mView: View
//private lateinit var containerScanner:FrameLayout
//    lateinit var scannerView: ZBarScannerView

//    private lateinit var scanAmount:TextView
//    private lateinit var blueSuccess:ImageView
//    val PERMISSION_REQUEST_CAMERA = 1
//
//    override fun onCreateView(
//        inflater: LayoutInflater, container: ViewGroup?,
//        savedInstanceState: Bundle?
//    ): View? {
//        // Inflate the layout for this fragment
//
//         mView = inflater.inflate(R.layout.fragment_scan_code, container, false)
//        progressBar = mView.findViewById(R.id.scanCodeProgressBar)
//        scanAmount = mView.findViewById(R.id.scanAmount)
//        blueSuccess = mView.findViewById(R.id.blueSucces1)
//
//        scanAmount.text ="$"+MyData.fundTransfer.toString()+"0"
//        containerScanner = mView.findViewById(R.id.containerScanner)
//initViews()
//        return mView.rootView
//
//    }
//
//
//    private fun initViews() {
//        initializeQRCamera()
////        setResultDialog(view)
//    }
//
//    private fun initializeQRCamera() {
//        scannerView = ZBarScannerView(context)
//        scannerView.setResultHandler(this)
//        scannerView.setBackgroundColor(ContextCompat.getColor(mView.context!!, R.color.white))
//        scannerView.setBorderColor(ContextCompat.getColor(mView.context!!, R.color.black))
//        scannerView.setLaserColor(ContextCompat.getColor(mView.context!!, R.color.black))
//        scannerView.setBorderStrokeWidth(10)
//        scannerView.setSquareViewFinder(true)
//        scannerView.setupScanner()
//        scannerView.setAutoFocus(true)
////        startQRCamera()
//
////        mView.containerScanner.addView(scannerView)
//        mView.findViewById<FrameLayout>(R.id.containerScanner).addView(scannerView)
//        if (!haveCameraPermission())
//            requestPermissions( arrayOf(Manifest.permission.CAMERA), PERMISSION_REQUEST_CAMERA);
//    }
//
//
////    private fun setResultDialog(view: View) {
////        resultDialog = QrCodeResultDialog(view.context!!)
////        resultDialog.setOnDismissListener(object : QrCodeResultDialog.OnDismissListener {
////            override fun onDismiss() {
////                resetPreview()
////            }
////        })
////    }


//
//private  fun executeCall(funderTransferByQrCode: FunderTransferByQrCode) {
//    scope.launch {
//        try {
//
//            val header = mutableMapOf<String, String>()
//            val tokenn = MyData.token.toString()
//            header["Authorization"] = tokenn
//            val response =
//                ApiClient.apiService.fundTransferByQrCode(headers = header, funderTransferByQrCode)
//
//            if (response.isSuccessful && response.body() != null) {
//                val content = response.body()
//                Log.d("Tag", "content ${response.body()}")
////                progressBar.visibility = View.GONE
//                publishMessage("Receive fund  ${funderTransferByQrCode.toAccountQrCode}");
//
//                progressBar.visibility = View.GONE
//
////                val dlgAlert: android.app.AlertDialog.Builder =
////                    android.app.AlertDialog.Builder(activity)
////                dlgAlert.setIcon(R.drawable.ic_tick_24)
////                dlgAlert.setPositiveButton("OK", null)
////                dlgAlert.setCancelable(true)
////                dlgAlert.create().show()
////
////                dlgAlert.setPositiveButton("Ok",
////                    DialogInterface.OnClickListener { dialog, which -> })
//                blueSuccess.visibility = View.VISIBLE
//                getUserDetail(mView)
//
//            } else {
//                val dlgAlert: android.app.AlertDialog.Builder =
//                    android.app.AlertDialog.Builder(activity)
//
//                dlgAlert.setMessage("QrCode is not valid")
//                dlgAlert.setTitle("Error Message...")
//                dlgAlert.setPositiveButton("OK", null)
//                dlgAlert.setCancelable(true)
//                dlgAlert.create().show()
//
//                dlgAlert.setPositiveButton("Ok",
//                    DialogInterface.OnClickListener { dialog, which -> })
//                progressBar.visibility = View.GONE
//
//
//            }
//        } catch (e: Exception) {
//            Log.d("Tag", "In the catch ${e.message}")
//
//            val dlgAlert: android.app.AlertDialog.Builder =
//                android.app.AlertDialog.Builder(activity)
//
//            dlgAlert.setMessage("Please verify if you have internet connection")
//            dlgAlert.setTitle("Error Message...")
//            dlgAlert.setPositiveButton("OK", null)
//            dlgAlert.setCancelable(true)
//            dlgAlert.create().show()
//
//            dlgAlert.setPositiveButton("Ok",
//                DialogInterface.OnClickListener { dialog, which -> })
//            progressBar.visibility = View.GONE
//
//
//        }
//
//
//    }
////
//}
//
//    override fun handleResult(rawResult: Result?) {
//        onQrResult(rawResult?.contents)
//    }
//
//    private fun onQrResult(contents: String?) {
//        if (contents==null)
//            showToast("Empty Qr Result")
//        else
////            saveToDataBase(contents)
//
//            showToast("The is a result ${contents}")
//
//        containerScanner.visibility = View.GONE
//        progressBar.visibility = View.VISIBLE
//        if(contents == MyData.user.qrCode){
//            val dlgAlert: android.app.AlertDialog.Builder = android.app.AlertDialog.Builder(mView.context)
//
//            dlgAlert.setMessage("It is impossible for you to send fund to your own account")
//            dlgAlert.setTitle("Bad Request")
//            dlgAlert.setPositiveButton("OK", null)
//            dlgAlert.setCancelable(true)
//            dlgAlert.create().show()
//
//            dlgAlert.setPositiveButton("Ok",
//                DialogInterface.OnClickListener { dialog, which -> })
//            progressBar.visibility =View.GONE
//            containerScanner.visibility = View.VISIBLE
//        }else {
//        val funderTransferByQrCode = FunderTransferByQrCode(MyData.user?.email,contents,MyData.fundTransfer)
//            executeCall(funderTransferByQrCode)
//        }
//
//    }
//
//    private fun showToast(message: String) {
//        Toast.makeText(mView.context!!, message, Toast.LENGTH_SHORT).show()
//    }
//
////    private fun saveToDataBase(contents: String) {
////        val insertedResultId = dbHelperI.insertQRResult(contents)
////        val qrResult = dbHelperI.getQRResult(insertedResultId)
////        resultDialog.show(qrResult)
////    }
//    private fun  haveCameraPermission():Boolean
//    {
//        if(Build.VERSION.SDK_INT < 23){
//            return true
//        }
//
//        return checkSelfPermission(mView.context,Manifest.permission.CAMERA)== PermissionChecker.PERMISSION_GRANTED
//    }
//    fun startCamera() {
//        scannerView.setResultHandler(this) // Register ourselves as a handler for scan results.
//        scannerView.startCamera() // Start camera on resume
//    }
//    override fun onRequestPermissionsResult(
//        requestCode: Int,
//        permissions: Array<out String>,
//        grantResults: IntArray
//    ) {
//        if(permissions.size ==0  || grantResults.size ==0)return;
//        when (requestCode) {
//            PERMISSION_REQUEST_CAMERA -> {
//                if (grantResults[0] === PackageManager.PERMISSION_GRANTED) {
//                    startCamera()
//                } else {
//                }
//            }
//        }
//        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
//    }
//    private fun startQRCamera() {
//
//            scannerView.startCamera()
//
//    }
//
//    private fun resetPreview() {
//        scannerView.stopCamera()
//        scannerView.startCamera()
//        scannerView.stopCameraPreview()
//        scannerView.resumeCameraPreview(this)
//    }
//
////    private fun onClicks() {
////        mView.flashToggle.setOnClickListener {
////            if (mView.flashToggle.isSelected) {
////                offFlashLight()
////            } else {
////                onFlashLight()
////            }
////        }
////    }
//
////    private fun onFlashLight() {
////        mView.flashToggle.isSelected = true
////        scannerView.flash = true
////    }
////
////    private fun offFlashLight() {
////        mView.flashToggle.isSelected = false
////        scannerView.flash = false
////    }
//
//    override fun onResume() {
//        super.onResume()
//        scannerView.setResultHandler(this)
//        scannerView.startCamera()
//    }
//
//    override fun onPause() {
//        super.onPause()
//        scannerView.stopCamera()
//    }
//
//    override fun onDestroy() {
//        super.onDestroy()
//        scannerView.stopCamera()
//    }

//
//}

