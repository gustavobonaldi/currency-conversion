package br.com.bonaldi.currency.conversion.api.utils

import br.com.bonaldi.currency.conversion.api.api.config.ResponseResource
import br.com.bonaldi.currency.conversion.api.dto.ApiResponse
import br.com.bonaldi.currency.conversion.api.dto.ErrorDTO
import kotlinx.coroutines.currentCoroutineContext
import kotlinx.coroutines.flow.*
import retrofit2.HttpException

open class BaseRepository() {

    suspend fun <RequestType : ApiResponse, LocalType> request(
        apiRequest: suspend () -> RequestType?,
        localMapper: (RequestType?) -> LocalType?,
        localRequest: () -> Flow<LocalType>,
        updateLocal: (suspend (LocalType) -> Unit)? = null,
    ): Flow<ResponseResource<LocalType>> {
        try {
            return flowOf<ResponseResource<LocalType>>().onStart {
                emit(ResponseResource.Loading(true))
                val response = localMapper(apiRequest())
                response?.let { data -> updateLocal?.invoke(data) }
            }.onEach { result ->
                result.data?.let { data -> updateLocal?.invoke(data) }
            }.onCompletion {
                emit(ResponseResource.Loading(false))
                emitAll(localRequest().map {
                    ResponseResource.Success(it)
                })
            }.catch { exception ->
                when (exception) {
                    is HttpException -> {
                        emit(
                            ResponseResource.Error(
                                ErrorDTO(
                                    exception.code(),
                                    exception.message()
                                )
                            )
                        )
                    }
                    else -> {
                        emit(
                            ResponseResource.Error(
                                ErrorDTO(
                                    400,
                                    exception.message
                                )
                            )
                        )
                    }
                }
            }
        } catch (e: HttpException) {
            return flow {
                emit(ResponseResource.Error(ErrorDTO(e.code(), e.message())))
            }
        } catch (e: Throwable){
            return flow {
                emit(ResponseResource.Error(ErrorDTO(400, e.message)))
            }
        }
    }
}