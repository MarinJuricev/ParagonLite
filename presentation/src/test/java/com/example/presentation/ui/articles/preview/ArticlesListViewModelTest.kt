package com.example.presentation.ui.articles.preview

import InstantExecutorExtension
import com.example.domain.model.Result
import com.example.domain.shared.CHECKOUT_BADGE_INITIAL_VALUE
import com.example.domain.usecase.article.DeleteArticle
import com.example.domain.usecase.article.GetArticles
import com.example.domain.usecase.checkout.SendArticleToCheckout
import com.example.mockfactory.articleTestData
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
internal class ArticlesListViewModelTest {

    private val getArticle: GetArticles = mockk()
    private val deleteArticle: DeleteArticle = mockk()
    private val sendArticleToCheckout: SendArticleToCheckout = mockk()

    private lateinit var articleListViewModel: ArticlesListViewModel

    private val mainThreadSurrogate = newSingleThreadContext("UI thread")

    @org.junit.jupiter.api.BeforeEach
    fun setUp() {
        Dispatchers.setMain(mainThreadSurrogate)

        setupViewModelInitAnswer()
        articleListViewModel =
            ArticlesListViewModel(getArticle, deleteArticle, sendArticleToCheckout)
    }

    private fun setupViewModelInitAnswer() {
        val useCaseResult = flow {
            emit(listOf(articleTestData))
        }

        coEvery {
            getArticle.execute()
        } coAnswers { Result.build { useCaseResult } }
    }

    @org.junit.jupiter.api.AfterEach
    fun tearDown() {
        Dispatchers.resetMain() // reset main dispatcher to the original Main dispatcher
        mainThreadSurrogate.close()
    }

    @Test
    fun `should update _articleData with new article data when getArticles is a success`() =
        runBlockingTest {
            coVerify { getArticle.execute() }
            assert(articleListViewModel.articleData.getOrAwaitValue() == listOf(articleTestData))
        }

    @Test
    fun `should update _isArticleDeletionSuccess with true when deleteArticle is a success`() =
        runBlockingTest {

            coEvery {
                deleteArticle.execute(articleTestData)
            } coAnswers { Result.build { Unit } }

            articleListViewModel.deleteArticle(articleTestData)

            coVerify { deleteArticle.execute(articleTestData) }
            assert(articleListViewModel.isArticleDeletionSuccess.value == true)
        }

    @Test
    fun `should update _isArticleDeletionSuccess with false when deleteArticle is a failure`() =
        runBlockingTest {

            coEvery {
                deleteArticle.execute(articleTestData)
            } coAnswers { Result.build { throw Exception() } }

            articleListViewModel.deleteArticle(articleTestData)

            coVerify { deleteArticle.execute(articleTestData) }
            assert(articleListViewModel.isArticleDeletionSuccess.value == false)
        }

    @Test
    fun `should update _checkoutBadgeCount with new badge count when sendArticleToCheckout is a success`() =
        runBlockingTest {

            val newBadgeCount = 4

            val useCaseResult = flow {
                emit(newBadgeCount)
            }

            coEvery {
                sendArticleToCheckout.execute(articleTestData)
            } coAnswers { Result.build { useCaseResult } }

            articleListViewModel.sendArticleToCheckout(articleTestData)

            coVerify { sendArticleToCheckout.execute(articleTestData) }
            assert(articleListViewModel.checkoutBadgeCount.value == newBadgeCount)
        }

    @Test
    fun `should update _checkoutBadgeCount with CHECKOUT_BADGE_INITIAL_VALUE when sendArticleToCheckout is a failure`() =
        runBlockingTest {
            coEvery {
                sendArticleToCheckout.execute(articleTestData)
            } coAnswers { Result.build { throw Exception() } }

            articleListViewModel.sendArticleToCheckout(articleTestData)

            coVerify { sendArticleToCheckout.execute(articleTestData) }
            assert(articleListViewModel.checkoutBadgeCount.value == CHECKOUT_BADGE_INITIAL_VALUE)
        }
}