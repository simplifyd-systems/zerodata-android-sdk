package com.simplifyd.zerodata.android.ui.update

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import com.simplifyd.zerodata.android.R
import kotlinx.android.synthetic.main.fragment_update.*


class UpdateFragment : Fragment(R.layout.fragment_update) {

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

        }

    }
}