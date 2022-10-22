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
import java.util.*


class ContactAdapter(val context: Context, var contactList: ArrayList<Friends>):RecyclerView.Adapter<ContactAdapter.viewHolder>() {

    inner class viewHolder(itemView: View):RecyclerView.ViewHolder(itemView){
        var b = false
        private var currentPosition  = -1
        private var currentContact:Friends?=null
        private val textContactName = itemView.findViewById<TextView>(R.id.contactN)
        private val textContactLast = itemView.findViewById<TextView>(R.id.contactL)
        private val image = itemView.findViewById<ImageView>(R.id.img_person)
        private val tick = itemView.findViewById<ImageView>(R.id.tick)

init {
    itemView.setOnClickListener {
        b = !b
        if (b){
            tick.visibility = View.VISIBLE
        }else{
            tick.visibility = View.INVISIBLE
        }
    }
}

        fun setData(friends: Friends, position: Int){
            currentPosition = position
            textContactName.text = friends.name
            textContactLast.text = friends.lname
            tick.setImageResource(R.drawable.ic_tick_24)
            val rnd = Random()
            val color = Color.argb(255, rnd.nextInt(256), rnd.nextInt(256), rnd.nextInt(256))
            val drawable1 = TextDrawable.builder()

                .buildRound(friends.name?.get(0).toString(), color)
            image.setImageDrawable(drawable1);


        }

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): viewHolder {
        val itemView = LayoutInflater.from(context).inflate(R.layout.contact_list, parent, false)
        return  viewHolder(itemView)
    }

    override fun onBindViewHolder(holder: viewHolder, position: Int) {
        val friend = contactList[position]
        holder.setData(friend, position)
    }

    override fun getItemCount(): Int {
    return contactList.size
    }
}