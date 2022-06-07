package com.simplifyd.zerodata.android.ui.main.catalogue

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.simplifyd.zerodata.android.R
import com.simplifyd.zerodata.android.utils.AutoUpdateRecyclerView
import kotlinx.android.synthetic.main.item_catalouge_filter.view.*
import kotlin.properties.Delegates

class CatalogueFilterAdapter(
    private val onFilterClicked: (String) -> Unit
) : RecyclerView.Adapter<CatalogueFilterAdapter.CatalogueFilterViewHolder>(), AutoUpdateRecyclerView{

    private var selectedItemPosition = -1

    var filters: List<String> by Delegates.observable(emptyList()) { _, _, _ ->
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CatalogueFilterViewHolder {
        return CatalogueFilterViewHolder(
            LayoutInflater.from(parent.context).inflate(R.layout.item_catalouge_filter, parent, false)
        )
    }

    override fun getItemCount(): Int = filters.size

    override fun onBindViewHolder(holder: CatalogueFilterViewHolder, position: Int) {
        holder.bind(filters[position], position)
    }

    inner class CatalogueFilterViewHolder(view: View) : RecyclerView.ViewHolder(view) {

        fun bind(filterTag: String, position: Int) {

            itemView.btnSubmit.isSelected = position==selectedItemPosition

//            itemView.context.getString(filterTag).also { itemView.btnSubmit.text = it }
            itemView.btnSubmit.text = filterTag

            itemView.btnSubmit.setOnClickListener {
                selectedItemPosition = position
                onFilterClicked(filterTag)
                notifyDataSetChanged()
            }


        }

    }


}