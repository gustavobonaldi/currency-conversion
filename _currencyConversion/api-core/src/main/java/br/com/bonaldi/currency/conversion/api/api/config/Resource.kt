package br.com.bonaldi.currency.conversion.api.api.config

import br.com.bonaldi.currency.conversion.api.dto.ApiResponse

data class Resource<T>(val status: Status, val data: T?, val message: String?) {
    companion object {
        fun <T: ApiResponse?> error(message: String, data: T?): Resource<T> {
            return Resource(
                Status.ERROR,
                data,
                data?.error?.info ?: message
            )
        }
    }
}