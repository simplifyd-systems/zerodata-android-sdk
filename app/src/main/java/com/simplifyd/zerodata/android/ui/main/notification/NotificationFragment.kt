package com.simplifyd.zerodata.android.ui.main.notification

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.simplifyd.zerodata.android.R
import com.simplifyd.zerodata.android.data.model.NotificationData
import kotlinx.android.synthetic.main.fragment_notification.*


class NotificationFragment : Fragment(R.layout.fragment_notification), (NotificationData) -> Unit {


    private val viewModel by viewModels<NotificationViewModel>()

    private val notificationAdapter by lazy { NotificationAdapter(this) }



    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        notificationRecyclerView.adapter = notificationAdapter

        observeGetNotifications()
    }

    override fun invoke(notification: NotificationData) {
        TODO("Not yet implemented")
    }

    fun observeGetNotifications(){

    }


}