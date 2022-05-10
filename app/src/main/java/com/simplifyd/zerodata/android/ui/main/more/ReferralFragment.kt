package com.simplifyd.zerodata.android.ui.main.more

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import com.simplifyd.zerodata.android.R
import com.simplifyd.zerodata.android.ui.main.MainViewModel
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.dialog_update.view.*


class ReferralFragment : Fragment(R.layout.fragment_referral) {

    private val mainViewModel by activityViewModels<MainViewModel>()


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        requireActivity().toolbar_title_.text = getString(R.string.refer_friend)
        requireActivity().ic_back.setOnClickListener {
            findNavController().popBackStack()
            mainViewModel.switchTab()
        }

    }

}