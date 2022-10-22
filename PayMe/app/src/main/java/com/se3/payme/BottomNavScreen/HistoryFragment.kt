package com.se3.payme.BottomNavScreen

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.se3.payme.Adapter.ContactAdapter
import com.se3.payme.Adapter.TransactionAdapter
import com.se3.payme.Adapter.TransactionContactAdapter
import com.se3.payme.Data.MyData
import com.se3.payme.R
import com.se3.payme.models.Friends
import com.se3.payme.models.Transaction
import java.util.ArrayList


class HistoryFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view =  inflater.inflate(R.layout.fragment_history, container, false)
        RecyclerViewFriends(view)
        RecyclerViewTransaction(view)
        return view
    }

    private fun RecyclerViewFriends(view: View) {
        val context = requireContext()
        val friendAdapter = TransactionContactAdapter(context, MyData.user?.friends as ArrayList<Friends>)
        val recyclerView = view.findViewById<RecyclerView>(R.id.transaction_contact_recycler_view)
        recyclerView.adapter = friendAdapter
        recyclerView.setHasFixedSize(true)
        val layoutManager = LinearLayoutManager(context)
        layoutManager.orientation = RecyclerView.HORIZONTAL
        recyclerView.layoutManager = layoutManager
    }
    private fun RecyclerViewTransaction(view: View) {
        val context = requireContext()
        val friendAdapter = TransactionAdapter(context, MyData.user?.transactions as ArrayList<Transaction>)
        val recyclerView = view.findViewById<RecyclerView>(R.id.transaction_recycler_view)
        recyclerView.adapter = friendAdapter
        recyclerView.setHasFixedSize(true)
        val layoutManager = LinearLayoutManager(context)
        layoutManager.orientation = RecyclerView.VERTICAL
        recyclerView.layoutManager = layoutManager
    }

}