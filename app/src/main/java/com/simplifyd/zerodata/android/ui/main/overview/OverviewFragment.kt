package com.simplifyd.zerodata.android.ui.main.overview

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.net.Uri
import android.os.Bundle
import android.os.SystemClock
import android.util.DisplayMetrics
import android.view.View
import android.widget.CompoundButton
import android.widget.Toast
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.localbroadcastmanager.content.LocalBroadcastManager
import androidx.navigation.fragment.findNavController
import androidx.work.Constraints
import androidx.work.OneTimeWorkRequestBuilder
import androidx.work.WorkManager
import com.simplifyd.zerodata.android.R
import com.simplifyd.zerodata.android.data.local.PreferenceManager
import com.simplifyd.zerodata.android.scheduling.RestartWorkWM
import com.simplifyd.zerodata.android.ui.auth.LoginActivity
import com.simplifyd.zerodata.android.ui.main.MainActivity
import com.simplifyd.zerodata.android.ui.main.overview.dialogs.AppUpdateDialogFragment
import com.simplifyd.zerodata.android.ui.main.overview.dialogs.DataDialog
import com.simplifyd.zerodata.android.ui.main.overview.dialogs.DisconnectDialogFragment
import com.simplifyd.zerodata.android.ui.main.overview.dialogs.PauseMode
import com.simplifyd.zerodata.android.utils.*
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

        if (PreferenceManager.getIsFirstLogin().not()) {

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
        LocalBroadcastManager.getInstance(requireContext()).registerReceiver(
            broadcastReceiver, IntentFilter(
                Constants.APP_INSTALLED
            )
        )
    }


    override fun onStart() {
        super.onStart()
        restartTimer()
    }

    override fun onPause() {
        super.onPause()
        saveElapsedTime()

    }

    override fun onDestroy() {
        super.onDestroy()
        LocalBroadcastManager.getInstance(requireContext()).unregisterReceiver(broadcastReceiver)

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

        viewModel.getSavedData.observe(viewLifecycleOwner){
            when (it) {

                is Status.Loading -> {

                }

                is Status.Success -> {

                    viewModel.dataSaved?.let { usedDatabytes ->
                        data_saved_val.text =  getString(R.string.data_saved_val,(usedDatabytes/(1024 * 1024)).toDouble())

                    }

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
                startTimer()
                data_saved_val.text =  getString(R.string.data_saved_val,0.0)
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
                stopTimer()
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


    private fun startTimer() {
        if (!PreferenceManager.getIsTiming()) {
            connection_time_val.start()
            PreferenceManager.setIsTiming(true)
        }
    }

    private fun saveElapsedTime() {
        if (PreferenceManager.getIsTiming()) {
            val elapsedMillis: Long = SystemClock.elapsedRealtime() - connection_time_val.base
            val currentTimeMilli = Calendar.getInstance().timeInMillis
            PreferenceManager.saveCurrentTime(currentTimeMilli)
            PreferenceManager.saveTimeElapsed(elapsedMillis)
        }
    }

    private fun stopTimer() {
        if (PreferenceManager.getIsTiming()) {
            connection_time_val.stop()
            PreferenceManager.setIsTiming(false)
            connection_time_val.base = SystemClock.elapsedRealtime()
        }
    }

    private fun restartTimer() {
        if (PreferenceManager.getIsTiming()) {
            val currentTimeMilli = Calendar.getInstance().timeInMillis
            val timeDiff = currentTimeMilli - PreferenceManager.getLastSavedTime()
            val timeElasped: Long = SystemClock.elapsedRealtime() - (PreferenceManager.getTimeElapsed() + timeDiff)
            connection_time_val.base = timeElasped
            viewModel.getDataSaved(timeElasped)
            connection_time_val.start()
        }
    }

    companion object {
        private const val DISCONNECT_FRAGMENT = "DISCONNECT_FRAGMENT"
        private const val APP_UPDATE_FRAGMENT = "APP_UPDATE_FRAGMENT"
    }

    private val broadcastReceiver = object : BroadcastReceiver() {
        override fun onReceive(context: Context?, intent: Intent?) {
            if (intent != null) {
                val state = intent.extras?.getString(Constants.PACKAGE_NAME)
                if(connect_switch.isChecked)
                    viewModel.logPackageChange(state!!, state, Calendar.getInstance().timeInMillis)
            }



        }
    }


}