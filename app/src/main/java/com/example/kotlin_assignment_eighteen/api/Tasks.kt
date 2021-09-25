package com.example.kotlin_assignment_eighteen.api

import com.example.kotlin_assignment_eighteen.model.CreateDataResponse
import com.example.kotlin_assignment_eighteen.model.DeleteDataResponse
import com.example.kotlin_assignment_eighteen.model.GetAllTaskResponse
import com.example.kotlin_assignment_eighteen.model.UpdateTaskResponse
import retrofit2.Call
import retrofit2.http.*

interface Tasks {
    @GET("?function=get_all_task")
    fun getAllTasks(): Call<GetAllTaskResponse>

    @FormUrlEncoded
    @POST("?function=update_task")
    fun updateTask(
        @Field("task_name") task_name: String,
        @Field("task_date") task_date: String,
        @Field("is_done") is_done: String,
        @Query("id") id: String
    ): Call<UpdateTaskResponse>

    @DELETE("?function=delete_task")
    fun deleteTask(
        @Query("id") id: String
    ): Call<DeleteDataResponse>

    @FormUrlEncoded
    @POST("?function=add_new_task")
    fun insertTask(
        @Field("id") id: String,
        @Field("task_name") task_name: String,
        @Field("task_date") task_date: String,
        @Field("is_done") is_done: String
    ): Call<CreateDataResponse>
}