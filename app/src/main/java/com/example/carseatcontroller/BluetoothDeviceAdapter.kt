package com.example.carseatcontroller

import android.bluetooth.BluetoothDevice
import android.graphics.Color
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class BluetoothDeviceAdapter(
    private val devices: List<BluetoothDevice>,
    private val onPairClick: (BluetoothDevice) -> Unit,
    private val onDeviceClick: (BluetoothDevice) -> Unit
) : RecyclerView.Adapter<BluetoothDeviceAdapter.DeviceViewHolder>() {

    inner class DeviceViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val tvDeviceName: TextView = view.findViewById(R.id.tvDeviceName)
        val btnPair: Button = view.findViewById(R.id.btnPair)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): DeviceViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_bluetooth_device, parent, false)
        return DeviceViewHolder(view)
    }

    override fun onBindViewHolder(holder: DeviceViewHolder, position: Int) {
        val device = devices[position]
        holder.tvDeviceName.text = device.name ?: "Unknown Device"
        holder.btnPair.text = if (device.bondState == BluetoothDevice.BOND_BONDED) "Unpair" else "Pair"
        holder.tvDeviceName.setTextColor(
            if (device.bondState == BluetoothDevice.BOND_BONDED)
                Color.parseColor("#FFA500")
            else Color.WHITE
        )

        holder.btnPair.setOnClickListener {
            onPairClick(device)
        }

        holder.tvDeviceName.setOnClickListener {
            onDeviceClick(device)
        }
    }

    override fun getItemCount(): Int = devices.size
}