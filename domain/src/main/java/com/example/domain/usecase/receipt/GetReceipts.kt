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

    suspend fun execute(startDate: Long, endDate: Long)
            : Result<Exception, LiveData<List<Receipt>>> {
        val startDateInCorrectDateFormat = formatDate(startDate)
        val endDateInCorrectDateFormat = formatDate(endDate)

        return receiptRepository.getReceipts(startDateInCorrectDateFormat, endDateInCorrectDateFormat)
    }

    private fun formatDate(dateToBeConverted: Long): String {
        val targetFormat = SimpleDateFormat("dd.MM.yy", Locale.ENGLISH)

        val calendar = Calendar.getInstance()
        calendar.timeInMillis = dateToBeConverted
        return targetFormat.format(calendar.time)
    }
}