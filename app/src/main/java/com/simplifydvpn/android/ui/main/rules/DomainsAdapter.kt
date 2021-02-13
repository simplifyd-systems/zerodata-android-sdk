package com.simplifydvpn.android.ui.main.rules

import android.graphics.Bitmap
import android.graphics.drawable.Drawable
import android.graphics.drawable.GradientDrawable
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.DrawableRes
import androidx.annotation.StringRes
import androidx.core.view.setPadding
import androidx.palette.graphics.Palette
import androidx.recyclerview.widget.RecyclerView
import com.simplifydvpn.android.R
import com.simplifydvpn.android.data.model.Rule
import com.simplifydvpn.android.ui.EmptyStateViewHolder
import com.simplifydvpn.android.utils.getUrlHost
import com.squareup.picasso.Picasso
import com.squareup.picasso.Target
import kotlinx.android.synthetic.main.item_domain.view.*

@ExperimentalStdlibApi
class DomainsAdapter(
    private val onClearDomain: (Rule) -> Unit,
    @DrawableRes private val emptyStateImage: Int,
    @StringRes private val emptyStateDesc: Int
) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    var domains = listOf<Rule>()
        set(value) {
            field = value
            notifyDataSetChanged()
        }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return when (viewType) {
            0 -> EmptyStateViewHolder.create(parent)
            else -> DomainViewHolder(
                LayoutInflater.from(parent.context).inflate(R.layout.item_domain, parent, false)
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
            is DomainViewHolder -> holder.bind(domains[position])
        }
    }

    override fun getItemViewType(position: Int): Int {
        return if (domains.isEmpty()) {
            0
        } else {
            1
        }
    }

    inner class DomainViewHolder(view: View) : RecyclerView.ViewHolder(view) {

        fun drawCircle(backgroundColor: Int): GradientDrawable? {
            val shape = GradientDrawable()
            shape.shape = GradientDrawable.OVAL
            shape.cornerRadii = floatArrayOf(0f, 0f, 0f, 0f, 0f, 0f, 0f, 0f)
            shape.setColor(backgroundColor)
            return shape
        }

        fun bind(domain: Rule) {
            itemView.tvDomainName.text = domain.domain
            itemView.tvDomainTitle.text = domain.domain?.getUrlHost()
            itemView.btnClear.setOnClickListener { onClearDomain(domain) }

            Picasso.get()
                .load(domain.domain + "/favicon.ico")
                .error(R.drawable.ic_domain_placeholder)
                .into(object : Target {
                    override fun onBitmapLoaded(bitmap: Bitmap?, from: Picasso.LoadedFrom?) {
                        Palette.from(bitmap ?: return)
                            .generate { palette ->
                                val swatch = palette?.vibrantSwatch ?: return@generate
                                itemView.ivDomainIcon.setPadding(
                                    itemView.context.resources.getDimensionPixelSize(
                                        R.dimen.one_space
                                    )
                                )
                                itemView.ivDomainIcon.setImageBitmap(bitmap)
                                itemView.ivDomainIcon.background = drawCircle(swatch.rgb)
                            }
                    }

                    override fun onBitmapFailed(e: Exception?, errorDrawable: Drawable?) {
                    }

                    override fun onPrepareLoad(placeHolderDrawable: Drawable?) {
                    }
                })
        }
    }
}