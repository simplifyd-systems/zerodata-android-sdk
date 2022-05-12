package com.simplifyd.zerodata.android.ui.main.catalogue

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import com.simplifyd.zerodata.android.R
import kotlinx.android.synthetic.main.activity_main.*

class CatalogueFragment : Fragment(R.layout.fragment_catalogue) {

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        requireActivity().toolbar_title_.text = getString(R.string.supported_apps)

    }

}