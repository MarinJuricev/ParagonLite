package com.example.paragonlite.ui.checkout

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.domain.model.CheckoutArticle
import com.example.paragonlite.R

class CheckoutAdapter():
    androidx.recyclerview.widget.ListAdapter<CheckoutArticle, CheckoutAdapter.CheckoutViewHolder>(CheckoutDiffUtilCallback()){

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CheckoutViewHolder {
        val inflater = LayoutInflater.from(parent.context)

        return CheckoutViewHolder(
            inflater.inflate(R.layout.item_checkout, parent, false)
        )
    }

    override fun onBindViewHolder(holder: CheckoutViewHolder, position: Int) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }


    class CheckoutViewHolder(root: View) : RecyclerView.ViewHolder(root) {
//    var name: AppCompatTextView = root.tvArticleName
//    var price: AppCompatTextView = root.tvArticlePrice
//    var quantity: AppCompatTextView = root.tvArticleQuantity
//    var root: ConstraintLayout = root.itemArticleRoot
    }
}

