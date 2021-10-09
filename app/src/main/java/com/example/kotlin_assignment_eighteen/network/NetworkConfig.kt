package com.example.kotlin_assignment_eighteen.network

import com.example.kotlin_assignment_eighteen.api.Notification
import com.example.kotlin_assignment_eighteen.api.Tasks
import com.example.kotlin_assignment_eighteen.api.Users
import com.example.kotlin_assignment_eighteen.const.Constants.Companion.BASE_FCM_URL
import com.example.kotlin_assignment_eighteen.const.Constants.Companion.BASE_URL_TASK
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class NetworkConfig {
    private fun getInterceptor(): OkHttpClient {
        val logging = HttpLoggingInterceptor()
        logging.level = HttpLoggingInterceptor.Level.BODY
        return OkHttpClient.Builder()
            .addInterceptor(logging)
            .build()
    }

    private fun getRetrofit() : Retrofit {
        return Retrofit.Builder()
            .baseUrl(BASE_URL_TASK)
            .client(getInterceptor())
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }

    private fun getRetrofitUser() : Retrofit {
        return Retrofit.Builder()
            .baseUrl(BASE_FCM_URL)
            .client(getInterceptor())
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }

    private fun getRetrofitNotification() : Retrofit {
        return Retrofit.Builder()
            .baseUrl(BASE_FCM_URL)
            .client(getInterceptor())
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }

    fun getService(): Tasks = getRetrofit().create(Tasks::class.java)

    fun getServiceUser(): Users = getRetrofitUser().create(Users::class.java)

    fun getServiceNotification(): Notification = getRetrofitNotification().create(Notification::class.java)
}