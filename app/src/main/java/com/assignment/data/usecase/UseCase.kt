package com.assignment.data.usecase

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers

interface UseCase<R> {
    fun execute(scope: CoroutineScope = CoroutineScope(Dispatchers.IO)): R
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