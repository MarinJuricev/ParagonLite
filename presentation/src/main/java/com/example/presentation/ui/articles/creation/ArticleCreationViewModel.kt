package com.example.presentation.ui.articles.creation

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.domain.model.Article
import com.example.domain.model.Result
import com.example.domain.usecase.article.CreateArticle
import kotlinx.coroutines.launch

class ArticleCreationViewModel(
    private val createArticle: CreateArticle
): ViewModel() {

    private val _currentPrice by lazy { MutableLiveData<String>() }
    private val _articleName by lazy { MutableLiveData<String>() }

    private val _shouldSaveButtonBeEnabled by lazy { MutableLiveData<Boolean>() }
    val shouldSaveButtonBeEnabled: LiveData<Boolean> get() = _shouldSaveButtonBeEnabled

    private val _isArticleCreationSuccess by lazy { MutableLiveData<Boolean>() }
    val isArticleCreationSuccess: LiveData<Boolean> get() = _isArticleCreationSuccess

    fun onSaveClick(article: Article) = viewModelScope.launch {
        when (createArticle.execute(article)) {
            is Result.Value -> _isArticleCreationSuccess.postValue(true)
            is Result.Error -> _isArticleCreationSuccess.postValue(false)
        }
    }

    @SuppressWarnings
    fun onPriceChanged(text: CharSequence, start: Int, before: Int, count: Int) {
        _currentPrice.postValue(text.toString())
        validateArticleData()
    }

    @SuppressWarnings
    fun onArticleNameChanged(text: CharSequence, start: Int, before: Int, count: Int) {
        _articleName.postValue(text.toString())
        validateArticleData()
    }

    private fun validateArticleData() {
        _shouldSaveButtonBeEnabled.postValue(
            !_currentPrice.value.isNullOrEmpty() &&
                    !_articleName.value.isNullOrEmpty()
        )
    }
}
