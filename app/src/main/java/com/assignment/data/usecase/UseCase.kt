package com.assignment.data.usecase

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.assignment.data.Event
import com.assignment.data.Result
import com.assignment.data.bean.ErrorResponse
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

interface UseCase<R> : BaseUseCase<Nothing, R> {

}

interface CompletableUseCase {
    fun execute(scope: CoroutineScope = CoroutineScope(Dispatchers.IO))
}

interface UseCaseWithParameter<P, R> {
    fun execute(parameter: P, scope: CoroutineScope = CoroutineScope(Dispatchers.IO)): R
}

interface CompletableUseCaseWithParameter<P> {
    fun execute(parameter: P, scope: CoroutineScope = CoroutineScope(Dispatchers.IO))
}

interface BaseUseCase<P, R> {
    suspend fun execute(parameter: P): Result<R>

    suspend fun execute(
        scope: CoroutineScope = CoroutineScope(Dispatchers.IO),
        parameter: P
    ): LiveData<Event<Result<R>>> {
        val mutableLiveData = MutableLiveData<Event<Result<R>>>()
        mutableLiveData.postValue(Event(Result.Loading))
        val handlerException = CoroutineExceptionHandler { _, throwable ->
            mutableLiveData.postValue(Event(Result.Error(ErrorResponse(throwable.message ?: ""))))
        }
        scope.launch(handlerException) {
            mutableLiveData.postValue(Event(execute(parameter)))
        }
        return mutableLiveData
    }
}