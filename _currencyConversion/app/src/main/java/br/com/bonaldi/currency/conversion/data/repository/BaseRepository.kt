package br.com.bonaldi.currency.conversion.data.repository

import androidx.navigation.NavDestination
import br.com.bonaldi.currency.conversion.api.api.config.Resource
import br.com.bonaldi.currency.conversion.api.api.config.ResponseHandler
import br.com.bonaldi.currency.conversion.api.dto.ApiResponse
import java.lang.reflect.Type
import kotlin.reflect.javaType
import kotlin.reflect.typeOf

open class BaseRepository(var responseHandler: ResponseHandler) {

    suspend inline fun <reified T : ApiResponse> createRequest(
        onShouldShowLoading: (Boolean) -> Unit,
        request: suspend () -> Resource<out T>,
        error: (e: Exception, type: Class<T>) -> Unit
    ) {
        try {
            onShouldShowLoading.invoke(true)
            request()
        } catch (e: Exception) {
            error.invoke(e, T::class.java)
        } finally {
            onShouldShowLoading.invoke(false)
        }
    }
}