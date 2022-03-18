package com.simplifyd.zerodata.android.ui.main.settings

import android.content.Intent
import android.net.Uri
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

        allowedSitesView.setOnClickListener {
            val url = getString(R.string.zerodata_url)
            openWebUrl(url)
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

    fun openWebUrl(url: String) {
        val intent = Intent(Intent.ACTION_VIEW)
        intent.data = Uri.parse(url)
        startActivity(intent)
    }


}