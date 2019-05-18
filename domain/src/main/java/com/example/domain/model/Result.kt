package com.example.domain.model

/**
 * Result Wrapper <Left = Exception, Right = Value/Success>
 */
sealed class Result<out E, out V> {

    data class Value<out V>(val value: V) : Result<Nothing, V>()
    data class Error<out E>(val error: E) : Result<E, Nothing>()

    companion object Factory {
        //higher order functions take functions as parameters or return a function
        //Kotlin has function types name: () -> V
        inline fun <V> build(function: () -> V): Result<Exception, V> =
            try {
                Value(function.invoke())
            } catch (e: java.lang.Exception) {
                Error(e)
            }
    }

}