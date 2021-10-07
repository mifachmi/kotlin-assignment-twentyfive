package com.example.kotlin_assignment_eighteen.api

import com.example.kotlin_assignment_eighteen.model.NotificationResponse
import retrofit2.Call
import retrofit2.http.Field
import retrofit2.http.FormUrlEncoded
import retrofit2.http.POST

interface Notification {

    @FormUrlEncoded
    @POST("?function=sendNotification")
    fun pushNotification(
        @Field("title") title: String,
        @Field("message") message: String
    ): Call<NotificationResponse>
}