package com.practice.composetest.network

import com.practice.composetest.utils.BadRequestException
import com.practice.composetest.utils.NetworkResponseException
import com.practice.composetest.utils.getHttpStatusResponseMessage
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import org.json.JSONObject
import retrofit2.Response
import timber.log.Timber
import java.net.HttpURLConnection.HTTP_BAD_REQUEST

abstract class SafeApiRequest {

    suspend fun <R : Any> apiRequest(request: suspend () -> Response<R>): R? {
        request.invoke().let { response ->
            val errorResponse: String? = response.errorBody()?.charStream()?.readText()
            when {
                response.isSuccessful -> {
                    Timber.d("SafeApiRequest success")
                    return response.body()
                }
                response.code() == HTTP_BAD_REQUEST -> {
                    withContext(Dispatchers.IO) {
                        throw BadRequestException(JSONObject(errorResponse!!).getString("message"))
                    }
                }
                else -> {
                    Timber.d("SafeApiRequest error")
                    StringBuilder().apply {
                        withContext(Dispatchers.IO) {
                            append(
                                runCatching {
                                    JSONObject(errorResponse!!).getString("message")
                                }.getOrElse {
                                    it.message
                                }
                            )
                        }
                    }.also { message ->
                        throw NetworkResponseException(
                            response.code(),
                            message.toString(),
                            getHttpStatusResponseMessage(response.code())
                        )
                    }
                }
            }
        }
    }
}