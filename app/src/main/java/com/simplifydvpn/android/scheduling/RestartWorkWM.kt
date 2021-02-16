/*
 * Copyright (c) 2012-2020 Abdul-Mujeeb Aliu for ekoVPN
 * Distributed under the GNU GPL v2 with additional terms. For full terms see the file doc/LICENSE.txt
 */

package com.simplifydvpn.android.scheduling

import android.content.Context
import android.content.Intent
import android.util.Log
import androidx.work.Worker
import androidx.work.WorkerParameters
import com.simplifydvpn.android.data.local.PreferenceManager
import de.blinkt.openvpn.VpnProfile
import de.blinkt.openvpn.core.OpenVPNService
import de.blinkt.openvpn.core.ProfileManager

class RestartWorkWM(appContext: Context, workerParams: WorkerParameters) :
        Worker(appContext, workerParams) {


    private fun resumeVPN(profile: VpnProfile) {
        val pauseVPN = Intent(applicationContext, OpenVPNService::class.java)
        pauseVPN.action = OpenVPNService.RESUME_VPN
        applicationContext.startService(pauseVPN)
    }

    override fun doWork(): Result {

        Log.d(TAG, "RUNNING START TASK")
        PreferenceManager.getProfileName()?.let {
            try {
                Log.d(TAG, "STARTING VPN with id: $it")
                resumeVPN(ProfileManager.get(applicationContext, it))
            }catch (e: Exception){
                e.printStackTrace()
                Log.d(TAG, "FAILED STARTING VPN with id: $it")
            }
        }
        // Indicate whether the work finished successfully with the Result
        return Result.success()
    }

    companion object {
        const val TAG = "RestartWorkWM"
    }

}