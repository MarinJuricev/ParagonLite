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
        binding.btnSave.isClickable = true
        binding.btnSave.isEnabled = true
    }

    private fun disableSaveButton() {
        binding.btnSave.isClickable = false
        binding.btnSave.isEnabled = false
    }

    private fun showArticleCreationFail() =
        Snackbar.make(
            binding.articleCreationRoot, getString(R.string.error_occurred), Snackbar.LENGTH_LONG
        ).show()

    private fun showArticleCreationSuccess() =
        Snackbar.make(
            binding.articleCreationRoot,
            getString(R.string.article_successfully_created),
            Snackbar.LENGTH_LONG
        ).show()

    fun onCancelClick() {
        binding.etArticlePrice.editableText.clear()
        binding.etArticleName.editableText.clear()
    }

    fun onSaveClick() {
        val article = Article(
            binding.etArticleName.text.toString(),
            binding.spinnerQuantity.selectedItem.toString(),
            binding.etArticlePrice.text.toString().toDouble()
        )

        articleCreationViewModel.onSaveClick(article)
    }

}
