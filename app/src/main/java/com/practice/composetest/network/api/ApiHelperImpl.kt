package com.practice.composetest.network.api

import javax.inject.Inject

class ApiHelperImpl @Inject constructor(private val apiService: ApiService) : ApiHelper {

    override suspend fun getSuperHeroes() = apiService.getSuperHeroes()
}