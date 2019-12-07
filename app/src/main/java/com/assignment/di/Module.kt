package com.assignment.di

import com.assignment.data.RetrofitManager
import com.assignment.data.api.ApiService
import com.assignment.data.repository.InfoRepository
import com.assignment.data.repository.InfoRepositoryImpl
import com.assignment.data.usecase.GetInfoUseCase
import com.assignment.view.vm.MainVM
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val context = module {
    single { RetrofitManager.retrofit("https://dl.dropboxusercontent.com/") }
}

val repository = module {
    single { InfoRepositoryImpl(get()) as InfoRepository }
}

val useCase = module {
    factory { GetInfoUseCase(get()) }
}

val apiService = module {
    single { ApiService.createService(get()) }
}

val vm = module {
    viewModel { MainVM(get()) }
}