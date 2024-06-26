package com.example.citysound

import android.content.Context
import android.content.SharedPreferences

//Object a diferencia de class es para tener una única instancia.
object SessionManager {
    //Donde almacena el token
    const val PREFS_NAME = "MyAppPrefs"
    //clave para alamecenar el token
    private const val ACCESS_TOKEN_KEY = "accessToken"
    //clave para almacenar ususario
    private const val USER_KEY = "user"

    //Almacena el token en sharedPreferences
    fun saveAccessToken(context: Context, token: String) {
        val sharedPreferences = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)
        val editor = sharedPreferences.edit()
        editor.putString(ACCESS_TOKEN_KEY, token)
        editor.apply()
    }

    //Recibe el token como argumento
    fun getAccessToken(sharedPreferences: SharedPreferences): String? {
        return sharedPreferences.getString(ACCESS_TOKEN_KEY, null)
    }

    //borra el token
    fun clearAccessToken(context: Context) {
        val sharedPreferences = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)
        val editor = sharedPreferences.edit()
        editor.remove(ACCESS_TOKEN_KEY)
        editor.apply()
    }

    //IN PROGRESS
    fun saveUser(context: Context, user: String) {
        val sharedPreferences = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)
        val editor = sharedPreferences.edit()
        editor.putString(USER_KEY, user)
        editor.apply()
    }

    fun getUser(sharedPreferences: SharedPreferences): String? {
        return sharedPreferences.getString(USER_KEY, null)
    }


}

