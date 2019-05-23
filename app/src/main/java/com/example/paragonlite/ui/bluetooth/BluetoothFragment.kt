package com.example.paragonlite.ui.bluetooth

import android.bluetooth.BluetoothAdapter
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import com.example.paragonlite.R
import com.example.paragonlite.databinding.BluetoothFragmentBinding
import kotlinx.android.synthetic.main.bluetooth_fragment.*
import org.koin.androidx.viewmodel.ext.android.viewModel

const val REQUEST_ENABLE_BT = 15

class BluetoothFragment : Fragment() {

    private val bluetoothViewModel: BluetoothViewModel by viewModel()
    private lateinit var binding: BluetoothFragmentBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = DataBindingUtil.inflate(
            inflater, R.layout.bluetooth_fragment, container, false
        )

        return binding.root
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        bluetoothViewModel.isBluetoothAvailable.observe(this@BluetoothFragment, Observer {
            handleBluetoothAvailability(it)
        })

        bluetoothViewModel.isBluetoothEnabled.observe(this@BluetoothFragment, Observer {
            if (it)
                swBluetoothEnabled.isChecked = true
            else
                enableBluetooth()
        })
    }

    private fun handleBluetoothAvailability(isBluetoothAvailable: Boolean) {
        if (!isBluetoothAvailable) {
            swBluetoothEnabled.isChecked = false
            tvBluetoothTurnOn.text = getString(R.string.no_bluetooth)
        }
    }

    private fun enableBluetooth() {
        swBluetoothEnabled.isChecked = false

        val enableBtIntent = Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE)
        startActivityForResult(enableBtIntent, REQUEST_ENABLE_BT)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if(requestCode == REQUEST_ENABLE_BT)
            bluetoothViewModel.isBluetoothAvailable()
    }
}
