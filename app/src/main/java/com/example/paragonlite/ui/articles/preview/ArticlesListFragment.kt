package com.example.paragonlite.ui.articles.preview

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProviders
import androidx.navigation.findNavController
import com.example.paragonlite.R
import kotlinx.android.synthetic.main.articles_list_fragment.*

class ArticlesListFragment : Fragment() {

    private lateinit var viewModel: ArticlesListViewModel
    private lateinit var articleAdapter: ArticleAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.articles_list_fragment, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel = ViewModelProviders.of(this).get(ArticlesListViewModel::class.java)

        bindUI()
    }

    private fun bindUI() {
        fabArticleCreation.setOnClickListener { view ->
            view.findNavController().navigate(R.id.action_navigation_articles_to_articleCreation)
        }
    }

}
