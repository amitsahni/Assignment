package com.assignment.view.vm

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Transformations
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.assignment.data.usecase.GetInfoUseCase

class MainVM(private val getInfoUseCase: GetInfoUseCase) : ViewModel() {

    private val _fetchInfo = MutableLiveData<Unit>()
    val fetchInfoLiveData = Transformations.switchMap(_fetchInfo) {
        getInfoUseCase.execute(scope = viewModelScope)
    }


    fun onAction(action : MainAction){
        when(action){

            is MainAction.UserInfo -> {
                _fetchInfo.postValue(Unit)
            }
        }
    }
}

sealed class MainAction {

    object UserInfo : MainAction()
}