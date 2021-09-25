package com.example.kotlin_assignment_eighteen.network

import com.example.kotlin_assignment_eighteen.api.Tasks
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class NetworkConfig {
    // set interceptor
    fun getInterceptor() : OkHttpClient {
        val logging = HttpLoggingInterceptor()
        logging.level = HttpLoggingInterceptor.Level.BODY
        val okHttpClient = OkHttpClient.Builder()
            .addInterceptor(logging)
            .build()
        return  okHttpClient
    }

    fun getRetrofit() : Retrofit {
        return Retrofit.Builder()
            .baseUrl("http://192.168.1.6/kotlin-assignment-seventeen/php_rest_api.php/")
            .client(getInterceptor())
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }

    fun getService() = getRetrofit().create(Tasks::class.java)
}