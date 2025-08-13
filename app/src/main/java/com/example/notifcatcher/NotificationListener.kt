package com.example.notifcatcher

import android.service.notification.NotificationListenerService
import android.service.notification.StatusBarNotification
import android.util.Log

class NotificationListener : NotificationListenerService() {

    override fun onNotificationPosted(sbn: StatusBarNotification?) {
        sbn?.let {
            val packageName = it.packageName
            val extras = it.notification.extras
            val title = extras.getString("android.title")
            val text = extras.getCharSequence("android.text")?.toString()

            Log.i("NotifCatcher", "Thông báo mới từ: $packageName")
            Log.i("NotifCatcher", "Tiêu đề: $title")
            Log.i("NotifCatcher", "Nội dung: $text")
        }
    }

    override fun onNotificationRemoved(sbn: StatusBarNotification?) {
        sbn?.let {
            Log.i("NotifCatcher", "Thông báo bị xóa từ: ${it.packageName}")
        }
    }
}
