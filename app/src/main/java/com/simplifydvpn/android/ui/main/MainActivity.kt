package com.simplifydvpn.android.ui.main

import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.isGone
import androidx.core.view.isInvisible
import androidx.lifecycle.lifecycleScope
import androidx.navigation.Navigation
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.setupWithNavController
import com.simplifydvpn.android.R
import de.blinkt.openvpn.LaunchVPN
import de.blinkt.openvpn.VpnProfile
import de.blinkt.openvpn.activities.DisconnectVPN
import de.blinkt.openvpn.core.OpenVPNService
import de.blinkt.openvpn.core.VpnStatus
import kotlinx.android.synthetic.main.activity_main.*


class MainActivity : AppCompatActivity(R.layout.activity_main) {

    private val viewModel by viewModels<MainViewModel>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val navController = Navigation.findNavController(this@MainActivity, R.id.navHostFragment)
        val appBarConfiguration = AppBarConfiguration(navController.graph)
        toolbar.setupWithNavController(navController, appBarConfiguration)

        navController.addOnDestinationChangedListener { _, destination, _ ->
            toolbar?.setNavigationIcon(R.drawable.ic_back)
            val isOverviewScreenActive = destination.id == R.id.navigation_overview
            logout_link.isGone = isOverviewScreenActive.not()
            imageView2.isGone = isOverviewScreenActive.not()
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
}
