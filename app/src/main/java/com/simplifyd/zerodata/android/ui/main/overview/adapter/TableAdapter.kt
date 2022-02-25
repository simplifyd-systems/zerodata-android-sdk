package com.simplifyd.zerodata.android.ui.main.overview.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.DrawableRes
import androidx.annotation.StringRes
import androidx.recyclerview.widget.RecyclerView
import com.simplifyd.zerodata.android.R
import com.simplifyd.zerodata.android.ui.EmptyStateViewHolder
import kotlinx.android.synthetic.main.item_table.view.*


class TableAdapter(
    @DrawableRes private val emptyStateImage: Int,
    @StringRes private val emptyStateDesc: Int
) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    var domains = listOf<TableItem>()
        set(value) {
            field = value
            notifyDataSetChanged()
        }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return when (viewType) {
            0 -> EmptyStateViewHolder.create(parent)
            else -> TableViewHolder(
                LayoutInflater.from(parent.context).inflate(R.layout.item_table, parent, false)
            )
        }
    }

    override fun getItemCount(): Int {
        return if (domains.isEmpty()) {
            1
        } else {
            domains.size
        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        when (holder) {
            is EmptyStateViewHolder -> holder.bind(emptyStateImage, emptyStateDesc)
            is TableViewHolder -> holder.bind(domains[position])
        }
    }

    override fun getItemViewType(position: Int): Int {
        return if (domains.isEmpty()) {
            0
        } else {
            1
        }
    }

    inner class TableViewHolder(view: View) : RecyclerView.ViewHolder(view) {

        fun bind(domain: TableItem) {
            itemView.table_item.text = domain.title
            itemView.table_item_second_clm.text = domain.secondColumn
        }
    }
}