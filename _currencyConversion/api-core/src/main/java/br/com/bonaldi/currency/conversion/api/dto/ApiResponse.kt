package br.com.bonaldi.currency.conversion.api.dto

import kotlin.collections.HashMap


open class ApiResponse(
   open val success: Boolean? = null,
   open val error: ErrorDTO? = null,
   open val terms: String? = null,
   open val privacy: String? = null,
   open val timestamp: Int? = null,
   open val path: String? = null,
   open val source: String? = null
) {

}