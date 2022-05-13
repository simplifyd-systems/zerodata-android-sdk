package com.simplifyd.zerodata.android.ui.main.catalogue

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.simplifyd.zerodata.android.R
import com.simplifyd.zerodata.android.utils.AutoUpdateRecyclerView
import kotlinx.android.synthetic.main.item_catalogue.view.*
import kotlin.properties.Delegates

class ListedAppsAdapter(
    private val onListedAppClicked: (ListedApp) -> Unit
) : RecyclerView.Adapter<ListedAppsAdapter.ListedAppsViewHolder>(), AutoUpdateRecyclerView {

    var listedApps: List<ListedApp> by Delegates.observable(emptyList()) { _, _, _ ->
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ListedAppsViewHolder {
        return ListedAppsViewHolder(
            LayoutInflater.from(parent.context).inflate(R.layout.item_catalogue, parent, false)
        )
    }

    override fun getItemCount(): Int = listedApps.size

    override fun onBindViewHolder(holder: ListedAppsViewHolder, position: Int) {
        holder.bind(listedApps[position])
    }

    inner class ListedAppsViewHolder(view: View) : RecyclerView.ViewHolder(view) {

        fun bind(listedApp: ListedApp) {
            itemView.listedAppLogo.setImageResource(listedApp.imageResourceId)
            itemView.listedAppName.text = listedApp.title
            "Open ${listedApp.title}".also { itemView.btnSubmit.text = it }
            itemView.setOnClickListener { onListedAppClicked(listedApp) }

        }

    }
}