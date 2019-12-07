package com.assignment.data.api

import com.assignment.data.bean.InfoModel
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.http.GET

interface ApiService {

    @GET("s/2iodh4vg0eortkl/facts.json")
    suspend fun info(): Response<InfoModel>

    companion object {

        fun createService(retrofit: Retrofit): ApiService {
            return retrofit.create(ApiService::class.java)
        }
    }
}