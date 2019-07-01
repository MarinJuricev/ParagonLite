package com.example.presentation.ui.settings


import android.content.Context
import android.os.Bundle
import androidx.preference.EditTextPreference
import androidx.preference.PreferenceFragmentCompat
import com.example.presentation.R

const val BLUETOOTH_MAC_ADDRESS_KEY = "BLUETOOTH_MAC_ADDRESS_KEY"
const val RECEIPT_KEY = "RECEIPT_KEY"
const val PACKAGE_NAME = "\"com.example.data\""

class SettingsFragment : PreferenceFragmentCompat() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val preferences = context?.getSharedPreferences(PACKAGE_NAME, Context.MODE_PRIVATE)

        val receiptPref = findPreference(RECEIPT_KEY) as EditTextPreference
        receiptPref.text = preferences?.getInt(RECEIPT_KEY, 1).toString()

        val macAddressPref = findPreference(BLUETOOTH_MAC_ADDRESS_KEY) as EditTextPreference
        macAddressPref.text = preferences?.getString(BLUETOOTH_MAC_ADDRESS_KEY, "")
    }

    override fun onCreatePreferences(savedInstanceState: Bundle?, rootKey: String?) {
        addPreferencesFromResource(R.xml.preferences)
    }

}
