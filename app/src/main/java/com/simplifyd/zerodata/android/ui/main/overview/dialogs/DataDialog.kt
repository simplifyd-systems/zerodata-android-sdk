package com.simplifyd.zerodata.android.ui.main.overview.dialogs

import android.app.Activity
import android.app.Dialog
import android.content.Intent
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.view.View
import android.view.Window
import android.view.WindowManager
import android.widget.ImageView
import androidx.appcompat.widget.AppCompatButton
import com.google.android.material.button.MaterialButton
import com.simplifyd.zerodata.android.R
import com.simplifyd.zerodata.android.data.local.PreferenceManager
import com.simplifyd.zerodata.android.ui.main.MainActivity

@Suppress("RECEIVER_NULLABILITY_MISMATCH_BASED_ON_JAVA_ANNOTATIONS")
class DataDialog (private val activity: Activity, val type: Int = 0) {


    private var dialog: Dialog? = null

    private val layoutParams: WindowManager.LayoutParams
        get() {
            val lp = WindowManager.LayoutParams()
            lp.copyFrom(dialog!!.window!!.attributes)
            lp.width = WindowManager.LayoutParams.MATCH_PARENT
            lp.height = WindowManager.LayoutParams.MATCH_PARENT
            return lp
        }

    init {
        initDialog()
        bindViews()
    }

    private fun initDialog() {
        dialog = Dialog(activity)
        dialog!!.requestWindowFeature(Window.FEATURE_NO_TITLE)
        dialog!!.setCancelable(false)
        dialog!!.window!!.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        when (type)
        {
            1 ->{
                dialog!!.setContentView(
                    R.layout.dialog_get_started
                )
                PreferenceManager.setIsFirstLogin(true)
            }

            2 ->{
                dialog!!.setContentView(
                    R.layout.dialog_offline
                )
            }

            3 ->{
                dialog!!.setContentView(
                    R.layout.dialog_nonsupported_network
                )
            }

            else -> {
                dialog!!.setContentView(
                    R.layout.dialog_verification_successful
                )
            }
        }

    }

    private fun bindViews() {
        val closeButton = dialog!!.findViewById<AppCompatButton>(R.id.btnSubmit)
        val backButton = dialog!!.findViewById<ImageView>(R.id.ic_back)

        closeButton.setOnClickListener { v ->

            when (type)
            {
                1 ->{
                    dialog!!.dismiss()
                }

                else -> {
                    dialog!!.dismiss()
                }
            }


        }

        backButton.setOnClickListener {
            dialog!!.dismiss()
        }
    }

    fun showDialog() {
        dialog!!.show()
        try {
            dialog!!.window!!.attributes = layoutParams
        } catch (ignored: Exception) {
        }

    }

    private fun startMainActivity() {
        dialog!!.dismiss()
        val intent = Intent(activity, MainActivity::class.java)
        intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK
        activity.startActivity(intent)
        activity.finish()
    }


}
