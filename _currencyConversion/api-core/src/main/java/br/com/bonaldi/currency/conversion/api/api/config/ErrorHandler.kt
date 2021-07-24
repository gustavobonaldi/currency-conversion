package br.com.bonaldi.currency.conversion.api.api.config

import br.com.bonaldi.currency.conversion.api.dto.ApiResponse
import br.com.bonaldi.currency.conversion.api.dto.ErrorDTO
import com.google.gson.Gson
import retrofit2.HttpException
import java.net.SocketTimeoutException
import java.lang.Exception
import kotlin.reflect.javaType
import kotlin.reflect.typeOf

open class ErrorHandler {
    companion object {
        fun <T: Exception> handleException(exception: T): ErrorDTO {
            return when (exception) {
                is HttpException -> Gson().fromJson(exception.response()?.errorBody()?.string(), ErrorDTO::class.java)
                is SocketTimeoutException -> ErrorDTO(info = getErrorMessage(ErrorCodes.SocketTimeOut.code), code = ErrorCodes.SocketTimeOut.code)
                else -> ErrorDTO(info = getErrorMessage(Int.MAX_VALUE))
            }
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