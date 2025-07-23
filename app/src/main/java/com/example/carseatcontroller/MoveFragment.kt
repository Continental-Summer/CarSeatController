package com.example.carseatcontroller

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.ImageView
import androidx.fragment.app.Fragment

class MoveFragment : Fragment() {

    private lateinit var seatImage: ImageView

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_move, container, false)

        seatImage = view.findViewById(R.id.seatImage)

        val buttonTetieraSus = view.findViewById<ImageButton>(R.id.button_tetiera_sus)
        val buttonTetieraJos = view.findViewById<ImageButton>(R.id.button_tetiera_jos)
        val buttonSpatarFata = view.findViewById<ImageButton>(R.id.button_spatar_fata)
        val buttonSpatarSpate = view.findViewById<ImageButton>(R.id.button_spatar_spate)
        val buttonSezutFata = view.findViewById<ImageButton>(R.id.button_sezut_fata)
        val buttonSezutSpate = view.findViewById<ImageButton>(R.id.button_sezut_spate)
        val buttonSezutSus = view.findViewById<ImageButton>(R.id.button_sezut_sus)
        val buttonSezutJos = view.findViewById<ImageButton>(R.id.button_sezut_jos)
        val buttonScaunSus = view.findViewById<ImageButton>(R.id.button_scaun_sus)
        val buttonScaunJos = view.findViewById<ImageButton>(R.id.button_scaun_jos)
        val buttonScaunSpate = view.findViewById<ImageButton>(R.id.button_scaun_spate)
        val buttonScaunFata = view.findViewById<ImageButton>(R.id.button_scaun_fata)

        setTouchWithImageChange(buttonTetieraSus, R.drawable.tetiera)
        setTouchWithImageChange(buttonTetieraJos, R.drawable.tetiera)

        setTouchWithImageChange(buttonSpatarFata, R.drawable.spatar)
        setTouchWithImageChange(buttonSpatarSpate, R.drawable.spatar)

        setTouchWithImageChange(buttonSezutFata, R.drawable.sezut)
        setTouchWithImageChange(buttonSezutSpate, R.drawable.sezut)
        setTouchWithImageChange(buttonSezutSus, R.drawable.sezut_full)
        setTouchWithImageChange(buttonSezutJos, R.drawable.sezut_full)

        setTouchWithImageChange(buttonScaunSus, R.drawable.scaun_verde)
        setTouchWithImageChange(buttonScaunJos, R.drawable.scaun_verde)
        setTouchWithImageChange(buttonScaunSpate, R.drawable.scaun_verde)
        setTouchWithImageChange(buttonScaunFata, R.drawable.scaun_verde)

        return view
    }

    private fun setTouchWithImageChange(button: ImageButton, pressedImageRes: Int) {
        button.setOnTouchListener { _, event ->
            when (event.action) {
                android.view.MotionEvent.ACTION_DOWN -> seatImage.setImageResource(pressedImageRes)
                android.view.MotionEvent.ACTION_UP, android.view.MotionEvent.ACTION_CANCEL -> seatImage.setImageResource(R.drawable.seat)
            }
            false
        }
    }
}