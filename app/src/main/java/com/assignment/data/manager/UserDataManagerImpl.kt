package com.assignment.data.manager

import com.assignment.data.Result
import com.assignment.data.bean.InfoModel
import com.assignment.data.repository.InfoRepository
import com.assignment.data.toResult

class UserDataManagerImpl(private val infoRepository: InfoRepository) : UserDataManager{

    override suspend fun userInfo(): Result<InfoModel> {
        return infoRepository.fetchInfo().toResult()
    }

}