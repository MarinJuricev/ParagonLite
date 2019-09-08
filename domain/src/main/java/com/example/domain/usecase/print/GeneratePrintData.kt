package com.example.domain.usecase.print

import com.example.domain.model.CheckoutArticle
import com.example.domain.model.Result
import com.example.domain.shared.DispatcherProvider
import com.example.domain.usecase.print.ESCPrinterCommand.SHENZEN_CENTER
import com.example.domain.usecase.print.ESCPrinterCommand.SHENZEN_LINE
import com.example.domain.usecase.print.ESCPrinterCommand.SHENZEN_LINE_LENGHT_WIDTH_0
import com.example.domain.usecase.print.ESCPrinterCommand.alignCenter
import com.example.domain.usecase.print.ESCPrinterCommand.alignRight
import kotlinx.coroutines.withContext
import java.text.SimpleDateFormat
import java.util.*

class GeneratePrintData(
    private val dispatcherProvider: DispatcherProvider
) {
    suspend fun execute(
        valuesToPrint: List<CheckoutArticle>,
        checkoutSum: String,
        receiptNumber: Int
    ): Result<Exception, List<ByteArray>> =
        withContext(dispatcherProvider.provideComputationContext()) {

            //TODO actually validate
            //            if(validatePrintData(valuesToPrint))
//                return@withContext Result.build { ParagonError.PrintException }

            val dataToReturn = mutableListOf<ByteArray>()

            dataToReturn.add(ESCPrinterCommand.POS_Set_Cut(1)!!)
            dataToReturn.add(ESCPrinterCommand.POS_Set_PrtInit())

            dataToReturn.add(
                ESCPrinterCommand.POS_Set_Font_And_Print(
                    alignRight(
                        "Prodavac:",
                        "Filip",
                        SHENZEN_LINE_LENGHT_WIDTH_0
                    ) + "\n",
                    0, 0, 0, 0
                )!!
            )

            dataToReturn.add(
                ESCPrinterCommand.POS_Set_Font_And_Print(
                    alignRight(
                        generateCurrentDate(),
                        generateCurrentTime(),
                        SHENZEN_LINE_LENGHT_WIDTH_0
                    ) + "\n",
                    0, 0, 0, 0
                )!!
            )

            dataToReturn.add(
                ESCPrinterCommand.POS_Set_Font_And_Print(
                    "Broj 5/P1/1:\n",
                    0, 0, 0, 0
                )!!
            )

            dataToReturn.add(
                ESCPrinterCommand.POS_Set_Font_And_Print(
                    "Broj racuna: $receiptNumber\n",
                    0, 0, 0, 0
                )!!
            )

            dataToReturn.add(
                ESCPrinterCommand.POS_Set_Font_And_Print(
                    "Oznaka poslovnog prostora: P1\n",
                    0, 0, 0, 0
                )!!
            )

            dataToReturn.add(
                ESCPrinterCommand.POS_Set_Font_And_Print(
                    "Oznaka blagajne: 1\n\n",
                    0, 0, 0, 0
                )!!
            )

            // Article section

            dataToReturn.add(
                ESCPrinterCommand.POS_Set_Font_And_Print(
                    SHENZEN_LINE,
                    0, 0, 0, 0
                )!!
            )

            dataToReturn.add(
                ESCPrinterCommand.POS_Set_Font_And_Print(
                    alignRight(
                        "Artikli",
                        "Jed. Mjera",
                        SHENZEN_LINE_LENGHT_WIDTH_0
                    ) + "\n",
                    0, 0, 0, 0
                )!!
            )

            dataToReturn.add(
                ESCPrinterCommand.POS_Set_Font_And_Print(
                    alignRight(
                        "Kolicina",
                        "Jed. cijena Cijena(Kn)",
                        SHENZEN_LINE_LENGHT_WIDTH_0
                    ) + "\n",
                    0, 0, 0, 0
                )!!
            )

            for (value in valuesToPrint) {

                dataToReturn.add(
                    ESCPrinterCommand.POS_Set_Font_And_Print(
                        alignRight(
                            value.name,
                            value.quantity,
                            SHENZEN_LINE_LENGHT_WIDTH_0
                        ) + "\n",
                        0, 0, 0, 0
                    )!!
                )

                dataToReturn.add(
                    ESCPrinterCommand.POS_Set_Font_And_Print(
                        alignRight(
                            value.inCheckout.toString(),
                            "${value.price}       ${value.price * value.inCheckout}",
                            SHENZEN_LINE_LENGHT_WIDTH_0
                        ) + "\n",
                        0, 0, 0, 0
                    )!!
                )
            }

            dataToReturn.add(
                ESCPrinterCommand.POS_Set_Font_And_Print(
                    SHENZEN_LINE,
                    0, 0, 0, 0
                )!!
            )

            dataToReturn.add(
                ESCPrinterCommand.POS_Set_Font_And_Print(
                    alignRight(
                        "Ukupno",
                        "$checkoutSum kn",
                        SHENZEN_LINE_LENGHT_WIDTH_0
                    ) + "\n",
                    1, 0, 0, 0
                )!!
            )

            dataToReturn.add(
                ESCPrinterCommand.POS_Set_Font_And_Print(
                    "Nacin placanja: \n",
                    0, 0, 0, 0
                )!!
            )

            dataToReturn.add(
                ESCPrinterCommand.POS_Set_Font_And_Print(
                    alignRight(
                        "",
                        "NOVCANICA",
                        SHENZEN_LINE_LENGHT_WIDTH_0
                    ) + "\n",
                    0, 0, 0, 0
                )!!
            )

            dataToReturn.add(
                ESCPrinterCommand.POS_Set_Font_And_Print(
                    alignCenter("HVALA", SHENZEN_CENTER) + "\n",
                    0, 0, 0, 0
                )!!
            )

            dataToReturn.add(
                ESCPrinterCommand.POS_Set_Font_And_Print(
                    alignCenter("Dodite nam opet", SHENZEN_CENTER) + "\n\n\n",
                    0, 0, 0, 0
                )!!
            )

            return@withContext Result.build { dataToReturn }
        }

    private fun generateCurrentDate(): String {
        val formatter = SimpleDateFormat("dd.MM.yy", Locale.ENGLISH)
        return formatter.format(Date())
    }

    private fun generateCurrentTime(): String {
        val sdf = SimpleDateFormat("hh:mm:ss", Locale.ENGLISH)
        return sdf.format(Date())
    }


    private fun validatePrintData(valuesToPrint: List<CheckoutArticle>?) =
        valuesToPrint?.isEmpty()
}