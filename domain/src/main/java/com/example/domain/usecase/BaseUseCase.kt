package com.example.domain.usecase

import com.example.domain.model.Result
import java.lang.Exception

abstract class BaseUseCase {

    abstract suspend fun execute(): Result<Exception, Any>
}