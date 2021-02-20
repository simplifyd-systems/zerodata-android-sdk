/*
 * Copyright (c) 2012-2020 Abdul-Mujeeb Aliu for ekoVPN
 * Distributed under the GNU GPL v2 with additional terms. For full terms see the file doc/LICENSE.txt
 */

package com.simplifydvpn.android.data.config.downloader

import android.content.Context
import android.os.Environment
import android.util.Log
import androidx.core.content.ContextCompat
import com.downloader.OnDownloadListener
import com.downloader.PRDownloader
import com.simplifydvpn.android.data.local.PreferenceManager
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.channels.ConflatedBroadcastChannel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.asFlow
import kotlinx.coroutines.launch
import java.io.File
import javax.inject.Inject


@ExperimentalCoroutinesApi
class FileDownloader @Inject constructor(private val context: Context) {

    private fun getRootDirPath(context: Context): String {
        return if (Environment.MEDIA_MOUNTED == Environment.getExternalStorageState()) {
            val file: File = ContextCompat.getExternalFilesDirs(context.applicationContext, null)[0]
            file.absolutePath
        } else {
            context.applicationContext.filesDir.absolutePath
        }
    }


    fun downloadOVPNConfig(configFileURL: String): Flow<Result<File>> {
        return downloadConfigFile(configFileURL)
    }


    private fun downloadConfigFile(configFileURL: String): Flow<Result<File>> {
        val fileName = "ovpn_config.ovpn".replace(" ", "_")
        val channel = ConflatedBroadcastChannel<Result<File>>()

        val filePath = "${getRootDirPath(context)}/${fileName}"
        PRDownloader.download(configFileURL, getRootDirPath(context), fileName)
            .setHeader("Authorization", "Bearer " + PreferenceManager.getToken())
            .build()
            .setOnStartOrResumeListener { }
            .setOnPauseListener { }
            .setOnProgressListener { }
            .start(object : OnDownloadListener {
                override fun onDownloadComplete() {
                    GlobalScope.launch(Dispatchers.IO) {
                        Log.d(FileDownloader::class.java.simpleName, "Downloaded: $filePath")
                        channel.send(Result.success(File(filePath)))
                    }
                }

                override fun onError(error: com.downloader.Error?) {
                    GlobalScope.launch(Dispatchers.IO) {
                        Log.d(
                            FileDownloader::class.java.simpleName,
                            "Error downloading ${configFileURL}, :" +
                                    " ${error?.isConnectionError} ${error?.isServerError} ${error?.connectionException?.localizedMessage} ${error?.serverErrorMessage}"
                        )
                        channel.send(Result.failure(FileDownloaderException(error.toString())))
                    }
                }
            })

        return channel.asFlow()
    }

}