package com.example.kotlin_assignment_eighteen.model

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.parcelize.Parcelize

@Parcelize
data class GetAllUserResponse(

	@field:SerializedName("data")
	val data: List<DataItemUser>? = null,

	@field:SerializedName("message")
	val message: String? = null,

	@field:SerializedName("status")
	val status: Int? = null
) : Parcelable

@Parcelize
data class DataItemUser(

	@field:SerializedName("password")
	var password: String? = null,

	@field:SerializedName("name_user")
	var nameUser: String? = null,

	@field:SerializedName("email_user")
	var emailUser: String? = null,

	@field:SerializedName("photo_user")
	val photoUser: String? = null,

	@field:SerializedName("id")
	val id: String? = null
) : Parcelable
