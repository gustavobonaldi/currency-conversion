package br.com.bonaldi.currency.conversion.api.api.config

import br.com.bonaldi.currency.conversion.api.dto.ApiResponse
import com.google.gson.Gson
import retrofit2.HttpException
import java.net.SocketTimeoutException
import java.lang.Exception


enum class ErrorCodes(val code: Int) {
    SocketTimeOut(-1)
}

open class ResponseHandler {
    fun <T : ApiResponse> handleSuccess(data: T): Resource<T> {
        return Resource.success(data)
    }

    fun <T: Exception> handleException(e: T): Resource<out ApiResponse> {
        if(e is HttpException)
        {
            if(e.response()?.errorBody() != null && e.response()?.errorBody() != null) {
                val errorString = e.response()?.errorBody()?.string()
                val apiResponse: ApiResponse = Gson().fromJson(errorString, ApiResponse::class.java)
                return Resource.error(getErrorMessage(e.code()), apiResponse)
            }
        }
        return when (e) {
            is SocketTimeoutException -> Resource.error(getErrorMessage( ErrorCodes.SocketTimeOut.code), null)
            else -> Resource.error(getErrorMessage(Int.MAX_VALUE), null)
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