package br.com.bonaldi.currency.conversion.core.common.data.model

import br.com.bonaldi.currency.conversion.core.common.data.model.error.ErrorDTO

open class ApiResponse(
    val success: Boolean? = null,
    val error: ErrorDTO? = null,
    val terms: String? = null,
    val timestamp: Long? = null,
    val source: String? = null
)