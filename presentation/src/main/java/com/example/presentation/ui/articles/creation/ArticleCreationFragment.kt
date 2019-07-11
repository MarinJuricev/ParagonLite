package com.example.presentation.ui.articles.creation

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
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

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        articleCreationViewModel.isArticleCreationSuccess.observe(this@ArticleCreationFragment, Observer {
            when (it) {
                true -> showArticleCreationSuccess()
                false -> showArticleCreationFail()
            }
        })
    }


    private fun showArticleCreationFail() {
        val snack = Snackbar.make(articleCreationRoot, "Doslo je do pogreske!", Snackbar.LENGTH_LONG)
        snack.show()
    }

    private fun showArticleCreationSuccess() {
        val snack = Snackbar.make(articleCreationRoot, "Artikl Uspjesno Kreiran", Snackbar.LENGTH_LONG)
        snack.show()
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
