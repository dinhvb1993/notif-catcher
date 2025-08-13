package com.example.notifcatcher

import android.content.ComponentName
import android.content.Intent
import android.os.Bundle
import android.provider.Settings
import android.service.notification.NotificationListenerService
import androidx.activity.ComponentActivity


class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // Mở màn hình xin quyền Notification Access
        val intent = Intent(Settings.ACTION_NOTIFICATION_LISTENER_SETTINGS)
        startActivity(intent)

        // Đóng app ngay vì không cần UI
//        finish()
    }
}


//class MainActivity : ComponentActivity() {
//    override fun onCreate(savedInstanceState: Bundle?) {
//        super.onCreate(savedInstanceState)
//
//        if (!isNotificationServiceEnabled()) {
//            // Chưa có quyền → mở màn hình cấp quyền
//            val intent = Intent(Settings.ACTION_NOTIFICATION_LISTENER_SETTINGS)
//            startActivity(intent)
//        } else {
//            // Đã có quyền → bind lại service để nhận thông báo
//            NotificationListenerService.requestRebind(
//                ComponentName(this, NotificationListener::class.java)
//            )
//        }
//
//        // Đóng app vì không cần UI
//        finish()
//    }
//
//    private fun isNotificationServiceEnabled(): Boolean {
//        val pkgName = packageName
//        val flat = Settings.Secure.getString(
//            contentResolver,
//            "enabled_notification_listeners"
//        )
//        return flat?.contains(pkgName) == true
//    }
//}