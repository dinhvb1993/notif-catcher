package com.example.notifcatcher

import android.R
import android.service.notification.NotificationListenerService
import android.service.notification.StatusBarNotification
import android.util.Log
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch


import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.Body
import retrofit2.http.POST
import retrofit2.Response
import kotlin.Int


// Model
data class BankNotification(
//    val id: Int,
    var packageName: String,
    var name: String,
//    val amount: Int,
//    val type: String,
//    val content: String,
    val message: String
)

// API interface
interface ApiService {
    @POST("api-create-custom-bank-notification") // đổi path nếu cần
    suspend fun send(@Body data: BankNotification): Response<BankNotification>
}

class NotificationListener : NotificationListenerService() {

    override fun onNotificationPosted(sbn: StatusBarNotification?) {
        sbn?.let {
            val packageName = it.packageName
            val extras = it.notification.extras
            val title = extras.getString("android.title")
            val text = extras.getCharSequence("android.text")?.toString()

            Log.i("NotifCatcher", "--- Thông báo mới từ: $packageName")
            Log.i("NotifCatcher", "Tiêu đề: $title")
            Log.i("NotifCatcher", "Nội dung: $text")

            if (packageName.equals("com.VCB") || packageName.equals("com.mbmobile") || packageName.equals("com.zing.zalo")) {


                CoroutineScope(Dispatchers.IO).launch {
                    val content = "$text"
                    val data = BankNotification(
                        packageName,
                        name = "",
                        message = content
                    )

                    if (packageName.equals("com.VCB")){
                        data.name = "vcb"
                    }
                    else if (packageName.equals("com.mbmobile")){
                        data.name = "mb"
                    }



                    try {
                        val api = Retrofit.Builder()
                            .baseUrl("http://116.110.4.220:8081/") // đổi URL
                            .addConverterFactory(GsonConverterFactory.create())
                            .build()
                            .create(ApiService::class.java)

                        val res = api.send(data)
                        if (res.isSuccessful) {
                            println("✅ Thành công: ${res.body()}")
                        } else {
                            println("❌ Lỗi: ${res.code()} - ${res.message()}")
                        }
                    } catch (e: Exception) {
                        println("🚨 Lỗi kết nối: ${e.message}")
                    }


                }


            }
        }
    }

    override fun onNotificationRemoved(sbn: StatusBarNotification?) {
        sbn?.let {
            Log.i("NotifCatcher", "Thông báo bị xóa từ: ${it.packageName}")
        }
    }
}
