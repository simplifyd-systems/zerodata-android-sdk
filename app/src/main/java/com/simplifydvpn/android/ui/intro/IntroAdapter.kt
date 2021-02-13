package com.simplifydvpn.android.ui.intro

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.simplifydvpn.android.R
import kotlinx.android.synthetic.main.item_introduction.view.*

class IntroAdapter : ListAdapter<Intro, IntroAdapter.ItemIntroductionViewHolder>(IntroItemDiffUtilCallback()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ItemIntroductionViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_introduction, parent, false)

        return ItemIntroductionViewHolder(view)
    }

    override fun onBindViewHolder(holder: ItemIntroductionViewHolder, position: Int) {
        holder.bind(getItem(position))
    }

    class ItemIntroductionViewHolder(private val binding: View) : RecyclerView.ViewHolder(binding) {

        fun bind(intro: Intro) {
            binding.intro_bg.setImageResource(intro.imageResourceId)
            binding.intro_title.text = itemView.context.getString(intro.titleId)
            binding.intro_desc.text = itemView.context.getString(intro.descriptionId)
        }
    }

    class IntroItemDiffUtilCallback : DiffUtil.ItemCallback<Intro>() {

        override fun areItemsTheSame(oldItem: Intro, newItem: Intro): Boolean =
            oldItem.id == newItem.id

        override fun areContentsTheSame(oldItem: Intro, newItem: Intro): Boolean =
            oldItem == newItem
    }
}