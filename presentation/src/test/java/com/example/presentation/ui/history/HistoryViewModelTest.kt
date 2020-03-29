package com.example.presentation.ui.history

import InstantExecutorExtension
import com.example.domain.model.Receipt
import com.example.domain.model.Result
import com.example.domain.shared.BLUETOOTH_MAC_ADDRESS_DEFAULT_VALUE
import com.example.domain.shared.BLUETOOTH_MAC_ADDRESS_KEY
import com.example.domain.shared.ISharedPrefsService
import com.example.domain.usecase.print.GenerateReceiptPrintData
import com.example.domain.usecase.print.PrintHistory
import com.example.domain.usecase.receipt.GetReceipts
import com.example.mockfactory.macAddressTestData
import com.example.mockfactory.receiptTestData
import com.example.mockfactory.valuesToPrintTestData
import com.example.presentation.ui.getOrAwaitValue
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.newSingleThreadContext
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runBlockingTest
import kotlinx.coroutines.test.setMain
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith

@ExperimentalCoroutinesApi
@ExtendWith(InstantExecutorExtension::class)
internal class HistoryViewModelTest {

    private val getReceipts: GetReceipts = mockk()
    private val generateReceiptPrintData: GenerateReceiptPrintData = mockk()
    private val printHistory: PrintHistory = mockk()
    private val sharedPrefsService: ISharedPrefsService = mockk()

    private lateinit var historyViewModel: HistoryViewModel

    private val mainThreadSurrogate = newSingleThreadContext("UI thread")

    @org.junit.jupiter.api.BeforeEach
    fun setUp() {
        Dispatchers.setMain(mainThreadSurrogate)
        historyViewModel = HistoryViewModel(
            getReceipts,
            generateReceiptPrintData,
            printHistory,
            sharedPrefsService
        )
    }

    @AfterEach
    fun tearDown() {
        Dispatchers.resetMain() // reset main dispatcher to the original Main dispatcher
        mainThreadSurrogate.close()
    }

    private fun setupSharedPrefsSuccess() {
        coEvery {
            sharedPrefsService.getValue(
                BLUETOOTH_MAC_ADDRESS_KEY,
                BLUETOOTH_MAC_ADDRESS_DEFAULT_VALUE
            )
        } coAnswers { macAddressTestData }
    }

    @Test
    fun `should update _receiptData with new receipt data when getReceipts is a success`() =
        runBlockingTest {
            val startDate = 1583017200000
            val endDate = 1584313200000

            val useCaseResult = flow {
                emit(listOf(receiptTestData))
            }

            coEvery {
                getReceipts.execute(startDate, endDate)
            } coAnswers { Result.build { useCaseResult } }

            historyViewModel.fetchReceiptsFromTheSelectedDateRange(startDate, endDate)

            coVerify { getReceipts.execute(startDate, endDate) }
            assert(historyViewModel.receiptData.getOrAwaitValue() == listOf(receiptTestData))
        }

    @Test
    fun `should update _receiptData with empty receipt list when getReceipts is a failure`() =
        runBlockingTest {

            val startDate = 1583017200000
            val endDate = 1584313200000

            coEvery {
                getReceipts.execute(startDate, endDate)
            } coAnswers { Result.build { throw Exception() } }

            historyViewModel.fetchReceiptsFromTheSelectedDateRange(startDate, endDate)

            coVerify { getReceipts.execute(startDate, endDate) }
            assert(historyViewModel.receiptData.getOrAwaitValue() == listOf<Receipt>())
        }

    @Test
    fun `should update _getBluetoothAddressError with true when shared prefs return BLUETOOTH_MAC_ADDRESS_DEFAULT_VALUE`() =
        runBlockingTest {

            coEvery {
                sharedPrefsService.getValue(
                    BLUETOOTH_MAC_ADDRESS_KEY,
                    BLUETOOTH_MAC_ADDRESS_DEFAULT_VALUE
                )
            } coAnswers { BLUETOOTH_MAC_ADDRESS_DEFAULT_VALUE }

            historyViewModel.prepareDataForPrint(listOf(receiptTestData))

            coVerify {
                sharedPrefsService.getValue(
                    BLUETOOTH_MAC_ADDRESS_KEY,
                    BLUETOOTH_MAC_ADDRESS_DEFAULT_VALUE
                )
            }

            assert(historyViewModel.getBluetoothAddressError.getOrAwaitValue())
        }

    @Test
    fun `should update _getBluetoothAddressError with true when generateReceiptPrintData fails`() =
        runBlockingTest {
            setupSharedPrefsSuccess()
            val receiptTestData = listOf(receiptTestData)

            coEvery {
                generateReceiptPrintData.execute(
                    receiptTestData
                )
            } coAnswers { Result.build { throw Exception() } }

            historyViewModel.prepareDataForPrint(receiptTestData)

            coVerify {
                sharedPrefsService.getValue(
                    BLUETOOTH_MAC_ADDRESS_KEY,
                    BLUETOOTH_MAC_ADDRESS_DEFAULT_VALUE
                )
            }

            coVerify { generateReceiptPrintData.execute(receiptTestData) }

            assert(historyViewModel.getBluetoothAddressError.value == true)
        }

    @Test
    fun `should update trigger printHistory use-case when generateReceiptPrintData is a success`() =
        runBlockingTest {
            setupSharedPrefsSuccess()
            val receiptTestData = listOf(receiptTestData)

            coEvery {
                generateReceiptPrintData.execute(
                    receiptTestData
                )
            } coAnswers { Result.build { valuesToPrintTestData } }

            historyViewModel.prepareDataForPrint(receiptTestData)

            coVerify {
                sharedPrefsService.getValue(
                    BLUETOOTH_MAC_ADDRESS_KEY,
                    BLUETOOTH_MAC_ADDRESS_DEFAULT_VALUE
                )
            }

            coVerify { generateReceiptPrintData.execute(receiptTestData) }
            coVerify { printHistory.execute(macAddressTestData, valuesToPrintTestData) }
        }


}