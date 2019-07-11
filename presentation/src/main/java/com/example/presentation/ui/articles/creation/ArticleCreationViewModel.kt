package com.example.presentation.ui.articles.creation

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.domain.model.Article
import com.example.domain.model.Result
import com.example.domain.usecase.article.CreateArticle
import com.example.presentation.shared.BaseViewModel
import kotlinx.coroutines.launch

class ArticleCreationViewModel(
    private val createArticle: CreateArticle
) : BaseViewModel() {

    private val _isArticleCreationSuccess by lazy { MutableLiveData<Boolean>() }
    val isArticleCreationSuccess: LiveData<Boolean> get() = _isArticleCreationSuccess

    private val _shouldSaveButtonBeEnabled by lazy { MutableLiveData<Boolean>() }
    val shouldSaveButtonBeEnabled: LiveData<Boolean> get() = _shouldSaveButtonBeEnabled

    fun onSaveClick(article: Article) = launch {

        when (createArticle.execute(article)) {
            is Result.Value -> _isArticleCreationSuccess.postValue(true)
            is Result.Error -> _isArticleCreationSuccess.postValue(false)
        }
    }

    @SuppressWarnings
    fun onPriceChanged(text: CharSequence, start: Int, before: Int, count: Int) {
        Log.d("PRICE", text.toString())
    }

}
