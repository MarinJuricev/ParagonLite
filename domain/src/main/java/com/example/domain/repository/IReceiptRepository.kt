package com.example.domain.repository

import com.example.domain.model.Receipt
import com.example.domain.model.Result
import kotlinx.coroutines.flow.Flow

interface IReceiptRepository {

    suspend fun getReceipts(
        startDate: String,
        endDate: String
    ): Result<Exception, Flow<List<Receipt>>>

    suspend fun addReceipt(
        receipt: Receipt
    ): Result<Exception, Unit>
}