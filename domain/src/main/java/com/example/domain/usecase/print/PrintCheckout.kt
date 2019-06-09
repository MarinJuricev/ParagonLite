package com.example.domain.usecase.print

import com.example.domain.DispatcherProvider
import com.example.domain.model.Result
import com.example.domain.repository.IBluetoothRepository
import com.example.domain.repository.ICheckoutRepository
import kotlinx.coroutines.withContext

class PrintCheckout(
    private val bluetoothRepository: IBluetoothRepository,
    private val checkoutRepository: ICheckoutRepository,
    private val dispatcherProvider: DispatcherProvider
) {

    suspend fun execute(
        valuesToPrint: List<ByteArray>,
        savedMacAddress: String
    ): Result<Exception, Unit> {
        return when (val result = bluetoothRepository.connectAndSendDataOverBluetooth(
            savedMacAddress,
            valuesToPrint
        )) {
            is Result.Value -> {
                deleteCurrentCheckoutItems(checkoutRepository, dispatcherProvider)
                result
            }
            is Result.Error -> result
        }
    }

    // TODO save the printed checkout items and the sum into a new table for statistics down the road
    // it should contain the receipt number so we can print the correct receipt number in the future
    // for now it will be saved into saved prefs as a int...
    private suspend fun deleteCurrentCheckoutItems(
        checkoutRepository: ICheckoutRepository,
        dispatcherProvider: DispatcherProvider
    ) = withContext(dispatcherProvider.provideIOContext()) {
        checkoutRepository.deleteAllArticles()
    }

}


