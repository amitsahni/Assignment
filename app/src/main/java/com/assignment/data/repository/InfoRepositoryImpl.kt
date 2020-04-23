package com.assignment.data.repository

import com.assignment.data.Response
import com.assignment.data.api.ApiService
import com.assignment.data.bean.InfoModel
import com.assignment.data.toResponse

class InfoRepositoryImpl(private val apiService: ApiService) : InfoRepository {

    override suspend fun fetchInfo(): Response<InfoModel> {
        return apiService.info().toResponse()
    }
}