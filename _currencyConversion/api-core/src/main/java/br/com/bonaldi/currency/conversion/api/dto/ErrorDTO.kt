package br.com.bonaldi.currency.conversion.api.dto


import androidx.annotation.StringRes

data class ErrorDTO(
    val code: Int? = null,
    val info: String? = null,
    val type: String? = null
)