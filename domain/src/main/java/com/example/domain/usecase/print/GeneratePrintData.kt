package com.example.domain.usecase.print

import com.example.domain.DispatcherProvider
import com.example.domain.model.CheckoutArticle
import com.example.domain.model.Result
import kotlinx.coroutines.withContext
import java.text.SimpleDateFormat
import java.util.*


private const val SHENZEN_LINE = "================================\n"
private const val SHENZEN_LINE_LENGHT_WIDTH_0 = 30 //48
private const val SHENZEN_LINE_LENGHT_WIDTH_1 = 30 //24
private const val SHENZEN_CENTER = 17 // 24


class GeneratePrintData {

    suspend fun execute(
        valuesToPrint: List<CheckoutArticle>,
        checkoutSum: String,
        receiptNumber: Int,
        dispatcherProvider: DispatcherProvider
    ): Result<Exception, List<ByteArray>> =
        withContext(dispatcherProvider.provideComputationContext()) {

            //            if(validatePrintData(valuesToPrint))
//                return@withContext Result.build { ParagonError.PrintException }

            val dataToReturn = mutableListOf<ByteArray>()

            dataToReturn.add(ESCPrinterCommand.POS_Set_Cut(1)!!)
            dataToReturn.add(ESCPrinterCommand.POS_Set_PrtInit())

            dataToReturn.add(
                ESCPrinterCommand.POS_Set_Font_And_Print(
                    alignCenter("OPG Oaza Jelinjak", SHENZEN_CENTER) + "\n",
                    0, 0, 0, 0
                )!!
            )

            dataToReturn.add(
                ESCPrinterCommand.POS_Set_Font_And_Print(
                    alignCenter("Vlake /bb", SHENZEN_CENTER) + "\n",
                    0, 0, 0, 0
                )!!
            )

            dataToReturn.add(
                ESCPrinterCommand.POS_Set_Font_And_Print(
                    alignCenter("22202, Primosten Burnji", SHENZEN_CENTER) + "\n",
                    0, 0, 0, 0
                )!!
            )

            dataToReturn.add(
                ESCPrinterCommand.POS_Set_Font_And_Print(
                    alignCenter("Sjediste:", SHENZEN_CENTER) + "\n",
                    0, 0, 0, 0
                )!!
            )

            dataToReturn.add(
                ESCPrinterCommand.POS_Set_Font_And_Print(
                    alignCenter("Primosten, Vlake /bb", SHENZEN_CENTER) + "\n",
                    0, 0, 0, 0
                )!!
            )

            dataToReturn.add(
                ESCPrinterCommand.POS_Set_Font_And_Print(
                    alignCenter("OIB 93020714603", SHENZEN_CENTER) + "\n",
                    0, 0, 0, 0
                )!!
            )

            dataToReturn.add(
                ESCPrinterCommand.POS_Set_Font_And_Print(
                    alignCenter(
                        "vl. Zdravko Cobanov",
                        SHENZEN_CENTER
                    ) + "\n\n",
                    0, 0, 0, 0
                )!!
            )

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
                    "Zastitni kod: ${generateRandomCharacters(36)}\n",
                    0, 0, 0, 0
                )!!
            )

            dataToReturn.add(
                ESCPrinterCommand.POS_Set_Font_And_Print(
                    "JIR: ${generateRandomCharacters(8)}-${generateRandomCharacters(4)}-" +
                            "${generateRandomCharacters(4)}-${generateRandomCharacters(4)}-" +
                            "${generateRandomCharacters(12)}\n\n",
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
        val date = Date()
        val formatter = SimpleDateFormat("dd.mm.yy", Locale.ENGLISH)

        return formatter.format(date)
    }

    private fun generateCurrentTime(): String {
        val sdf = SimpleDateFormat("hh:mm:ss", Locale.ENGLISH)
        return sdf.format(Date())
    }

    private fun generateRandomCharacters(length: Int): String {
        val allowedChars = "abcdefghiklmnopqrstuvwxyz1234567890"
        return (1..length)
            .map { allowedChars.random() }
            .joinToString("")
    }

    private fun validatePrintData(valuesToPrint: List<CheckoutArticle>?) =
        valuesToPrint?.isEmpty()
}

/**
 * Used in any other printer to help align text, in this case center the text
 *
 * @param stringToBeCentered the string that should be centered within a line
 * @param lineCenter         the center of the line, let's imagine that the max char length is 48, the
 * line center argument in this case should be 24
 * @return the output should be a text that's centered, EX: "            Random Text"
 */
private fun alignCenter(stringToBeCentered: String, lineCenter: Int): String {
    val stringToReturn = StringBuilder()

    if (stringToBeCentered.length / 2 <= lineCenter) {
        val startOfString = lineCenter - stringToBeCentered.length / 2

        for (i in 0 until startOfString)
            stringToReturn.append(" ")

        return stringToReturn.append(stringToBeCentered).toString()
    }

    return stringToBeCentered
}

/**
 * Used in any other printer to help align text, in this case align the text to the right
 *
 * @param leftString    The string that should stay in the right EX: Stake:
 * @param rightString   The string that should be aligned right EX: 10,00
 * @param maxLineLength The maximum length of the printer line.
 * *
 * @return the second argument rightString, should be aligned to the right
 *
 *
 * EX Input("Stake", "10,00", 48)
 * EX Output Stake                       10,00
 *
 *
 * NOTE: This isn't the correct number of white space, i've just randomly added whitespace for the EX
 */
private fun alignRight(leftString: String, rightString: String, maxLineLength: Int): String {
    val stringToReturn = StringBuilder()

    stringToReturn.append(leftString)

    val paddingDifference = maxLineLength - rightString.length - leftString.length

    for (i in 0 until paddingDifference) {
        stringToReturn.append(" ")
    }

    return stringToReturn.append(rightString).toString()
}
