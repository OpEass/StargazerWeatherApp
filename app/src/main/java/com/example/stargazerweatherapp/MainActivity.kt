package com.example.stargazerweatherapp

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.ImageButton
import android.widget.TextView
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.cardview.widget.CardView
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.core.widget.NestedScrollView

class MainActivity : AppCompatActivity() {

    private lateinit var locationResultLauncher: ActivityResultLauncher<Intent>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_main)

        setupWindowInsets()
        setupActivityResultLaunchers()
        setupClickListeners()
        setupScrollListener()
    }

    private fun setupActivityResultLaunchers() {
        locationResultLauncher = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
            if (result.resultCode == Activity.RESULT_OK) {
                val selectedCity = result.data?.getStringExtra("EXTRA_SELECTED_CITY")
                if (!selectedCity.isNullOrEmpty()) {
                    showToast("Kota diubah menjadi: $selectedCity")
                    // Mengubah tampilan nama kota di UI
                    findViewById<TextView>(R.id.tvCity)?.text = selectedCity
                    findViewById<TextView>(R.id.tvTopCity)?.text = selectedCity
                }
            }
        }
    }

    private fun setupWindowInsets() {
        val topBar = findViewById<View>(R.id.topBar)
        topBar?.let { view ->
            ViewCompat.setOnApplyWindowInsetsListener(view) { v, insets ->
                val statusBarInset = insets.getInsets(WindowInsetsCompat.Type.statusBars())
                v.setPadding(
                    v.paddingLeft,
                    statusBarInset.top + 12,
                    v.paddingRight,
                    v.paddingBottom
                )
                insets
            }
        }
    }

    private fun setupScrollListener() {
        val nestedScrollView = findViewById<NestedScrollView>(R.id.nestedScrollView)
        val tvTopCity = findViewById<TextView>(R.id.tvTopCity)
        val llLocation = findViewById<View>(R.id.llLocation)

        nestedScrollView?.setOnScrollChangeListener(NestedScrollView.OnScrollChangeListener { _, _, scrollY, _, _ ->
            val triggerHeight = llLocation?.height ?: 200
            val alphaProgress = (scrollY.toFloat() / triggerHeight).coerceIn(0f, 1f)

            tvTopCity?.alpha = alphaProgress
        })
    }

    private fun setupClickListeners() {
        findViewById<ImageButton>(R.id.btnRefresh)?.setOnClickListener {
            showToast("Memperbarui data cuaca...")
        }

        findViewById<ImageButton>(R.id.btnAdd)?.setOnClickListener {
            openLocationActivity()
        }

        findViewById<View>(R.id.llLocation)?.setOnClickListener {
            openLocationActivity()
        }

        findViewById<ImageButton>(R.id.btnSettings)?.setOnClickListener {
            val intent = Intent(this, SettingsActivity::class.java)
            startActivity(intent)
        }

        findViewById<CardView>(R.id.btnAddOutdoor)?.setOnClickListener {
            val intent = Intent(this, OutdoorActivity::class.java)
            startActivity(intent)
        }

        findViewById<CardView>(R.id.btnAddIndoor)?.setOnClickListener {
            val intent = Intent(this, IndoorActivity::class.java)
            startActivity(intent)
        }
    }

    private fun openLocationActivity() {
        val intent = Intent(this, LocationActivity::class.java)
        locationResultLauncher.launch(intent)
    }

    private fun showToast(message: String) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
    }
}