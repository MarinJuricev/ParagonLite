package com.example.domain.usecase.print

import com.example.domain.DispatcherProvider
import com.example.domain.model.CheckoutArticle
import com.example.domain.model.Result
import com.example.domain.repository.IBluetoothRepository
import com.example.domain.repository.ICheckoutRepository
import kotlinx.coroutines.withContext

class PrintCheckout {

    suspend fun execute(
        bluetoothRepository: IBluetoothRepository,
        checkoutRepository: ICheckoutRepository,
        dispatcherProvider: DispatcherProvider,
        valuesToPrint: List<ByteArray>,
        savedMacAddress: String
    ): Result<Exception, Unit> {
        return when(val result = bluetoothRepository.connectAndSendDataOverBluetooth(
            savedMacAddress,
            valuesToPrint
        )){
            is Result.Value -> {
//                deleteCurrentCheckoutItems(checkoutRepository, dispatcherProvider)
                result
            }
            is Result.Error -> result
        }
    }

    private suspend fun deleteCurrentCheckoutItems(
        checkoutRepository: ICheckoutRepository,
        dispatcherProvider: DispatcherProvider
    ) = withContext(dispatcherProvider.provideIOContext()){

    }

}


