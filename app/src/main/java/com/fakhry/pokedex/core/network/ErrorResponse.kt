package com.fakhry.pokedex.core.network

import com.google.gson.annotations.SerializedName

data class ErrorResponse(

	@field:SerializedName("code")
	val code: Int,

	@field:SerializedName("message")
	val message: String,
)
