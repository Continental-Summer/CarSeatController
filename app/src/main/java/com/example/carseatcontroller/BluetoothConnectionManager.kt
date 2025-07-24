package com.example.carseatcontroller

import android.bluetooth.BluetoothSocket

object BluetoothConnectionManager {
    var socket: BluetoothSocket? = null

    fun sendString(message: String) {
        Thread {
            try {
                socket?.outputStream?.write(message.toByteArray())
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }.start()
    }
}