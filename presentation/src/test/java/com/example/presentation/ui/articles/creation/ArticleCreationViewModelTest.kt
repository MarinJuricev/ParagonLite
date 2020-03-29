package com.example.presentation.ui.articles.creation

import InstantExecutorExtension
import com.example.domain.model.Result
import com.example.domain.usecase.article.CreateArticle
import com.example.mockfactory.articleTestData
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.newSingleThreadContext
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runBlockingTest
import kotlinx.coroutines.test.setMain
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith

@ExperimentalCoroutinesApi
@ExtendWith(InstantExecutorExtension::class)
internal class ArticleCreationViewModelTest {

    private val createArticle: CreateArticle = mockk()

    private lateinit var articleCreationViewModel: ArticleCreationViewModel

    private val mainThreadSurrogate = newSingleThreadContext("UI thread")

    @org.junit.jupiter.api.BeforeEach
    fun setUp() {
        Dispatchers.setMain(mainThreadSurrogate)
        articleCreationViewModel = ArticleCreationViewModel(createArticle)
    }

    @AfterEach
    fun tearDown() {
        Dispatchers.resetMain() // reset main dispatcher to the original Main dispatcher
        mainThreadSurrogate.close()
    }

    @Test
    fun `should update _isArticleCreationSuccess with true when create article is a success`() =
        runBlockingTest {
            coEvery {
                createArticle.execute(articleTestData)
            } coAnswers { Result.build { Unit } }

            articleCreationViewModel.onSaveClick(articleTestData)

            coVerify { createArticle.execute(articleTestData) }

            assert(articleCreationViewModel.isArticleCreationSuccess.value == true)
        }

    @Test
    fun `should update _isArticleCreationSuccess with false when create article is fails`() =
        runBlockingTest {
            coEvery {
                createArticle.execute(articleTestData)
            } coAnswers { Result.build { throw Exception() } }

            articleCreationViewModel.onSaveClick(articleTestData)

            coVerify { createArticle.execute(articleTestData) }
            assert(articleCreationViewModel.isArticleCreationSuccess.value == false)
        }

    @Test
    fun `should update _shouldSaveButtonBeEnabled with false when onPriceChanged gets triggered`() {
        articleCreationViewModel.onPriceChanged("ValidText", 0, 0, 0)

        assert(articleCreationViewModel.shouldSaveButtonBeEnabled.value == false)
    }

    @Test
    fun `should update _shouldSaveButtonBeEnabled with false when onArticleNameChanged gets triggered`() {
        articleCreationViewModel.onArticleNameChanged("ValidText", 0, 0, 0)

        assert(articleCreationViewModel.shouldSaveButtonBeEnabled.value == false)
    }

    @Test
    fun `should update _shouldSaveButtonBeEnabled with true when _articleName and _currentPrice are set`() {
        articleCreationViewModel.onArticleNameChanged("ValidText", 0, 0, 0)
        articleCreationViewModel.onPriceChanged("ValidText", 0, 0, 0)

        assert(articleCreationViewModel.shouldSaveButtonBeEnabled.value == true)
    }
}