package com.se3.payme.models

import java.util.*

data class PostProject(
    val id:Long?= null,
    val personName:String?=null,
    val theme:String?=null,
    val description:String?=null,
    val minInvest:String?=null,
    val numLike:Int?=null,
    val numOfInvestor:Int,
    val email:String,
    val date: String,
    val user:Users?=null
)