package com.se3.payme.models

data class FundTransferRequest(
    val fromAccount:String,
val toAccount:String,
val amount:Double,
val ref:String
)