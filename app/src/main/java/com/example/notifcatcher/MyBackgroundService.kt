package com.example.notifcatcher

import android.annotation.SuppressLint
import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.Service
import android.content.Intent
import android.os.Build
import android.os.IBinder
import android.util.Log
import androidx.core.app.NotificationCompat

class MyBackgroundService : Service() {

    override fun onCreate() {
        super.onCreate()
        Log.d("MyBackgroundService", "Service được tạo")
    }

    @SuppressLint("ForegroundServiceType")
    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        Log.d("MyBackgroundService", "Service bắt đầu chạy")

        val channelId = "notifcatcher_channel"
        val channelName = "NotifCatcher Background"

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channel = NotificationChannel(
                channelId, channelName, NotificationManager.IMPORTANCE_LOW
            )
            val manager = getSystemService(NotificationManager::class.java)
            manager.createNotificationChannel(channel)
        }

        val notification: Notification = NotificationCompat.Builder(this, channelId)
            .setContentTitle("NotifCatcher đang chạy nền")
            .setContentText("Service hoạt động sau khi khởi động máy")
            .setSmallIcon(android.R.drawable.ic_popup_sync)
            .build()

        startForeground(1, notification)

        // TODO: Xử lý công việc nền tại đây
        return START_STICKY
    }

    override fun onBind(intent: Intent?): IBinder? = null
}
