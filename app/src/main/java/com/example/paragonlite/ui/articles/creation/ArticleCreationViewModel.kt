package com.example.paragonlite.ui.articles.creation

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.domain.DispatcherProvider
import com.example.domain.model.Article
import com.example.domain.model.Result
import com.example.domain.repository.IArticleRepository
import com.example.domain.usecase.CreateArticle
import com.example.paragonlite.shared.BaseViewModel
import kotlinx.coroutines.launch

class ArticleCreationViewModel(
    private val articleRepository: IArticleRepository,
    private val createArticle: CreateArticle
) : BaseViewModel() {

    private val _isArticleCreationSuccess = MutableLiveData<Boolean>()
    val isArticleCreationSuccess: LiveData<Boolean> get() = _isArticleCreationSuccess

    fun onSaveClick(article: Article) = launch {

        when (createArticle.createArticle(articleRepository, article)) {
            is Result.Value -> _isArticleCreationSuccess.postValue(true)
            is Result.Error -> _isArticleCreationSuccess.postValue(false)
        }
    }
}
