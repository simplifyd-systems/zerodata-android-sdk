package com.simplifydvpn.android.ui.main.rules.categories

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.simplifydvpn.android.R
import com.simplifydvpn.android.data.model.Rule
import com.simplifydvpn.android.utils.AutoUpdateRecyclerView
import kotlinx.android.synthetic.main.item_category.view.*
import kotlin.properties.Delegates

class CategoriesAdapter(
    private val onCategoryClicked: (Rule) -> Unit
) : RecyclerView.Adapter<CategoriesAdapter.CategoryViewHolder>(), AutoUpdateRecyclerView {

    var categories: List<Rule> by Delegates.observable(emptyList()) { _, _, _ ->
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CategoryViewHolder {
        return CategoryViewHolder(
            LayoutInflater.from(parent.context).inflate(R.layout.item_category, parent, false)
        )
    }

    override fun getItemCount(): Int = categories.size

    override fun onBindViewHolder(holder: CategoryViewHolder, position: Int) {
        holder.bind(categories[position])
    }

    inner class CategoryViewHolder(view: View) : RecyclerView.ViewHolder(view) {

        fun bind(category: Rule) {
            itemView.tvCategoryName.text = category.category_name

            itemView.switchBlocked.isChecked = category.isBlocked
            itemView.setOnClickListener { onCategoryClicked(category) }

            itemView.tvBlockedStatus.text = if (category.isBlocked) "Blocked" else "Not Blocked"
        }

    }
}