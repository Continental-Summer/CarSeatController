package com.example.carseatcontroller

import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.View
import android.view.MotionEvent
import android.widget.ImageButton
import android.widget.ImageView
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.bottomnavigation.BottomNavigationView

class MainActivity : AppCompatActivity() {

    private lateinit var seatImage: ImageView
    private val handler = Handler(Looper.getMainLooper())

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val bottomNavigationView = findViewById<BottomNavigationView>(R.id.bottom_navigation)
        seatImage = findViewById<ImageView>(R.id.seatImage)

        val buttonTetieraSus = findViewById<ImageButton>(R.id.button_tetiera_sus)
        val buttonTetieraJos = findViewById<ImageButton>(R.id.button_tetiera_jos)
        val buttonSpatarFata = findViewById<ImageButton>(R.id.button_spatar_fata)
        val buttonSpatarSpate = findViewById<ImageButton>(R.id.button_spatar_spate)
        val buttonSezutFata = findViewById<ImageButton>(R.id.button_sezut_fata)
        val buttonSezutSpate = findViewById<ImageButton>(R.id.button_sezut_spate)
        val buttonSezutSus = findViewById<ImageButton>(R.id.button_sezut_sus)
        val buttonSezutJos = findViewById<ImageButton>(R.id.button_sezut_jos)
        val buttonScaunSus = findViewById<ImageButton>(R.id.button_scaun_sus)
        val buttonScaunJos = findViewById<ImageButton>(R.id.button_scaun_jos)
        val buttonScaunSpate = findViewById<ImageButton>(R.id.button_scaun_spate)
        val buttonScaunFata = findViewById<ImageButton>(R.id.button_scaun_fata)

        val buttonsGroup = findViewById<androidx.constraintlayout.widget.Group>(R.id.buttonsGroup)

        seatImage.visibility = View.GONE
        buttonsGroup.visibility = View.GONE

        bottomNavigationView.setOnItemSelectedListener { item ->
            when (item.itemId) {
                R.id.navigation_bluetooth -> {
                    seatImage.visibility = View.GONE
                    buttonsGroup.visibility = View.GONE
                    true
                }
                R.id.navigation_move -> {
                    seatImage.visibility = View.VISIBLE
                    buttonsGroup.visibility = View.VISIBLE
                    true
                }
                R.id.navigation_massage -> {
                    seatImage.visibility = View.VISIBLE
                    buttonsGroup.visibility = View.GONE
                    true
                }
                else -> false
            }
        }

        setTouchWithImageChange(buttonTetieraSus, R.drawable.tetiera)
        setTouchWithImageChange(buttonTetieraJos, R.drawable.tetiera)

        setTouchWithImageChange(buttonSpatarFata, R.drawable.spatar)
        setTouchWithImageChange(buttonSpatarSpate, R.drawable.spatar)

        setTouchWithImageChange(buttonSezutFata, R.drawable.sezut)
        setTouchWithImageChange(buttonSezutSpate, R.drawable.sezut)
        setTouchWithImageChange(buttonSezutSus, R.drawable.sezut)
        setTouchWithImageChange(buttonSezutJos, R.drawable.sezut)

        setTouchWithImageChange(buttonScaunSus, R.drawable.scaun_verde)
        setTouchWithImageChange(buttonScaunJos, R.drawable.scaun_verde)
        setTouchWithImageChange(buttonScaunSpate, R.drawable.scaun_verde)
        setTouchWithImageChange(buttonScaunFata, R.drawable.scaun_verde)
    }

    private fun setTouchWithImageChange(button: ImageButton, pressedImageRes: Int) {
        button.setOnTouchListener { _, event ->
            when (event.action) {
                MotionEvent.ACTION_DOWN -> seatImage.setImageResource(pressedImageRes)
                MotionEvent.ACTION_UP, MotionEvent.ACTION_CANCEL -> seatImage.setImageResource(R.drawable.seat)
            }
            false
        }
    }
}