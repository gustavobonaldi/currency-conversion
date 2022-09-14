package br.com.bonaldi.currency.conversion.core.common.data.model.error

import com.google.gson.annotations.SerializedName

data class ApiException(
    val code: Int? = null,
    @SerializedName("info") override val message: String? = null
) : Exception(message)