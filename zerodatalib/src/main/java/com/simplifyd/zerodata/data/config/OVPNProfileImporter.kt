/*
 * Copyright (c) 2012-2020 Abdul-Mujeeb Aliu for ekoVPN
 * Distributed under the GNU GPL v2 with additional terms. For full terms see the file doc/LICENSE.txt
 */

package com.simplifyd.zerodata.data.config

import android.util.Log
import de.blinkt.openvpn.VpnProfile
import de.blinkt.openvpn.core.ConfigParser
import java.io.InputStream
import java.io.InputStreamReader
import javax.inject.Inject

class OVPNProfileImporter @Inject constructor() {

    fun parseServerConfig(serverConfig: String): VpnProfile {
        val result = doImport(serverConfig.byteInputStream())
        Log.d("OVPN:", "${result!!.uuid}")
        return result
    }

    private fun doImport(inputStream: InputStream): VpnProfile? {
        val configParser = ConfigParser()

        val inputStreamReader = InputStreamReader(inputStream)
        configParser.parseConfig(inputStreamReader)
        val result: VpnProfile? = configParser.convertProfile()
        inputStream.close()
        return result
    }
}