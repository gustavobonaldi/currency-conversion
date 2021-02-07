package br.com.bonaldi.currency.conversion.api.api.config

import com.google.gson.GsonBuilder
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class NetworkUtils {
    companion object Factory {
        fun <T> provideRetrofit(webServiceApi: Class<T>, apiUrl: String, okHttpClient: OkHttpClient): T {
            var gson = GsonBuilder().setLenient().create()
            val retrofit = Retrofit.Builder()
                .baseUrl(apiUrl)
                .addConverterFactory(GsonConverterFactory.create(gson))
                .client(okHttpClient)
                .build()
            return retrofit.create(webServiceApi)
        }

        fun provideOkHttpClient(requestInterceptor: RequestInterceptor): OkHttpClient {
            val loggingInterceptor = HttpLoggingInterceptor()
            loggingInterceptor.level = HttpLoggingInterceptor.Level.BODY
            return OkHttpClient()
                .newBuilder()
                .addInterceptor(requestInterceptor)
                .addInterceptor(loggingInterceptor)
                .build()
        }
    }
}