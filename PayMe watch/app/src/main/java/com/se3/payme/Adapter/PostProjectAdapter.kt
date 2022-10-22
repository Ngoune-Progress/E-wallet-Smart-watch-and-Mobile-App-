package com.se3.payme.Adapter

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.core.content.ContextCompat.startActivity
import androidx.recyclerview.widget.RecyclerView
import com.se3.payme.R
import com.se3.payme.models.PostProject
import com.se3.payme.croudFunding.SeeMoreInfoFragment

class PostProjectAdapter(val context: Context,var postProject: ArrayList<PostProject>):RecyclerView.Adapter<PostProjectAdapter.viewHolder>() {

    inner class viewHolder(itemView: View):RecyclerView.ViewHolder(itemView){
        private val personIm = itemView.findViewById<ImageView>(R.id.personIm)
        private val personName = itemView.findViewById<TextView>(R.id.personName)
        private val theme = itemView.findViewById<TextView>(R.id.theme)
        private val numOfInvestors = itemView.findViewById<TextView>(R.id.numberOfInvestors)
        private val description = itemView.findViewById<TextView>(R.id.decription)
        private var currentPosition= -1
        private  val postDate = itemView.findViewById<TextView>(R.id.DatePublish)
        private  val seePost = itemView.findViewById<Button>(R.id.seePost)


        fun setData(post: PostProject, position: Int){
            currentPosition = position
            postDate.text = post.date
            description.text = post.description
            theme.text = post.theme
            numOfInvestors.text = post.numOfInvestor.toString()
            personIm.setImageResource(R.drawable.man1)
            personName.text = post.personName

            seePost.setOnClickListener {

                val intent = Intent(context.applicationContext,SeeMoreInfoFragment::class.java)
                startActivity(context,intent,null)
            }

        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): viewHolder {
        val itemView = LayoutInflater.from(context).inflate(R.layout.post_project,parent,false)
    return viewHolder(itemView)
    }

    override fun onBindViewHolder(holder: viewHolder, position: Int) {
        val post = postProject[position]
        holder.setData(post,position)
    }

    override fun getItemCount(): Int {
        return  postProject.size
    }
}

//    (val context:Context,val posts:ArrayList<PostProject>):RecyclerView() {
//    inner class  viewHolder(itemView: View):RecyclerView.ViewHolder(itemView){
//        private var currentPosition = -1
//        private  val bankText = itemView.findViewById<TextView>(R.id.linkBankName)
//        private  var imageView = itemView.findViewById<ImageView>(R.id.move_next)
//
//        fun setData(bank: BankL, position: Int){
//            currentPosition = position
//            bankText.text = bank.name
//            imageView.setImageResource(R.drawable.ic_arrow_right_24)
//
//        }
//    }
//}
