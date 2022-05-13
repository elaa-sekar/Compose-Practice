package com.practice.composetest.network.interceptors

import android.content.Context
import com.practice.composetest.R
import com.practice.composetest.utils.NoInternetException
import com.practice.composetest.utils.isInternetAvailable
import okhttp3.Interceptor
import okhttp3.Response

class NetworkConnectionInterceptor(private val context: Context) : Interceptor {

    override fun intercept(chain: Interceptor.Chain): Response {
        if (context.isInternetAvailable()) {
            runCatching {
                return chain.proceed(chain.request())
            }.getOrElse { throw it }
        } else {
            throw NoInternetException(context.getString(R.string.no_connectivity_message))
        }
    }
}