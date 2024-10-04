package com.example.urlnasa

import android.annotation.SuppressLint
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.widget.Button
import android.widget.VideoView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat

class MainActivity2 : AppCompatActivity() {
    @SuppressLint("MissingInflatedId")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_main2)

        val button: Button = findViewById(R.id.button)
        val videoView: VideoView = findViewById(R.id.videoView2)

        // إعداد وتشغيل الفيديو التعليمي
        val videoUrl = "https://svs.gsfc.nasa.gov/vis/a000000/a004800/a004851/starmap_2020_tour_720p30.mp4"
        val videoUri = Uri.parse(videoUrl)
        videoView.setVideoURI(videoUri)
        videoView.setOnPreparedListener { mp -> mp.start() }


        // تعيين OnClickListener للزر
        button.setOnClickListener {
            // إنشاء Intent للانتقال إلى النشاط الثاني
            val intent = Intent(this, MainActivity::class.java)
            startActivity(intent) // بدء النشاط الثاني
        }

    }
}