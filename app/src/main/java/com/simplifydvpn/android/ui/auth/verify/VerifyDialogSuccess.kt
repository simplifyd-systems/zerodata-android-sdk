package com.simplifydvpn.android.ui.auth.verify

import android.app.Activity
import android.app.Dialog
import android.content.Intent
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.view.Window
import android.view.WindowManager
import android.widget.ImageView
import com.google.android.material.button.MaterialButton
import com.simplifydvpn.android.R
import com.simplifydvpn.android.ui.main.MainActivity

@Suppress("RECEIVER_NULLABILITY_MISMATCH_BASED_ON_JAVA_ANNOTATIONS")
class VerifyDialogSuccess(private val activity: Activity, val type: Int = 0) {


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
        dialog!!.setContentView(
            R.layout.dialog_verification_successful
        )
    }

    private fun bindViews() {
        val closeButton = dialog!!.findViewById<MaterialButton>(R.id.btnSubmit)
        val backButton = dialog!!.findViewById<ImageView>(R.id.ic_back)
        closeButton.setOnClickListener { v ->
            dialog!!.dismiss()
            if (type != 0) {
                startMainActivity()
            }
            activity.finish()

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
