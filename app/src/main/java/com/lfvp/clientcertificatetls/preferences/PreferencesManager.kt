package com.lfvp.clientcertificatetls.preferences

import android.content.SharedPreferences
import javax.inject.Inject

class PreferencesManager @Inject constructor(private val sharedPreferences: SharedPreferences) {

    fun save(key: String, value: String) {
        sharedPreferences.edit().putString(key, value).apply()
    }

    fun get(key: String): String? {
        return sharedPreferences.getString(key, null)
    }
    fun clear(key: String){
        sharedPreferences.edit().remove(key).apply()
    }
}