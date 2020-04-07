package com.assignment.data.usecase

import com.assignment.data.Result
import com.assignment.data.bean.InfoModel
import com.assignment.data.datasource.InfoDataSource
import com.assignment.data.repository.InfoRepository

class GetInfoUseCase(private val infoRepository: InfoRepository) :
    UseCase<InfoModel> {
    /*override fun execute(scope: CoroutineScope): LiveData<Event<Result<InfoModel>>> {
        val mutableLiveData = MutableLiveData<Event<Result<InfoModel>>>()
        mutableLiveData.postValue(Event(Result.Loading))
        val handlerException = CoroutineExceptionHandler { _, throwable ->
            mutableLiveData.postValue(Event(Result.Error(ErrorResponse(throwable.message ?: ""))))
        }
        scope.launch(handlerException) {
            infoRepository.fetchInfo().apply {
                val toPost = when (this) {
                    is Response.Success -> {
                        Result.Success(data)
                    }
                    is Response.Error -> {
                        Result.Error(data.fromJson())
                    }
                }
                mutableLiveData.postValue(Event(toPost))
            }
        }
        return mutableLiveData
    }*/

    override suspend fun execute(parameter: Nothing): Result<InfoModel> {
        return InfoDataSource().infoResult()
    }
}

/*
fun <T> Response<T>.toResult(): Result<T> {
    return when (this) {
        is Response.Success -> Result.Success(this.data)
        is Response.Error -> Result.Error(this.data.fromJson())
    }
}*/
