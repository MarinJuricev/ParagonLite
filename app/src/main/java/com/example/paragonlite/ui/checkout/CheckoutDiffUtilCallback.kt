package com.example.paragonlite.ui.checkout

import androidx.recyclerview.widget.DiffUtil
import com.example.domain.model.CheckoutArticle

class CheckoutDiffUtilCallback : DiffUtil.ItemCallback<CheckoutArticle>() {

    override fun areItemsTheSame(oldItem: CheckoutArticle, newItem: CheckoutArticle): Boolean {
        return oldItem.name == newItem.name
    }

    override fun areContentsTheSame(oldItem: CheckoutArticle, newItem: CheckoutArticle): Boolean {
        return oldItem == newItem
    }

}