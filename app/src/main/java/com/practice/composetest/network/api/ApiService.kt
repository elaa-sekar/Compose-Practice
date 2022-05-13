package com.practice.composetest.network.api

import com.practice.composetest.data.responses.SuperHeroResponse
import retrofit2.Response
import retrofit2.http.GET

interface ApiService {

    @GET("demos/marvel")
    suspend fun getSuperHeroes() : Response<SuperHeroResponse>
}