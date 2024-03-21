package com.example.citysound

import android.content.Context
import android.content.SharedPreferences

object SessionManager {
    const val PREFS_NAME = "MyAppPrefs"
    private const val ACCESS_TOKEN_KEY = "accessToken"

    fun saveAccessToken(context: Context, token: String) {
        val sharedPreferences = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)
        val editor = sharedPreferences.edit()
        editor.putString(ACCESS_TOKEN_KEY, token)
        editor.apply()
    }

    fun getAccessToken(sharedPreferences: SharedPreferences): String? {
        // No necesitas obtener un nuevo SharedPreferences aquí, ya que ya lo tienes como argumento
        return sharedPreferences.getString(ACCESS_TOKEN_KEY, null)
    }

    fun clearAccessToken(context: Context) {
        val sharedPreferences = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)
        val editor = sharedPreferences.edit()
        editor.remove(ACCESS_TOKEN_KEY)
        editor.apply()
    }
}

