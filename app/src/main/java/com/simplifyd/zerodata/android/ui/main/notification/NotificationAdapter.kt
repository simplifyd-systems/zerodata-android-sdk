package com.simplifyd.zerodata.android.ui.main.notification

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.simplifyd.zerodata.android.R
import com.simplifyd.zerodata.android.data.model.NotificationData
import com.simplifyd.zerodata.android.utils.AutoUpdateRecyclerView
import kotlinx.android.synthetic.main.notification_item.view.*
import kotlin.properties.Delegates

class NotificationAdapter(
    private val onNotificationClicked: (NotificationData) -> Unit
) : RecyclerView.Adapter<NotificationAdapter.NotificationViewHolder>(), AutoUpdateRecyclerView {

    var notifications: List<NotificationData> by Delegates.observable(emptyList()) { _, _, _ ->
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): NotificationViewHolder {
        return NotificationViewHolder(
            LayoutInflater.from(parent.context).inflate(R.layout.notification_item, parent, false)
        )
    }

    override fun getItemCount(): Int = notifications.size

    override fun onBindViewHolder(holder: NotificationViewHolder, position: Int) {
        holder.bind(notifications[position])
    }

    inner class NotificationViewHolder(view: View) : RecyclerView.ViewHolder(view) {

        fun bind(notification: NotificationData) {

            itemView.textViewTitle.text = notification.notification_name
            itemView.textViewContent.text = notification.notification_content

            itemView.setOnClickListener { onNotificationClicked(notification) }

        }

    }
}