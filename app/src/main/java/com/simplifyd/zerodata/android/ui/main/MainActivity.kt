package com.simplifyd.zerodata.android.ui.main

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.isGone
import androidx.core.view.isInvisible
import androidx.navigation.Navigation
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.setupWithNavController
import com.google.android.play.core.appupdate.AppUpdateManagerFactory
import com.google.android.play.core.install.InstallState
import com.google.android.play.core.install.InstallStateUpdatedListener
import com.google.android.play.core.install.model.ActivityResult
import com.google.android.play.core.install.model.AppUpdateType
import com.google.android.play.core.install.model.AppUpdateType.IMMEDIATE
import com.google.android.play.core.install.model.InstallStatus
import com.google.android.play.core.install.model.UpdateAvailability
import com.simplifyd.zerodata.android.R
import de.blinkt.openvpn.LaunchVPN
import de.blinkt.openvpn.VpnProfile
import de.blinkt.openvpn.activities.DisconnectVPN
import de.blinkt.openvpn.core.OpenVPNService
import de.blinkt.openvpn.core.VpnStatus
import kotlinx.android.synthetic.main.activity_main.*


class MainActivity : AppCompatActivity(R.layout.activity_main), InstallStateUpdatedListener {


    private val appUpdateManager by lazy { AppUpdateManagerFactory.create(this) }
    private val MY_REQUEST_CODE = 3

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        appUpdateManager.registerListener(this)
        checkUpdates()

        val navController = Navigation.findNavController(this@MainActivity, R.id.navHostFragment)
        val appBarConfiguration = AppBarConfiguration(navController.graph)
        toolbar.setupWithNavController(navController, appBarConfiguration)

        navController.addOnDestinationChangedListener { _, destination, _ ->
            toolbar?.setNavigationIcon(R.drawable.ic_back)
            val isOverviewScreenActive = destination.id == R.id.navigation_overview
            settings_link.isGone = isOverviewScreenActive.not()
            notifications_link.isGone = isOverviewScreenActive.not()
            imageViNew2.isGone = isOverviewScreenActive.not()
            toolbar.isInvisible = isOverviewScreenActive
        }

        toolbar.setNavigationOnClickListener {
            navController.navigateUp() || super.onSupportNavigateUp()
        }
    }


    fun startOrStopOpenVPN(profile: VpnProfile) {
        if (isVPNConnected() && profile.uuidString == VpnStatus.getLastConnectedVPNProfile()) {
            disconnectVPN()
        } else {
            startOpenVPN(profile)
        }
    }

    fun disconnectVPN() {
        val disconnectVPN = Intent(this, DisconnectVPN::class.java)
        startActivity(disconnectVPN)
    }

    fun isVPNConnected() =
        VpnStatus.isVPNActive()


    private fun startOpenVPN(profile: VpnProfile) {
        val intent = Intent(this, LaunchVPN::class.java)
        intent.putExtra(LaunchVPN.EXTRA_KEY, profile.uuid.toString())
        intent.action = Intent.ACTION_MAIN
        startActivity(intent)
    }

    fun pauseVPN() {
        val pauseVPN = Intent(this, OpenVPNService::class.java)
        pauseVPN.action = OpenVPNService.PAUSE_VPN
        startService(pauseVPN)
    }

    override fun onResume() {
        super.onResume()
        appUpdateManager
            .appUpdateInfo
            .addOnSuccessListener { appUpdateInfo ->
                if (appUpdateInfo.updateAvailability()
                    == UpdateAvailability.DEVELOPER_TRIGGERED_UPDATE_IN_PROGRESS
                ) {
                    // If an in-app update is already running, resume the update.
                    appUpdateManager.startUpdateFlowForResult(
                        appUpdateInfo,
                        IMMEDIATE,
                        this,
                        MY_REQUEST_CODE
                    )
                }
            }
    }

    fun checkUpdates(){
        val appUpdateInfoTask = appUpdateManager?.appUpdateInfo
        // Checks that the platform will allow the specified type of update.
        appUpdateInfoTask?.addOnSuccessListener { appUpdateInfo ->
            if (appUpdateInfo.updateAvailability() == UpdateAvailability.UPDATE_AVAILABLE && (appUpdateInfo.clientVersionStalenessDays() ?: -1) >= 7
                && appUpdateInfo.isUpdateTypeAllowed(AppUpdateType.FLEXIBLE)) {
                // Request the update.
                Log.d( "Update available", "Update available")
                appUpdateManager.startUpdateFlowForResult(
                    // Pass the intent that is returned by 'getAppUpdateInfo()'.
                    appUpdateInfo,
                    // Or 'AppUpdateType.FLEXIBLE' for flexible updates.
                    AppUpdateType.FLEXIBLE,
                    // The current activity making the update request.
                    this,
                    // Include a request code to later monitor this update request.
                    MY_REQUEST_CODE)
            } else {
                Log.d("No Update available", "No Update available")
            }
        }
    }

    override fun onStateUpdate(p0: InstallState) {
        if (p0.installStatus() == InstallStatus.DOWNLOADED) {
            // After the update is downloaded, show a notification
            // and request user confirmation to restart the app.
            Log.d("Update Downloaded","An update has been downloaded")

            appUpdateManager.completeUpdate()

            appUpdateManager.unregisterListener(this)
//            showSnackBarForCompleteUpdate()
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (requestCode == MY_REQUEST_CODE) {
            when (resultCode) {
                Activity.RESULT_OK -> {
                    Log.d("", "Result Ok")
                    //  handle user's approval }
                }
                Activity.RESULT_CANCELED -> {

                    Log.d("","Result Cancelled")
                    //  handle user's rejection  }
                }
                ActivityResult.RESULT_IN_APP_UPDATE_FAILED -> {
                    //if you want to request the update again just call checkUpdate()
                    Log.d("", "Update Failure")
                    //  handle update failure
                }
            }
        }
    }
}
