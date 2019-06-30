package com.example.domain.repository

import androidx.lifecycle.LiveData
import com.example.domain.model.Receipt
import com.example.domain.model.Result

interface IReceiptRepository {

    suspend fun getReceipts(
        startDate: String,
        endDate: String
    ): Result<Exception, LiveData<List<Receipt>>>

    suspend fun addReceipt(
        receipt: Receipt
    ): Result<Exception, Unit>
}