package com.assignment.data.manager

import com.assignment.data.Result
import com.assignment.data.bean.InfoModel


interface UserDataManager {

    suspend fun userInfo(): Result<InfoModel>
}