package com.se3.payme.BottomNavScreen

import android.animation.Animator
import android.animation.AnimatorListenerAdapter
import android.content.Intent
import android.graphics.BlurMaskFilter
import android.hardware.biometrics.BiometricPrompt
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.Switch
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.muryno.cardfinder.view.activity.ScanCard

import com.se3.payme.BottomSheet.ModalBottomSheet
import com.se3.payme.ContaclessPayment.SenderActivity
import com.se3.payme.Data.MyData
//import com.se3.payme.NFC.NfcScanner

import com.se3.payme.R
import com.se3.payme.SWITCHCHECK
//import com.se3.payme.ScanCard.ScanCard


class CashBalance :Fragment(){

    private lateinit var cashOutButton:Button
    private lateinit var balanceText:TextView

//    private lateinit var linkAccount:View
    private var isCheckHide:Boolean = false
    private lateinit var userFirst:TextView
    private lateinit var bankA:TextView
    private lateinit var addCash:Button
    private lateinit var nfcReceveiver:ImageView

 val modalBottomSheet = ModalBottomSheet()

    override fun onCreateView(
            inflater: LayoutInflater,
            container: ViewGroup?,
            savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.activity_cash_balance, container, false)
        initializeValue(view)
        nfcReceveiver = view.findViewById(R.id.receiveNFC)
        nfcReceveiver.setOnClickListener {
            val intent = Intent(view.context,SenderActivity::class.java)
            startActivity(intent)

        }
        onClick()
//        onTouchSwitch()
        onClickButton()

        return view

    }

    private fun onClick(){
//        linkAccount.setOnClickListener {
////           findNavController().navigate(R.id.action_CashBalance_to_ScanCard)
//val intent = Intent(activity, ScanCard::class.java)
//            startActivity(intent)
//        }

        addCash.setOnClickListener {
            findNavController().navigate(R.id.action_CashBalance_to_LinkAccountFragment,null)
        }
    }

    private fun initializeValue(view: View) {
        addCash = view.findViewById(R.id.add_cash_button)
        balanceText = view.findViewById(R.id.balance_text)
        cashOutButton = view.findViewById(R.id.cashOutButton)
//        linkAccount = view.findViewById(R.id.linkAccount)
        bankA = view.findViewById(R.id.bankNum)
        userFirst =view.findViewById(R.id.userFirst)
        bankA.text = MyData.user?.accountNumber
        userFirst.text = MyData.user?.firstName?.get(0).toString() + "."+MyData.user?.lastName
        balanceText.text =  MyData.user!!.balance.toString()+" XAF"
//        tel.text = MyData.user?.tel.toString()

    }

    override fun onStart() {
        super.onStart()

    }
    private fun onClickButton(){
        cashOutButton.setOnClickListener {
            modalBottomSheet.show(getParentFragmentManager(), ModalBottomSheet.TAG)



        }
    }

    override fun onSaveInstanceState(outState: Bundle) {
        outState.putBoolean(SWITCHCHECK,isCheckHide)
        super.onSaveInstanceState(outState)
    }


}