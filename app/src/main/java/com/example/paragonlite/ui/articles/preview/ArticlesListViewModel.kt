package com.example.paragonlite.ui.articles.preview

import androidx.lifecycle.LiveData
import androidx.lifecycle.MediatorLiveData
import androidx.lifecycle.MutableLiveData
import com.example.domain.model.Article
import com.example.domain.model.Result
import com.example.domain.repository.IArticleRepository
import com.example.domain.repository.ICheckoutRepository
import com.example.domain.usecase.article.DeleteArticle
import com.example.domain.usecase.article.GetArticles
import com.example.domain.usecase.checkout.SendArticleToCheckout
import com.example.paragonlite.shared.BaseViewModel
import kotlinx.coroutines.launch

class ArticlesListViewModel(
    private val articleRepository: IArticleRepository,
    private val checkoutRepository: ICheckoutRepository,
    private val getArticles: GetArticles,
    private val deleteArticle: DeleteArticle,
    private val sendArticleToCheckout: SendArticleToCheckout
) : BaseViewModel() {

    init {
        fetchArticles()
    }

    private val _articleData = MediatorLiveData<List<Article>>()
    val articleData: LiveData<List<Article>> get() = _articleData

    private val _isArticleDeletionSuccess = MutableLiveData<Boolean>()
    val isArticleDeletionSuccess: LiveData<Boolean> get() = _isArticleDeletionSuccess

    private fun fetchArticles() = launch {
        when (val result = getArticles.execute(articleRepository)) {
            is Result.Value -> {
                _articleData.addSource(
                    result.value
                ) { newArticleList ->
                    _articleData.value = newArticleList
                }
            }
            is Result.Error -> _articleData.postValue(null)
        }
    }

    fun deleteArticle(article: Article) = launch {
        when (deleteArticle.execute(articleRepository, article)) {
            is Result.Value -> _isArticleDeletionSuccess.postValue(true)
            is Result.Error -> _isArticleDeletionSuccess.postValue(false)
        }
    }

    fun sendArticleToCheckout(article: Article) = launch {
        when(sendArticleToCheckout.execute(checkoutRepository, article)){
            //TODO IMPLEMENT ?
//            is Result.Value ->
//            is Result.Error ->
        }
    }
}
