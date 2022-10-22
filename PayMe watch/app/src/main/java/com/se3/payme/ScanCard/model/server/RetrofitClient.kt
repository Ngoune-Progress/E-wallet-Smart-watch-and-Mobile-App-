package com.muryno.cardfinder.model.server

import ApiInterface
import com.google.gson.GsonBuilder
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.security.SecureRandom
import java.security.cert.X509Certificate
import java.util.concurrent.TimeUnit
import javax.net.ssl.HostnameVerifier
import javax.net.ssl.SSLContext
import javax.net.ssl.TrustManager
import javax.net.ssl.X509TrustManager


class RetrofitClient {
    companion object{

        val BASE_URL: String = "https://lookup.binlist.net/"



        private   var mInstance: RetrofitClient? = null

        @Synchronized
        fun getInstance(): RetrofitClient? {
            if (mInstance == null) {
                mInstance =
                    RetrofitClient()
            }
            return mInstance
        }
    }
    private var mRetrofit: Retrofit? = null

    init {


        val interceptor: HttpLoggingInterceptor = HttpLoggingInterceptor().apply {
            level = HttpLoggingInterceptor.Level.BODY
        }

        val client = OkHttpClient.Builder()
            .readTimeout(3, TimeUnit.MINUTES)
            .connectTimeout(3, TimeUnit.MINUTES)
            .addInterceptor { chain ->
                val newRequest = chain.request().newBuilder()
                    .addHeader("Content-Type", "application/json")
                    .build()
                chain.proceed(newRequest)

            }.addInterceptor(interceptor).build()

        val gson = GsonBuilder()
            .setLenient()
            .create()


        mRetrofit = Retrofit.Builder()
            .baseUrl(BASE_URL)
//            .client(client)
            .client(unSafeOkHttpClient().build())
            .addConverterFactory(GsonConverterFactory.create(gson))
            .build()
    }
    fun getApi(): ApiInterface? {
        return mRetrofit?.create(ApiInterface::class.java)
    }

    fun reset() {
        mInstance = null
        getInstance()
    }
    fun unSafeOkHttpClient() :OkHttpClient.Builder {
        val okHttpClient = OkHttpClient.Builder()
        try {
            // Create a trust manager that does not validate certificate chains
            val trustAllCerts:  Array<TrustManager> = arrayOf(object : X509TrustManager {
                override fun checkClientTrusted(chain: Array<out X509Certificate>?, authType: String?){}
                override fun checkServerTrusted(chain: Array<out X509Certificate>?, authType: String?) {}
                override fun getAcceptedIssuers(): Array<X509Certificate>  = arrayOf()
            })

            // Install the all-trusting trust manager
            val  sslContext = SSLContext.getInstance("SSL")
            sslContext.init(null, trustAllCerts, SecureRandom())

            // Create an ssl socket factory with our all-trusting manager
            val sslSocketFactory = sslContext.socketFactory
            if (trustAllCerts.isNotEmpty() &&  trustAllCerts.first() is X509TrustManager) {
                okHttpClient.sslSocketFactory(sslSocketFactory, trustAllCerts.first() as X509TrustManager)
//                okHttpClient.hostnameVerifier { HostnameVerifier { _, _ -> true } }
                okHttpClient.hostnameVerifier { _, _ -> true }
            }

            return okHttpClient
        } catch (e: Exception) {
            return okHttpClient
        }
    }

}