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

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SimpleViewHolder {
        return SimpleViewHolder(parent.inflateIntoSelf(R.layout.item_receipt))
    }

    override fun getItemCount(): Int = currentList.size


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