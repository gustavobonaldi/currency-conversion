package br.com.bonaldi.currency.conversion.api.dto.exception

import com.google.gson.annotations.SerializedName

data class ApiException(val code: Int? = null,
                        @SerializedName("info") override val message: String? = null): Exception(message)