package com.practice.composetest.network.interceptors

import com.practice.composetest.utils.AppConstants.ACCEPT
import com.practice.composetest.utils.AppConstants.APPLICATION_JSON
import com.practice.composetest.utils.AppConstants.CONTENT_TYPE
import okhttp3.Interceptor
import okhttp3.Response

class HeaderInterceptor : Interceptor {

    override fun intercept(chain: Interceptor.Chain): Response {
        chain.request().newBuilder().apply {
            header(CONTENT_TYPE, APPLICATION_JSON)
            header(ACCEPT, APPLICATION_JSON)
            /*Session.getAuthToken().ifNotEmpty { token ->
                header(AUTHORIZATION, BEARER + SPACE + token)
            }*/
        }.also { request ->
            return chain.proceed(request.build())
        }
    }
}