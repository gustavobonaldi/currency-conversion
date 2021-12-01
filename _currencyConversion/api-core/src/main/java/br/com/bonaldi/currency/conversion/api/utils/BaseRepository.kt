package br.com.bonaldi.currency.conversion.api.utils

import br.com.bonaldi.currency.conversion.api.api.config.ErrorHandler
import br.com.bonaldi.currency.conversion.api.api.config.Resource
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.*

open class BaseRepository(private val defaultDispatcher: CoroutineDispatcher){
    suspend fun <T> createRequest(
        request: suspend () -> T,
        localRequest: (() -> Flow<T>)? = null,
        updateLocal: (suspend (T) -> Unit)? = null,
        onResult: suspend (Flow<Resource<T>>) -> Unit
    ) {
        when {
            localRequest != null && updateLocal != null -> {
                localRequest.invoke().collectLatest {
                    onResult.invoke(flowOf<Resource<T>>(Resource.Success(it)))
                    updateLocal.invoke(request())
                }
            }
            else -> {
                val remoteFlow = flow {
                    try {
                        val apiResponse = request()
                        if (localRequest == null) {
                            emit(Resource.Loading(true))
                        }
                        if (localRequest == null) {
                            emit(Resource.Success(apiResponse))
                        }
                        updateLocal?.invoke(apiResponse)
                    } catch (ex: Exception) {
                        emit(ErrorHandler.handleException<T>(ex))
                    } finally {
                        emit(Resource.Loading<T>(false))
                    }
                }
                onResult.invoke(remoteFlow)
            }
        }
    }

    fun <T> createRequest3(
        request: suspend () -> T,
        localRequest: (suspend () -> T)? = null,
        updateLocal: (suspend (T) -> Unit)? = null,
    ): Flow<Resource<T>> = flow {
        emit(Resource.Loading(true))
        try {
            localRequest?.let {
                emit(Resource.Success(it.invoke()))
                emit(Resource.Loading(false))

                request.invoke().apply {
                    updateLocal?.invoke(this)
                }
            } ?: run {
                request.invoke().apply {
                    updateLocal?.invoke(this)
                    emit(Resource.Success(this))
                }
            }
        } catch (e: Exception) {
            emit(ErrorHandler.handleException(e))
        } finally {
            emit(Resource.Loading(false))
        }
    }
}