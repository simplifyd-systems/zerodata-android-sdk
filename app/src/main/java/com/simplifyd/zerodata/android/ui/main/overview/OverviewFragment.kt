package com.simplifyd.zerodata.android.ui.main.overview

import android.content.Context
import android.content.Intent
import android.graphics.drawable.Drawable
import android.net.Uri
import android.os.Bundle
import android.text.Html
import android.util.DisplayMetrics
import android.view.View
import android.view.inputmethod.InputMethodManager
import android.widget.CompoundButton
import androidx.core.content.ContextCompat.getSystemService
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.work.Constraints
import androidx.work.OneTimeWorkRequestBuilder
import androidx.work.WorkManager
import com.google.android.material.button.MaterialButton
import com.simplifyd.zerodata.android.R
import com.simplifyd.zerodata.android.data.local.PreferenceManager
import com.simplifyd.zerodata.android.scheduling.RestartWorkWM
import com.simplifyd.zerodata.android.ui.auth.LoginActivity
import com.simplifyd.zerodata.android.ui.main.MainActivity
import com.simplifyd.zerodata.android.ui.main.MainViewModel
import com.simplifyd.zerodata.android.ui.main.bottomsheets.InventorySortBottomDialogFragment
import com.simplifyd.zerodata.android.ui.main.bottomsheets.PauseMode
import com.simplifyd.zerodata.android.utils.Status
import com.simplifyd.zerodata.android.utils.showRetrySnackBar
import com.simplifyd.zerodata.android.utils.showToast
import de.blinkt.openvpn.core.ConnectionStatus
import de.blinkt.openvpn.core.NetworkUtils
import de.blinkt.openvpn.core.ProfileManager
import de.blinkt.openvpn.core.VpnStatus
import kotlinx.android.synthetic.main.fragment_dashboard.*
import java.util.concurrent.TimeUnit


@ExperimentalStdlibApi
class OverviewFragment : Fragment(R.layout.fragment_dashboard), VpnStatus.StateListener,
    InventorySortBottomDialogFragment.Companion.Callback {

    private val viewModel by viewModels<OverviewViewModel>()
    private val mainViewModel by activityViewModels<MainViewModel>()
    private var btnProtectMe: MaterialButton? = null

    private val checkChangedListener =
        CompoundButton.OnCheckedChangeListener { buttonView, isChecked ->
            if (isChecked.not()) {
                connect_switch.isChecked = true
                InventorySortBottomDialogFragment
                    .newInstance()
                    .show(childFragmentManager, DISCONNECT_FRAGMENT)
            } else {
                progressBar.isVisible = true
                zerodata_off.visibility = View.GONE
                zerodata_on.visibility = View.GONE
                toggle_to_protect.isVisible = false
                protection_status.text = getString(R.string.checking_network_availability)
                if(com.simplifyd.zerodata.android.utils.NetworkUtils.isOnline(requireContext())){
                    viewModel.checkIsPartnerNewtwork()
                }else {
                    noNetworkActive()
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

        val image: Drawable = requireContext().resources.getDrawable(R.drawable.ic_question_fill)
        val h: Int = image.intrinsicHeight
        val w: Int = image.intrinsicWidth
        image.setBounds(0, 0, w, h)
        help_text.setCompoundDrawables(null, image, null, null)

        btnProtectMe = requireActivity().findViewById<MaterialButton>(R.id.btnProtectMe)

        requireActivity().findViewById<View>(R.id.settings_link).setOnClickListener {
            (requireActivity() as? MainActivity)?.let {

                findNavController().navigate(R.id.action_navigation_overview_to_navigation_settings)


            }
        }

        requireActivity().findViewById<View>(R.id.notifications_link).setOnClickListener {
            (requireActivity() as? MainActivity)?.let {

                findNavController().navigate(R.id.action_navigation_overview_to_navigation_notification)


            }
        }


        connect_switch.isEnabled = PreferenceManager.getProfileName() != null

        help_text.setOnClickListener {
            val url = getString(R.string.zerodata_url)
            openWebUrl(url)
        }
    }

    private fun openWebUrl(url: String) {
        val intent = Intent(Intent.ACTION_VIEW)
        intent.data = Uri.parse(url)
        startActivity(intent)
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
        viewModel.getDashboardDataStatus.observe(viewLifecycleOwner) {
            when (it) {
                is Status.Loading -> {

                }
                is Status.Error -> {
                    zerodata_off.visibility = View.VISIBLE
                    zerodata_on.visibility = View.GONE
                    protection_status.text = getString(R.string.internet_not_free)
                    connect_switch.setOnCheckedChangeListener(null)
                    connect_switch.isChecked = false
                    connect_switch.isEnabled = true
                    toggle_to_protect.isVisible = true
                    progressBar.isVisible = false
//                    PreferenceManager.setIsSeen(false)
                    connect_switch.setOnCheckedChangeListener(checkChangedListener)
                    showRetrySnackBar(it.error.localizedMessage) { }
                }
                is Status.Success -> {
                    help_text.text = Html.fromHtml(
                        getString(
                            R.string.data_count_text,
                            it.data.topDomains?.values?.sum() ?: 0
                        )
                    )

                    val image: Drawable =
                        requireContext().resources.getDrawable(R.drawable.ic_shield_protected)
                    val h: Int = image.intrinsicHeight
                    val w: Int = image.intrinsicWidth
                    image.setBounds(0, 0, w, h)
                    help_text.setCompoundDrawablesRelative(
                        null,
                        image,
                        null,
                        null
                    )
                }
            }
        }

        viewModel.connectProfileStatus.observe(viewLifecycleOwner) {
            when (it) {
                is Status.Loading -> {

                }

                is Status.Success -> {
                    PreferenceManager.getProfileName()?.let {
                        (requireActivity() as MainActivity).startOrStopOpenVPN(
                            ProfileManager.get(
                                requireContext(),
                                it
                            )
                        )
                    }
                }

                is Status.Error -> {
                    zerodata_off.visibility = View.VISIBLE
                    zerodata_on.visibility = View.GONE
                    protection_status.text = getString(R.string.internet_not_free)
                    connect_switch.setOnCheckedChangeListener(null)
                    connect_switch.isChecked = false
                    connect_switch.isEnabled = true
                    toggle_to_protect.isVisible = true
                    progressBar.isVisible = false
//                    PreferenceManager.setIsSeen(false)
                    connect_switch.setOnCheckedChangeListener(checkChangedListener)
//                    showRetrySnackBar(it.error.localizedMessage) { }
                    if(it.error.localizedMessage.contains("1001", ignoreCase = true)){
                        gotoUpdateScreen()

                    }else{
                        logOutUser()
//                        showRetrySnackBar(it.error.localizedMessage) { }

                    }

                }

            }
        }

        viewModel.checkPartnerNetwork.observe(viewLifecycleOwner){
            when (it){

                is Status.Loading -> {

                }
                is Status.Error -> {
                    zerodata_off.visibility = View.VISIBLE
                    zerodata_on.visibility = View.GONE
                    protection_status.text = getString(R.string.internet_not_free)
                    connect_switch.setOnCheckedChangeListener(null)
                    connect_switch.isChecked = false
                    connect_switch.isEnabled = true
                    toggle_to_protect.isVisible = true
                    progressBar.isVisible = false
//                    PreferenceManager.setIsSeen(false)
                    connect_switch.setOnCheckedChangeListener(checkChangedListener)
                    showRetrySnackBar(getString(R.string.not_on_partner_network)) { }
                }
                is Status.Success -> {

                    viewModel.connect()


                }

            }
        }

    }

    fun gotoUpdateScreen() {
        findNavController().navigate(R.id.action_navigation_overview_to_update)
    }

    fun logOutUser()
    {
        (requireActivity() as? MainActivity)?.let { it ->

            if (it.isVPNConnected()) {
                it.disconnectVPN()
            } else {
                viewModel.logOut()
                startActivity(Intent(context, LoginActivity::class.java))
                activity?.finish()
            }
        }
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
                zerodata_off.visibility = View.GONE
                zerodata_on.visibility = View.VISIBLE
                protection_status.text = getString(R.string.internet_free)
                connect_switch.isEnabled = PreferenceManager.getProfileName() != null
                connect_switch.setOnCheckedChangeListener(null)
                connect_switch.isChecked = true
                connect_switch.isEnabled = true
                progressBar.isVisible = false
                toggle_to_protect.isVisible = false
                connect_switch.setOnCheckedChangeListener(checkChangedListener)
                viewModel.connectUrl?.let {
                    if (!PreferenceManager.getIsSeen()) {
                        openWebUrl(it)
                        PreferenceManager.setIsSeen(true)
                    }
                }
            } else if (level == ConnectionStatus.LEVEL_CONNECTING_SERVER_REPLIED || level == ConnectionStatus.LEVEL_CONNECTING_NO_SERVER_REPLY_YET || level == ConnectionStatus.LEVEL_START) {
                progressBar.isVisible = true
                zerodata_off.visibility = View.GONE
                zerodata_on.visibility = View.GONE
                protection_status.text = getString(R.string.connecting)
                connect_switch.setOnCheckedChangeListener(null)
                connect_switch.isChecked = true
                connect_switch.isEnabled = false
                toggle_to_protect.isVisible = false
                PreferenceManager.setIsSeen(false)
                connect_switch.setOnCheckedChangeListener(checkChangedListener)
            } else if (level == ConnectionStatus.LEVEL_VPNPAUSED) {
                zerodata_off.visibility = View.GONE
                zerodata_on.visibility = View.GONE
                progressBar.isVisible = false
                protection_status.text = getString(R.string.paused)
                connect_switch.setOnCheckedChangeListener(null)
                connect_switch.isChecked = false
                connect_switch.isEnabled = true
                toggle_to_protect.isVisible = false
                PreferenceManager.setIsSeen(false)
                connect_switch.setOnCheckedChangeListener(checkChangedListener)
            } else if (level == ConnectionStatus.LEVEL_NOTCONNECTED) {
                zerodata_off.visibility = View.VISIBLE
                zerodata_on.visibility = View.GONE
                protection_status.text = getString(R.string.internet_not_free)
                connect_switch.setOnCheckedChangeListener(null)
                connect_switch.isChecked = false
                connect_switch.isEnabled = true
                toggle_to_protect.isVisible = true
                progressBar.isVisible = false
                PreferenceManager.setIsSeen(false)
                connect_switch.setOnCheckedChangeListener(checkChangedListener)
            }
        }
    }

    private fun enqueueRestart(minutes: Int) {

        WorkManager.getInstance(requireContext()).cancelAllWorkByTag(RestartWorkWM.TAG)

        val constraintsBuilder = Constraints.Builder()
        val constraints = constraintsBuilder.build()
        val work = OneTimeWorkRequestBuilder<RestartWorkWM>()
            .setConstraints(constraints)
            .setInitialDelay(minutes.toLong(), TimeUnit.MINUTES)
            .addTag(RestartWorkWM.TAG)
            .build()
        val workManager = WorkManager.getInstance(requireContext())
        workManager.enqueue(work)
    }

    override fun setConnectedVPN(uuid: String?) {

    }

    override fun onSelectSortParameter(pauseMode: PauseMode) {
        when (pauseMode) {
            PauseMode.PAUSE_FOR_FIFTEEN_MINUTES -> {
                enqueueRestart(15)
                (requireActivity() as? MainActivity)?.pauseVPN()
            }
            PauseMode.PAUSE_FOR_ONE_HOUR -> {
                enqueueRestart(60)
                (requireActivity() as? MainActivity)?.pauseVPN()
            }
            PauseMode.DISABLE -> {
                (requireActivity() as? MainActivity)?.disconnectVPN()
            }
            PauseMode.CANCEL -> {

            }
        }
    }

    fun noNetworkActive(){
        zerodata_off.visibility = View.VISIBLE
        zerodata_on.visibility = View.GONE
        protection_status.text = getString(R.string.internet_not_free)
        connect_switch.setOnCheckedChangeListener(null)
        connect_switch.isChecked = false
        connect_switch.isEnabled = true
        toggle_to_protect.isVisible = true
        progressBar.isVisible = false
//                    PreferenceManager.setIsSeen(false)
        connect_switch.setOnCheckedChangeListener(checkChangedListener)
        showRetrySnackBar(getString(R.string.error_network_connectivity)) { }
    }




    companion object {
        private const val DISCONNECT_FRAGMENT = "DISCONNECT_FRAGMENT"
    }

}