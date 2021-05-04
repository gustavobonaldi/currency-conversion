package br.com.bonaldi.currency.conversion.api.dto

data class ErrorDTO(val errorMessage: Int,
                    val code: Int? = null,
                    val info: String? = null){

}