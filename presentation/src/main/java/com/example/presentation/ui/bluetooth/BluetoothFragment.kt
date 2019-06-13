package com.example.presentation.ui.bluetooth

import android.bluetooth.BluetoothAdapter
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import com.example.presentation.R
import com.example.presentation.databinding.BluetoothFragmentBinding
import com.google.android.material.snackbar.Snackbar
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
            bluetoothViewModel.getBluetoothDevices()
        })

        bluetoothViewModel.isBluetoothEnabled.observe(this@BluetoothFragment, Observer { isBluetoothEnabled ->
            when (isBluetoothEnabled) {
                true -> swBluetoothEnabled.isChecked = true
                false -> enableBluetooth()
            }
        })

        bindUI()
    }

    private fun bindUI() {
        pbBluetooth.show()

        val blueToothAdapter = BluetoothDeviceAdapter { macAddress -> saveBluetoothMACAddress(macAddress) }

        bluetoothViewModel.bluetoothData.observe(this@BluetoothFragment, Observer {
            pbBluetooth.hide()
            blueToothAdapter.submitList(it)
        })

        bluetoothViewModel.isMacAddressSaved.observe(this@BluetoothFragment, Observer {
            when (it) {
                true -> showMacAddressSavedSuccess()
                false -> showMacAddressSavedFail()
            }
        })

        rvAvailableBluetoothDevices.adapter = blueToothAdapter
    }

    private fun showMacAddressSavedSuccess() =
        Snackbar.make(bluetoothRoot, getString(R.string.mac_address_saved), Snackbar.LENGTH_SHORT).show()


    private fun showMacAddressSavedFail() =
        Snackbar.make(bluetoothRoot, getString(R.string.printer_save_error), Snackbar.LENGTH_SHORT).show()

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

    private fun saveBluetoothMACAddress(macAddress: String) = bluetoothViewModel.saveMacAddress(macAddress)

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (requestCode == REQUEST_ENABLE_BT)
            bluetoothViewModel.isBluetoothAvailable()
    }
}
