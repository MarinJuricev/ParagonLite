package com.example.data.shared

import android.content.Context
import android.content.SharedPreferences
import android.preference.PreferenceManager
import com.example.domain.shared.ISharedPrefsService
import java.util.concurrent.ConcurrentHashMap

class SharedPrefsService(private val context: Context?) : ISharedPrefsService {

    @Volatile
    private var cacheBoolean = ConcurrentHashMap<String, Boolean>()

    @Volatile
    private var cacheString = ConcurrentHashMap<String, String>()

    @Volatile
    private var cacheInt = ConcurrentHashMap<String, Int>()

    private fun getPreferences(): SharedPreferences {
        return PreferenceManager.getDefaultSharedPreferences(context)
    }

    private fun getPreferencesEditor(): SharedPreferences.Editor {
        return getPreferences().edit()
    }

    override fun saveValue(key: String, value: Any) {
        return when (value) {
            is String -> saveString(key, value)
            is Boolean -> saveBoolean(key, value)
            is Int -> saveInt(key, value)
            else -> throw IllegalArgumentException("Value type not supported")
        }
    }

    override fun getValue(key: String, defaultValue: Any): Any? {
        return when (defaultValue) {
            is Boolean -> getBoolean(key, defaultValue)
            is String -> getString(key, defaultValue)
            is Int -> getInt(key, defaultValue)
            else -> throw IllegalArgumentException("Value type not supported")
        }
    }

    private fun saveInt(key: String, value: Int) {
        cacheInt[key] = value
        getPreferencesEditor().putInt(key, value).apply()
    }

    private fun saveString(key: String, value: String) {
        cacheString[key] = value
        getPreferencesEditor().putString(key, value).apply()
    }

    private fun saveBoolean(key: String, value: Boolean) {
        cacheBoolean[key] = value
        getPreferencesEditor().putBoolean(key, value).apply()
    }

    private fun getString(key: String, defaultValue: String): String? {
        var result = cacheString[key]
        if (result == null) {
            result = getPreferences().getString(key, defaultValue)
            if (result != null) {
                cacheString[key] = result
            }
        }
        return result
    }

    private fun getInt(key: String, defaultValue: Int): Int? {
        var result = cacheInt[key]
        if (result == null) {
            result = getPreferences().getInt(key, defaultValue)
            cacheInt[key] = result
        }
        return result
    }

    private fun getBoolean(key: String, defaultValue: Boolean): Boolean {
        var result = cacheBoolean[key]
        if (result == null) {
            result = getPreferences().getBoolean(key, defaultValue)
            cacheBoolean[key] = result
        }
        return result
    }

}