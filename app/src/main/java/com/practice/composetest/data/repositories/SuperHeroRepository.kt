package com.practice.composetest.data.repositories

import com.practice.composetest.network.SafeApiRequest
import com.practice.composetest.network.api.ApiHelper
import javax.inject.Inject

class SuperHeroRepository @Inject constructor(private val apiHelper: ApiHelper) : SafeApiRequest() {

    suspend fun getSuperHeroes() = apiRequest {
        apiHelper.getSuperHeroes()
    }
}