package com.example.stargazerweatherapp

import android.os.Bundle
import android.widget.ImageButton
import android.widget.RelativeLayout
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat

class SettingsActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_settings)

        val mainLayout = findViewById<android.view.View>(android.R.id.content)
        mainLayout?.let { view ->
            ViewCompat.setOnApplyWindowInsetsListener(view) { v, insets ->
                val statusBarInset = insets.getInsets(WindowInsetsCompat.Type.statusBars())
                v.setPadding(0, statusBarInset.top, 0, 0)
                insets
            }
        }

        setupClickListeners()
    }

    private fun setupClickListeners() {
        findViewById<ImageButton>(R.id.btnBack)?.setOnClickListener {
            finish()
        }

        findViewById<RelativeLayout>(R.id.btnSatuanSuhu)?.setOnClickListener {
            showToast("Pengaturan Satuan Suhu")
        }

        findViewById<RelativeLayout>(R.id.btnSatuanAngin)?.setOnClickListener {
            showToast("Pengaturan Kecepatan Angin")
        }

        findViewById<RelativeLayout>(R.id.btnSatuanTekanan)?.setOnClickListener {
            showToast("Pengaturan Tekanan Atmosfer")
        }

        findViewById<RelativeLayout>(R.id.btnMasukan)?.setOnClickListener {
            showToast("Membuka Masukan")
        }

        findViewById<RelativeLayout>(R.id.btnPrivasi)?.setOnClickListener {
            showToast("Membuka Kebijakan Privasi")
        }
    }

    private fun showToast(message: String) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
    }
}