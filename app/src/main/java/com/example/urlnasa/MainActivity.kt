package com.example.urlnasa

import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.net.Uri
import android.os.Bundle
import android.widget.TextView
import android.widget.Toast
import android.widget.VideoView
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.AppCompatImageButton
import androidx.core.graphics.drawable.toBitmap
import com.bumptech.glide.Glide
import io.getstream.photoview.OnPhotoTapListener
import io.getstream.photoview.PhotoView
import org.json.JSONArray

class MainActivity : AppCompatActivity() {

    private lateinit var photoView: PhotoView
    private lateinit var infoTextView: TextView
    private lateinit var starsArray: JSONArray
    private val selectedStars = mutableListOf<Star>() // لتخزين النجوم التي يختارها المستخدم
    private var isDrawing = false // حالة الرسم

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        // إعداد مكونات واجهة المستخدم
        photoView = findViewById(R.id.photoView)
        infoTextView = findViewById(R.id.infoTextView)

        // تحميل وعرض صورة الخريطة النجمية باستخدام Glide
        loadStarMapImage()


        // تحميل بيانات النجوم من ملف JSON
        val jsonString = assets.open("stars.json").bufferedReader().use { it.readText() }
        starsArray = JSONArray(jsonString)

        // رسم النجوم على الصورة بعد تحميلها
        photoView.post {
            drawStarsOnImage()
        }

        // تفاعل المستخدم مع الخريطة وعرض معلومات النجوم
        photoView.setOnPhotoTapListener(OnPhotoTapListener { view, x, y ->
            val clickedStar = findStarAtPosition(x, y)
            clickedStar?.let {
                selectedStars.add(it) // إضافة النجم إلى قائمة النجوم المختارة
                infoTextView.text = "Star Selected: ${it.name}\nTotal Selected: ${selectedStars.size}"
                drawConstellation() // رسم الأبراج
            } ?: run {
                Toast.makeText(this, "No star found at this position", Toast.LENGTH_SHORT).show()
            }
        })

        // إعداد الأزرار
        val buttonDraw: AppCompatImageButton = findViewById(R.id.buttonDraw)
        buttonDraw.setOnClickListener {
            isDrawing = true // تفعيل وضع الرسم
        }

        val buttonClear: AppCompatImageButton = findViewById(R.id.buttonClear)
        buttonClear.setOnClickListener {
            clearDrawing() // مسح الرسم
        }
    }

    // وظيفة للعثور على النجم بناءً على إحداثيات النقطة
    private fun findStarAtPosition(x: Float, y: Float): Star? {
        for (i in 0 until starsArray.length()) {
            val star = starsArray.getJSONObject(i)
            val starX = star.getDouble("x")
            val starY = star.getDouble("y")
            val name = star.getString("name")
            val brightness = star.getDouble("brightness")

            // حساب المسافة بين النجم والنقطة التي تم النقر عليها
            val distance = Math.sqrt(Math.pow((x - starX).toDouble(), 2.0) + Math.pow((y - starY).toDouble(), 2.0))
            if (distance < 0.05) { // إذا كانت النقطة قريبة بما يكفي من النجم
                return Star(name, brightness, starX, starY)
            }
        }
        return null
    }

    // رسم النجوم على الصورة
    private fun drawStarsOnImage() {
        // تحقق من أن الصورة قد تم تحميلها
        val drawable = photoView.drawable ?: return
        val bitmap = drawable.toBitmap().copy(Bitmap.Config.ARGB_8888, true) // تحويل الصورة إلى Bitmap قابلة للتعديل
        val canvas = Canvas(bitmap)

        for (i in 0 until starsArray.length()) {
            val star = starsArray.getJSONObject(i)
            val starX = star.getDouble("x")
            val starY = star.getDouble("y")
            val brightness = star.getDouble("brightness")

            // اختيار لون النجم بناءً على سطوعه
            val paint = Paint().apply {
                color = when {
                    brightness < 1 -> Color.CYAN // نجوم ذات سطوع عالٍ
                    brightness < 2 -> Color.WHITE // نجوم متوسطة السطوع
                    else -> Color.RED // نجوم ذات سطوع منخفض
                }
                strokeWidth = 10f
                style = Paint.Style.FILL
            }

            // حساب الإحداثيات النسبية داخل الصورة
            val x = (starX * bitmap.width).toFloat()
            val y = (starY * bitmap.height).toFloat()

            // رسم دائرة صغيرة تمثل النجم
            canvas.drawCircle(x, y, 5f, paint)
        }

        // تحديث الصورة لعرض النجوم
        photoView.setImageBitmap(bitmap)
    }

    // رسم الأبراج من النجوم المختارة
    private fun drawConstellation() {
        val paint = Paint().apply {
            color = Color.WHITE
            strokeWidth = 2f
        }

        // التأكد من أن الصورة قد تم تحميلها
        val drawable = photoView.drawable ?: return
        val bitmap = drawable.toBitmap().copy(Bitmap.Config.ARGB_8888, true) // تحويل الصورة إلى Bitmap
        val canvas = Canvas(bitmap)

        for (i in 0 until selectedStars.size - 1) {
            val star1 = selectedStars[i]
            val star2 = selectedStars[i + 1]

            val x1 = (star1.x * bitmap.width).toFloat()
            val y1 = (star1.y * bitmap.height).toFloat()
            val x2 = (star2.x * bitmap.width).toFloat()
            val y2 = (star2.y * bitmap.height).toFloat()

            canvas.drawLine(x1, y1, x2, y2, paint) // رسم خط بين النجمتين
        }

        photoView.setImageBitmap(bitmap) // تحديث الصورة بالرسم الجديد
    }

    // وظيفة لمسح الرسم
    private fun clearDrawing() {
        // إعادة تحميل الصورة الأصلية
        loadStarMapImage()
        selectedStars.clear() // مسح النجوم المختارة
        infoTextView.text = "" // إعادة تعيين نص المعلومات
    }

    // وظيفة لتحميل صورة الخريطة النجمية
    private fun loadStarMapImage() {
        val imageUrl = "https://svs.gsfc.nasa.gov/vis/a000000/a004800/a004851/hiptyc_2020_4k_print.jpg"
        Glide.with(this)
            .load(imageUrl)
            .error(R.drawable.mmmmmm)
            .into(photoView)
    }
}

// كلاس يحتوي على معلومات النجم
data class Star(val name: String, val brightness: Double, val x: Double, val y: Double)
