package com.example.kotlin_assignment_eighteen.api

import com.example.kotlin_assignment_eighteen.const.Constants.Companion.CONTENT_TYPE
import com.example.kotlin_assignment_eighteen.const.Constants.Companion.SERVER_KEY
import com.example.kotlin_assignment_eighteen.model.DataNotification
import com.example.kotlin_assignment_eighteen.model.NotificationResponse
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.Headers
import retrofit2.http.POST

interface Notification {

    @Headers("Authorization: key = $SERVER_KEY", "Content-Type: $CONTENT_TYPE")
    @POST("fcm/send")
    fun pushNotification(
        @Body notification: DataNotification
    ): Response<NotificationResponse>
}