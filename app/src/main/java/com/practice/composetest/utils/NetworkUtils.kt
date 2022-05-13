package com.practice.composetest.utils

import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import android.os.Build
import com.facebook.stetho.okhttp3.StethoInterceptor
import com.practice.composetest.BuildConfig
import com.practice.composetest.network.interceptors.HeaderInterceptor
import com.practice.composetest.network.interceptors.NetworkConnectionInterceptor
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import timber.log.Timber
import java.io.IOException
import java.net.HttpURLConnection.*
import java.net.InetSocketAddress
import java.net.Socket
import java.util.concurrent.TimeUnit

private const val HTTP_TOO_MANY_REQUESTS = 429
private const val CONNECTION_TIME_OUT = 45L
private const val WRITE_TIME_OUT = 45L
private const val READ_TIME_OUT = 45L
private const val SOCKET_CONNECTION_TIME_OUT = 2000

// Method to check internet availability
fun Context.isInternetAvailable(): Boolean = isWifiTurnedOn() || isMobileDataTurnedOn()

// Method to check wifi availability
@Suppress("DEPRECATION")
private fun Context.isWifiTurnedOn(): Boolean {

    getConnectivityManager()?.apply {
        return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            getNetworkCapabilities(activeNetwork)?.hasTransport(NetworkCapabilities.TRANSPORT_WIFI) == true
        } else activeNetworkInfo?.type == ConnectivityManager.TYPE_WIFI
    }
    return false
}

// Method to check Mobile Data availability
@Suppress("DEPRECATION")
private fun Context.isMobileDataTurnedOn(): Boolean {

    getConnectivityManager()?.apply {
        return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M)
            getNetworkCapabilities(activeNetwork)
                ?.hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR) == true
        else activeNetworkInfo?.type == ConnectivityManager.TYPE_MOBILE
    }
    return false
}

fun String?.isHostAvailable(port: Int): Boolean {
    return try {
        Socket().use { socket ->
            socket.connect(InetSocketAddress(this, port), SOCKET_CONNECTION_TIME_OUT)
            true
        }
    } catch (e: IOException) {
        // Either we have a timeout or unreachable host or failed DNS lookup
        Timber.d("Network Connection Exception $e")
        false
    }
}

private fun Context.getConnectivityManager(): ConnectivityManager? {
    return getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager?
}

// Common method to get message for the specific network response code
fun getHttpStatusResponseMessage(httpStatus: Int): String = "$httpStatus - ${
    when (httpStatus) {
        HTTP_UNAUTHORIZED -> "Unauthorized"
        HTTP_FORBIDDEN -> "Forbidden"
        HTTP_NOT_FOUND -> "Not Found"
        HTTP_BAD_METHOD -> "Method Not Allowed"
        HTTP_NOT_ACCEPTABLE -> "Not Acceptable"
        HTTP_CLIENT_TIMEOUT -> "Request Time-Out"
        HTTP_TOO_MANY_REQUESTS -> "Too Many Requests"
        HTTP_INTERNAL_ERROR -> "Internal Server Error"
        HTTP_UNAVAILABLE -> "Service Unavailable"
        HTTP_BAD_GATEWAY -> "Bad Gateway"
        HTTP_NOT_IMPLEMENTED -> "Not Implemented"
        HTTP_GATEWAY_TIMEOUT -> "Gateway Timeout"
        else -> "Unknown API Error/Exception"
    }
}"

fun getOkhttpClient(
    headerInterceptor: HeaderInterceptor,
    loggingInterceptor: HttpLoggingInterceptor,
    networkConnectionInterceptor: NetworkConnectionInterceptor
): OkHttpClient {
    OkHttpClient.Builder().apply {
        connectTimeout(CONNECTION_TIME_OUT, TimeUnit.SECONDS)
        readTimeout(READ_TIME_OUT, TimeUnit.SECONDS)
        writeTimeout(WRITE_TIME_OUT, TimeUnit.SECONDS)
        addInterceptor(networkConnectionInterceptor)
        addInterceptor(headerInterceptor)
        if (BuildConfig.DEBUG) {
            addInterceptor(loggingInterceptor)
            addNetworkInterceptor(StethoInterceptor())
        }
    }.also {
        return it.build()
    }
}
