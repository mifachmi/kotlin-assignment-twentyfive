package com.example.kotlin_assignment_eighteen.api

import com.example.kotlin_assignment_eighteen.model.CreateDataResponse
import com.example.kotlin_assignment_eighteen.model.GetAllUserResponse
import com.example.kotlin_assignment_eighteen.model.UploadImageResponse
import okhttp3.MultipartBody
import retrofit2.Call
import retrofit2.http.*


interface Users {
    @GET("?function=get_all_users")
    fun getAllUsers(): Call<GetAllUserResponse>

    @Multipart
    @POST("?function=upload_image")
    fun uploadImage(@Part body: MultipartBody.Part): Call<UploadImageResponse>

    @FormUrlEncoded
    @POST("?function=add_new_user")
    fun insertUser(
        @Field("id") id: String,
        @Field("name_user") name_user: String,
        @Field("email_user") email_user: String,
        @Field("password") password: String,
        @Field("photo_user") photo_user: String
    ): Call<CreateDataResponse>
}