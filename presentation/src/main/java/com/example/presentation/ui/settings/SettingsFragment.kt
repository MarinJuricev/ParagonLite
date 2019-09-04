package com.example.presentation.ui.settings


import android.content.SharedPreferences
import android.os.Bundle
import androidx.appcompat.app.AppCompatDelegate
import androidx.preference.EditTextPreference
import androidx.preference.PreferenceFragmentCompat
import com.example.domain.shared.*
import com.example.presentation.R
import com.example.presentation.ui.BaseActivity
import org.koin.android.ext.android.inject

class SettingsFragment : PreferenceFragmentCompat(),
    SharedPreferences.OnSharedPreferenceChangeListener {

    private val sharedPrefsService: ISharedPrefsService by inject()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val receiptPref = findPreference(RECEIPT_KEY) as EditTextPreference
        receiptPref.text = sharedPrefsService.getValue(RECEIPT_KEY, "1") as String

        val macAddressPref = findPreference(BLUETOOTH_MAC_ADDRESS_KEY) as EditTextPreference
        macAddressPref.text = sharedPrefsService.getValue(BLUETOOTH_MAC_ADDRESS_KEY, "") as String
    }

    override fun onCreatePreferences(savedInstanceState: Bundle?, rootKey: String?) {
        addPreferencesFromResource(R.xml.preferences)
    }

    override fun onSharedPreferenceChanged(sharedPreferences: SharedPreferences, key: String) {
        when (key) {
            RECEIPT_KEY -> updateReceiptNumber(sharedPrefsService.getValue(RECEIPT_KEY, 1) as Int)
            BLUETOOTH_MAC_ADDRESS_KEY ->
                updateBluetoothAddress(
                    sharedPrefsService.getValue(BLUETOOTH_MAC_ADDRESS_KEY, "Empty") as String
                )
            APP_MODE -> updateAppTheme(sharedPrefsService.getValue(APP_MODE, LIGHT) as String)
        }
    }

    private fun updateAppTheme(theme: String?) {
        val newTheme = theme ?: LIGHT
        activity?.let {
            val baseActivity = it as BaseActivity

            if (newTheme == DARK)
                baseActivity.delegate.localNightMode = AppCompatDelegate.MODE_NIGHT_YES
            else
                baseActivity.delegate.localNightMode = AppCompatDelegate.MODE_NIGHT_NO

            updatePreferenceAppTheme(newTheme)

            baseActivity.recreate()
        }
    }

    private fun updatePreferenceAppTheme(newTheme: String) =
        sharedPrefsService.saveValue(APP_MODE, newTheme)

    private fun updateReceiptNumber(receiptNumber: Int?) =
        sharedPrefsService.saveValue(RECEIPT_KEY, receiptNumber ?: 1)

    private fun updateBluetoothAddress(newMacAddress: String?) =
        sharedPrefsService.saveValue(BLUETOOTH_MAC_ADDRESS_KEY, newMacAddress ?: "Empty")

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
