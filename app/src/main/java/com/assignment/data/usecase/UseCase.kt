package com.assignment.data.usecase

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.assignment.data.Event
import com.assignment.data.Result
import kotlinx.coroutines.*

interface UseCase<P, R> : BaseUseCase<P, R> {
    override suspend fun onExecute(parameter: P?): Result<R>
}

interface BaseUseCase<P, R> {

    suspend fun onExecute(parameter: P?): Result<R>

    fun execute(parameter: P? = null, scope: CoroutineScope): LiveData<Event<Result<R>>> {
        val mutableLiveData = MutableLiveData<Event<Result<R>>>()
        mutableLiveData.postValue(Event(Result.Loading))
        val handlerException = CoroutineExceptionHandler { _, throwable ->
            mutableLiveData.postValue(Event(Result.Exception(throwable)))
        }
        scope.plus(Dispatchers.IO).launch(handlerException) {
            mutableLiveData.postValue(Event(onExecute(parameter)))
        }
        return mutableLiveData
    }
}