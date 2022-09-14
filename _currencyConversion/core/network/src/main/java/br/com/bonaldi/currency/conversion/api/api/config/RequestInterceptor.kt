package br.com.bonaldi.currency.conversion.api.api.config

import br.com.bonaldi.currency.conversion.api.BuildConfig
import okhttp3.HttpUrl
import okhttp3.Interceptor
import okhttp3.Request
import okhttp3.Response


class RequestInterceptor() : Interceptor {

    override fun intercept(chain: Interceptor.Chain): Response {
        var request: Request = chain.request()
        val url: HttpUrl = request.url.newBuilder().addQueryParameter("access_key", BuildConfig.CURRENCY_LAYER_TOKEN).build()
        request = request.newBuilder().url(url).build()
        return chain.proceed(request)
    }
}