package com.example.domain.shared

interface ISharedPrefsService {
    fun saveValue(key: String, value: Any)
    fun getValue(key: String, defaultValue: Any): Any?
}