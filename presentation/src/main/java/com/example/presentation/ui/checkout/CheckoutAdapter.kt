package com.example.presentation.ui.checkout

import android.view.ViewGroup
import androidx.recyclerview.widget.ListAdapter
import com.example.domain.model.CheckoutArticle
import com.example.presentation.R
import com.example.presentation.shared.SimpleViewHolder
import com.example.presentation.shared.inflateIntoSelf
import kotlinx.android.synthetic.main.item_checkout.view.*

class CheckoutAdapter(
    private val onDeleteClick: (CheckoutArticle) -> (Unit)
) :
    ListAdapter<CheckoutArticle, SimpleViewHolder>(
        CheckoutDiffUtilCallback()
    ) {

    private enum class CheckoutViewType { CHECKOUT, NO_CONTENT }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SimpleViewHolder {
        return if (viewType == CheckoutViewType.CHECKOUT.ordinal)
            SimpleViewHolder(parent.inflateIntoSelf(R.layout.item_checkout))
        else
            SimpleViewHolder(parent.inflateIntoSelf(R.layout.no_checkout_layout))
    }

    override fun getItemViewType(position: Int): Int {
        return if (currentList.isEmpty()) CheckoutViewType.NO_CONTENT.ordinal
        else CheckoutViewType.CHECKOUT.ordinal
    }

    override fun getItemCount(): Int {
        return if (currentList.isEmpty()) {
            1
        } else
            currentList.size
    }

    override fun onBindViewHolder(holder: SimpleViewHolder, position: Int) {
        if (currentList.isNotEmpty()) {
            getItem(position).let { checkoutItem ->
                holder.itemView.apply {
                    etCheckoutQuantity.setText(checkoutItem.inCheckout.toString())
                    tvCheckoutArticleName.text = checkoutItem.name
                    tvCheckoutQuantityPrice.text = checkoutItem.quantity
                    tvCheckoutQuantityValue.text = checkoutItem.price.toString()
                    ivRemoveCheckoutItem.setOnClickListener { onDeleteClick(checkoutItem) }
                }
            }
        }
    }

}

