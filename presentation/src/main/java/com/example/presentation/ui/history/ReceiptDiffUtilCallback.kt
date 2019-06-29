package com.example.presentation.ui.history

import androidx.recyclerview.widget.DiffUtil
import com.example.domain.model.Receipt

class ReceiptDiffUtilCallback : DiffUtil.ItemCallback<Receipt>() {
    override fun areItemsTheSame(oldItem: Receipt, newItem: Receipt): Boolean {
        return oldItem.number == newItem.number
    }

    override fun areContentsTheSame(oldItem: Receipt, newItem: Receipt): Boolean {
        return oldItem == newItem
    }

}