package com.simplifyd.zerodata.android.ui.main.more

import android.os.Bundle
import android.view.View
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.simplifyd.zerodata.android.R
import com.simplifyd.zerodata.android.common.BaseRoundedBottomSheetDialogFragment
import kotlinx.android.synthetic.main.fragment_more.*


class MoreFragment : BaseRoundedBottomSheetDialogFragment() {

    override fun getLayoutRes(): Int = R.layout.fragment_more

    private val viewModel by viewModels<MoreSharedViewModel>()


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)


        referalCodeView.setOnClickListener {
            findNavController().navigate(R.id.action_dialogmore_to_referalFrragment)
        }

        logoutView.setOnClickListener {
            viewModel.logOut()
        }
    }


}