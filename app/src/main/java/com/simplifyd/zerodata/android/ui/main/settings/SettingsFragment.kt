package com.simplifyd.zerodata.android.ui.main.settings

import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.simplifyd.zerodata.android.R
import com.simplifyd.zerodata.android.ui.auth.LoginActivity
import com.simplifyd.zerodata.android.ui.main.MainActivity
import kotlinx.android.synthetic.main.fragment_settings.*

class SettingsFragment : Fragment(R.layout.fragment_settings) {
    private val viewModel by viewModels<SettingsViewModel>()


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

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