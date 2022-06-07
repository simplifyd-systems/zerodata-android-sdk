package com.simplifyd.zerodata.android.ui.main.broadcastreceiver

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.widget.Toast
import androidx.localbroadcastmanager.content.LocalBroadcastManager
import com.simplifyd.zerodata.android.utils.Constants

class PackageChangeReceiver : BroadcastReceiver() {

    override fun onReceive(context: Context?, intent: Intent?) {
        // fetching package names from extras
        val packageName = intent?.data?.encodedSchemeSpecificPart
        when (intent?.action) {
            Intent.ACTION_PACKAGE_CHANGED -> {
                val intentToBroadcast = Intent(Constants.APP_INSTALLED).putExtra(Constants.PACKAGE_NAME, packageName.toString())
                LocalBroadcastManager.getInstance(context!!).sendBroadcast(intentToBroadcast)
            }

            Intent.ACTION_PACKAGE_ADDED ->{
                val intentToBroadcast = Intent(Constants.APP_INSTALLED).putExtra(Constants.PACKAGE_NAME, packageName.toString())
                LocalBroadcastManager.getInstance(context!!).sendBroadcast(intentToBroadcast)

            }
        }
    }
}


