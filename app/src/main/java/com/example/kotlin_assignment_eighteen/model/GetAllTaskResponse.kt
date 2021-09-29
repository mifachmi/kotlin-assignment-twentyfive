package com.example.kotlin_assignment_eighteen.model

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.parcelize.Parcelize

@Parcelize
data class GetAllTaskResponse(

	@field:SerializedName("data")
	val data: List<DataItem?>? = null,

	@field:SerializedName("message")
	val message: String? = null,

	@field:SerializedName("status")
	val status: Int? = null
) : Parcelable

@Parcelize
data class DataItem(

	@field:SerializedName("task_name")
	val taskName: String? = null,

	@field:SerializedName("is_done")
	val isDone: String? = null,

	@field:SerializedName("task_date")
	val taskDate: String? = null,

	@field:SerializedName("id")
	val id: String? = null
) : Parcelable
