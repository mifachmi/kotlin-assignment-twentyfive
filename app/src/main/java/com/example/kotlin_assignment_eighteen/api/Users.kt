package com.example.kotlin_assignment_eighteen.api

import com.example.kotlin_assignment_eighteen.model.*
import okhttp3.MultipartBody
import retrofit2.Call
import retrofit2.http.*

interface Users {
    @GET("?function=get_all_users")
    fun getAllUsers(): Call<GetAllUserResponse>

    @GET("?function=login_user")
    fun checkLogin(
        @Query("email_user") email_user: String,
        @Query("password") password: String
    ): Call<GetAllUserResponse>

    @GET("?function=get_user_by_device_id")
    fun checkLoginBiometric(
        @Query("device_id") device_id: String
    ): Call<GetAllUserResponse>

    @GET("?function=check_device_id")
    fun checkDeviceId(
        @Query("device_id") device_id: String
    ): Call<GetAllUserResponse>

    @GET("?function=check_device_id_by_user_id")
    fun checkDeviceIdByUserId(
        @Query("id") id: String
    ): Call<GetAllUserResponse>

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
        @Field("photo_user") photo_user: String,
        @Field("device_id") device_id: String
    ): Call<CreateDataResponse>

    @FormUrlEncoded
    @POST("?function=update_user")
    fun updateUser(
        @Field("name_user") name_user: String,
        @Field("email_user") email_user: String,
        @Field("password") password: String,
        @Field("photo_user") photo_user: String,
        @Field("device_id") device_id: String,
        @Query("id") id: String
    ): Call<UpdateDataResponse>

    @DELETE("?function=delete_user")
    fun deleteUser(
        @Query("id") id: String
    ): Call<DeleteDataResponse>
}