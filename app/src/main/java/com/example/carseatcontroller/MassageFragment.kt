package com.example.carseatcontroller

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.NumberPicker
import android.widget.SeekBar
import android.widget.Switch
import androidx.fragment.app.Fragment

class MassageFragment : Fragment() {

    private lateinit var switchEnabled: Switch
    private lateinit var numberPickerSoftStrong: NumberPicker
    private lateinit var numberPickerNumbers: NumberPicker
    private lateinit var slider1: SeekBar
    private lateinit var slider2: SeekBar
    private lateinit var seatImage: ImageView

    private val softStrongValues = arrayOf("Soft", "Strong")

    private val treaptaCoduri = mapOf(
        1 to "1011",
        2 to "1021",
        3 to "1031",
        4 to "1041",
        5 to "1051",
        6 to "1061",
        7 to "1071",
        8 to "1081",
        9 to "1091",
        10 to "1101",
        11 to "1111",
        12 to "1121",
        13 to "1131",
        14 to "1141",
        15 to "1151"
    )

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_massage, container, false)

        numberPickerSoftStrong = view.findViewById(R.id.numberPickerSoftStrong)
        numberPickerNumbers = view.findViewById(R.id.numberPickerNumbers)
        slider1 = view.findViewById(R.id.slider1)
        slider2 = view.findViewById(R.id.slider2)
        seatImage = view.findViewById(R.id.seatImage)

        setupSoftStrongNumberPicker()
        setupNumbersNumberPicker()
        setupSliders()

        return view
    }

    private fun setupSoftStrongNumberPicker() {
        numberPickerSoftStrong.minValue = 0
        numberPickerSoftStrong.maxValue = softStrongValues.size - 1
        numberPickerSoftStrong.displayedValues = softStrongValues
        numberPickerSoftStrong.wrapSelectorWheel = false
        numberPickerSoftStrong.setOnValueChangedListener { _, _, newVal ->
            val selected = softStrongValues[newVal]
        }
    }

    private fun setupNumbersNumberPicker() {
        numberPickerNumbers.minValue = 0
        numberPickerNumbers.maxValue = 14
        numberPickerNumbers.wrapSelectorWheel = false
        numberPickerNumbers.setOnValueChangedListener { _, _, newVal ->
            val codTrimis = treaptaCoduri[newVal]
            codTrimis?.let {
                sendStringToRaspberryPi(it)
            }
        }
    }

    private fun setupSliders() {
        slider1.setOnSeekBarChangeListener(object : SeekBar.OnSeekBarChangeListener {
            override fun onProgressChanged(seekBar: SeekBar?, progress: Int, fromUser: Boolean) {
                val formattedValue = String.format("1161%02d", progress)
                sendStringToRaspberryPi(formattedValue)
            }

            override fun onStartTrackingTouch(seekBar: SeekBar?) {
                seatImage.setImageResource(R.drawable.pernite)
            }

            override fun onStopTrackingTouch(seekBar: SeekBar?) {
                seatImage.setImageResource(R.drawable.seat)
            }
        })

        slider2.setOnSeekBarChangeListener(object : SeekBar.OnSeekBarChangeListener {
            override fun onProgressChanged(seekBar: SeekBar?, progress: Int, fromUser: Boolean) {
                val formattedValue = String.format("1171%02d", progress)
                sendStringToRaspberryPi(formattedValue)
            }

            override fun onStartTrackingTouch(seekBar: SeekBar?) {
                seatImage.setImageResource(R.drawable.pernelaterale)
            }

            override fun onStopTrackingTouch(seekBar: SeekBar?) {
                seatImage.setImageResource(R.drawable.seat)
            }
        })
    }

    private fun sendStringToRaspberryPi(message: String) {
        try {
            BluetoothConnectionManager.socket?.outputStream?.write(message.toByteArray())
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }
}
