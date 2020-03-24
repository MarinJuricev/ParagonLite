package com.example.presentation.ui.articles.creation

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import com.example.domain.model.Article
import com.example.presentation.R
import com.example.presentation.databinding.ArticleCreationFragmentBinding
import com.google.android.material.snackbar.Snackbar
import kotlinx.android.synthetic.main.article_creation_fragment.*
import org.koin.androidx.viewmodel.ext.android.viewModel

class ArticleCreationFragment : Fragment() {

    private val articleCreationViewModel: ArticleCreationViewModel by viewModel()
    private lateinit var binding: ArticleCreationFragmentBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = DataBindingUtil.inflate(
            inflater, R.layout.article_creation_fragment, container, false
        )
        binding.articleCreationFragment = this
        binding.articleCreationViewModel = articleCreationViewModel

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        articleCreationViewModel.isArticleCreationSuccess.observe(viewLifecycleOwner, Observer {
            when (it) {
                true -> showArticleCreationSuccess()
                false -> showArticleCreationFail()
            }
        })

        articleCreationViewModel.shouldSaveButtonBeEnabled.observe(viewLifecycleOwner, Observer {
            when (it) {
                true -> enableSaveButton()
                false -> disableSaveButton()
            }
        })
    }

    private fun enableSaveButton() {
        btnSave.isClickable = true
        btnSave.isEnabled = true
    }

    private fun disableSaveButton() {
        btnSave.isClickable = false
        btnSave.isEnabled = false
    }

    private fun showArticleCreationFail() {
        val snack =
            Snackbar.make(articleCreationRoot, getString(R.string.error_occurred), Snackbar.LENGTH_LONG)
        snack.show()
    }

    private fun showArticleCreationSuccess() {
        val snack =
            Snackbar.make(articleCreationRoot, getString(R.string.article_successfully_created), Snackbar.LENGTH_LONG)
        snack.show()
    }

    fun onCancelClick() {
        etArticlePrice.editableText.clear()
        etArticleName.editableText.clear()
    }

    fun onSaveClick() {
        val article = Article(
            etArticleName.text.toString(),
            spinnerQuantity.selectedItem.toString(),
            etArticlePrice.text.toString().toDouble()
        )

        articleCreationViewModel.onSaveClick(article)
    }

}
