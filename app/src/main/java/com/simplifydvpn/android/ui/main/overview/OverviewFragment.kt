package com.simplifydvpn.android.ui.main.overview

import android.content.Intent
import android.content.res.ColorStateList
import android.os.Bundle
import android.text.Html
import android.util.DisplayMetrics
import android.view.View
import android.widget.CompoundButton
import androidx.core.content.ContextCompat
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.work.Constraints
import androidx.work.OneTimeWorkRequestBuilder
import androidx.work.WorkManager
import com.google.android.material.button.MaterialButton
import com.simplifydvpn.android.R
import com.simplifydvpn.android.data.local.PreferenceManager
import com.simplifydvpn.android.scheduling.RestartWorkWM
import com.simplifydvpn.android.ui.main.MainActivity
import com.simplifydvpn.android.ui.main.MainViewModel
import com.simplifydvpn.android.ui.main.bottomsheets.InventorySortBottomDialogFragment
import com.simplifydvpn.android.ui.main.bottomsheets.PauseMode
import com.simplifydvpn.android.utils.Status
import com.simplifydvpn.android.utils.getColorInt
import com.simplifydvpn.android.utils.showRetrySnackBar
import de.blinkt.openvpn.core.ConnectionStatus
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
                PreferenceManager.getProfileName()?.let {
                    (requireActivity() as MainActivity).startOrStopOpenVPN(
                        ProfileManager.get(
                            requireContext(),
                            it
                        )
                    )
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

        btnProtectMe = requireActivity().findViewById<MaterialButton>(R.id.btnProtectMe)

        requireActivity().findViewById<View>(R.id.logout_link).setOnClickListener {
        //logout
        }

        connect_switch.isEnabled = PreferenceManager.getProfileName() != null

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


    override fun updateState(
        state: String?,
        logmessage: String?,
        localizedResId: Int,
        level: ConnectionStatus?,
        Intent: Intent?
    ) {
        requireActivity().runOnUiThread {
            if (level == ConnectionStatus.LEVEL_CONNECTED) {
                protection_status.text = "Your are conected to Edge."
                connect_switch.isEnabled = PreferenceManager.getProfileName() != null
                connect_switch.setOnCheckedChangeListener(null)
                connect_switch.isChecked = true
                connect_switch.isEnabled = true
                progressBar.isVisible = false
                connect_switch.setOnCheckedChangeListener(checkChangedListener)
            } else if (level == ConnectionStatus.LEVEL_CONNECTING_SERVER_REPLIED || level == ConnectionStatus.LEVEL_CONNECTING_NO_SERVER_REPLY_YET || level == ConnectionStatus.LEVEL_START) {
                progressBar.isVisible = true
                protection_status.text = "Connecting"
                connect_switch.setOnCheckedChangeListener(null)
                connect_switch.isChecked = true
                connect_switch.isEnabled = false
                connect_switch.setOnCheckedChangeListener(checkChangedListener)
            }else if (level == ConnectionStatus.LEVEL_VPNPAUSED ) {
                progressBar.isVisible = false
                protection_status.text = "Paused"
                connect_switch.setOnCheckedChangeListener(null)
                connect_switch.isChecked = false
                connect_switch.isEnabled = true
                connect_switch.setOnCheckedChangeListener(checkChangedListener)
            } else if (level == ConnectionStatus.LEVEL_NOTCONNECTED) {
                protection_status.text = "Your are not connected to Edge."
                connect_switch.setOnCheckedChangeListener(null)
                connect_switch.isChecked = false
                connect_switch.isEnabled = true
                toggle_to_protect.isVisible = true
                progressBar.isVisible = false
                connect_switch.setOnCheckedChangeListener(checkChangedListener)
            }
        }
    }

    private fun enqueueRestart(minutes:Int){

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
        when(pauseMode){
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


    companion object{
        private const val DISCONNECT_FRAGMENT = "DISCONNECT_FRAGMENT"
    }

}