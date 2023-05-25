package com.simplifyd.zerodata.settings

import de.blinkt.openvpn.core.VpnStatus

interface ZeroDataStateListener : VpnStatus.StateListener{
    fun initializationSuccess()
    fun initializationFailed(error: String)
    fun configureSuccess()
    fun configureFailed(error: String)
}