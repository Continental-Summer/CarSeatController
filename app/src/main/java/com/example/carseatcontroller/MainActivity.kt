package com.example.carseatcontroller

import android.os.Bundle
import android.view.View
import android.widget.ImageView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.bottomnavigation.BottomNavigationView

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val bottomNavigationView = findViewById<BottomNavigationView>(R.id.bottom_navigation)
        val seatImage = findViewById<ImageView>(R.id.seatImage)
        seatImage.visibility = View.GONE

        bottomNavigationView.setOnItemSelectedListener { item ->
            when (item.itemId) {
                R.id.navigation_bluetooth -> {
                    seatImage.setVisibility(View.GONE)
                    true
                }
                R.id.navigation_move -> {
                    seatImage.setVisibility(View.VISIBLE)
                    true
                }
                R.id.navigation_massage -> {
                    seatImage.setVisibility(View.VISIBLE)
                    true
                }
                else -> false
            }
        }
    }
}
