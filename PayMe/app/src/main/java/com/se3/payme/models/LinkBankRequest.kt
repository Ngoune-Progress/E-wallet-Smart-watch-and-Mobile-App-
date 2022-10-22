package com.se3.payme.models

data class LinkBankRequest (
    val email:String?=null,
    val number:String?=null,
    val ccv:String?=null,
//    val pin:String?=null,
    val cardType:String?=null,
//    val amount:Double?=null,
//    val exp:String?=null,
    val name:String?=null,

)