package com.simplifyd.zerodata.android.ui.main.notification

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.simplifyd.zerodata.android.R
import com.simplifyd.zerodata.android.data.model.NotificationData
import com.simplifyd.zerodata.android.utils.hideLoadingDialog
import com.simplifyd.zerodata.android.utils.showLoadingDialog
import com.simplifyd.zerodata.android.utils.showToast
import kotlinx.android.synthetic.main.fragment_notification.*
import kotlinx.android.synthetic.main.fragment_notification.swipeRefresh


class NotificationFragment : Fragment(R.layout.fragment_notification), (NotificationData) -> Unit {


    private val viewModel by viewModels<NotificationViewModel>()

    private val notificationAdapter by lazy { NotificationAdapter(this) }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        notificationRecyclerView.adapter = notificationAdapter

        observeGetNotifications()
        viewModel.fetchNotifications()
    }

    private fun showLoading(isLoading: Boolean) {
        swipeRefresh.isEnabled = isLoading
        swipeRefresh.isRefreshing = isLoading
    }

    override fun invoke(notification: NotificationData) {

    }

    private fun observeGetNotifications(){
        viewModel.fetchNotification.observe(viewLifecycleOwner) {

            showLoading(false)

            if (it != null) {

                notificationAdapter.notifications = it
            } else {
                showToast("You currently don't have any notifications")
            }
        }

        viewModel.isLoading.observe(viewLifecycleOwner){
            if (it){
                showLoading(true)
            }else{
                showLoading(false)
            }
        }

        viewModel.message.observe(viewLifecycleOwner){
            if (it != null) {
                showLoading(false)
                showToast(it)
            }
        }

    }


}