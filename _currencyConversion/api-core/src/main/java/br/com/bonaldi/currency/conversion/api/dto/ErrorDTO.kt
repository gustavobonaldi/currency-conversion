package br.com.bonaldi.currency.conversion.api.dto

import com.google.gson.annotations.SerializedName

data class ErrorDTO(
    val code: Int? = null,
    @SerializedName("info") val message: String? = null
)