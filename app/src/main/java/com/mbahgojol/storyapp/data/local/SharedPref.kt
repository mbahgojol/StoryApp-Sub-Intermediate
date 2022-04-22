package com.mbahgojol.storyapp.data.local

import android.content.Context

class SharedPref constructor(context: Context) {
    private val pref = context.getSharedPreferences("pref-story", Context.MODE_PRIVATE)
    private val tokenKey = "TokenKey"
    private val isLoginKey = "isLoginKey"

    var token: String
        set(value) {
            pref.edit()
                .putString(tokenKey, value)
                .apply()
        }
        get() = pref.getString(tokenKey, "").toString()

    var isLogin: Boolean
        set(value) {
            pref.edit()
                .putBoolean(isLoginKey, value)
                .apply()
        }
        get() = pref.getBoolean(isLoginKey, false)
}