/**
 * Created by Amit Singh on 07/04/20.
 * Tila
 * asingh@tila.com
 */

package com.assignment.data.datasource

import com.assignment.data.Response
import com.assignment.data.Result
import com.assignment.data.bean.InfoModel
import com.assignment.data.repository.InfoRepository
import com.assignment.data.usecase.GetInfoUseCase
import com.assignment.extension.fromJson
import org.koin.core.inject


class InfoDataSource : BaseDataSource {
    private val getInfoUseCase: GetInfoUseCase by inject()
    private val infoRepository: InfoRepository by inject()

    suspend fun infoResult(): Result<InfoModel> {

        return when (val res= infoRepository.fetchInfo()) {
            is Response.Success -> Result.Success(res.data)
            is Response.Error -> Result.Error(res.data.fromJson())
        }
    }
}