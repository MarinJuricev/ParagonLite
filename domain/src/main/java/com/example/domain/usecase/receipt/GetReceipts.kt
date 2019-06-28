package com.example.domain.usecase.receipt

import androidx.lifecycle.LiveData
import com.example.domain.model.Receipt
import com.example.domain.model.Result
import com.example.domain.repository.IReceiptRepository

class GetReceipts(
    private val receiptRepository: IReceiptRepository
) {

    suspend fun execute()
            : Result<Exception, LiveData<List<Receipt>>> = receiptRepository.getReceipts()
}