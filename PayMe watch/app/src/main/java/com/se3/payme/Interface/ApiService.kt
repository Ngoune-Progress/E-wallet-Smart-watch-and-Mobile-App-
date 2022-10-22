package com.se3.payme.Interface

import com.se3.payme.bubbleNavigation.PostProjectFragment
import com.se3.payme.models.*
import retrofit2.Response
import retrofit2.http.*

interface ApiService {

    @POST("registration/authenticate")
    suspend fun authenticate(@Body post: JwtRequest): Response<JwtResponse>

    @GET("user/find/{email}")
    suspend fun findUserByEmail(
        @HeaderMap headers: Map<String, String>,
        @Path("email") email : String): Response<Users>

    @POST("transfer/fund-transfer-qr")
    suspend fun fundTransferByQrCode(@HeaderMap headers: Map<String, String>,@Body post: FunderTransferByQrCode): Response<FunderTransferByQrCodeRespond>

    @POST("transfer/fund-transfer")
    suspend fun fundTransfer(@HeaderMap headers: Map<String, String>,@Body post: FundTransferRequest): Response<FundTransferRespond>
    @GET("all")
    suspend fun listPost(@HeaderMap headers: Map<String, String>):Response<ArrayList<PostProject>>
    @POST("savePost")
    suspend fun addPost(@HeaderMap headers: Map<String, String>,@Body post: PostProject):Response<PostProject>
    @POST("link_bank/addCard")
    suspend fun linkBank(@HeaderMap headers: Map<String, String>,@Body bankCard: LinkBankRequest):Response<BankCard>

}