package com.example.presentation.ui.articles.preview

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.domain.model.Article
import com.example.domain.model.Result
import com.example.domain.shared.CHECKOUT_BADGE_INITIAL_VALUE
import com.example.domain.usecase.article.DeleteArticle
import com.example.domain.usecase.article.GetArticles
import com.example.domain.usecase.checkout.SendArticleToCheckout
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch

class ArticlesListViewModel(
    private val getArticles: GetArticles,
    private val deleteArticle: DeleteArticle,
    private val sendArticleToCheckout: SendArticleToCheckout
) : ViewModel() {

    init {
        fetchArticles()
    }

    private val _articleData by lazy { MutableLiveData<List<Article>>() }
    val articleData: LiveData<List<Article>> get() = _articleData

    private val _isArticleDeletionSuccess by lazy { MutableLiveData<Boolean>() }
    val isArticleDeletionSuccess: LiveData<Boolean> get() = _isArticleDeletionSuccess

    private val _checkoutBadgeCount by lazy { MutableLiveData<Int>() }
    val checkoutBadgeCount: LiveData<Int> get() = _checkoutBadgeCount

    private fun fetchArticles() = viewModelScope.launch {
        when (val result = getArticles.execute()) {
            is Result.Value -> result.value.collect { data -> _articleData.postValue(data) }
            is Result.Error -> _articleData.postValue(null)
        }
    }

    fun deleteArticle(article: Article) = viewModelScope.launch {
        when (deleteArticle.execute(article)) {
            is Result.Value -> _isArticleDeletionSuccess.postValue(true)
            is Result.Error -> _isArticleDeletionSuccess.postValue(false)
        }
    }

    fun sendArticleToCheckout(article: Article) = viewModelScope.launch {
        when (val result = sendArticleToCheckout.execute(article)) {
            is Result.Value -> result.value.collect { data -> _checkoutBadgeCount.postValue(data) }
            is Result.Error -> _checkoutBadgeCount.postValue(CHECKOUT_BADGE_INITIAL_VALUE)
        }
    }
}
