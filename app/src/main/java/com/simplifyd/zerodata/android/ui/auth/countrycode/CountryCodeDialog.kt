package com.simplifyd.zerodata.android.ui.auth.countrycode

import android.os.Bundle
import android.os.Handler
import android.view.View
import androidx.navigation.fragment.findNavController
import com.simplifyd.zerodata.android.R
import com.simplifyd.zerodata.android.common.BaseRoundedBottomSheetDialogFragment
import com.simplifyd.zerodata.android.utils.showToast
import kotlinx.android.synthetic.main.dialog_country_code.*

class CountryCodeDialog:BaseRoundedBottomSheetDialogFragment(), (CountryData) -> Unit {

    private val countryCodeAdapter by lazy { CountryCodeAdapter(this)}
    override fun getLayoutRes(): Int = R.layout.dialog_country_code

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        countryCodeRecyclerView.adapter = countryCodeAdapter
        countryCodeAdapter.countryCodes = countryList


    }

    companion object{

        val countryList = listOf(
            CountryData(1, R.string.nigeria, R.string.nigeria_code, R.drawable.nigerian_flag),
            CountryData(2, R.string.kenya, R.string.kenya_code, R.drawable.kenya_flag)
        )
    }

    override fun invoke(countryData: CountryData) {
        Handler().postDelayed({ findNavController().navigate(CountryCodeDialogDirections.actionCountryCodeDialogToNavigationSignIn(getString(countryData.countryCode)))
            this.dismiss()}, 500)

    }
}