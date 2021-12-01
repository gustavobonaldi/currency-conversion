package br.com.bonaldi.currency.conversion.api.dto

open class ApiResponse(
   val success: Boolean? = null,
   val error: ErrorDTO? = null,
   val terms: String? = null,
   val timestamp: Long? = null,
   val source: String? = null
)