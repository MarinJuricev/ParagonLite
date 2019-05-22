package com.example.paragonlite.ui.checkout

import androidx.lifecycle.LiveData
import androidx.lifecycle.MediatorLiveData
import androidx.lifecycle.MutableLiveData
import com.example.domain.DispatcherProvider
import com.example.domain.model.CheckoutArticle
import com.example.domain.model.Result
import com.example.domain.repository.ICheckoutRepository
import com.example.domain.usecase.checkout.CalculateCheckout
import com.example.domain.usecase.checkout.DeleteCheckoutArticle
import com.example.domain.usecase.checkout.GetArticlesInCheckout
import com.example.paragonlite.shared.BaseViewModel
import kotlinx.coroutines.launch


class CheckoutViewModel(
    private val checkoutRepository: ICheckoutRepository,
    private val getArticlesInCheckout: GetArticlesInCheckout,
    private val deleteCheckoutArticle: DeleteCheckoutArticle,
    private val calculateCheckout: CalculateCheckout,
    private val dispatcherProvider: DispatcherProvider
) : BaseViewModel() {

    init {
        fetchCheckoutArticles()
    }

    private val _checkoutArticles = MediatorLiveData<List<CheckoutArticle>>()
    val articleData: LiveData<List<CheckoutArticle>> get() = _checkoutArticles

    private val _isArticleDeletionSuccess = MutableLiveData<Boolean>()
    val isArticleDeletionSuccess: LiveData<Boolean> get() = _isArticleDeletionSuccess

    private val _checkoutValue = MutableLiveData<String>()
    val checkoutValue: LiveData<String> get() = _checkoutValue

    private fun fetchCheckoutArticles() = launch {
        when (val result = getArticlesInCheckout.getArticlesInCheckout(checkoutRepository)) {
            is Result.Value -> {
                _checkoutArticles.addSource(
                    result.value
                ) { newArticleList ->
                    _checkoutArticles.value = newArticleList
                    updateCheckoutAmount(newArticleList)
                }
            }
            is Result.Error -> _checkoutArticles.value = listOf()
        }
    }

    private fun updateCheckoutAmount(newArticleList: List<CheckoutArticle>?) = launch {
        val list = newArticleList ?: listOf()

        _checkoutValue.postValue(calculateCheckout.calculateCheckoutValue(dispatcherProvider, list))
    }

    fun deleteArticle(checkoutArticle: CheckoutArticle) = launch {
        //TODO Implement after crunch is over...
        when (deleteCheckoutArticle.deleteArticle(checkoutRepository, checkoutArticle)) {
            is Result.Value -> _isArticleDeletionSuccess.postValue(true)
            is Result.Error -> _isArticleDeletionSuccess.postValue(false)
        }
    }
}
