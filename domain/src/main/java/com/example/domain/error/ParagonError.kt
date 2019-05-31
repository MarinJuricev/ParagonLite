package com.example.domain.error

import java.lang.Exception

sealed class ParagonError: Exception(){

    object LocalIOException: ParagonError()
    object BluetoothException: ParagonError()
    object PrintException: ParagonError()
}