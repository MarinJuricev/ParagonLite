package com.example.presentation.ui.checkout

import InstantExecutorExtension
import com.example.domain.model.Result
import com.example.domain.shared.BLUETOOTH_MAC_ADDRESS_DEFAULT_VALUE
import com.example.domain.shared.BLUETOOTH_MAC_ADDRESS_KEY
import com.example.domain.shared.ISharedPrefsService
import com.example.domain.usecase.checkout.*
import com.example.domain.usecase.print.GeneratePrintData
import com.example.domain.usecase.print.PrintCheckout
import com.example.domain.usecase.receipt.AddReceipt
import com.example.mockfactory.checkoutArticleTestData
import com.example.mockfactory.checkoutBadgeCountTestData
import com.example.mockfactory.checkoutValueTestData
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
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith

@ExperimentalCoroutinesApi
@ExtendWith(InstantExecutorExtension::class)
internal class CheckoutViewModelTest {

    private val getArticlesInCheckout: GetArticlesInCheckout = mockk()
    private val deleteCheckoutArticle: DeleteCheckoutArticle = mockk()
    private val calculateCheckout: CalculateCheckout = mockk()
    private val generatePrintData: GeneratePrintData = mockk()
    private val printCheckout: PrintCheckout = mockk()
    private val getArticlesInCheckoutSize: GetArticlesInCheckoutSize = mockk()
    private val updateCheckoutArticle: UpdateCheckoutArticle = mockk()
    private val addReceipt: AddReceipt = mockk()
    private val sharedPrefsService: ISharedPrefsService = mockk()

    private lateinit var checkoutViewModel: CheckoutViewModel

    private val mainThreadSurrogate = newSingleThreadContext("UI thread")

    @org.junit.jupiter.api.BeforeEach
    fun setUp() {
        Dispatchers.setMain(mainThreadSurrogate)

        setupInitAnswers()

        checkoutViewModel =
            CheckoutViewModel(
                getArticlesInCheckout,
                deleteCheckoutArticle,
                calculateCheckout,
                generatePrintData,
                printCheckout,
                getArticlesInCheckoutSize,
                updateCheckoutArticle,
                addReceipt,
                sharedPrefsService
            )
    }

    private fun setupInitAnswers() {
        val getArticlesUseCaseResult = flow {
            emit(listOf(checkoutArticleTestData))
        }

        val getArticlesInCheckoutSizeUseCaseResult = flow {
            emit(checkoutBadgeCountTestData)
        }

        coEvery {
            getArticlesInCheckout.execute()
        } coAnswers { Result.build { getArticlesUseCaseResult } }

        coEvery {
            calculateCheckout.execute(listOf(checkoutArticleTestData))
        } coAnswers { checkoutValueTestData }

        coEvery {
            getArticlesInCheckoutSize.execute()
        } coAnswers { getArticlesInCheckoutSizeUseCaseResult }
    }

    @org.junit.jupiter.api.AfterEach
    fun tearDown() {
        Dispatchers.resetMain() // reset main dispatcher to the original Main dispatcher
        mainThreadSurrogate.close()
    }

    @Test
    fun `should update _checkoutArticles with new data when getArticlesInCheckout is a success, should update _checkoutValue with new calculate checkout data`() =
        runBlockingTest {
            coVerify { getArticlesInCheckout.execute() }
            coVerify { calculateCheckout.execute(listOf(checkoutArticleTestData)) }
            coVerify { getArticlesInCheckoutSize.execute() }

            assert(checkoutViewModel.articleData.getOrAwaitValue() == listOf(checkoutArticleTestData))
            assert(checkoutViewModel.checkoutValue.getOrAwaitValue() == checkoutValueTestData)
            assert(checkoutViewModel.checkoutBadgeCount.getOrAwaitValue() == checkoutBadgeCountTestData)
        }

    @Test
    fun `should update _isArticleDeletionSuccess with true when deleteCheckoutArticle is a success`() =
        runBlockingTest {
            coEvery {
                deleteCheckoutArticle.execute(checkoutArticleTestData)
            } coAnswers { Result.build { Unit } }

            checkoutViewModel.deleteArticle(checkoutArticleTestData)

            coVerify { deleteCheckoutArticle.execute(checkoutArticleTestData) }

            assert(checkoutViewModel.isArticleDeletionSuccess.getOrAwaitValue() == true)
        }

    @Test
    fun `should update _isArticleDeletionSuccess with false when deleteCheckoutArticle is a failure`() =
        runBlockingTest {
            coEvery {
                deleteCheckoutArticle.execute(checkoutArticleTestData)
            } coAnswers { Result.build { throw Exception() } }

            checkoutViewModel.deleteArticle(checkoutArticleTestData)

            coVerify { deleteCheckoutArticle.execute(checkoutArticleTestData) }

            assert(checkoutViewModel.isArticleDeletionSuccess.getOrAwaitValue() == false)
        }

    @Test
    fun `should update _getBluetoothAddressError with true when sharedPrefsService returns BLUETOOTH_MAC_ADDRESS_DEFAULT_VALUE`() =
        runBlockingTest {
            coEvery {
                deleteCheckoutArticle.execute(checkoutArticleTestData)
            } coAnswers { Result.build { throw Exception() } }

            coEvery {
                sharedPrefsService.getValue(
                    BLUETOOTH_MAC_ADDRESS_KEY,
                    BLUETOOTH_MAC_ADDRESS_DEFAULT_VALUE
                )
            } coAnswers { BLUETOOTH_MAC_ADDRESS_DEFAULT_VALUE }

            checkoutViewModel.printCheckout()

            coVerify {
                sharedPrefsService.getValue(
                    BLUETOOTH_MAC_ADDRESS_KEY,
                    BLUETOOTH_MAC_ADDRESS_DEFAULT_VALUE
                )
            }

            assert(checkoutViewModel.getBluetoothAddressError.getOrAwaitValue() == true)
        }

    @Test
    fun `should update _articleUpdate with true when updateCheckoutArticle is a success`() =
        runBlockingTest {
            coEvery {
                updateCheckoutArticle.execute(checkoutArticleTestData)
            } coAnswers { Result.build { Unit } }

            checkoutViewModel.updateArticle(checkoutArticleTestData)

            coVerify {
                updateCheckoutArticle.execute(checkoutArticleTestData)
            }

            assert(checkoutViewModel.articleUpdate.getOrAwaitValue() == true)
        }

    @Test
    fun `should update _articleUpdate with false when updateCheckoutArticle is a failure`() =
        runBlockingTest {
            coEvery {
                updateCheckoutArticle.execute(checkoutArticleTestData)
            } coAnswers { Result.build { throw Exception() } }

            checkoutViewModel.updateArticle(checkoutArticleTestData)

            coVerify {
                updateCheckoutArticle.execute(checkoutArticleTestData)
            }

            assert(checkoutViewModel.articleUpdate.getOrAwaitValue() == false)
        }
}