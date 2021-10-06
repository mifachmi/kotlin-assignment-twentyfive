package com.example.kotlin_assignment_eighteen.model

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.android.parcel.Parcelize

@Parcelize
data class NotificationResponse(

	@field:SerializedName("data")
	val data: List<DataItem?>? = null,

	@field:SerializedName("to")
	val to: String? = null
) : Parcelable

@Parcelize
data class DataNotification(

	@field:SerializedName("body")
	val body: String? = null,

	@field:SerializedName("title")
	val title: String? = null
) : Parcelable
