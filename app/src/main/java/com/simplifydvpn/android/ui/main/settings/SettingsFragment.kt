package com.simplifydvpn.android.ui.main.settings

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.View
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.simplifydvpn.android.R
import com.simplifydvpn.android.ui.login.LoginActivity
import com.simplifydvpn.android.ui.main.MainActivity
import com.simplifydvpn.android.ui.main.profile.ProfileViewModel
import kotlinx.android.synthetic.main.fragment_settings.*


class SettingsFragment : Fragment(R.layout.fragment_settings) {

    private val viewModel by viewModels<ProfileViewModel>()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        filters_link.setOnClickListener {
            findNavController().navigate(R.id.action_navigation_settings_to_navigation_filters)
        }

        protection_link.setOnClickListener {
            findNavController().navigate(R.id.action_navigation_settings_to_navigation_protection)
        }

        logout_link.setOnClickListener {
            (requireActivity() as? MainActivity)?.let{
                if(it.isVPNConnected()){
                    it.disconnectVPN()
                }else{
                    viewModel.logOut()
                    startActivity(Intent(context, LoginActivity::class.java))
                    activity?.finish()
                }
            }
        }
    }


}