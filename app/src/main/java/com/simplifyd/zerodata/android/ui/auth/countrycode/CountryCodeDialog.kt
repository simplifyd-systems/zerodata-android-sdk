package com.simplifyd.zerodata.android.ui.auth.countrycode

import android.os.Bundle
import android.view.View
import com.simplifyd.zerodata.android.R
import com.simplifyd.zerodata.android.common.BaseRoundedBottomSheetDialogFragment
import kotlinx.android.synthetic.main.dialog_country_code.*

class CountryCodeDialog:BaseRoundedBottomSheetDialogFragment() {
    override fun getLayoutRes(): Int = R.layout.dialog_country_code

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        country_code_layout.setOnClickListener {

            this.dismiss()

        }


    }
}