package br.com.bonaldi.currency.conversion.api.api.config

import br.com.bonaldi.currency.conversion.core.common.data.model.error.ErrorDTO

sealed class ResponseResource<T>(open val data: T? = null, val message: String? = null) {
    class Loading<T>(val isLoading: Boolean): ResponseResource<T>()
    class Success<T>(override val data: T?): ResponseResource<T>(data)
    class Error<T>(val error: ErrorDTO): ResponseResource<T>()
}
