package com.example.presentation.ui.articles.preview

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.widget.AppCompatTextView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.domain.model.Article
import com.example.presentation.R
import com.example.presentation.shared.SimpleViewHolder
import com.example.presentation.shared.inflateIntoSelf
import com.example.presentation.ui.checkout.CheckoutAdapter
import kotlinx.android.synthetic.main.item_article.view.*
import kotlinx.android.synthetic.main.item_checkout.view.*

class ArticleAdapter(
    private val onArticleClick: (Article) -> (Unit),
    private val onArticleLongClick: (Article) -> (Unit)
) : ListAdapter<Article, SimpleViewHolder>(ArticleDiffUtilCallback()) {

    private enum class ArticleViewType { CHECKOUT, NO_CONTENT }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SimpleViewHolder {
        return if (viewType == ArticleViewType.CHECKOUT.ordinal)
            SimpleViewHolder(parent.inflateIntoSelf(R.layout.item_article))
        else
            SimpleViewHolder(parent.inflateIntoSelf(R.layout.no_articles_layout))
    }

    override fun getItemViewType(position: Int): Int {
        return if (currentList.isEmpty()) ArticleViewType.NO_CONTENT.ordinal
        else ArticleViewType.CHECKOUT.ordinal
    }

    override fun getItemCount(): Int {
        return if (currentList.isEmpty()) {
            1
        } else
            currentList.size
    }

    override fun onBindViewHolder(holder: SimpleViewHolder, position: Int) {
        if (currentList.isNotEmpty()) {
            getItem(position).let { article ->
                holder.itemView.apply {
                    tvArticleName.text = article.name
                    tvArticlePrice.text = article.price.toString()
                    tvArticleQuantity.text = article.quantity
                    itemArticleRoot.setOnClickListener { onArticleClick(article) }
                    itemArticleRoot.setOnLongClickListener {
                        onArticleLongClick(article)
                        true}
                }
            }
        }
    }
}
