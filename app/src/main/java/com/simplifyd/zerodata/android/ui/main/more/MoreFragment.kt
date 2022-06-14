package com.simplifyd.zerodata.android.ui.main.more

import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import com.simplifyd.zerodata.android.R
import com.simplifyd.zerodata.android.common.BaseRoundedBottomSheetDialogFragment
import com.simplifyd.zerodata.android.ui.auth.LoginActivity
import com.simplifyd.zerodata.android.ui.main.MainActivity
import com.simplifyd.zerodata.android.ui.main.MainViewModel
import kotlinx.android.synthetic.main.fragment_more.*


class MoreFragment : BaseRoundedBottomSheetDialogFragment() {

    override fun getLayoutRes(): Int = R.layout.fragment_more

    private val viewModel by viewModels<MoreSharedViewModel>()
    private val mainViewModel by activityViewModels<MainViewModel>()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

//        referalCodeView.setOnClickListener {
//            findNavController().navigate(R.id.action_dialogmore_to_referalFrragment)
//        }

        handle_bar.setOnClickListener {
            dismiss()
            mainViewModel.switchTab()
        }

        cancel.setOnClickListener {
            dismiss()
            mainViewModel.switchTab()
        }



        logoutView.setOnClickListener {
            (requireActivity() as? MainActivity)?.let {

                if (it.isVPNConnected()) {
                    it.disconnectVPN()
                } else {
                    viewModel.logOut()
                    startActivity(Intent(context, LoginActivity::class.java))
                    activity?.finish()
                }
            }
        }
    }

}