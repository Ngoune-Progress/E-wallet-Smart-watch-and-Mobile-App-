package com.se3.payme.croudFunding

import android.content.DialogInterface
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.ProgressBar
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.se3.payme.Adapter.ContactAdapter
import com.se3.payme.Adapter.PostProjectAdapter
import com.se3.payme.ApiClient
import com.se3.payme.Data.MyData
import com.se3.payme.R
import com.se3.payme.models.Friends
import com.se3.payme.models.PostProject
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class croundFundingViewFragment : Fragment() {
    private  var postes: ArrayList<PostProject>? =null
    private  val scope = CoroutineScope(Dispatchers.Main)
    private lateinit var progressBar: ProgressBar
//    private lateinit var recyclerView: RecyclerView


    private lateinit var addPost:ImageView
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_cround_funding_view, container, false)
        progressBar = view.findViewById(R.id.progressCrowd)
        init(view)

//        if(postes == null){
//            Log.d("TAG","Post is null")
//        }
//        else{

//            val context = requireContext()
//            val postAdapter = PostProjectAdapter(context, postes!! as ArrayList<PostProject>)
//            val recyclerView = view.findViewById<RecyclerView>(R.id.postProjectList)
//            recyclerView.adapter = postAdapter
//            recyclerView.setHasFixedSize(true)
//            val layoutManager = LinearLayoutManager(context)
//            layoutManager.orientation = RecyclerView.VERTICAL
//            recyclerView.layoutManager = layoutManager
       // }
        return  view
    }
//    private fun RecyclerViewFriends(view: View) {
//        val context = requireContext()
//        val friendAdapter = PostProjectAdapter(context, postes as ArrayList<PostProject>)
//        val recyclerView = view.findViewById<RecyclerView>(R.id.postProjectList1)
//        recyclerView.adapter = friendAdapter
//        recyclerView.setHasFixedSize(true)
//        val layoutManager = LinearLayoutManager(context)
//        layoutManager.orientation = RecyclerView.VERTICAL
//        recyclerView.layoutManager = layoutManager
//    }
    private fun init(view: View){
//        recyclerView = view.findViewById(R.id.postProjectList1)
        addPost = view.findViewById(R.id.addPost)
        addPost.setOnClickListener {
            findNavController().navigate(R.id.croudFundingView_to_postProject)
        }
//        RecyclerViewFriends(view)

        getUserDetail(view)
    }
    private fun getUserDetail(view: View){
        scope.launch {
            try {
                val token = MyData.token.toString()
                val header = mutableMapOf<String,String>()
                val tokenn = token
                header["Authorization"]=tokenn

                val response = ApiClient.apiService.listPost(headers =header)
                if (response.isSuccessful && response.body()!=null){
                    progressBar.visibility = View.GONE
                    val content = response.body()
//                    MyData.user = Users(content!!.usersId,content!!.firstName,content!!.lastName,content.email,content.password,content.accountNumber,content.tel,content.locked,content.enabled,content.balance,content.qrCode,content.friends
//                        ,content.transactions,content.postProjects,content.bankCards
//                    )
                    Log.d("TagSee",content.toString())
                    if (content != null) {
                        postes = content
                        val context = requireContext()
                        val friendAdapter = PostProjectAdapter(context, postes!!)
        val recyclerView = view.findViewById<RecyclerView>(R.id.postProjectList1)
        recyclerView.adapter = friendAdapter
        recyclerView.setHasFixedSize(true)
        val layoutManager = LinearLayoutManager(context)
        layoutManager.orientation = RecyclerView.VERTICAL
        recyclerView.layoutManager = layoutManager
                    }



                }else{
                    val dlgAlert: android.app.AlertDialog.Builder =
                        android.app.AlertDialog.Builder(view.context)

                    dlgAlert.setMessage("${response.body()}")
                    dlgAlert.setTitle("Error Message...")
                    dlgAlert.setCancelable(true)
                    dlgAlert.create().show()

                    dlgAlert.setPositiveButton("Ok",
                        DialogInterface.OnClickListener { dialog, which -> })
//                    progressBar.visibility = View.GONE
                }
            }catch (e:Exception){
                val dlgAlert: android.app.AlertDialog.Builder =
                    android.app.AlertDialog.Builder(view.context)

                dlgAlert.setMessage("${e.message}")
                dlgAlert.setTitle("Error Message...")
                dlgAlert.setPositiveButton("OK", null)
                dlgAlert.setCancelable(true)
                dlgAlert.create().show()

                dlgAlert.setPositiveButton("Ok",
                    DialogInterface.OnClickListener { dialog, which -> })
//                progressBar.visibility = View.GONE
            }
        }

    }

}