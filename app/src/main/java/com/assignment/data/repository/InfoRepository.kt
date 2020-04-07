package com.assignment.data.repository

import com.assignment.data.Response
import com.assignment.data.bean.InfoModel

interface InfoRepository {

    suspend fun fetchInfo(): Response<InfoModel>
}

