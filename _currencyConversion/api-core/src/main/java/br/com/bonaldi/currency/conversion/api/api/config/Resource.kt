package br.com.bonaldi.currency.conversion.api.api.config

import br.com.bonaldi.currency.conversion.api.dto.ErrorDTO

sealed class Resource<T>(val data: T? = null, val message: String? = null) {
    class Loading<T>(val isLoading: Boolean): Resource<T>()
    class Success<T>(data: T?): Resource<T>(data)
    class Error<T>(val error: ErrorDTO): Resource<T>()
}
