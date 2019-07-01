package com.example.domain.usecase.print

import com.example.domain.DispatcherProvider
import com.example.domain.model.Receipt
import com.example.domain.model.Result
import com.example.domain.usecase.print.ESCPrinterCommand.SHENZEN_LINE
import com.example.domain.usecase.print.ESCPrinterCommand.SHENZEN_LINE_LENGHT_WIDTH_0
import com.example.domain.usecase.print.ESCPrinterCommand.alignRight
import kotlinx.coroutines.withContext

class GenerateReceiptPrintData(
    private val dispatcherProvider: DispatcherProvider
) {
    suspend fun execute(
        valuesToPrint: List<Receipt>
    ): Result<Exception, List<ByteArray>> =
        withContext(dispatcherProvider.provideComputationContext()) {

            val dataToReturn = mutableListOf<ByteArray>()
            var receiptSum = 0.00

            dataToReturn.add(ESCPrinterCommand.POS_Set_Cut(1)!!)
            dataToReturn.add(ESCPrinterCommand.POS_Set_PrtInit())

            dataToReturn.add(
                ESCPrinterCommand.POS_Set_Font_And_Print(
                    SHENZEN_LINE,
                    0, 0, 0, 0
                )!!
            )

            dataToReturn.add(
                ESCPrinterCommand.POS_Set_Font_And_Print(
                    alignRight(
                        "Broj",
                        "Datum       Iznos",
                        SHENZEN_LINE_LENGHT_WIDTH_0
                    ) + "\n",
                    0, 0, 0, 0
                )!!
            )

            valuesToPrint.forEach {
                dataToReturn.add(
                    ESCPrinterCommand.POS_Set_Font_And_Print(
                        alignRight(
                            it.number.toString(),
                            "${it.date}       ${it.price}",
                            SHENZEN_LINE_LENGHT_WIDTH_0
                        ) + "\n",
                        0, 0, 0, 0
                    )!!
                )

                receiptSum += it.price
            }

            dataToReturn.add(
                ESCPrinterCommand.POS_Set_Font_And_Print(
                    SHENZEN_LINE + "\n",
                    0, 0, 0, 0
                )!!
            )

            dataToReturn.add(
                ESCPrinterCommand.POS_Set_Font_And_Print(
                    alignRight(
                        "UKUPNO:",
                        receiptSum.toString(),
                        SHENZEN_LINE_LENGHT_WIDTH_0
                    ) + "\n\n\n",
                    0, 0, 0, 0
                )!!
            )


            return@withContext Result.build { dataToReturn }
        }
}