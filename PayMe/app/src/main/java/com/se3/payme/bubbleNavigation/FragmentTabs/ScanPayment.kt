package com.se3.payme.bubbleNavigation.FragmentTabs

import android.app.Activity
import android.media.MediaPlayer
import android.os.Bundle
import android.provider.SyncStateContract
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.google.zxing.Result
import com.se3.payme.R
import me.dm7.barcodescanner.zxing.ZXingScannerView


class ScanPayment : Activity(),ZXingScannerView.ResultHandler {
    // TODO: Rename and change types of parameters
    private var mScannerView: ZXingScannerView? = null


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mScannerView = ZXingScannerView(this)
        setContentView(mScannerView)
        mScannerView!!.setResultHandler(this)
        mScannerView!!.startCamera()

    }

//    override fun onCreateView(
//        inflater: LayoutInflater, container: ViewGroup?,
//        savedInstanceState: Bundle?
//    ): View? {
//        // Inflate the layout for this fragment
//        return inflater.inflate(R.layout.fragment_scan_payment, container, false)
//    }
    override fun onResume() {
        super.onResume()
    }
    private fun cleanResult(){
        try {
            mScannerView!!.stopCamera()
        }catch (e:Exception){
            Log.d("Constant.Error", e.message.toString())
        }
    }

    override fun onPause() {
        super.onPause()
        this.cleanResult()
    }
    override fun handleResult(rawResult: Result) {
        try{
            mScannerView!!.stopCamera()

            MediaPlayer.create(this,R.raw.ios).start()
        }catch (e:Exception){

        }
        TODO("Not yet implemented")
    }


}