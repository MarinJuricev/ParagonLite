package com.example.domain.error

sealed class ParagonError : Exception() {

    object LocalIOException : ParagonError()
    object BluetoothException : ParagonError()
    object PrintException : ParagonError()
    object ReceiptException : ParagonError()
}