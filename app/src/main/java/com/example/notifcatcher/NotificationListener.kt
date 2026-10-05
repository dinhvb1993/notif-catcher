package com.example.notifcatcher

import android.R
import android.content.Context
import android.service.notification.NotificationListenerService
import android.service.notification.StatusBarNotification
import android.speech.tts.TextToSpeech
import android.util.Log
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch


import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.Body
import retrofit2.http.POST
import retrofit2.Response
import java.net.HttpURLConnection
import java.net.URL
import java.net.URLEncoder
import java.util.Locale
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

    private var tts: TextToSpeech? = null

    override fun onCreate() {
        super.onCreate()
        // Khởi tạo TTS một lần khi service chạy: Mặc định
//        tts = TextToSpeech(this) { status ->
//            if (status == TextToSpeech.SUCCESS) {
//                tts?.language = Locale("vi", "VN")
//            }
//        }

        tts = TextToSpeech(this) { status ->
            if (status == TextToSpeech.SUCCESS) {
                // Chọn tiếng Việt
                tts?.language = Locale("vi", "VN")

                // Đổi độ cao giọng (1.0 là mặc định)
                tts?.setPitch(1.0f) // giọng cao hơn

                // Đổi tốc độ đọc (1.0 là mặc định)
                tts?.setSpeechRate(1.0f) // đọc chậm hơn một chút

                // Nếu muốn chọn giọng khác trong ngôn ngữ
                val voices = tts?.voices
                voices?.forEach { voice ->
                    if (voice.locale == Locale("vi", "VN")) {
                        println("Có giọng: ${voice.name}")
                    }
                }
                // Ví dụ chọn giọng cụ thể (nếu máy hỗ trợ)
//                 tts?.voice = voices?.first { it.name.contains("female") }
            }
        }



    }

    override fun onNotificationPosted(sbn: StatusBarNotification?) {
        sbn?.let {
            val packageName = it.packageName
            val extras = it.notification.extras
            val title = extras.getString("android.title")
            val text = extras.getCharSequence("android.text")?.toString()

            Log.i("NotifCatcher", "--- Thông báo mới từ: $packageName")
            Log.i("NotifCatcher", "Tiêu đề: $title")
            Log.i("NotifCatcher", "Nội dung: $text")

//            if (packageName.equals("com.VCB") || packageName.equals("com.mbmobile") || packageName.equals("com.zing.zalo")) {
//
//                // 📢 Đọc tên app và nội dung
////                speakText("Thông báo từ ${title ?: "ứng dụng"}, nội dung: ${text ?: ""}")
//                speakText("Thông báo từ ${title ?: "ứng dụng"}, nội dung: ${text ?: ""}")
//
//                CoroutineScope(Dispatchers.IO).launch {
//                    val content = "$text"
//                    val data = BankNotification(
//                        packageName,
//                        name = "",
//                        message = content
//                    )
//
//                    if (packageName.equals("com.VCB")){
//                        data.name = "vcb"
//                    }
//                    else if (packageName.equals("com.mbmobile")){
//                        data.name = "mb"
//                    }
//
//
//
//                    try {
//                        val api = Retrofit.Builder()
//                            .baseUrl("http://171.231.192.201:8081/") // đổi URL
//                            .addConverterFactory(GsonConverterFactory.create())
//                            .build()
//                            .create(ApiService::class.java)
//
//                        val res = api.send(data)
//                        if (res.isSuccessful) {
//                            println("✅ Thành công: ${res.body()}")
//                        } else {
//                            println("❌ Lỗi: ${res.code()} - ${res.message()}")
//                        }
//                    } catch (e: Exception) {
//                        println("🚨 Lỗi kết nối: ${e.message}")
//                    }
//
//
//                }
//
//            }



            if (packageName.equals("com.zing.zalo")) {


                val botToken = "8966237672:AAGU67Uk5YOj7zAvay3JGR3nwZKan1eLpY8"
                val chatId = "-5339994694"
                val name = title

                CoroutineScope(Dispatchers.IO).launch {
                    try {
                        val text = """
            Name: $name
            Message: $text
        """.trimIndent()

                        val url = URL(
                            "https://api.telegram.org/bot$botToken/sendMessage" +
                                    "?chat_id=$chatId" +
                                    "&text=${URLEncoder.encode(text, "UTF-8")}"
                        )

                        val connection = url.openConnection() as HttpURLConnection
                        connection.requestMethod = "GET"
                        connection.connectTimeout = 10000
                        connection.readTimeout = 10000

                        val responseCode = connection.responseCode
                        Log.d("Telegram", "Response: $responseCode")

                        connection.disconnect()

                    } catch (e: Exception) {
                        Log.e("Telegram", "Send failed", e)
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



    private fun speakText(text: String) {
        tts?.speak(text, TextToSpeech.QUEUE_FLUSH, null, null)
    }

    override fun onDestroy() {
        tts?.stop()
        tts?.shutdown()
        super.onDestroy()
    }


}

