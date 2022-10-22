package com.se3.payme.models

data class Users(
     val usersId: Long? = null,
     val firstName: String? = null,
     val lastName: String? = null,
     val email: String? = null,
     val password: String? = null,
     val accountNumber: String? = null,
     val tel: String? = null,
     val locked: Boolean? = null,
     val enabled: Boolean? = null,
     val balance: Double? = null,
     val qrCode: String? = null,
     val pin: String? = null,

     val friends: List<Friends>?= null,
     val transactions:List<Transaction>?=null,
     val postProjects:List<PostProject>?=null,
     val bankCards:List<BankCard>?=null
)
