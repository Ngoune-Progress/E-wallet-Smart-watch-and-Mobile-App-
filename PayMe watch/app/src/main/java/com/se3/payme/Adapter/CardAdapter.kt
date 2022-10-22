package com.se3.payme.Adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.recyclerview.widget.RecyclerView
import com.se3.payme.R
import com.se3.payme.models.BankCard
import java.util.*
import kotlin.collections.ArrayList

class CardAdapter(val context: Context, var cardList: ArrayList<BankCard>): RecyclerView.Adapter<CardAdapter.viewHoler>(){
    inner class viewHoler(itemView:View) : RecyclerView.ViewHolder(itemView){
        private var currentPosition = -1
//        private var currentCard:Card? = null
        private val textCityImage = itemView.findViewById<ImageView>(R.id.imv_city)

        fun setData(card:BankCard,position: Int){
            textCityImage.setOnClickListener {

            }
            currentPosition = position
            if(card.cardType.equals("UBA")) {
            textCityImage.setImageResource(R.drawable.uba)
            }
            if(card.name.equals("SGBC")){
                textCityImage.setImageResource(R.drawable.sgbc)

            }
            if(card.cardType.equals("AFRI")){
                textCityImage.setImageResource(R.drawable.africard)

            }
        }

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): viewHoler {
    val itemView = LayoutInflater.from(context).inflate(R.layout.list_item_city,parent,false)
        return  viewHoler(itemView)
    }

    override fun onBindViewHolder(holder: viewHoler, position: Int) {
        val city = cardList[position]
        holder.setData(city, position)
    }
    override fun getItemCount(): Int {
        return  cardList.size
    }
}