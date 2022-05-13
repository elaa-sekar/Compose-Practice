package com.practice.composetest.di

import android.content.Context
import com.practice.composetest.BuildConfig
import com.practice.composetest.network.api.ApiHelper
import com.practice.composetest.network.api.ApiHelperImpl
import com.practice.composetest.network.api.ApiService
import com.practice.composetest.network.interceptors.HeaderInterceptor
import com.practice.composetest.network.interceptors.NetworkConnectionInterceptor
import com.practice.composetest.utils.getOkhttpClient
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object NetworkModule {

    @Provides
    fun provideBaseUrl() = BuildConfig.BASE_URL

    @Singleton
    @Provides
    fun provideOkHttpClient(
        headerInterceptor: HeaderInterceptor,
        loggingInterceptor: HttpLoggingInterceptor,
        networkConnectionInterceptor: NetworkConnectionInterceptor
    ): OkHttpClient = getOkhttpClient(
        headerInterceptor, loggingInterceptor, networkConnectionInterceptor
    )

    @Singleton
    @Provides
    fun provideNetworkConnectionInterceptor(@ApplicationContext context: Context): NetworkConnectionInterceptor =
        NetworkConnectionInterceptor(context)

    @Singleton
    @Provides
    fun provideHeaderInterceptor() = HeaderInterceptor()

    @Singleton
    @Provides
    fun provideLoggingInterceptor(): HttpLoggingInterceptor {
        HttpLoggingInterceptor().apply {
            setLevel(HttpLoggingInterceptor.Level.BODY)
        }.also {
            return it
        }
    }

    @Singleton
    @Provides
    fun provideGsonConverterFactory(): GsonConverterFactory = GsonConverterFactory.create()

    @Singleton
    @Provides
    fun provideRetrofit(
        okHttpClient: OkHttpClient,
        BASE_URL: String,
        gsonConverterFactory: GsonConverterFactory
    ): Retrofit = Retrofit.Builder()
        .addConverterFactory(gsonConverterFactory)
        .baseUrl(BASE_URL)
        .client(okHttpClient)
        .build()

    @Provides
    @Singleton
    fun provideApiService(retrofit: Retrofit): ApiService =
        retrofit.create(ApiService::class.java)

    @Provides
    @Singleton
    fun provideApiHelper(apiHelper: ApiHelperImpl): ApiHelper = apiHelper
}