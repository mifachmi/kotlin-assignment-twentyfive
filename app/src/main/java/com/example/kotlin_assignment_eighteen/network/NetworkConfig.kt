package com.example.kotlin_assignment_eighteen.network

import com.example.kotlin_assignment_eighteen.api.Notification
import com.example.kotlin_assignment_eighteen.api.Tasks
import com.example.kotlin_assignment_eighteen.api.Users
import com.example.kotlin_assignment_eighteen.const.Constants.Companion.BASE_FCM_URL
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.create

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
            .baseUrl("http://192.168.1.5/kotlin-assignment-nineteen/todolist_rest_api.php/")
            .client(getInterceptor())
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }

    private fun getRetrofitUser() : Retrofit {
        return Retrofit.Builder()
            .baseUrl("http://192.168.1.5/kotlin-assignment-nineteen/users_rest_api.php/")
            .client(getInterceptor())
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }

    private fun getRetrofitNotification() : Retrofit {
        return Retrofit.Builder()
            .baseUrl("http://192.168.1.5/kotlin-assignment-twenty-backend/users_rest_api.php/")
            .client(getInterceptor())
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }

    fun getService() = getRetrofit().create(Tasks::class.java)

    fun getServiceUser() = getRetrofitUser().create(Users::class.java)

    fun getServiceNotification() = getRetrofitNotification().create(Notification::class.java)
}