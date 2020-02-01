package com.assignment.data.usecase

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.assignment.data.Event
import com.assignment.data.Response
import com.assignment.data.Result
import com.assignment.data.bean.ErrorResponse
import com.assignment.data.bean.InfoModel
import com.assignment.data.repository.InfoRepository
import com.assignment.extension.fromJson
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch

class GetInfoUseCase(private val infoRepository: InfoRepository) :
    UseCase<LiveData<Event<Result<InfoModel>>>> {
    override fun execute(scope: CoroutineScope): LiveData<Event<Result<InfoModel>>> {
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
    }
}