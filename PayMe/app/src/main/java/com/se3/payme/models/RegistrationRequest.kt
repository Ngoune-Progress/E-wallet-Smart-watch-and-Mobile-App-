package com.se3.payme.models

data class RegistrationRequest(
    val firstName:String,
    val lastName:String,
val email:String,
val tel:String,
val password:String,
val pin:String)