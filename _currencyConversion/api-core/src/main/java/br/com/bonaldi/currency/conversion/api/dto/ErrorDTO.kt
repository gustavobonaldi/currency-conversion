package br.com.bonaldi.currency.conversion.api.dto

data class ErrorDTO(
    val code: Int? = null,
    val info: String = "",
    val type: String? = null
)