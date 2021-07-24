package br.com.bonaldi.currency.conversion.api.dto

import kotlin.collections.HashMap


open class ApiResponse(
   open val success: Boolean? = null,
   open val error: ErrorDTO? = null,
   open val terms: String? = null,
   open val timestamp: Long? = null,
   open val source: String? = null
) {

}