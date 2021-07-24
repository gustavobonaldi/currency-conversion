package br.com.bonaldi.currency.conversion.data.repository

import br.com.bonaldi.currency.conversion.api.api.config.Resource
import br.com.bonaldi.currency.conversion.api.api.config.ErrorHandler
import br.com.bonaldi.currency.conversion.api.dto.ApiResponse
import br.com.bonaldi.currency.conversion.api.dto.ErrorDTO
import br.com.bonaldi.currency.conversion.api.room.dao.BaseDao
import kotlinx.coroutines.*
import kotlin.coroutines.CoroutineContext

open class BaseRepository() {

    suspend fun <T> createRequest(
        onShouldShowLoading: (Boolean) -> Unit,
        onError: (ErrorDTO) -> Unit,
        request: suspend () -> T,
        onSuccess: (T) -> Unit
    ) {
        try {
            onShouldShowLoading.invoke(true)
            coroutineScope {
                launch(context = Dispatchers.IO) {
                    val response = request.invoke()
                    onSuccess.invoke(response)
                }
            }
        } catch (e: Exception) {
            onError.invoke(ErrorHandler.handleException(e))
        } finally {
            onShouldShowLoading.invoke(false)
        }
    }

    fun runOnBG(run: () -> Unit){
        GlobalScope.launch(Dispatchers.IO){
            run.invoke()
        }
    }
}