package com.practice.composetest.network.api

import com.practice.composetest.data.responses.SuperHeroResponse
import retrofit2.Response

interface ApiHelper {
    suspend fun getSuperHeroes() : Response<SuperHeroResponse>
}