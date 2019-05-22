package com.example.paragonlite.ui.checkout

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.widget.AppCompatImageView
import androidx.appcompat.widget.AppCompatTextView
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.domain.model.CheckoutArticle
import com.example.paragonlite.R
import com.google.android.material.textfield.TextInputEditText
import com.google.android.material.textfield.TextInputLayout
import kotlinx.android.synthetic.main.item_checkout.view.*

class CheckoutAdapter(
    private val onDeleteClick: (CheckoutArticle) -> (Unit)
) :
    ListAdapter<CheckoutArticle, CheckoutAdapter.CheckoutViewHolder>(
        CheckoutDiffUtilCallback()
    ) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CheckoutViewHolder {
        val inflater = LayoutInflater.from(parent.context)

        return if (viewType == CheckoutViewType.CHECKOUT.ordinal) CheckoutViewHolder(
            inflater.inflate(R.layout.item_checkout, parent, false)
        ) else CheckoutViewHolder(
            inflater.inflate(R.layout.no_checkout_layout, parent, false)
        )
    }

    override fun getItemViewType(position: Int): Int {
        return if (itemCount == 0) CheckoutViewType.NO_CONTENT.ordinal
        else CheckoutViewType.CHECKOUT.ordinal
    }



    override fun onBindViewHolder(holder: CheckoutViewHolder, position: Int) {
        getItem(position).let { checkoutItem ->
            with(holder) {
                name.text = checkoutItem.name
                //.text is bugged out refer to
                //https://stackoverflow.com/questions/37374075/how-does-kotlin-property-access-syntax-work-for-java-classes-i-e-edittext-sett/37374301#37374301
                tieQuantity.setText(checkoutItem.inCheckout.toString())
                quantityPrice.text = checkoutItem.price.toString()
                quantityValue.text = checkoutItem.quantity

                deleteItem.setOnClickListener { onDeleteClick(checkoutItem) }
            }
        }
    }

    class CheckoutViewHolder(root: View) : RecyclerView.ViewHolder(root) {
        var name: AppCompatTextView = root.tvCheckoutArticleName
        var tilQuantity: TextInputLayout = root.tiCheckoutQuantity
        var tieQuantity: TextInputEditText = root.etCheckoutQuantity
        var quantityPrice: AppCompatTextView = root.tvCheckoutQuantityPrice
        var quantityValue: AppCompatTextView = root.tvCheckoutQuantityValue
        var deleteItem: AppCompatImageView = root.ivRemoveCheckoutItem
    }
}

