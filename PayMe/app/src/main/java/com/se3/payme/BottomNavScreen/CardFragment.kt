package com.se3.payme.BottomNavScreen

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.se3.payme.Adapter.CardAdapter
import com.se3.payme.Data.MyData
import com.se3.payme.ItemDecorator
import com.se3.payme.R
import com.se3.payme.models.BankCard
import java.util.*
import kotlin.collections.ArrayList


class CardFragment : Fragment() {


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_card, container, false)
        setupRecyclerView(view)
        return view
    }
    fun setupRecyclerView(view: View){
        if(MyData.user?.bankCards?.size == 0){

        }else {


            val context = requireContext()
            val cardAdapter = CardAdapter(context, MyData.user?.bankCards as ArrayList<BankCard>)
            val recyclerView = view.findViewById<RecyclerView>(R.id.city_recycler_view)

            val itemTouchHelper = ItemTouchHelper(object :
                ItemTouchHelper.SimpleCallback(ItemTouchHelper.DOWN or ItemTouchHelper.UP, 0) {
                override fun onMove(
                    recyclerView: RecyclerView,
                    viewHolder: RecyclerView.ViewHolder,
                    target: RecyclerView.ViewHolder
                ): Boolean {

                    val initial = viewHolder.adapterPosition
                    val final = target.adapterPosition
                    Collections.swap(MyData.user.bankCards as ArrayList<BankCard>, initial, final)
                    cardAdapter.notifyItemMoved(initial, final)
                    return false
                }

                override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {

                }
            })

            itemTouchHelper.attachToRecyclerView(recyclerView)
            recyclerView.adapter = cardAdapter
            recyclerView.setHasFixedSize(true)
            val layoutManager = LinearLayoutManager(context)
//            layoutManager.setReverseLayout(true);
//            layoutManager.setStackFromEnd(true);

            layoutManager.orientation = RecyclerView.VERTICAL
            recyclerView.layoutManager = layoutManager
            recyclerView.addItemDecoration(ItemDecorator(-600))
            itemTouchHelper.attachToRecyclerView(recyclerView)


        }

    }


}