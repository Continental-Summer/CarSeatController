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

        setTouchWithImageChange(buttonTetieraSus, R.drawable.tetiera, "000071")
        setTouchWithImageChange(buttonTetieraJos, R.drawable.tetiera, "000081")

        setTouchWithImageChange(buttonSpatarFata, R.drawable.spatar, "000061")
        setTouchWithImageChange(buttonSpatarSpate, R.drawable.spatar, "000091")

        setTouchWithImageChange(buttonSezutFata, R.drawable.sezut, "000041")
        setTouchWithImageChange(buttonSezutSpate, R.drawable.sezut, "000051")
        setTouchWithImageChange(buttonSezutSus, R.drawable.sezut_full, "000031")
        setTouchWithImageChange(buttonSezutJos, R.drawable.sezut_full, "000011")

        setTouchWithImageChange(buttonScaunSus, R.drawable.scaun_verde, "000101")
        setTouchWithImageChange(buttonScaunJos, R.drawable.scaun_verde, "000121")
        setTouchWithImageChange(buttonScaunSpate, R.drawable.scaun_verde, "000111")
        setTouchWithImageChange(buttonScaunFata, R.drawable.scaun_verde, "000021")

        return view
    }

    private fun setTouchWithImageChange(
        button: ImageButton,
        pressedImageRes: Int,
        messageToSend: String
    ) {
        button.setOnTouchListener { _, event ->
            when (event.action) {
                android.view.MotionEvent.ACTION_DOWN -> {
                    seatImage.setImageResource(pressedImageRes)
                    sendStringToRaspberryPi(messageToSend)
                }

                android.view.MotionEvent.ACTION_UP, android.view.MotionEvent.ACTION_CANCEL -> {
                    seatImage.setImageResource(R.drawable.seat)
                }
            }
            false
        }
    }

    private fun sendStringToRaspberryPi(message: String) {
        try {
            BluetoothConnectionManager.socket?.outputStream?.write(message.toByteArray())
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }
}