package com.example.presentation.ui.history

import android.view.ViewGroup
import androidx.recyclerview.widget.ListAdapter
import com.example.domain.model.Receipt
import com.example.presentation.R
import com.example.presentation.shared.SimpleViewHolder
import com.example.presentation.shared.inflateIntoSelf
import kotlinx.android.synthetic.main.history_fragment.view.tvReceiptDate
import kotlinx.android.synthetic.main.history_fragment.view.tvReceiptNumber
import kotlinx.android.synthetic.main.item_receipt.view.*

class ReceiptAdapter : ListAdapter<Receipt, SimpleViewHolder>(ReceiptDiffUtilCallback()) {

    private enum class ReceiptViewType { RECEIPT, NO_CONTENT }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SimpleViewHolder {
        return if (viewType == ReceiptViewType.RECEIPT.ordinal)
            SimpleViewHolder(parent.inflateIntoSelf(R.layout.item_receipt))
        else
            SimpleViewHolder(parent.inflateIntoSelf(R.layout.no_receipt_layout))
    }

    override fun getItemViewType(position: Int): Int {
        return if (currentList.isEmpty()) ReceiptViewType.NO_CONTENT.ordinal
        else ReceiptViewType.RECEIPT.ordinal
    }

    override fun getItemCount(): Int {
        return if (currentList.isEmpty()) {
            1
        } else
            currentList.size
    }

    override fun onBindViewHolder(holder: SimpleViewHolder, position: Int) {
        if (currentList.isNotEmpty()) {
            getItem(position).let { receipt ->
                holder.itemView.apply {
                    tvReceiptNumber.text = receipt.number.toString()
                    tvReceiptDate.text = receipt.date
                    tvReceiptAmount.text = receipt.price.toString()

                }
            }
        }
    }

}