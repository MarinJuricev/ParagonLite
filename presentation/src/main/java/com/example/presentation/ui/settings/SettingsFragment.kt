package com.example.presentation.ui.settings


import android.content.Context
import android.content.SharedPreferences
import android.os.Bundle
import androidx.preference.EditTextPreference
import androidx.preference.PreferenceFragmentCompat
import com.example.presentation.R

const val BLUETOOTH_MAC_ADDRESS_KEY = "BLUETOOTH_MAC_ADDRESS_KEY"
const val RECEIPT_KEY = "RECEIPT_KEY"
const val PACKAGE_NAME = "\"com.example.data\""

class SettingsFragment : PreferenceFragmentCompat(), SharedPreferences.OnSharedPreferenceChangeListener {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val preferences = context?.getSharedPreferences(PACKAGE_NAME, Context.MODE_PRIVATE)

        val receiptPref = findPreference(RECEIPT_KEY) as EditTextPreference
        receiptPref.text = preferences?.getString(RECEIPT_KEY, "1")

        val macAddressPref = findPreference(BLUETOOTH_MAC_ADDRESS_KEY) as EditTextPreference
        macAddressPref.text = preferences?.getString(BLUETOOTH_MAC_ADDRESS_KEY, "")
    }

    override fun onCreatePreferences(savedInstanceState: Bundle?, rootKey: String?) {
        addPreferencesFromResource(R.xml.preferences)
    }

    override fun onSharedPreferenceChanged(sharedPreferences: SharedPreferences, key: String) {
        val preference = findPreference(key)
        if (preference is EditTextPreference) {
            when (preference.key) {
                RECEIPT_KEY -> updateReceiptNumber(sharedPreferences.getString(RECEIPT_KEY, "1"))
                BLUETOOTH_MAC_ADDRESS_KEY ->
                    updateBluetoothAddress(
                        sharedPreferences.getString(BLUETOOTH_MAC_ADDRESS_KEY, "Empty")
                    )
            }
        }
    }

    private fun updateReceiptNumber(receiptNumber: String?) {
        val preferences = context?.getSharedPreferences(PACKAGE_NAME, Context.MODE_PRIVATE)

        preferences?.edit()?.putString(
            RECEIPT_KEY,
            receiptNumber ?: "1"
        )?.apply()
    }

    private fun updateBluetoothAddress(newMacAddress: String?) {
        val preferences = context?.getSharedPreferences(PACKAGE_NAME, Context.MODE_PRIVATE)

        preferences?.edit()?.putString(
            BLUETOOTH_MAC_ADDRESS_KEY,
            newMacAddress ?: "Empty"
        )?.apply()
    }

    override fun onResume() {
        super.onResume()
        preferenceScreen.sharedPreferences
            .registerOnSharedPreferenceChangeListener(this)
    }

    override fun onPause() {
        super.onPause()
        preferenceScreen.sharedPreferences
            .unregisterOnSharedPreferenceChangeListener(this)
    }
}
