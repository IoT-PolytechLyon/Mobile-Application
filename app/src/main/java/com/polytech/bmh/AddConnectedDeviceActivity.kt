package com.polytech.bmh

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.ImageView
import com.polytech.bmh.ui.login.LoginActivity

class AddConnectedDeviceActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_connected_device)

        val backArrow = findViewById<ImageView>(R.id.imageView2)

        backArrow.setOnClickListener {
            val ChoiceConnectedDeviceActivityIntent = Intent(this, ChoiceConnectedDeviceActivity::class.java)
            startActivity(ChoiceConnectedDeviceActivityIntent)
        }
    }
}