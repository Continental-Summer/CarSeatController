package com.example.carseatcontroller

import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.bottomnavigation.BottomNavigationView

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val bottomNavigationView = findViewById<BottomNavigationView>(R.id.bottom_navigation)

        bottomNavigationView.setOnItemSelectedListener { item ->
            when (item.itemId) {
                R.id.navigation_bluetooth -> {
                    Toast.makeText(this, "Bluetooth pressed", Toast.LENGTH_SHORT).show()
                    true
                }
                R.id.navigation_move -> {
                    Toast.makeText(this, "Move pressed", Toast.LENGTH_SHORT).show()
                    true
                }
                R.id.navigation_massage -> {
                    Toast.makeText(this, "Massage pressed", Toast.LENGTH_SHORT).show()
                    true
                }
                else -> false
            }
        }
    }
}
