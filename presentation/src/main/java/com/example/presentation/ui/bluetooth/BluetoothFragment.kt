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
import com.example.presentation.ext.extendFabIfPossible
import com.example.presentation.ext.shrinkFabIfPossible
import com.google.android.material.snackbar.Snackbar
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

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val bluetoothAdapter: BluetoothAdapter? = BluetoothAdapter.getDefaultAdapter()
        val isEnabled = bluetoothAdapter?.isEnabled ?: false

        bluetoothViewModel.isBluetoothAvailable(
            bluetoothAdapter != null,
            isEnabled
        )

        bluetoothViewModel.isBluetoothAvailable.observe(viewLifecycleOwner, Observer {
            handleBluetoothAvailability(it)
        })

        bluetoothViewModel.isBluetoothEnabled.observe(
            viewLifecycleOwner,
            Observer { isBluetoothEnabled ->
                when (isBluetoothEnabled) {
                    BluetoothStatus.Enabled -> {
                        binding.tvEmptyBluetoothDevices.text =
                            getString(R.string.add_bluetooth_devices)
                        binding.btnEnableBluetooth.visibility = View.GONE
                        binding.fabActivateBluetoothSearch.show()

                        binding.swBluetoothEnabled.isChecked = true

                        bluetoothViewModel.getData()
                    }
                    BluetoothStatus.Disabled -> enableBluetooth()
                    BluetoothStatus.Dismissed -> {
                        binding.fabActivateBluetoothSearch.hide()
                        binding.tvEmptyBluetoothDevices.text =
                            getString(R.string.bluetooth_canceled)
                        binding.btnEnableBluetooth.visibility = View.VISIBLE
                        showEmptyScreenFields()
                    }
                }
            })

        bindUI()
    }

    private fun bindUI() {
        val blueToothAdapter =
            BluetoothDeviceAdapter { macAddress -> saveBluetoothMACAddress(macAddress) }

        bluetoothViewModel.bluetoothData.observe(viewLifecycleOwner, Observer {
            binding.pbBluetooth.hide()
            binding.fabActivateBluetoothSearch.extendFabIfPossible()

            if (it.isEmpty())
                showEmptyScreenFields()
            else
                hideEmptyScreensFields()

            blueToothAdapter.submitList(it)
        })

        bluetoothViewModel.isMacAddressSaved.observe(viewLifecycleOwner, Observer {
            when (it) {
                true -> showMacAddressSavedSuccess()
                false -> showMacAddressSavedFail()
            }
        })

        binding.fabActivateBluetoothSearch.setOnClickListener {
            binding.pbBluetooth.show()
            binding.fabActivateBluetoothSearch.shrinkFabIfPossible()

            bluetoothViewModel.updateBluetoothData()
        }

        binding.btnEnableBluetooth.setOnClickListener {
            enableBluetooth()
        }

        binding.rvAvailableBluetoothDevices.adapter = blueToothAdapter
    }

    private fun showEmptyScreenFields() {
        binding.noBluetoothGroup.visibility = View.VISIBLE
    }

    private fun hideEmptyScreensFields() {
        binding.noBluetoothGroup.visibility = View.GONE
    }

    private fun showMacAddressSavedSuccess() =
        Snackbar.make(
            binding.bluetoothRoot,
            getString(R.string.mac_address_saved),
            Snackbar.LENGTH_SHORT
        ).show()


    private fun showMacAddressSavedFail() =
        Snackbar.make(
            binding.bluetoothRoot,
            getString(R.string.printer_save_error),
            Snackbar.LENGTH_SHORT
        ).show()

    private fun handleBluetoothAvailability(isBluetoothAvailable: Boolean) {
        if (!isBluetoothAvailable) {
            binding.swBluetoothEnabled.isChecked = false
            binding.tvBluetoothTurnOn.text = getString(R.string.no_bluetooth)
        }
    }

    private fun enableBluetooth() {
        binding.swBluetoothEnabled.isChecked = false

        val enableBtIntent = Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE)
        startActivityForResult(enableBtIntent, REQUEST_ENABLE_BT)
    }

    private fun saveBluetoothMACAddress(macAddress: String) =
        bluetoothViewModel.saveMacAddress(macAddress)

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (requestCode == REQUEST_ENABLE_BT)
            bluetoothViewModel.handleBluetoothRequest(resultCode)
    }
}
