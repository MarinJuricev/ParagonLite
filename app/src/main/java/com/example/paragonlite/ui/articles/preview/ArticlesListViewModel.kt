package com.example.paragonlite.ui.articles.preview

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.domain.model.Article
import com.example.domain.model.Result
import com.example.domain.repository.IArticleRepository
import com.example.domain.usecase.GetArticles
import com.example.paragonlite.shared.BaseViewModel
import kotlinx.coroutines.launch

class ArticlesListViewModel(
    private val articleRepository: IArticleRepository,
    private val getArticles: GetArticles
) : BaseViewModel() {

    private val _articleData = MutableLiveData<List<Article>>()
    val articleData: LiveData<List<Article>> get() = _articleData

    init {
        fetchArticles()
    }

    private fun fetchArticles() = launch {
        val result = getArticles.getArticles(articleRepository)

        when (result) {
            is Result.Value -> _articleData.postValue(result.value)
            is Result.Error -> _articleData.postValue(null)
        }
    }
}
