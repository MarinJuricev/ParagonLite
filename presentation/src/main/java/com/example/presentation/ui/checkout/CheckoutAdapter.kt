package com.example.presentation.ui.checkout

import android.view.ViewGroup
import androidx.recyclerview.widget.ListAdapter
import com.example.domain.model.CheckoutArticle
import com.example.presentation.R
import com.example.presentation.ext.afterTextChanged
import com.example.presentation.shared.SimpleViewHolder
import com.example.presentation.shared.inflateIntoSelf
import kotlinx.android.synthetic.main.item_checkout.view.*

class CheckoutAdapter(
    private val onDeleteClick: (CheckoutArticle) -> (Unit),
    private val updateQuantityCount: (CheckoutArticle) -> (Unit)
) :
    ListAdapter<CheckoutArticle, SimpleViewHolder>(
        CheckoutDiffUtilCallback()
    ) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SimpleViewHolder {
        return SimpleViewHolder(parent.inflateIntoSelf(R.layout.item_checkout))
    }

    override fun getItemCount() = currentList.size

    override fun onBindViewHolder(holder: SimpleViewHolder, position: Int) {
        if (currentList.isNotEmpty()) {
            getItem(position).let { checkoutItem ->
                holder.itemView.apply {
                    etCheckoutQuantity.setText(checkoutItem.inCheckout.toString())

                    etCheckoutQuantity.afterTextChanged {
                        val newCheckoutValue = it.toDoubleOrNull() ?: 1.00

                        val newItem = CheckoutArticle(
                            checkoutItem.name,
                            checkoutItem.quantity,
                            checkoutItem.price,
                            newCheckoutValue
                        )
                        updateQuantityCount(newItem)
                    }

                    tvCheckoutArticleName.text = checkoutItem.name
                    tvCheckoutQuantityPrice.text = checkoutItem.quantity
                    tvCheckoutQuantityValue.text = checkoutItem.price.toString()
                    ivRemoveCheckoutItem.setOnClickListener { onDeleteClick(checkoutItem) }
                }
            }
        }
    }
}

