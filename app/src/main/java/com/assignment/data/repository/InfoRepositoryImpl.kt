package com.assignment.data.repository

import com.assignment.data.Response
import com.assignment.data.api.ApiService
import com.assignment.data.bean.InfoModel

class InfoRepositoryImpl(private val apiService: ApiService) : InfoRepository {

    override suspend fun fetchInfo(): Response<InfoModel> {
        val result = apiService.info()
        return if (result.isSuccessful) {
            Response.Success(result.body())
        } else {
            Response.Error(result.errorBody()?.string())
        }
    }
}