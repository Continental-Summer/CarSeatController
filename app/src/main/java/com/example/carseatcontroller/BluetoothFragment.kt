package com.example.carseatcontroller

import android.Manifest
import android.bluetooth.*
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.*
import android.widget.Button
import android.widget.Switch
import android.widget.TextView
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import java.io.IOException
import java.util.*

class BluetoothFragment : Fragment() {

    companion object {
        private const val TAG = "BluetoothFragment"
        private const val REQUEST_ENABLE_BT = 1001
        private const val REQUEST_PERMISSIONS = 1002
        private val SPP_UUID: UUID = UUID.fromString("00001101-0000-1000-8000-00805F9B34FB")
    }

    private lateinit var bluetoothAdapter: BluetoothAdapter
    private lateinit var switchBluetooth: Switch
    private lateinit var rvDevices: RecyclerView
    private lateinit var adapter: BluetoothDeviceAdapter
    private val deviceList = mutableListOf<BluetoothDevice>()

    private var connectionThread: BluetoothConnectionThread? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_bluetooth, container, false)

        bluetoothAdapter = BluetoothAdapter.getDefaultAdapter()

        switchBluetooth = view.findViewById(R.id.switchBluetooth)
        rvDevices = view.findViewById(R.id.rvDevices)

        adapter = BluetoothDeviceAdapter(deviceList,
            onNameClick = { device -> connectToDevice(device) },
            onPairClick = { device -> togglePairing(device) })
        rvDevices.layoutManager = LinearLayoutManager(requireContext())
        rvDevices.adapter = adapter

        switchBluetooth.isChecked = bluetoothAdapter.isEnabled
        switchBluetooth.setOnCheckedChangeListener { _, isChecked ->
            if (isChecked) enableBluetooth() else disableBluetooth()
        }

        checkAndRequestPermissions()
        registerReceivers()

        if (bluetoothAdapter.isEnabled) {
            loadPairedDevices()
            startDiscovery()
        }

        return view
    }

    private fun checkAndRequestPermissions() {
        val permissions = mutableListOf<String>()
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
            if (ContextCompat.checkSelfPermission(requireContext(), Manifest.permission.BLUETOOTH_SCAN) != PackageManager.PERMISSION_GRANTED)
                permissions.add(Manifest.permission.BLUETOOTH_SCAN)
            if (ContextCompat.checkSelfPermission(requireContext(), Manifest.permission.BLUETOOTH_CONNECT) != PackageManager.PERMISSION_GRANTED)
                permissions.add(Manifest.permission.BLUETOOTH_CONNECT)
            if (ContextCompat.checkSelfPermission(requireContext(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED)
                permissions.add(Manifest.permission.ACCESS_FINE_LOCATION)
        } else {
            if (ContextCompat.checkSelfPermission(requireContext(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED)
                permissions.add(Manifest.permission.ACCESS_FINE_LOCATION)
        }
        if (permissions.isNotEmpty()) {
            requestPermissions(permissions.toTypedArray(), REQUEST_PERMISSIONS)
        }
    }

    private fun enableBluetooth() {
        if (!bluetoothAdapter.isEnabled) {
            val enableBtIntent = Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE)
            startActivityForResult(enableBtIntent, REQUEST_ENABLE_BT)
        }
    }

    private fun disableBluetooth() {
        if (bluetoothAdapter.isEnabled) {
            bluetoothAdapter.disable()
            deviceList.clear()
            adapter.notifyDataSetChanged()
            disconnectConnection()
        }
    }

    private fun loadPairedDevices() {
        deviceList.clear()
        bluetoothAdapter.bondedDevices?.let { deviceList.addAll(it) }
        adapter.notifyDataSetChanged()
    }

    private fun startDiscovery() {
        if (bluetoothAdapter.isDiscovering) bluetoothAdapter.cancelDiscovery()
        bluetoothAdapter.startDiscovery()
    }

    private val discoveryReceiver = object : BroadcastReceiver() {
        override fun onReceive(context: Context?, intent: Intent?) {
            when (intent?.action) {
                BluetoothDevice.ACTION_FOUND -> {
                    val device: BluetoothDevice? = intent.getParcelableExtra(BluetoothDevice.EXTRA_DEVICE)
                    device?.let {
                        if (!deviceList.contains(it) && it.name != null) {
                            deviceList.add(it)
                            adapter.notifyDataSetChanged()
                        }
                    }
                }
                BluetoothAdapter.ACTION_DISCOVERY_FINISHED -> {
                    Toast.makeText(requireContext(), "Discovery finished", Toast.LENGTH_SHORT).show()
                }
                BluetoothAdapter.ACTION_STATE_CHANGED -> {
                    val state = intent.getIntExtra(BluetoothAdapter.EXTRA_STATE, BluetoothAdapter.ERROR)
                    if (state == BluetoothAdapter.STATE_OFF) {
                        deviceList.clear()
                        adapter.notifyDataSetChanged()
                        switchBluetooth.isChecked = false
                        disconnectConnection()
                    } else if (state == BluetoothAdapter.STATE_ON) {
                        loadPairedDevices()
                        startDiscovery()
                        switchBluetooth.isChecked = true
                    }
                }
                BluetoothDevice.ACTION_BOND_STATE_CHANGED -> {
                    val device: BluetoothDevice? = intent.getParcelableExtra(BluetoothDevice.EXTRA_DEVICE)
                    val bondState = intent.getIntExtra(BluetoothDevice.EXTRA_BOND_STATE, BluetoothDevice.BOND_NONE)
                    device?.let {
                        adapter.notifyDataSetChanged()
                        if (bondState == BluetoothDevice.BOND_BONDED) {
                            Toast.makeText(requireContext(), "Paired with ${device.name}", Toast.LENGTH_SHORT).show()
                        } else if (bondState == BluetoothDevice.BOND_NONE) {
                            Toast.makeText(requireContext(), "Unpaired from ${device.name}", Toast.LENGTH_SHORT).show()
                        }
                    }
                }
            }
        }
    }

    private fun registerReceivers() {
        val filter = IntentFilter().apply {
            addAction(BluetoothDevice.ACTION_FOUND)
            addAction(BluetoothAdapter.ACTION_DISCOVERY_FINISHED)
            addAction(BluetoothAdapter.ACTION_STATE_CHANGED)
            addAction(BluetoothDevice.ACTION_BOND_STATE_CHANGED)
        }
        requireContext().registerReceiver(discoveryReceiver, filter)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        try {
            requireContext().unregisterReceiver(discoveryReceiver)
        } catch (e: Exception) {
            // ignore if already unregistered
        }
        disconnectConnection()
    }

    private fun togglePairing(device: BluetoothDevice) {
        try {
            if (device.bondState == BluetoothDevice.BOND_BONDED) {
                val method = device.javaClass.getMethod("removeBond")
                method.invoke(device)
            } else if (device.bondState == BluetoothDevice.BOND_NONE) {
                device.createBond()
            }
        } catch (e: Exception) {
            e.printStackTrace()
            Toast.makeText(requireContext(), "Pairing error: ${e.message}", Toast.LENGTH_SHORT).show()
        }
    }

    private fun connectToDevice(device: BluetoothDevice) {
        Thread {
            try {
                if (bluetoothAdapter.isDiscovering) bluetoothAdapter.cancelDiscovery()

                disconnectConnection()

                Log.d(TAG, "Creating socket to device: ${device.name}")

                val socket = device.createRfcommSocketToServiceRecord(SPP_UUID)

                Log.d(TAG, "Connecting to socket...")
                socket.connect()
                Log.d(TAG, "Connected!")

                connectionThread = BluetoothConnectionThread(socket)
                connectionThread?.start()

                requireActivity().runOnUiThread {
                    Toast.makeText(requireContext(), "Connected to ${device.name}", Toast.LENGTH_SHORT).show()
                    adapter.notifyDataSetChanged()
                }

            } catch (e: Exception) {
                Log.e(TAG, "Connection failed", e)
                requireActivity().runOnUiThread {
                    Toast.makeText(requireContext(), "Connection failed: ${e.message}", Toast.LENGTH_LONG).show()
                }
            }
        }.start()
    }

    private fun disconnectConnection() {
        connectionThread?.cancel()
        connectionThread = null
        requireActivity().runOnUiThread {
            adapter.notifyDataSetChanged()
        }
    }

    // Trimite comenzi text catre Raspberry Pi
    fun sendCommand(command: String) {
        connectionThread?.write("$command\n")
    }

    // Thread pentru comunicatia Bluetooth
    private inner class BluetoothConnectionThread(val socket: BluetoothSocket) : Thread() {
        private val inputStream = socket.inputStream
        private val outputStream = socket.outputStream
        @Volatile private var running = true

        override fun run() {
            val buffer = ByteArray(1024)
            var bytes: Int
            val sb = StringBuilder()

            try {
                while (running) {
                    bytes = inputStream.read(buffer)
                    if (bytes > 0) {
                        val received = String(buffer, 0, bytes, Charsets.UTF_8)
                        sb.append(received)
                        // Procesare pe linii
                        var lineEndIndex = sb.indexOf("\n")
                        while (lineEndIndex != -1) {
                            val line = sb.substring(0, lineEndIndex).trim()
                            sb.delete(0, lineEndIndex + 1)
                            Log.d(TAG, "Received line: $line")
                            // Aici poti procesa mesajele primite de la Raspberry Pi
                        }
                    }
                }
            } catch (e: IOException) {
                Log.e(TAG, "Connection lost", e)
            } finally {
                try {
                    socket.close()
                } catch (e: IOException) { }
            }
        }

        fun write(data: String) {
            try {
                val message = if (data.endsWith("\n")) data else "$data\n"
                outputStream.write(message.toByteArray(Charsets.UTF_8))
                outputStream.flush()
                Log.d(TAG, "Sent: $message")
            } catch (e: IOException) {
                Log.e(TAG, "Write failed", e)
            }
        }

        fun cancel() {
            running = false
            try {
                socket.close()
            } catch (e: IOException) { }
        }
    }

    // Adapter RecyclerView pentru device-uri Bluetooth
    inner class BluetoothDeviceAdapter(
        private val devices: List<BluetoothDevice>,
        private val onNameClick: (BluetoothDevice) -> Unit,
        private val onPairClick: (BluetoothDevice) -> Unit
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
            holder.tvDeviceName.text = device.name ?: "Unknown"

            val isConnected = connectionThread != null && device == getConnectedDevice()
            holder.tvDeviceName.setTextColor(
                if (isConnected)
                    0xFFFFA500.toInt() // portocaliu
                else
                    0xFFFFFFFF.toInt() // alb
            )

            holder.tvDeviceName.setOnClickListener {
                onNameClick(device)
            }

            val isPaired = device.bondState == BluetoothDevice.BOND_BONDED
            holder.btnPair.text = if (isPaired) "Unpair" else "Pair"
            holder.btnPair.setOnClickListener {
                onPairClick(device)
            }
        }

        override fun getItemCount(): Int = devices.size

        private fun getConnectedDevice(): BluetoothDevice? {
            return connectionThread?.let {
                try {
                    it.socket.remoteDevice
                } catch (e: Exception) {
                    null
                }
            }
        }
    }
}
