package com.example.helloworld

import android.os.Bundle
import android.view.View
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity
import com.google.gson.Gson
import okhttp3.Call
import okhttp3.Callback
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.Response
import java.io.IOException


class ScheduleActivity : AppCompatActivity() {
    private lateinit var button: Button
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_schedule)
        button=findViewById(R.id.button2)
    }
    fun sendData2(view: View) {
//        val thread = Thread(Runnable {
            val client = OkHttpClient()
            val request = Request.Builder()
                .url("http://192.168.1.102:3002/addresses/q2")
                .build()
        val gson = Gson()
        val responseBody = client.newCall(request).execute().body
        val entity: DTO = gson.fromJson(responseBody!!.string(), DTO::class.java)
        client.newCall(request).enqueue(object : Callback {
            override fun onFailure(call: Call, e: IOException) {

                e.printStackTrace()
            }
            override fun onResponse(call: Call, response: Response) {
                response.use {
                    if (!response.isSuccessful) {
                        throw IOException("Запрос к серверу не был успешен:" +
                                " ${response.code} ${response.message}")
                    }
                    for ((name, value) in response.headers) {
                        println("$name: $value")
                    }
                    println(response.body!!.string())
                }
            }
        })
    }
}