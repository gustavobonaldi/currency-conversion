package br.com.bonaldi.currency.conversion.core.common.data.model.error

import com.google.gson.annotations.SerializedName

data class ErrorDTO(
    val code: Int? = null,
    @SerializedName("info") val message: String? = null
)