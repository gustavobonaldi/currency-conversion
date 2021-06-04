package br.com.bonaldi.currency.conversion.data.repository

import br.com.bonaldi.currency.conversion.api.api.config.Resource
import br.com.bonaldi.currency.conversion.api.api.config.ResponseHandler
import br.com.bonaldi.currency.conversion.api.dto.ApiResponse

open class BaseRepository(open val responseHandler: ResponseHandler) {

    suspend inline fun <reified T : ApiResponse> createRequest(
        onShouldShowLoading: (Boolean) -> Unit,
        request: suspend () -> Resource<out T>
    ): Resource<out T>{
        return try {
            onShouldShowLoading.invoke(true)
            request.invoke()
        } catch (e: Exception) {
            return responseHandler.handleException(e, T::class.java)
        } finally {
            onShouldShowLoading.invoke(false)
        }
    }
}