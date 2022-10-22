package com.se3.payme.Adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.se3.payme.R
import com.se3.payme.models.BankL
import java.util.*

class BankAdapter(val context: Context,val banks:ArrayList<BankL>):RecyclerView.Adapter<BankAdapter.viewHolder>() {

    inner class  viewHolder(itemView: View):RecyclerView.ViewHolder(itemView){
        private var currentPosition = -1
        private  val bankText = itemView.findViewById<TextView>(R.id.linkBankName)
        private  var imageView = itemView.findViewById<ImageView>(R.id.move_next)

        fun setData(bank: BankL, position: Int){
            currentPosition = position
            bankText.text = bank.name
            imageView.setImageResource(R.drawable.ic_arrow_right_24)

        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): viewHolder {
    val itemView = LayoutInflater.from(context).inflate(R.layout.bank_list,parent,false)
        return  viewHolder(itemView)
    }

    override fun onBindViewHolder(holder: viewHolder, position: Int) {
        val bank = banks[position]
        holder.setData(bank,position)
    }

    override fun getItemCount(): Int {
        return  banks.size
    }
}