package br.com.bonaldi.currency.conversion.data.repository

import br.com.bonaldi.currency.conversion.api.api.config.Resource
import br.com.bonaldi.currency.conversion.api.api.config.ResponseHandler

open class BaseRepository(private val responseHandler: ResponseHandler) {

    suspend fun createRequest(onShouldShowLoading: (Boolean) -> Unit, request: suspend () -> Resource<out Any> ): Resource<out Any> {
        return try {
            onShouldShowLoading.invoke(true)
            request()
        } catch (e: Exception) {
            responseHandler.handleException(e)
        } finally {
            onShouldShowLoading.invoke(false)
        }
    }
}