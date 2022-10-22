package com.se3.payme.Adapter

import android.content.Context
import android.graphics.Color
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.amulyakhare.textdrawable.TextDrawable
import com.se3.payme.R
import com.se3.payme.models.Friends
import com.se3.payme.models.Transaction
import java.util.*

class TransactionAdapter (val context: Context, var transactionList: ArrayList<Transaction>):
    RecyclerView.Adapter<TransactionAdapter.viewHolder>() {

    inner class viewHolder(itemView: View): RecyclerView.ViewHolder(itemView){
        var b = false
        private var currentPosition  = -1
        private var currentContact: Transaction?=null
        private var tname =itemView.findViewById<TextView>(R.id.TcontactN)

        private var amount =itemView.findViewById<TextView>(R.id.amountBalance)
        private val textRef = itemView.findViewById<TextView>(R.id.Tref)
        private val image = itemView.findViewById<ImageView>(R.id.T_img_person)

        init {
            itemView.setOnClickListener {

            }
        }

        fun setData(transaction: Transaction, position: Int){
            tname.text = transaction.receiverName
            amount.text = transaction.amount.toString()
            if (amount.text.toString().toDouble() < 0){
                amount.setTextColor(Color.RED)
            }else{
                amount.setTextColor(Color.GREEN)

            }
            currentPosition = position
            textRef.text = transaction.ref
            val rnd = Random()
            val color = Color.argb(255, rnd.nextInt(256), rnd.nextInt(256), rnd.nextInt(256))
            val drawable1 = TextDrawable.builder()

                .buildRound(transaction.receiverName?.get(0).toString(), color)
            image.setImageDrawable(drawable1);


        }

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): viewHolder {
        val itemView = LayoutInflater.from(context).inflate(R.layout.transaction_list, parent, false)
        return  viewHolder(itemView)
    }

    override fun onBindViewHolder(holder: viewHolder, position: Int) {
        val friend = transactionList[position]
        holder.setData(friend, position)
    }

    override fun getItemCount(): Int {
        return transactionList.size
    }
}