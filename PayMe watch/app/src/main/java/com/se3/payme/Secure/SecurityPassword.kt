package com.se3.payme.Secure

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.NavHostFragment.findNavController
import androidx.navigation.fragment.findNavController
import com.google.android.material.snackbar.Snackbar
import com.qusion.lib_pindotview.PinDotView
import com.se3.payme.Data.MyData
import com.se3.payme.R
import com.se3.payme.models.FunderTransferByQrCode
import kotlinx.coroutines.*


class SecurityPassword : Fragment() {
private  lateinit var pinDotView: PinDotView
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view=  inflater.inflate(R.layout.fragment_security_password, container, false)
        pinDotView = view.findViewById(R.id.pinViewFriend)
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
                findNavController().navigate(
            R.id.securePassword_to_loadPayment,
            null
        )

            }
            //Snackbar.make(pinDotView, pin, Snackbar.LENGTH_SHORT).show()
        }
        return  view
    }

    override fun onResume() {
        super.onResume()


    }
}
