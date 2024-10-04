package com.example.urlnasa

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.LinearLayout
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat

class MainList : AppCompatActivity() {
    @SuppressLint("MissingInflatedId")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_main_list)

        val bluGo: LinearLayout = findViewById(R.id.bluGo)

        // تعيين OnClickListener للزر
        bluGo.setOnClickListener {
            // إنشاء Intent للانتقال إلى النشاط الثاني
            val intent = Intent(this, MainActivity2::class.java)
            startActivity(intent) // بدء النشاط الثاني
        }


    }
}