package com.simplifyd.zerodata.android.ui.main.notification

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.navArgs
import com.simplifyd.zerodata.android.R
import kotlinx.android.synthetic.main.fragment_notification_detail.*

class NotificationDetailFragment : Fragment(R.layout.fragment_notification_detail) {

    private val args: NotificationDetailFragmentArgs by navArgs()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        textViewTitle.text = args.notification.notification_name
        textViewContent.text = args.notification.notification_content
        btnSubmit.visibility = View.GONE
    }
}