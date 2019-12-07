package com.assignment.data.usecase

interface UseCase<R> {
    fun execute() : R
}

interface CompletableUseCase {
    fun execute()
}

interface UseCaseWithParameter<P, R> {
    fun execute(parameter: P) : R
}

interface CompletableUseCaseWithParameter<P> {
    fun execute(parameter: P)
}