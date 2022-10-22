package com.se3.payme.BottomNavScreen

import android.content.DialogInterface
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.View.OnLongClickListener
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.commit
import androidx.navigation.fragment.findNavController
import com.se3.payme.BottomSheet.ModalBottomSheet
import com.se3.payme.ContaclessPayment.ReceiverActivity
import com.se3.payme.ContaclessPayment.SenderActivity
import com.se3.payme.Data.MyData
import com.se3.payme.NFC.ScanCardNfc
import com.se3.payme.R
import com.se3.payme.TRANSFER_AMOUNT
import com.se3.payme.bubbleNavigation.BottomBarActivity
import com.se3.payme.bubbleNavigation.ContactPayFragment


class TransferFragment : Fragment() {
   private lateinit var buttonNumber0: TextView
   private lateinit var buttonNumber1: TextView
   private lateinit var buttonNumber2: TextView
   private lateinit var buttonNumber3: TextView
   private lateinit var buttonNumber4: TextView
   private lateinit var buttonNumber5: TextView
   private lateinit var buttonNumber6: TextView
   private lateinit var buttonNumber7: TextView
   private lateinit var buttonNumber8: TextView
   private lateinit var buttonNumber9: TextView
   private lateinit var buttonDot: TextView
   private lateinit var nfcScanner:ImageView
   private lateinit var  textViewInputNumbers:TextView
   private lateinit var cashOutButton:Button
   private lateinit var makePaymentButton:TextView


    private var val1 = Double.NaN
    private var val2 = 0.0
    val modalBottomSheet = ModalBottomSheet()
    private lateinit var Mview:View

    private lateinit var buttonClear: TextView
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
      Mview= inflater.inflate(R.layout.fragment_transfer, container, false)
        makePaymentButton = Mview.findViewById(R.id.pay)
        var saveText = "300"
        initializeViewVariables(Mview)
        textViewInputNumbers.text = "100"
        onClick()
        onClickButton(Mview)
        Mview.findViewById<ImageView>(R.id.scanCode).setOnClickListener {
//            val intent = Intent(activity, BottomBarActivity::class.java)
            val text = textViewInputNumbers.text.toString()
            if(text.isEmpty()){
                MyData.fundTransfer=0.0

            }else{
                MyData.fundTransfer= text.toDouble()
            }
            if(MyData.fundTransfer!! <100){
                val dlgAlert: android.app.AlertDialog.Builder = android.app.AlertDialog.Builder(Mview.context)

                dlgAlert.setMessage("There minimum amount to send is 100 XAF")
                dlgAlert.setTitle("Error")
                dlgAlert.setPositiveButton("OK", null)
                dlgAlert.setCancelable(true)
                dlgAlert.create().show()

                dlgAlert.setPositiveButton("Ok",
                    DialogInterface.OnClickListener { dialog, which -> })
            }else {
//                startActivity(intent)
                findNavController().navigate(
                    R.id.action_Transfer_to_BottomActvity,
                    null
                )
            }
        }

        return Mview
    }

    private fun onClickButton(view: View){
        makePaymentButton.setOnClickListener {
                        val text = textViewInputNumbers.text.toString()
            if(text.isEmpty() || text.equals(".")){
                MyData.fundTransfer=0.0

            }else{
                MyData.fundTransfer= text.toDouble()
            }
            if(MyData.fundTransfer!! <100){
                val dlgAlert: android.app.AlertDialog.Builder = android.app.AlertDialog.Builder(view.context)

                dlgAlert.setMessage("There minimum amount to send is 100 XAF")
                dlgAlert.setTitle("Error")
                dlgAlert.setPositiveButton("OK", null)
                dlgAlert.setCancelable(true)
                dlgAlert.create().show()

                dlgAlert.setPositiveButton("Ok",
                    DialogInterface.OnClickListener { dialog, which -> })
            }else {
                findNavController().navigate(
                    R.id.action_Transfer_to_ContactInfo,
                    null
                )
            }
            // Create and commit a new transaction

//            modalBottomSheet.show(getParentFragmentManager(), ModalBottomSheet.TAG)

        }
    }

    override fun onViewStateRestored(savedInstanceState: Bundle?) {
        super.onViewStateRestored(savedInstanceState)
    }
    override fun onSaveInstanceState(outState: Bundle) {

//        outState.putString(TRANSFER_AMOUNT,textViewInputNumbers.text.toString())
        super.onSaveInstanceState(outState)

    }

    private fun onClick() {
        buttonNumber1.setOnClickListener(View.OnClickListener {
            if (exceedLength()) {
                textViewInputNumbers.setText(
                    textViewInputNumbers.getText().toString().toString() + "1"
                )
            }
        })

        buttonNumber2.setOnClickListener(View.OnClickListener {
            if (exceedLength()) {
                textViewInputNumbers.setText(
                    textViewInputNumbers.getText().toString().toString() + "2"
                )
            }
        })

        buttonNumber3.setOnClickListener(View.OnClickListener {
            if (exceedLength()) {
                textViewInputNumbers.setText(
                    textViewInputNumbers.getText().toString().toString() + "3"
                )
            }
        })

        buttonNumber4.setOnClickListener(View.OnClickListener {
            if (exceedLength()) {
                textViewInputNumbers.setText(
                    textViewInputNumbers.getText().toString().toString() + "4"
                )
            }
        })

        buttonNumber5.setOnClickListener(View.OnClickListener {
            if (exceedLength()) {
                textViewInputNumbers.setText(
                    textViewInputNumbers.getText().toString().toString() + "5"
                )
            }
        })

        buttonNumber6.setOnClickListener(View.OnClickListener {
            if (exceedLength()) {
                textViewInputNumbers.setText(
                    textViewInputNumbers.getText().toString().toString() + "6"
                )
            }
        })

        buttonNumber7.setOnClickListener(View.OnClickListener {
            if (exceedLength()) {
                textViewInputNumbers.setText(
                    textViewInputNumbers.getText().toString().toString() + "7"
                )
            }
        })

        buttonNumber8.setOnClickListener(View.OnClickListener {
            if (exceedLength()) {
                textViewInputNumbers.setText(
                    textViewInputNumbers.getText().toString().toString() + "8"
                )
            }
        })

        buttonNumber9.setOnClickListener(View.OnClickListener {
            if (exceedLength()) {
                textViewInputNumbers.setText(
                    textViewInputNumbers.getText().toString().toString() + "9"
                )
            }
        })

        buttonNumber0.setOnClickListener(View.OnClickListener {
            if (exceedLength()) {

                    textViewInputNumbers.setText(
                        textViewInputNumbers.getText().toString().toString() + "0"
                    )

            }
        })
nfcScanner.setOnClickListener {
    val text = textViewInputNumbers.text.toString()
    if(text.isEmpty()){
        MyData.fundTransfer=0.0

    }else{
        MyData.fundTransfer= text.toDouble()
    }
    if(MyData.fundTransfer!! <100){
        val dlgAlert: android.app.AlertDialog.Builder = android.app.AlertDialog.Builder(Mview.context)

        dlgAlert.setMessage("There minimum amount to send is 100 XAF")
        dlgAlert.setTitle("Error")
        dlgAlert.setPositiveButton("OK", null)
        dlgAlert.setCancelable(true)
        dlgAlert.create().show()

        dlgAlert.setPositiveButton("Ok",
            DialogInterface.OnClickListener { dialog, which -> })
    }else {
        val intent = Intent(activity, ReceiverActivity::class.java)
        startActivity(intent)
    }
}
        buttonDot.setOnClickListener(View.OnClickListener {
            if (exceedLength()) {
                if (textViewInputNumbers.text.toString().contains(".")) {

                } else {
                    textViewInputNumbers.setText(
                        textViewInputNumbers.getText().toString().toString() + "."
                    )
                }
            }
        })



        buttonClear.setOnClickListener(View.OnClickListener {

            if (textViewInputNumbers.getText().toString().length > 1) {
                val name: CharSequence = textViewInputNumbers.getText().toString()
                textViewInputNumbers.setText(name.subSequence(0, name.length - 1))
            } else {
                val1 = Double.NaN
                val2 = Double.NaN
                textViewInputNumbers.setText("")
                textViewInputNumbers.setText("")
            }

        })

        // Empty text views on long click.

        // Empty text views on long click.
        buttonClear.setOnLongClickListener(OnLongClickListener {
            if(textViewInputNumbers.text.length ==3){
                false
            }else {
                val1 = Double.NaN
                val2 = Double.NaN
                textViewInputNumbers.setText("0")
                true
            }
        })
    }

    private  fun initializeViewVariables(view: View){
        nfcScanner= view.findViewById(R.id.nfcScanner)
         buttonNumber0 = view.findViewById(R.id.button_zero)
         buttonNumber1  = view.findViewById(R.id.button_one)
         buttonNumber2  =  view.findViewById(R.id.button_two)
         buttonNumber3 = view.findViewById(R.id.button_three)
         buttonNumber4 = view.findViewById(R.id.button_four)
         buttonNumber5  = view.findViewById(R.id.button_five)
         buttonNumber6  = view.findViewById(R.id.button_six)
         buttonNumber7  = view.findViewById(R.id.button_seven)
         buttonNumber8 = view.findViewById(R.id.button_eight)
         buttonNumber9  = view.findViewById(R.id.button_nine)
        buttonClear= view.findViewById(R.id.button_clear)
       buttonDot =view.findViewById(R.id.button_dot);
        textViewInputNumbers= view.findViewById(R.id.textView_input_numbers)
    }

    private fun exceedLength():Boolean {
        if (textViewInputNumbers.getText().toString().length <=11) {
            return  true
        }
        return false

    }


}

