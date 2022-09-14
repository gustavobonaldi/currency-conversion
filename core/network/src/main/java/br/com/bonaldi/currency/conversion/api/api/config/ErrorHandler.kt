package br.com.bonaldi.currency.conversion.api.api.config

import br.com.bonaldi.currency.conversion.core.common.data.model.error.ErrorDTO
import com.google.gson.Gson
import retrofit2.HttpException
import java.net.SocketTimeoutException

open class ErrorHandler {
    companion object {
        fun <T> handleException(exception: Throwable): ResponseResource<T> {
            return ResponseResource.Error(error = when (exception) {
                is HttpException -> Gson().fromJson(exception.response()?.errorBody()?.string(), ErrorDTO::class.java)
                is SocketTimeoutException -> ErrorDTO(message = getErrorMessage(ErrorCodes.SocketTimeOut.code), code = ErrorCodes.SocketTimeOut.code)
                else -> ErrorDTO(message = getErrorMessage(Int.MAX_VALUE))
            })
        }

        private fun getErrorMessage(code: Int): String {
            return when (code) {
                ErrorCodes.SocketTimeOut.code -> "Timeout"
                401 -> "Unauthorised"
                404 -> "Not found"
                else -> "Something went wrong"
            }
        }
    }

    internal enum class ErrorCodes(val code: Int) {
        SocketTimeOut(-1)
    }
}