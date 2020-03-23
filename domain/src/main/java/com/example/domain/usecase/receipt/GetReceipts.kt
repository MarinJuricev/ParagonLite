package com.example.domain.usecase.receipt

import androidx.lifecycle.LiveData
import com.example.domain.model.Receipt
import com.example.domain.model.Result
import com.example.domain.repository.IReceiptRepository
import java.text.SimpleDateFormat
import java.util.*

class GetReceipts(
    private val receiptRepository: IReceiptRepository
) {

    suspend fun execute(startDate: String, endDate: String)
            : Result<Exception, LiveData<List<Receipt>>> {

        val startDateInCorrectDateFormat = formatDate(startDate)
        val endDateInCorrectDateFormat = formatDate(endDate)

        return receiptRepository.getReceipts(startDateInCorrectDateFormat, endDateInCorrectDateFormat)
    }

    private fun formatDate(dateToBeConverted: String): String {
        val originalFormat = SimpleDateFormat("E MMM dd HH:mm:ss 'GMT+02:00' yyyy", Locale.ENGLISH)
        val targetFormat = SimpleDateFormat("dd.MM.yy", Locale.ENGLISH)
        val date = originalFormat.parse(dateToBeConverted)

        return targetFormat.format(date)
    }
}