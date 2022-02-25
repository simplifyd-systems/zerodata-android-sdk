package com.simplifyd.zerodata.android.ui

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.DrawableRes
import androidx.annotation.StringRes
import androidx.recyclerview.widget.RecyclerView
import com.simplifyd.zerodata.android.R
import kotlinx.android.synthetic.main.layout_empty_list.view.*

class EmptyStateViewHolder(view: View, val context: Context) : RecyclerView.ViewHolder(view) {

    fun bind(@DrawableRes emptyStateImage: Int, @StringRes emptyStateDesc: Int) {
        itemView.ivEmptyStateImage.setImageResource(emptyStateImage)
        itemView.tvEmptyStateDesc.text = context.getString(emptyStateDesc)
    }

    companion object {
        fun create(parent: ViewGroup): EmptyStateViewHolder {
            val view = LayoutInflater.from(parent.context)
                .inflate(R.layout.layout_empty_list, parent, false)
            return EmptyStateViewHolder(view, parent.context)
        }
    }
}