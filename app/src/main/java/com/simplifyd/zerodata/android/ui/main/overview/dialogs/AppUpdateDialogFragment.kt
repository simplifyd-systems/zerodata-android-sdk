package com.simplifyd.zerodata.android.ui.main.overview.dialogs

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.DialogFragment
import com.simplifyd.zerodata.android.R
import kotlinx.android.synthetic.main.dialog_app_update.*
import kotlinx.android.synthetic.main.dialog_app_update.btnSubmit
class AppUpdateDialogFragment: DialogFragment(R.layout.dialog_app_update) {




    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        btnSubmit.setOnClickListener {

            val appPackageName = "com.simplifyd.zerodata.android"
            val intent = Intent(Intent.ACTION_VIEW).apply {
                data = Uri.parse(
                    "https://play.google.com/store/apps/details?id=$appPackageName")
                setPackage("com.android.vending")
            }
            startActivity(intent)

            dismiss()

        }

        ic_back.setOnClickListener {
            dismiss()
        }


    }

    override fun onStart() {
        super.onStart()
        dialog?.let {
            val width = ViewGroup.LayoutParams.MATCH_PARENT
            val height = ViewGroup.LayoutParams.MATCH_PARENT
            it.window?.setLayout(width, height)
            it.window?.setBackgroundDrawableResource(android.R.color.transparent)
        }
    }


    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
    }

    companion object {

        fun newInstance(): AppUpdateDialogFragment {
            val args = Bundle()
            val fragment = AppUpdateDialogFragment()
            fragment.arguments = args
            return fragment
        }


    }
}
