package br.com.btg.btgchallenge.data.api;

import br.com.btg.btgchallenge.api.dto.ApiResponse
import retrofit2.http.*

interface CurrencyLayerServices {
    @GET("/list")
    suspend fun getCurrencies(): ApiResponse<Any>

    @GET("/live")
    suspend fun getRealTimeRates(): ApiResponse<Any>
}