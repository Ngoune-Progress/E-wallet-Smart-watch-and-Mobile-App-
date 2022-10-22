package com.se3.payme.models

data class Transaction(val id:Long?=null, val amount:Double?=null,
val transactionType:TransactionType?=null,
val referenceNumber:String?=null,
val senderName:String?=null,
val receiverName:String?=null,
val ref:String?=null,
val users:Users?=null)
