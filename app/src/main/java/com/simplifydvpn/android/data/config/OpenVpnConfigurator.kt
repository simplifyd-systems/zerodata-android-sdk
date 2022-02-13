/*
 * Copyright (c) 2012-2020 Abdul-Mujeeb Aliu for ekoVPN
 * Distributed under the GNU GPL v2 with additional terms. For full terms see the file doc/LICENSE.txt
 */

package com.simplifydvpn.android.data.config

import android.content.Context
import androidx.core.net.toUri
import com.simplifydvpn.android.BuildConfig
import com.simplifydvpn.android.data.config.downloader.FileDownloader
import de.blinkt.openvpn.VpnProfile
import de.blinkt.openvpn.core.ProfileManager
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.launch
import java.io.File

@ExperimentalCoroutinesApi
object OpenVpnConfigurator {

    lateinit var context: Context

    private val fileDownloader: FileDownloader by lazy {
        FileDownloader(context)
    }
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

    private fun configureOVPNProfileForServer(file: File): VpnProfile {
        return ovpnProfileImporter.importServerConfig("file://${file.toURI()}".toUri()).let {
            file.delete()
            it
        }
    }

    private suspend fun saveOVPNProfile(result: VpnProfile): VpnProfile {
        val profile = result
        profile.mName = "vpn-profile"
        profile.mAllowedAppsVpn.add(BuildConfig.APPLICATION_ID)
        profile.mAllowedAppsVpnAreDisallowed = true
        GlobalScope.launch(Dispatchers.Main) {
            profileManager.addProfile(profile)
            profileManager.saveProfile(context, profile)
            profileManager.saveProfileList(context)
        }
        return profile
        //todo save name to root
    }
}