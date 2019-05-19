package com.example.paragonlite.ui.articles.preview

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.widget.AppCompatTextView
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.domain.model.Article
import com.example.paragonlite.R
import kotlinx.android.synthetic.main.item_article.view.*

class ArticleAdapter : ListAdapter<Article, ArticleAdapter.ArticleViewHolder>(ArticleDiffUtilCallback()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ArticleViewHolder {
        val inflater = LayoutInflater.from(parent.context)

        return ArticleViewHolder(
            inflater.inflate(R.layout.item_article, parent, false)
        )
    }

    override fun onBindViewHolder(holder: ArticleViewHolder, position: Int) {
        getItem(position).let { article ->
            with(holder) {
                name.text = article.name
                price.text = article.price.toString()
                quantity.text = article.quantity
            }
        }
    }

    class ArticleViewHolder(private val root: View) : RecyclerView.ViewHolder(root) {
        var name: AppCompatTextView = root.tvArticleName
        var price: AppCompatTextView = root.tvArticlePrice
        var quantity: AppCompatTextView = root.tvArticleQuantity
    }

}
