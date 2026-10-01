package com.example.stargazerweatherapp

import android.content.Context
import android.graphics.Color
import android.os.Bundle
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.inputmethod.EditorInfo
import android.view.inputmethod.InputMethodManager
import android.widget.EditText
import android.widget.GridLayout
import android.widget.ImageButton
import android.widget.LinearLayout
import android.widget.TextView
import android.widget.Toast
import androidx.activity.OnBackPressedCallback
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat

class LocationActivity : AppCompatActivity() {

    private lateinit var etSearchLocation: EditText
    private lateinit var btnCancelSearch: TextView
    private lateinit var llPopularCitiesContainer: LinearLayout
    private lateinit var llMainLocationsContainer: LinearLayout
    private lateinit var gridPopularCities: GridLayout
    private lateinit var llAddedLocations: LinearLayout

    private var isSearchActive = false

    // Daftar Kota Populer
    private val popularCities = listOf(
        "Tangerang", "Jakarta", "Denpasar",
        "Yogyakarta", "Bandung", "Surabaya",
        "Balikpapan", "Mataram", "Kupang",
        "Jambi", "Medan", "Pekanbaru",
        "Palu", "Manado", "Padang",
        "Makasar", "Pontianak", "Banjarmasin",
        "Lampung", "Palembang", "Batam"
    )

    // Data dummy lokasi yang ditambahkan (khusus preview UI front-end)
    private val staticDummyCities = listOf(
        CityModel("Jakarta", "Cerah Berawan", "28°", "34° / 24°"),
        CityModel("Bandung", "Hujan Ringan", "22°", "26° / 19°")
    )

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_location)

        setupWindowInsets()
        initViews()
        setupSearchLogic()
        setupBackPressHandling()
        renderPopularCitiesGrid()
        renderStaticAddedLocations()
    }

    private fun setupWindowInsets() {
        val headerBar = findViewById<View>(R.id.headerBar)
        headerBar?.let { view ->
            ViewCompat.setOnApplyWindowInsetsListener(view) { v, insets ->
                val statusBarInset = insets.getInsets(WindowInsetsCompat.Type.statusBars())
                v.setPadding(v.paddingLeft, statusBarInset.top, v.paddingRight, v.paddingBottom)
                insets
            }
        }
    }

    private fun initViews() {
        etSearchLocation = findViewById(R.id.etSearchLocation)
        btnCancelSearch = findViewById(R.id.btnCancelSearch)
        llPopularCitiesContainer = findViewById(R.id.llPopularCitiesContainer)
        llMainLocationsContainer = findViewById(R.id.llMainLocationsContainer)
        gridPopularCities = findViewById(R.id.gridPopularCities)
        llAddedLocations = findViewById(R.id.llAddedLocations)

        // Tombol Kembali di Header Bar
        findViewById<ImageButton>(R.id.btnBack)?.setOnClickListener {
            handleBackPress()
        }
    }

    private fun setupBackPressHandling() {
        onBackPressedDispatcher.addCallback(this, object : OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
                handleBackPress()
            }
        })
    }

    private fun handleBackPress() {
        if (isSearchActive) {
            closeSearchMode()
        } else {
            finish()
        }
    }

    private fun setupSearchLogic() {
        etSearchLocation.setOnFocusChangeListener { _, hasFocus ->
            if (hasFocus) {
                showPopularCitiesView(true)
            }
        }

        etSearchLocation.setOnClickListener {
            showPopularCitiesView(true)
        }

        btnCancelSearch.setOnClickListener {
            closeSearchMode()
        }

        // Ketika menekan tombol aksi pencarian di keyboard
        etSearchLocation.setOnEditorActionListener { _, actionId, _ ->
            if (actionId == EditorInfo.IME_ACTION_DONE || actionId == EditorInfo.IME_ACTION_SEARCH) {
                val cityName = etSearchLocation.text.toString().trim()
                if (cityName.isNotEmpty()) {
                    onCitySelected(cityName)
                }
                true
            } else {
                false
            }
        }
    }

    private fun closeSearchMode() {
        showPopularCitiesView(false)
        etSearchLocation.text.clear()
        etSearchLocation.clearFocus()
        hideKeyboard()
    }

    private fun showPopularCitiesView(show: Boolean) {
        isSearchActive = show
        if (show) {
            btnCancelSearch.visibility = View.VISIBLE
            llPopularCitiesContainer.visibility = View.VISIBLE
            llMainLocationsContainer.visibility = View.GONE
        } else {
            btnCancelSearch.visibility = View.GONE
            llPopularCitiesContainer.visibility = View.GONE
            llMainLocationsContainer.visibility = View.VISIBLE
        }
    }

    private fun renderPopularCitiesGrid() {
        gridPopularCities.removeAllViews()

        for (cityName in popularCities) {
            val textView = TextView(this).apply {
                text = cityName
                textSize = 14f
                gravity = Gravity.CENTER
                setPadding(0, 24, 0, 24)
                setTextColor(Color.parseColor("#111111"))
                background = ContextCompat.getDrawable(this@LocationActivity, R.drawable.bg_chip_city)

                val params = GridLayout.LayoutParams().apply {
                    width = 0
                    height = GridLayout.LayoutParams.WRAP_CONTENT
                    columnSpec = GridLayout.spec(GridLayout.UNDEFINED, 1f)
                    setMargins(8, 8, 8, 8)
                }
                layoutParams = params

                setOnClickListener {
                    onCitySelected(cityName)
                }
            }

            gridPopularCities.addView(textView)
        }
    }

    // Aksi saat memilih kota
    private fun onCitySelected(cityName: String) {
        Toast.makeText(this, "Memilih kota: $cityName", Toast.LENGTH_SHORT).show()
        closeSearchMode()
    }

    // Menampilkan item dummy secara statis untuk seksi "Lokasi yang ditambahkan"
    private fun renderStaticAddedLocations() {
        llAddedLocations.removeAllViews()
        val inflater = LayoutInflater.from(this)

        for (city in staticDummyCities) {
            val cardView = inflater.inflate(R.layout.item_city_card, llAddedLocations, false)

            cardView.findViewById<TextView>(R.id.tvCityName)?.text = city.name
            cardView.findViewById<TextView>(R.id.tvCityCondition)?.text = city.condition
            cardView.findViewById<TextView>(R.id.tvCityTemp)?.text = city.temp
            cardView.findViewById<TextView>(R.id.tvCityTempRange)?.text = city.tempRange

            llAddedLocations.addView(cardView)
        }
    }

    private fun hideKeyboard() {
        val imm = getSystemService(Context.INPUT_METHOD_SERVICE) as? InputMethodManager
        imm?.hideSoftInputFromWindow(etSearchLocation.windowToken, 0)
    }

    data class CityModel(
        val name: String,
        val condition: String,
        val temp: String,
        val tempRange: String
    )
}