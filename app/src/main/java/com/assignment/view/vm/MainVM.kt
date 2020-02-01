package com.assignment.view.vm

import androidx.lifecycle.*
import com.assignment.data.Event
import com.assignment.data.EventObserver
import com.assignment.data.Result
import com.assignment.data.bean.ErrorResponse
import com.assignment.data.bean.InfoModel
import com.assignment.data.bean.Row
import com.assignment.data.usecase.GetInfoUseCase

class MainVM(private val getInfoUseCase: GetInfoUseCase) : ViewModel() {

    private val _fetchInfo = MutableLiveData<MainState>()
    val fetchInfoLiveData = _fetchInfo as LiveData<MainState>
    private val _mediatorLiveData = MediatorLiveData<Event<Result<InfoModel>>>()

    init {
        Transformations.map(_mediatorLiveData) {}.observeForever {}
    }


    fun onAction(event: MainEvent) {

        when (event) {
            is MainEvent.FetchEvent -> {
                _mediatorLiveData.addSource(getInfoUseCase.execute(viewModelScope), EventObserver {
                    when (it) {
                        is Result.Loading -> {
                            _fetchInfo.postValue(MainState.Loading)
                        }
                        is Result.Error -> {
                            _fetchInfo.postValue(MainState.Error(it.error))
                        }
                        is Result.Success -> {
                            val rowItems = mutableListOf<RowItem>()
                            it.data?.rows?.mapTo(rowItems) {
                                RowItem(it, this::onAction)
                            }
                            _fetchInfo.postValue(MainState.Success(rowItems))
                        }
                    }
                })
            }

            is MainEvent.ClickEvent -> {
                _fetchInfo.postValue(MainState.Click(event.row))
            }
        }
    }
}


sealed class MainEvent {
    object FetchEvent : MainEvent()
    data class ClickEvent(val row: Row) : MainEvent()
}


sealed class MainState {
    object Loading : MainState()
    data class Success(val data: List<RowItem>) : MainState()
    data class Error(val error: ErrorResponse) : MainState()
    data class Click(val row: Row) : MainState()
}

class RowItem(
    val row: Row,
    private val callback: (MainEvent) -> Unit
) {
    fun onTapped() {
        callback.invoke(MainEvent.ClickEvent(row))
    }
}