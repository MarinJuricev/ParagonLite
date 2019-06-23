package com.example.presentation.ui.articles.preview

import androidx.lifecycle.LiveData
import androidx.lifecycle.MediatorLiveData
import androidx.lifecycle.MutableLiveData
import com.example.domain.model.Article
import com.example.domain.model.Result
import com.example.domain.usecase.article.DeleteArticle
import com.example.domain.usecase.article.GetArticles
import com.example.domain.usecase.checkout.SendArticleToCheckout
import com.example.presentation.shared.BaseViewModel
import kotlinx.coroutines.launch

class ArticlesListViewModel(
    private val getArticles: GetArticles,
    private val deleteArticle: DeleteArticle,
    private val sendArticleToCheckout: SendArticleToCheckout
) : BaseViewModel() {

    init {
        fetchArticles()
    }

    private val _articleData by lazy { MediatorLiveData<List<Article>>() }
    val articleData: LiveData<List<Article>> get() = _articleData

    private val _isArticleDeletionSuccess by lazy { MutableLiveData<Boolean>() }
    val isArticleDeletionSuccess: LiveData<Boolean> get() = _isArticleDeletionSuccess

    private val _checkoutBadgeCount by lazy { MediatorLiveData<Int>() }
    val checkoutBadgeCount: LiveData<Int> get() = _checkoutBadgeCount

    private fun fetchArticles() = launch {
        when (val result = getArticles.execute()) {
            is Result.Value -> {
                _articleData.addSource(
                    result.value
                ) {
                    _articleData.value = it
                }
            }
            is Result.Error -> _articleData.postValue(null)
        }
    }

    fun deleteArticle(article: Article) = launch {
        when (deleteArticle.execute(article)) {
            is Result.Value -> _isArticleDeletionSuccess.postValue(true)
            is Result.Error -> _isArticleDeletionSuccess.postValue(false)
        }
    }

    fun sendArticleToCheckout(article: Article) = launch {
        when (val result = sendArticleToCheckout.execute(article)) {
            is Result.Value -> {
                _checkoutBadgeCount.addSource(
                    result.value
                ) {
                    _checkoutBadgeCount.value = it ?: 0
                }
            }
            is Result.Error -> _checkoutBadgeCount.postValue(0)
        }
    }
}
