package com.example.helloworld

import android.content.Intent
import android.os.Bundle
import android.os.StrictMode
import android.os.StrictMode.ThreadPolicy
import android.view.View
import android.widget.CheckBox
import android.widget.EditText
import android.widget.ImageButton
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.RequestBody
import org.json.JSONObject
import java.text.DateFormat
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale


class MainActivity : AppCompatActivity() {
    private lateinit var imageButton: ImageButton
    private lateinit var editText: EditText
    private lateinit var checkBox: CheckBox
    private lateinit var textView: TextView
    var is_started: Boolean = false
    val EXTRA_MESSAGE = "com.example.helloworld.MESSAGE"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val policy = ThreadPolicy.Builder().permitAll().build()
        StrictMode.setThreadPolicy(policy)
        setContentView(R.layout.activity_main)
        imageButton = findViewById(R.id.Button)
        editText = findViewById(R.id.editTextNumber)
        checkBox = findViewById(R.id.checkBox)
    }

    fun sendData(view: View) {
        is_started = true
        val thread = Thread(Runnable {
                val currentDate = Date()
                val dateFormat: DateFormat = SimpleDateFormat("dd.MM.yyyy", Locale.getDefault())
                val dateText: String = dateFormat.format(currentDate)
                val timeFormat: DateFormat = SimpleDateFormat("HH:mm:ss", Locale.getDefault())
                val timeText: String = timeFormat.format(currentDate)
                val client = OkHttpClient()
                var j = JSONObject(mapOf("chat" to mapOf("a" to "get_history"))).toString()
                val mediaType = "application/json".toMediaTypeOrNull()
            var r=if (checkBox.isChecked){"yes"} else {"no"}
                var body = RequestBody.create(
                    mediaType,
                    JSONObject(
                        mapOf(
                            "data " + "${dateText}" to mapOf(
                                "time " + "${timeText}" to "pressure " + "${editText.text}",
                                "headache" to r
                            )
                        )
                    ).toString()
                )
                val request = Request.Builder()
                    .url("http://192.168.1.102:3002/addresses/q1/2")
                    .post(body)
                    .addHeader(
                        "cookie",
                        "caf_ipaddr=37.215.7.148; country=BY; city=%22Minsk%22; traffic_target=gd; _policy=%7B%22restricted_market%22%3Afalse%2C%22tracking_market%22%3A%22none%22%7D"
                    )
                    .addHeader("Content-Type", "application/json")
                    .addHeader("User-Agent", "insomnia/8.3.0")
                    .build()
                val response = client.newCall(request).execute()

                runOnUiThread(Runnable {
                    textView = findViewById(R.id.textView)
                    textView.setText("Отправлено")
                })
        })
        thread.start()
    }
    fun getSchedule(view: View){
        val intent:Intent= Intent(this, ScheduleActivity::class.java)
        val message = editText.text.toString()
        intent.putExtra(EXTRA_MESSAGE, message)
        startActivity(intent)
    }

    override fun onDestroy() {
        super.onDestroy()
        is_started = false
    }

}