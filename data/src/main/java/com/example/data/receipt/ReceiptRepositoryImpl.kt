package com.example.data.receipt

import androidx.lifecycle.LiveData
import androidx.lifecycle.Transformations
import com.example.data.model.RoomReceipt
import com.example.data.toReceiptList
import com.example.data.toRoomReceipt
import com.example.domain.DispatcherProvider
import com.example.domain.error.ParagonError
import com.example.domain.model.Receipt
import com.example.domain.model.Result
import com.example.domain.repository.IReceiptRepository
import kotlinx.coroutines.withContext

class ReceiptRepositoryImpl(
    private val dispatcherProvider: DispatcherProvider,
    private val receiptDao: ReceiptDao
) : IReceiptRepository {

    override suspend fun getReceipts(
        startDate: String,
        endDate: String
    ): Result<Exception, LiveData<List<Receipt>>> =
        withContext(dispatcherProvider.provideIOContext()) {

            when (val result = receiptDao.getReceipts(startDate, endDate)) {
                listOf<RoomReceipt>() -> Result.build { throw ParagonError.LocalIOException }
                else -> Result.build {
                    Transformations.map(
                        result,
                        ::mapToReceipt
                    )
                }
            }
        }

    override suspend fun addReceipt(
        receipt: Receipt
    ): Result<Exception, Unit> =
        withContext(dispatcherProvider.provideIOContext()) {

            when (receiptDao.upsert(receipt.toRoomReceipt)) {
                0L -> Result.build { throw ParagonError.LocalIOException }
                else -> Result.build { Unit }
            }
        }
}

private fun mapToReceipt(list: List<RoomReceipt>) = list.toReceiptList()