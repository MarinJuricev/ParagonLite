package com.example.presentation.ui.articles.preview

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.navigation.findNavController
import androidx.recyclerview.widget.RecyclerView
import com.example.domain.model.Article
import com.example.presentation.R
import com.example.presentation.databinding.ArticlesListFragmentBinding
import com.google.android.material.snackbar.Snackbar
import kotlinx.android.synthetic.main.activity_main.*
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

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

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
        articlesListViewModel.articleData.observe(viewLifecycleOwner, Observer {
            if (it.isEmpty())
                showEmptyScreenFields()
            else
                hideEmptyScreensFields()

            articleAdapter.submitList(it)
        })

        articlesListViewModel.isArticleDeletionSuccess.observe(viewLifecycleOwner, Observer {
            when (it) {
                true -> showArticleDeletionSuccess()
                false -> showArticleDeletionFail()
            }
        })

        articlesListViewModel.checkoutBadgeCount.observe(viewLifecycleOwner, Observer {
            when (it) {
                0 -> hideCheckoutBadge()
                else -> updateCheckoutBadgeCount(it)
            }
        })

        listenToRecyclerScroll()

        rvArticleList.adapter = articleAdapter
    }

    private fun showEmptyScreenFields() {
        noArticleGroup.visibility = View.VISIBLE
    }

    private fun hideEmptyScreensFields() {
        noArticleGroup.visibility = View.GONE
    }

    private fun listenToRecyclerScroll() {
        rvArticleList.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                if (dy > 0)
                    fabArticleCreation.hide()
                else if (dy < 0)
                    fabArticleCreation.show()
            }
        })
    }

    private fun hideCheckoutBadge() = activity?.bottom_nav?.removeBadge(R.id.navigation_checkout)

    private fun updateCheckoutBadgeCount(badgeCount: Int) {
        activity?.bottom_nav?.getOrCreateBadge(R.id.navigation_checkout)?.number = badgeCount
    }

    private fun showArticleDeletionSuccess() =
        Snackbar.make(
            articleListRoot,
            getString(R.string.article_successfully_deleted),
            Snackbar.LENGTH_LONG
        ).show()

    private fun showArticleDeletionFail() =
        Snackbar.make(articleListRoot, getString(R.string.error_occurred), Snackbar.LENGTH_LONG)
            .show()

    private fun onArticleClick(article: Article) {
        activity?.bottom_nav?.getOrCreateBadge(R.id.navigation_checkout)
        articlesListViewModel.sendArticleToCheckout(article)
    }

    private fun onArticleLongClick(article: Article) = articlesListViewModel.deleteArticle(article)

}
