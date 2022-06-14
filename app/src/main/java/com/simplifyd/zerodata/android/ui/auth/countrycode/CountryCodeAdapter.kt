package com.simplifyd.zerodata.android.ui.auth.countrycode

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.recyclerview.widget.RecyclerView
import com.simplifyd.zerodata.android.R
import com.simplifyd.zerodata.android.ui.main.catalogue.ListedApp
import com.simplifyd.zerodata.android.utils.AutoUpdateRecyclerView
import kotlinx.android.synthetic.main.item_country_code.view.*
import kotlin.properties.Delegates

class CountryCodeAdapter(
    private val onCountryClicked: (CountryData) -> Unit
) : RecyclerView.Adapter<CountryCodeAdapter.CountryCodeViewHolder>(),
    AutoUpdateRecyclerView {

    private var selectedItemPosition = -1

    var countryCodes: List<CountryData> by Delegates.observable(emptyList()) { _, _, _ ->
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CountryCodeViewHolder {
        return CountryCodeViewHolder(
            LayoutInflater.from(parent.context).inflate(R.layout.item_country_code, parent, false)
        )
    }

    override fun getItemCount(): Int = countryCodes.size

    override fun onBindViewHolder(holder: CountryCodeViewHolder, position: Int) {
        holder.bind(countryCodes[position], position)
    }

    inner class CountryCodeViewHolder(view: View) : RecyclerView.ViewHolder(view) {

        fun bind(countryData: CountryData, position: Int) {

            itemView.checkImage.isVisible = position==selectedItemPosition

            itemView.context.getString(countryData.countryName).also { itemView.countryName.text = it }
            itemView.context.getString(countryData.countryCode).also { itemView.countryCodeTV.text = "+${it}"}
            itemView.flagImage.setImageResource(countryData.flagImageResourceId)

            itemView.countryCodeLayout.setOnClickListener {
                selectedItemPosition = position
                onCountryClicked(countryData)
                notifyDataSetChanged()
            }


        }

    }

    fun filterList(filteredList: List<CountryData>) {
        countryCodes = filteredList
        notifyDataSetChanged()
    }


}