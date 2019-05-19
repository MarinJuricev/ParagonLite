package com.example.paragonlite.ui.articles.preview

import androidx.recyclerview.widget.DiffUtil
import com.example.domain.model.Article

class ArticleDiffUtilCallback : DiffUtil.ItemCallback<Article>() {
    override fun areItemsTheSame(oldItem: Article, newItem: Article): Boolean {
        return oldItem.name == newItem.name
    }

    override fun areContentsTheSame(oldItem: Article, newItem: Article): Boolean {
        return oldItem.name == newItem.name
    }

}