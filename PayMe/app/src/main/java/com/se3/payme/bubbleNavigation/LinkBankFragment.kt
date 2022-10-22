package com.se3.payme.bubbleNavigation

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.se3.payme.Adapter.BankAdapter
import com.se3.payme.R
import com.se3.payme.models.BankL
import java.util.*

class LinkBankFragment : Fragment() {

private lateinit var exit:ImageView
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view =  inflater.inflate(R.layout.fragment_link_bank, container, false)
        exit = view.findViewById(R.id.left_arrow)
        val context = requireContext()
        val bankAdapter = BankAdapter(context, BankL.bankObect.bankList as ArrayList<BankL>)
//        val recyclerView = view.findViewById<RecyclerView>(R.id.bank_recycler_view)
//        recyclerView.adapter = bankAdapter
//        recyclerView.setNestedScrollingEnabled(false);
//
//        recyclerView.setHasFixedSize(true)
//        val layoutManager = LinearLayoutManager(context)
//        layoutManager.orientation  = RecyclerView.VERTICAL
//        recyclerView.layoutManager = layoutManager
        exit.setOnClickListener {
            findNavController().popBackStack()
        }
        return view
    }

}