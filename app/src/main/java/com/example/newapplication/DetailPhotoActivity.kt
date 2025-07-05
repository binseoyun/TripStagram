package com.example.newapplication

import android.os.Bundle
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity

class DetailPhotoActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_photo_detail)

        val text = intent.getStringExtra("itemText")

        val textView = findViewById<TextView>(R.id.texta)
        textView.text = text ?: "No data received"
    }
}