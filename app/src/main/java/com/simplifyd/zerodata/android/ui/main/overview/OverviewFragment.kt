package com.simplifyd.zerodata.android.ui.main.overview

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.os.Handler
import android.util.DisplayMetrics
import android.view.View
import android.widget.CompoundButton
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.work.Constraints
import androidx.work.OneTimeWorkRequestBuilder
import androidx.work.WorkManager
import com.simplifyd.zerodata.android.R
import com.simplifyd.zerodata.android.data.local.PreferenceManager
import com.simplifyd.zerodata.android.scheduling.RestartWorkWM
import com.simplifyd.zerodata.android.ui.auth.LoginActivity
import com.simplifyd.zerodata.android.ui.main.MainActivity
import com.simplifyd.zerodata.android.ui.main.MainViewModel
import com.simplifyd.zerodata.android.ui.main.overview.dialogs.AppUpdateDialogFragment
import com.simplifyd.zerodata.android.ui.main.overview.dialogs.DataDialog
import com.simplifyd.zerodata.android.ui.main.overview.dialogs.DisconnectDialogFragment
import com.simplifyd.zerodata.android.ui.main.overview.dialogs.PauseMode
import com.simplifyd.zerodata.android.utils.NetworkUtils
import com.simplifyd.zerodata.android.utils.Status
import com.simplifyd.zerodata.android.utils.showRetrySnackBar
import de.blinkt.openvpn.core.ConnectionStatus
import de.blinkt.openvpn.core.ProfileManager
import de.blinkt.openvpn.core.VpnStatus
import kotlinx.android.synthetic.main.fragment_dashboard.*
import java.util.*
import java.util.concurrent.TimeUnit


@ExperimentalStdlibApi
class OverviewFragment : Fragment(R.layout.fragment_dashboard), VpnStatus.StateListener,
    DisconnectDialogFragment.Companion.Callback {

    private val viewModel by viewModels<OverviewViewModel>()
    private val mainViewModel by activityViewModels<MainViewModel>()

    private var sec = 0
    private var isRunning = false
    private var wasRunning = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        if (savedInstanceState != null) {
            sec = savedInstanceState.getInt("seconds")
            isRunning = savedInstanceState.getBoolean("running")
            wasRunning = savedInstanceState.getBoolean("wasRunning")
        }

        startTimer()
    }


    fun startTimer(){
            val customHandler = Handler()

        customHandler.post(object : Runnable {
            override fun run() {
                val hoursVar = sec / 3600
                val minuteVar = sec % 3600 / 60
                val secVar = sec % 60
                val timeValue = String.format(
                    Locale.getDefault(),
                    "%02d:%02d",
                    minuteVar,
                    secVar
                )
                if(connection_time_val !=null)
                    connection_time_val.text = timeValue
                if (isRunning) {
                    sec++
                }
                customHandler.postDelayed(this, 1000)
            }
        })
    }

    override fun onSaveInstanceState(savedInstanceState: Bundle) {
        super.onSaveInstanceState(savedInstanceState)
        savedInstanceState.putInt("seconds", sec)
        savedInstanceState.putBoolean("running", isRunning)
        savedInstanceState.putBoolean("wasRunning", wasRunning)
    }

     override fun onPause() {
        super.onPause()
        wasRunning = isRunning
        isRunning = false
    }



    private val checkChangedListener =
        CompoundButton.OnCheckedChangeListener { buttonView, isChecked ->
            if (isChecked.not()) {
                connect_switch.isChecked = true
                DisconnectDialogFragment
                    .newInstance()
                    .show(childFragmentManager, DISCONNECT_FRAGMENT)
            } else {
                if (progressBar != null)
                    progressBar.isVisible = true
                zerodata_off.visibility = View.GONE
                zerodata_on.visibility = View.GONE
                rippleGif.visibility = View.VISIBLE
                connectionDetailsCardView.visibility = View.GONE
                if (NetworkUtils.isOnline(requireContext())) {
                    viewModel.checkIsPartnerNewtwork()
                } else {
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



        requireActivity().findViewById<View>(R.id.notifications_link).setOnClickListener {
            (requireActivity() as? MainActivity)?.let {

                findNavController().navigate(R.id.action_navigation_overview_to_navigation_notification)


            }
        }

        if (PreferenceManager.getIsFirstLogin().not()){

            DataDialog(requireActivity(), 1).showDialog()

        }


        connect_switch.isEnabled = PreferenceManager.getProfileName() != null

    }

    private fun openWebUrl(url: String) {
        val intent = Intent(Intent.ACTION_VIEW)
        intent.data = Uri.parse(url)
        startActivity(intent)
    }

    override fun onResume() {
        super.onResume()
        VpnStatus.addStateListener(this)
        if (wasRunning) {
            isRunning = true
        }
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
                    rippleGif.visibility = View.GONE
                    connectionDetailsCardView.visibility = View.GONE
                    connect_switch.setOnCheckedChangeListener(null)
                    connect_switch.isChecked = false
                    connect_switch.isEnabled = true
                    if (progressBar != null)
                        progressBar.isVisible = false
                    connect_switch.setOnCheckedChangeListener(checkChangedListener)
                    showRetrySnackBar(it.error.localizedMessage) { }
                }
                is Status.Success -> {
//                    help_text.text = Html.fromHtml(
//                        getString(
//                            R.string.data_count_text,
//                            it.data.topDomains?.values?.sum() ?: 0
//                        )
//                    )

//                    val image: Drawable =
//                        requireContext().resources.getDrawable(R.drawable.ic_shield_protected)
//                    val h: Int = image.intrinsicHeight
//                    val w: Int = image.intrinsicWidth
//                    image.setBounds(0, 0, w, h)
//                    help_text.setCompoundDrawablesRelative(
//                        null,
//                        image,
//                        null,
//                        null
//                    )
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
                    rippleGif.visibility = View.GONE
                    connectionDetailsCardView.visibility = View.GONE
                    connect_switch.setOnCheckedChangeListener(null)
                    connect_switch.isChecked = false
                    connect_switch.isEnabled = true
                    if (progressBar != null)
                        progressBar.isVisible = false
                    connect_switch.setOnCheckedChangeListener(checkChangedListener)
                    if (it.error.localizedMessage.contains("1001", ignoreCase = true)) {
                        gotoUpdateScreen()

                    } else {
                        logOutUser()

                    }

                }

            }
        }

        viewModel.checkPartnerNetwork.observe(viewLifecycleOwner) {
            when (it) {

                is Status.Loading -> {

                }
                is Status.Error -> {
                    zerodata_off.visibility = View.VISIBLE
                    zerodata_on.visibility = View.GONE
                    rippleGif.visibility = View.GONE
                    connectionDetailsCardView.visibility = View.GONE
                    connect_switch.setOnCheckedChangeListener(null)
                    connect_switch.isChecked = false
                    connect_switch.isEnabled = true
                    if (progressBar != null)
                        progressBar.isVisible = false
                    connect_switch.setOnCheckedChangeListener(checkChangedListener)
                    DataDialog(requireActivity() as MainActivity, 3).showDialog()


                }
                is Status.Success -> {

                    viewModel.connect()


                }

            }
        }

    }

    fun gotoUpdateScreen() {
        AppUpdateDialogFragment
            .newInstance()
            .show(childFragmentManager, APP_UPDATE_FRAGMENT)
    }

    fun logOutUser() {
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
                rippleGif.visibility = View.VISIBLE
                connectionDetailsCardView.visibility = View.VISIBLE
                connect_switch.isEnabled = PreferenceManager.getProfileName() != null
                connect_switch.setOnCheckedChangeListener(null)
                connect_switch.isChecked = true
                connect_switch.isEnabled = true
                if (progressBar != null)
                    progressBar.isVisible = false
                isRunning = true
                connect_switch.setOnCheckedChangeListener(checkChangedListener)
                viewModel.connectUrl?.let {
                    if (!PreferenceManager.getIsSeen()) {
                        openWebUrl(it)
                        PreferenceManager.setIsSeen(true)
                    }
                }
            } else if (level == ConnectionStatus.LEVEL_CONNECTING_SERVER_REPLIED || level == ConnectionStatus.LEVEL_CONNECTING_NO_SERVER_REPLY_YET || level == ConnectionStatus.LEVEL_START) {
                if (progressBar != null)
                    progressBar.isVisible = true
                zerodata_off.visibility = View.GONE
                zerodata_on.visibility = View.GONE
                rippleGif.visibility = View.VISIBLE
                connectionDetailsCardView.visibility = View.GONE
                connect_switch.setOnCheckedChangeListener(null)
                connect_switch.isChecked = true
                connect_switch.isEnabled = false
                PreferenceManager.setIsSeen(false)
                connect_switch.setOnCheckedChangeListener(checkChangedListener)
            } else if (level == ConnectionStatus.LEVEL_VPNPAUSED) {
                zerodata_off.visibility = View.GONE
                zerodata_on.visibility = View.GONE
                rippleGif.visibility = View.VISIBLE
                connectionDetailsCardView.visibility = View.GONE
                if (progressBar != null)
                    progressBar.isVisible = false
                connect_switch.setOnCheckedChangeListener(null)
                connect_switch.isChecked = false
                connect_switch.isEnabled = true
                PreferenceManager.setIsSeen(false)
                connect_switch.setOnCheckedChangeListener(checkChangedListener)
            } else if (level == ConnectionStatus.LEVEL_NOTCONNECTED) {
                zerodata_off.visibility = View.VISIBLE
                zerodata_on.visibility = View.GONE
                rippleGif.visibility = View.GONE
                connectionDetailsCardView.visibility = View.GONE
                connect_switch.setOnCheckedChangeListener(null)
                connect_switch.isChecked = false
                connect_switch.isEnabled = true
                if (progressBar != null)
                    progressBar.isVisible = false
                isRunning = false
                sec = 0
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

    fun noNetworkActive() {
        zerodata_off.visibility = View.VISIBLE
        zerodata_on.visibility = View.GONE
        connectionDetailsCardView.visibility = View.GONE
        connect_switch.setOnCheckedChangeListener(null)
        connect_switch.isChecked = false
        connect_switch.isEnabled = true
        if (progressBar != null)
            progressBar.isVisible = false
        connect_switch.setOnCheckedChangeListener(checkChangedListener)
        DataDialog(requireActivity() as MainActivity, 2).showDialog()
    }



    companion object {
        private const val DISCONNECT_FRAGMENT = "DISCONNECT_FRAGMENT"
        private const val APP_UPDATE_FRAGMENT = "APP_UPDATE_FRAGMENT"
    }

}