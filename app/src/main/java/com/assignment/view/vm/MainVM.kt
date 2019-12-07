package com.assignment.view.vm

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Transformations
import androidx.lifecycle.ViewModel
import com.assignment.data.usecase.GetInfoUseCase

class MainVM(private val getInfoUseCase: GetInfoUseCase) : ViewModel() {

    private val _fetchInfo = MutableLiveData<Unit>()
    val fetchInfoLiveData = Transformations.switchMap(_fetchInfo) {
        getInfoUseCase.execute()
    }


    fun fetchInfo() {
        _fetchInfo.postValue(Unit)
    }
}