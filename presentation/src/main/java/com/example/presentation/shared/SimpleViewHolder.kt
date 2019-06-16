package com.example.presentation.shared

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.LayoutRes
import androidx.recyclerview.widget.RecyclerView

/**
 * Simple representation of [RecyclerView.ViewHolder] class.
 * */
open class SimpleViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView)

// Thing I usually use with SimpleViewHolder
fun ViewGroup.inflateIntoSelf(@LayoutRes layoutRes: Int, attachToRoot: Boolean = false): View {
    return LayoutInflater.from(context).inflate(layoutRes, this, attachToRoot)
}