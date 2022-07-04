/*
 * Copyright (c) 2012-2020 Abdul-Mujeeb Aliu for ekoVPN
 * Distributed under the GNU GPL v2 with additional terms. For full terms see the file doc/LICENSE.txt
 */

package com.simplifyd.zerodata.data.config

import android.content.Context
import com.simplifyd.zerodata.BuildConfig
import de.blinkt.openvpn.VpnProfile
import de.blinkt.openvpn.core.ProfileManager
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.launch

@ExperimentalCoroutinesApi
object OpenVpnConfigurator {

    lateinit var context: Context

    private val profileManager: ProfileManager by lazy {
        ProfileManager.getInstance(context)
    }
    private val ovpnProfileImporter: OVPNProfileImporter by lazy {
        OVPNProfileImporter()
    }

    fun configureOVPNServers(profileData: String): Flow<VpnProfile> {
        return (configureOVPNServer(profileData = profileData))
    }

    private fun configureOVPNServer(profileData: String): Flow<VpnProfile> {
        return flow {
            emit(saveOVPNProfile(ovpnProfileImporter.parseServerConfig(profileData)))
        }
    }

    private fun saveOVPNProfile(result: VpnProfile): VpnProfile {
        result.mName = "vpn-profile"
        result.mAllowedAppsVpn.add(BuildConfig.APPLICATION_ID)
        result.mAllowedAppsVpnAreDisallowed = true
        GlobalScope.launch(Dispatchers.Main) {
            profileManager.addProfile(result)
            profileManager.saveProfile(context, result)
            profileManager.saveProfileList(context)
        }

        return result
        //todo save name to root
    }
}