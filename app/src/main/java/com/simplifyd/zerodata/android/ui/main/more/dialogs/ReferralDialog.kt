package com.simplifyd.zerodata.android.ui.main.more.dialogs

import android.app.Activity
import android.app.Dialog
import android.content.Intent
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.view.Window
import android.view.WindowManager
import androidx.appcompat.widget.AppCompatImageView
import androidx.appcompat.widget.AppCompatTextView
import com.simplifyd.zerodata.android.R
import com.simplifyd.zerodata.android.ui.main.MainActivity

@Suppress("RECEIVER_NULLABILITY_MISMATCH_BASED_ON_JAVA_ANNOTATIONS")
class ReferralDialog (private val activity: Activity, val type: Int = 0) {

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
                dialog!!.setContentView(R.layout.dialog_referee)
            }

            2 ->{
                dialog!!.setContentView(R.layout.dialog_referrer)
            }

        }

    }

    private fun bindViews() {
        val shareButton = dialog!!.findViewById<AppCompatImageView>(R.id.share)
        val copyButton = dialog!!.findViewById<AppCompatImageView>(R.id.copy)
        val cancelButton = dialog!!.findViewById<AppCompatTextView>(R.id.cancel)

        shareButton.setOnClickListener { v ->

        }

        copyButton.setOnClickListener {

        }

        cancelButton.setOnClickListener {
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