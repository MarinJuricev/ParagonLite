package com.example.paragonlite.ui.articles.preview

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.navigation.findNavController
import com.example.domain.model.Article
import com.example.paragonlite.R
import com.example.paragonlite.databinding.ArticlesListFragmentBinding
import com.google.android.material.snackbar.Snackbar
import kotlinx.android.synthetic.main.articles_list_fragment.*
import org.koin.androidx.viewmodel.ext.android.viewModel

class ArticlesListFragment : Fragment() {

    private val articlesListViewModel: ArticlesListViewModel by viewModel()
    private lateinit var binding: ArticlesListFragmentBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = DataBindingUtil.inflate(
            inflater, R.layout.articles_list_fragment, container, false
        )

        return binding.root
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        bindUI()
    }

    private fun bindUI() {
        fabArticleCreation.setOnClickListener { view ->
            view.findNavController().navigate(R.id.action_navigation_articles_to_articleCreation)
        }

        val articleAdapter = ArticleAdapter(
            ({ article: Article -> onArticleClick(article) }),
            ({ article: Article -> onArticleLongClick(article) })
        )
        articlesListViewModel.articleData.observe(this@ArticlesListFragment, Observer {
            articleAdapter.submitList(it)
        })

        articlesListViewModel.isArticleDeletionSuccess.observe(this@ArticlesListFragment, Observer {
            when (it) {
                true -> showArticleDeletionSuccess()
                false -> showArticleDeletionFail()
            }
        })

        rvArticleList.adapter = articleAdapter
    }

    private fun showArticleDeletionSuccess() =
        Snackbar.make(articleListRoot, "Artikl uspjesno pobrisan!", Snackbar.LENGTH_LONG).show()

    private fun showArticleDeletionFail() =
        Snackbar.make(articleListRoot, "Doslo je do pogreske!", Snackbar.LENGTH_LONG).show()

    private fun onArticleClick(article: Article) = articlesListViewModel.sendArticleToCheckout(article)

    private fun onArticleLongClick(article: Article) = articlesListViewModel.deleteArticle(article)

}
