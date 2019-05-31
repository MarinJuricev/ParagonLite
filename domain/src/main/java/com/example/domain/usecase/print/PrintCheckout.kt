package com.example.domain.usecase.print

import android.os.Looper
import com.example.domain.DispatcherProvider
import com.example.domain.error.ParagonError
import com.example.domain.model.CheckoutArticle
import com.example.domain.model.Result
import com.example.domain.repository.IBluetoothRepository
import com.example.domain.repository.ICheckoutRepository
import com.zebra.sdk.comm.BluetoothConnectionInsecure
import kotlinx.coroutines.withContext

class PrintCheckout {

    suspend fun execute(
        bluetoothRepository: IBluetoothRepository,
        checkoutRepository: ICheckoutRepository,
        dispatcherProvider: DispatcherProvider,
        valuesToPrint: List<CheckoutArticle>?
    ): Result<Exception, Unit> {

        val savedMacAddress = bluetoothRepository.getMacAddress()

//        if (validateMacAddress(savedMacAddress) || validatePrintData(valuesToPrint))
//            return Result.build { throw ParagonError.PrintException }

        val escPrintData = generatePrintData(
            valuesToPrint!!,
            dispatcherProvider
        )

        connectAndSendDataOverBluetooth(
            "66:12:5E:8B:7C:3D",
            escPrintData,
            dispatcherProvider
        )


        return Result.build { Unit }
    }

    private suspend fun generatePrintData(
        valuesToPrint: List<CheckoutArticle>,
        dispatcherProvider: DispatcherProvider
    ): List<ByteArray> =
        withContext(dispatcherProvider.provideComputationContext()) {

            val dataToReturn = mutableListOf<ByteArray>()

            dataToReturn.add(ESCPrinterCommand.POS_Set_Cut(1)!!)
            dataToReturn.add(ESCPrinterCommand.POS_Set_PrtInit())

            dataToReturn.add(
                ESCPrinterCommand.POS_Set_Font_And_Print(
                    "  " + "MATE KAD JE PEKA" + "\n\n\n\n\n",
                    0, 0, 0, 0
                )!!
            )

            dataToReturn.add(ESCPrinterCommand.POS_Print_Text(
                "MATE", "UTF-8", 0, 1, 1, 0)!!)

            return@withContext dataToReturn
        }

    private fun connectAndSendDataOverBluetooth(
        savedMacAddress: String,
        escPrintData: List<ByteArray>,
        dispatcherProvider: DispatcherProvider
    ) {
        Thread(Runnable {
            try {
                // Instantiate insecure connection for given Bluetooth&reg; MAC Address.
                val thePrinterConn = BluetoothConnectionInsecure(savedMacAddress)

                // Initialize
                Looper.prepare()

                // Open the connection - physical connection is established here.
                thePrinterConn.open()

                // Send the data to printer as a byte array.
                for (data in escPrintData)
                    thePrinterConn.write(data)

                // Make sure the data got to the printer before closing the connection
                Thread.sleep(500)

                // Close the insecure connection to release resources.
                thePrinterConn.close()

                Looper.myLooper()!!.quit()
            } catch (e: Exception) {
                // Handle communications error here.
                e.printStackTrace()
            }
        }).start()

    }

    /**
     * If the mac address is BluetoothException then it means we didn't pair with any bluetooth device yet!
     */
    private fun validateMacAddress(savedMacAddress: Result<Exception, String>) =
        savedMacAddress == Result.build { ParagonError.BluetoothException }

    private fun validatePrintData(valuesToPrint: List<CheckoutArticle>?): Boolean =
        valuesToPrint != null
}