package com.assignment.data.usecase

import com.assignment.data.Result
import com.assignment.data.bean.InfoModel
import com.assignment.data.manager.UserDataManager

class GetInfoUseCase(private val userDataManager: UserDataManager) :
    UseCase<Nothing, InfoModel> {

    override suspend fun onExecute(parameter: Nothing?): Result<InfoModel> {
        return userDataManager.userInfo()
    }
}