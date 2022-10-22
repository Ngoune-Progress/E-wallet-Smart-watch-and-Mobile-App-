package com.se3.payme.bubbleNavigation

import android.content.DialogInterface
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.TextView
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.se3.payme.Adapter.ContactAdapter
import com.se3.payme.ApiClient
import com.se3.payme.Data.MyData
import com.se3.payme.R
import com.se3.payme.models.Friends
import com.se3.payme.models.FunderTransferByQrCode
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.util.*


class ContactPayFragment : Fragment() {
    private lateinit var balace:TextView
    private lateinit var exit:ImageView
    private lateinit var  pay: Button
    private lateinit var toAccount: EditText;
    private lateinit var ref:EditText

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view=  inflater.inflate(R.layout.fragment_contact_pay, container, false)
        balace = view.findViewById(R.id.balancePay)
        exit = view.findViewById(R.id.left_arrow)
        balace.text = "XAF "+MyData.fundTransfer
        ref= view.findViewById(R.id.forText)
        pay = view.findViewById(R.id.payFriend)
        toAccount =view.findViewById(R.id.toAccount)
        RecyclerViewFriends(view)

        exit.setOnClickListener {
            findNavController().popBackStack()

        }
        pay.setOnClickListener {
            paymentButton(view)
        }
        return view
    }
fun paymentButton(view: View){
    if(toAccount.text.isEmpty() || ref.text.isEmpty()){
        val dlgAlert: android.app.AlertDialog.Builder = android.app.AlertDialog.Builder(view.context)

        dlgAlert.setMessage("Please fill form correctly")
        dlgAlert.setTitle("Fill the blanks")
        dlgAlert.setPositiveButton("OK", null)
        dlgAlert.setCancelable(true)
        dlgAlert.create().show()

        dlgAlert.setPositiveButton("Ok",
            DialogInterface.OnClickListener { dialog, which -> })
    }
    else{
    if(MyData.user.balance!! < MyData.fundTransfer!!){
        val dlgAlert: android.app.AlertDialog.Builder = android.app.AlertDialog.Builder(view.context)

        dlgAlert.setMessage("You cant send more than your have")
        dlgAlert.setTitle("Insufficient Balance")
        dlgAlert.setPositiveButton("OK", null)
        dlgAlert.setCancelable(true)
        dlgAlert.create().show()

        dlgAlert.setPositiveButton("Ok",
            DialogInterface.OnClickListener { dialog, which -> })
    }
    if(toAccount.text.toString() == MyData.user.email){
        val dlgAlert: android.app.AlertDialog.Builder = android.app.AlertDialog.Builder(view.context)

        dlgAlert.setMessage("It is impossible for you to send fund to your own email")
        dlgAlert.setTitle("Bad Request")
        dlgAlert.setPositiveButton("OK", null)
        dlgAlert.setCancelable(true)
        dlgAlert.create().show()

        dlgAlert.setPositiveButton("Ok",
            DialogInterface.OnClickListener { dialog, which -> })
    }else{
        MyData.toAccount = toAccount.text.toString()
        MyData.ref = ref.text.toString()
        findNavController().navigate(
            R.id.contactPayPage_to_SecurePassword,
            null
        )
    }
    }
}



    private fun RecyclerViewFriends(view: View) {
        val context = requireContext()
        val friendAdapter = ContactAdapter(context, MyData.user?.friends as ArrayList<Friends>)
        val recyclerView = view.findViewById<RecyclerView>(R.id.contact_recycler_view)
        recyclerView.adapter = friendAdapter
        recyclerView.setHasFixedSize(true)
        val layoutManager = LinearLayoutManager(context)
        layoutManager.orientation = RecyclerView.VERTICAL
        recyclerView.layoutManager = layoutManager
    }


}