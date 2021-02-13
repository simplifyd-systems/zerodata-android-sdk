package com.simplifydvpn.android.ui.main.overview

import android.content.Intent
import android.os.Bundle
import android.text.Html
import android.util.DisplayMetrics
import android.view.View
import android.widget.CompoundButton
import androidx.core.content.ContextCompat
import androidx.core.graphics.drawable.DrawableCompat
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import com.simplifydvpn.android.R
import com.simplifydvpn.android.data.local.PreferenceManager
import com.simplifydvpn.android.ui.main.MainActivity
import com.simplifydvpn.android.ui.main.MainViewModel
import com.simplifydvpn.android.utils.Status
import com.simplifydvpn.android.utils.showRetrySnackBar
import de.blinkt.openvpn.LaunchVPN
import de.blinkt.openvpn.VpnProfile
import de.blinkt.openvpn.activities.DisconnectVPN
import de.blinkt.openvpn.core.ConnectionStatus
import de.blinkt.openvpn.core.ProfileManager
import de.blinkt.openvpn.core.VpnStatus
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.fragment_dashboard.*


@ExperimentalStdlibApi
class OverviewFragment : Fragment(R.layout.fragment_dashboard), VpnStatus.StateListener {

    private val viewModel by viewModels<OverviewViewModel>()
    private val mainViewModel by activityViewModels<MainViewModel>()


    private val checkChangedListener = object : CompoundButton.OnCheckedChangeListener {
        override fun onCheckedChanged(buttonView: CompoundButton?, isChecked: Boolean) {
            PreferenceManager.getProfileName()?.let {
                startOrStopOpenVPN(ProfileManager.get(requireContext(), it))
            }
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setUpSwipeRefresh()
        observeGetDashboardData()

        val displayMetrics = DisplayMetrics()
        requireActivity().windowManager.defaultDisplay.getMetrics(displayMetrics)
        val width = displayMetrics.widthPixels
        help_text.text = Html.fromHtml(getString(R.string.how_does_simplifyd_work_we))
        help_text.setCompoundDrawablesRelative(
            null,
            ContextCompat.getDrawable(requireContext(), R.drawable.ic_question_fill),
            null,
            null
        )

        requireActivity().findViewById<View>(R.id.hamburger).setOnClickListener {
            findNavController().navigate(R.id.action_navigation_overview_to_navigation_settings)
        }

        connect_switch.isEnabled = PreferenceManager.getProfileName() != null
        connect_switch.setOnCheckedChangeListener(checkChangedListener)

    }

    override fun onResume() {
        super.onResume()
        VpnStatus.addStateListener(this)
    }

    override fun onStop() {
        super.onStop()
        VpnStatus.removeStateListener(this)
    }

    private fun setUpSwipeRefresh() {

    }


    private fun observeGetDashboardData() {
        viewModel.getDashboardDataStatus.observe(viewLifecycleOwner, {
            when (it) {
                is Status.Loading -> {

                }
                is Status.Error -> {
                    showRetrySnackBar(it.error.localizedMessage) { viewModel.getDashboardData() }
                }
                is Status.Success -> {
                    help_text.text = Html.fromHtml(
                        getString(
                            R.string.protection_count_text,
                            it.data.topDomains?.values?.sum() ?: 0
                        )
                    )

                    help_text.setCompoundDrawablesRelative(
                        null,
                        ContextCompat.getDrawable(requireContext(), R.drawable.ic_shield_protected),
                        null,
                        null
                    )
                }
            }
        })
    }

    private fun startOrStopOpenVPN(profile: VpnProfile) {
        if (VpnStatus.isVPNActive() && profile.uuidString == VpnStatus.getLastConnectedVPNProfile()) {
            val disconnectVPN = Intent(activity, DisconnectVPN::class.java)
            startActivity(disconnectVPN)
        } else {
            startOpenVPN(profile)
        }
    }


    private fun startOpenVPN(profile: VpnProfile) {
        val intent = Intent(activity, LaunchVPN::class.java)
        intent.putExtra(LaunchVPN.EXTRA_KEY, profile.uuid.toString())
        intent.action = Intent.ACTION_MAIN
        startActivity(intent)
    }

    override fun updateState(
        state: String?,
        logmessage: String?,
        localizedResId: Int,
        level: ConnectionStatus?,
        Intent: Intent?
    ) {
        requireActivity().runOnUiThread {
            if (level == ConnectionStatus.LEVEL_CONNECTED) {
                protection_status.text = "Your Internet is Protected."
                connect_switch.isEnabled = PreferenceManager.getProfileName() != null
                connect_switch.setOnCheckedChangeListener(null)
                connect_switch.isChecked = true
                connect_switch.isEnabled = true
                progressBar.isVisible = false
                connect_switch.setOnCheckedChangeListener(checkChangedListener)
                (requireActivity() as MainActivity).customiseProtectMeButton(true)
            } else if (level == ConnectionStatus.LEVEL_CONNECTING_SERVER_REPLIED || level == ConnectionStatus.LEVEL_CONNECTING_NO_SERVER_REPLY_YET || level == ConnectionStatus.LEVEL_START) {
                progressBar.isVisible = true
                protection_status.text = "Connecting"
                connect_switch.setOnCheckedChangeListener(null)
                connect_switch.isChecked = false
                connect_switch.isEnabled = false
                connect_switch.setOnCheckedChangeListener(checkChangedListener)
                (requireActivity() as MainActivity).customiseProtectMeButton(false)
            } else if (level == ConnectionStatus.LEVEL_NOTCONNECTED) {
                protection_status.text = "Your Internet is Not Protected."
                connect_switch.setOnCheckedChangeListener(null)
                connect_switch.isChecked = false
                connect_switch.isEnabled = true
                progressBar.isVisible = false
                connect_switch.setOnCheckedChangeListener(checkChangedListener)
                (requireActivity() as MainActivity).customiseProtectMeButton(false)
            }
        }
    }

    override fun setConnectedVPN(uuid: String?) {

    }


}