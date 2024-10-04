package com.example.urlnasa

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat

class MainSplash : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_main_splash)

        // استخدام Handler لتأخير 3000 مللي ثانية (3 ثوانٍ)
        Handler(Looper.getMainLooper()).postDelayed({
            // الانتقال إلى النشاط الرئيسي (MainActivity)
            val intent = Intent(this, MainList::class.java)
            startActivity(intent)
            finish() // لإنهاء النشاط الحالي وعدم العودة إليه
        }, 3000) // تأخير 3000 مللي ثانية

    }
}