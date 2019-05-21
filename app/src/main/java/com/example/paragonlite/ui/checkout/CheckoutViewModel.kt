package com.example.paragonlite.ui.checkout

import androidx.lifecycle.LiveData
import androidx.lifecycle.MediatorLiveData
import com.example.domain.model.CheckoutArticle
import com.example.domain.model.Result
import com.example.domain.repository.ICheckoutRepository
import com.example.domain.usecase.checkout.GetArticlesInCheckout
import com.example.paragonlite.shared.BaseViewModel
import kotlinx.coroutines.launch


class CheckoutViewModel(
    private val checkoutRepository: ICheckoutRepository,
    private val getArticlesInCheckout: GetArticlesInCheckout
) : BaseViewModel() {

    init {
        fetchCheckoutArticles()
    }

    private val _checkoutArticles = MediatorLiveData<List<CheckoutArticle>>()
    val articleData: LiveData<List<CheckoutArticle>> get() = _checkoutArticles

    private fun fetchCheckoutArticles() = launch {
        when (val result = getArticlesInCheckout.getArticlesInCheckout(checkoutRepository)) {
            is Result.Value -> {
                _checkoutArticles.addSource(
                    result.value
                ) { newArticleList ->
                    _checkoutArticles.value = newArticleList
                }
            }
            is Result.Error -> _checkoutArticles.postValue(null)
        }
    }
}
