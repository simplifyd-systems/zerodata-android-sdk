package com.simplifyd.zerodata.android.ui.main

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.graphics.toColor
import androidx.core.view.isGone
import androidx.core.view.isInvisible
import androidx.core.view.isVisible
import androidx.lifecycle.LiveData
import androidx.lifecycle.Observer
import androidx.navigation.NavController
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
import com.simplifyd.zerodata.android.data.local.PreferenceManager
import com.simplifyd.zerodata.android.ui.auth.LoginActivity
import com.simplifyd.zerodata.android.utils.setupWithNavController
import de.blinkt.openvpn.LaunchVPN
import de.blinkt.openvpn.VpnProfile
import de.blinkt.openvpn.activities.DisconnectVPN
import de.blinkt.openvpn.core.OpenVPNService
import de.blinkt.openvpn.core.VpnStatus
import kotlinx.android.synthetic.main.activity_main.*


class MainActivity : AppCompatActivity(R.layout.activity_main), InstallStateUpdatedListener {

    private val viewModel by viewModels<MainViewModel>()

    private var currentNavController: LiveData<NavController>? = null
    private val appUpdateManager by lazy { AppUpdateManagerFactory.create(this) }
    private val MY_REQUEST_CODE = 3

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        if (savedInstanceState == null) {
            setupBottomNavigationBar()
        }


        if (PreferenceManager.getToken().isNullOrEmpty()) {
            goToAuthScreen()
        }

      observeData()


        appUpdateManager.registerListener(this)
        checkUpdates()
    }

    private fun observeData() {
        viewModel.switchState.observe(this, Observer {

            if (it){
                bottom_nav.selectedItemId = R.id.nav_connect
            }
        })
    }

    override fun onRestoreInstanceState(savedInstanceState: Bundle) {
        super.onRestoreInstanceState(savedInstanceState)
        setupBottomNavigationBar()
    }


    private fun goToAuthScreen() {
        val intent = Intent(this, LoginActivity::class.java)
        intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK
        startActivity(intent)
        finish()
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

    private fun setupBottomNavigationBar() {
        val bottomNavigationView = bottom_nav
        val navGraphIds =
            listOf(R.navigation.nav_connect, R.navigation.nav_catalogue, R.navigation.nav_more)

        // Setup the bottom navigation view with a list of navigation graphs
        val controller = bottom_nav.setupWithNavController(
            navGraphIds = navGraphIds,
            fragmentManager = supportFragmentManager,
            containerId = R.id.navHostFragment,
            intent = intent
        )
        bottomNavigationView.itemIconTintList = null


        // Whenever the selected controller changes, setup the action bar.
        controller.observe(this, Observer { navController ->
            val appBarConfiguration = AppBarConfiguration(navController.graph)
            toolbar.setupWithNavController(navController, appBarConfiguration)

            navController.addOnDestinationChangedListener { _, destination, _ ->
                toolbar?.setNavigationIcon(R.drawable.ic_back_blue)
                bottomNavigationView.isInvisible = true
                toolbar?.labelFor?.toColor()
                val isConnectActive = destination.id == R.id.navigation_overview
                val isCatalogueActive = destination.id == R.id.fragment_catalogue
                val isMoreActive = destination.id == R.id.fragment_more
                val isReferralActive = destination.id == R.id.fragment_referral

                notifications_link.isGone = isConnectActive.not()

                    isReferralActive.not().let {
                        ic_back.isGone = it
                        toolbar_title_.isGone =it
                    }







                (isConnectActive || isCatalogueActive || isMoreActive).let {
                    app_bar.isInvisible = it
                    bottomNavigationView.isVisible = it
                    
                }

            }
        })

        currentNavController = controller
    }

    override fun onSupportNavigateUp(): Boolean {
        return currentNavController?.value?.navigateUp() ?: false
    }
}
