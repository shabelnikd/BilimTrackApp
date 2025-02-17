package com.shabelnikd.bilimtrack.utils

import android.content.Context
import android.content.SharedPreferences
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject
import javax.inject.Singleton

class PreferenceHelper {

    companion object {
        private const val PREFS_NAME = "sharedPreferences"
        private const val KEY_IS_FIRST_LAUNCH = "isFirstLaunch"
        private const val KEY_ACCESS_TOKEN = "accessToken"
        private const val KEY_REFRESH_TOKEN = "refreshToken"
    }

    private var _sharedPreferences: SharedPreferences? = null
    private val sharedPreferences get() = _sharedPreferences!!

    fun initialize(context: Context) {
        _sharedPreferences = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)
    }
    var isFirstLaunch: Boolean
        get() = sharedPreferences.getBoolean(KEY_IS_FIRST_LAUNCH, true)
        set(value) = sharedPreferences.edit().putBoolean(KEY_IS_FIRST_LAUNCH, value).apply()

    val isLoggedIn: Boolean
        get() = accessToken.isNullOrEmpty().not() && refreshToken.isNullOrEmpty().not()

    var accessToken: String?
        get() = sharedPreferences.getString(KEY_ACCESS_TOKEN, null)
        set(value) = sharedPreferences.edit().putString(KEY_ACCESS_TOKEN, value).apply()

    var refreshToken: String?
        get() = sharedPreferences.getString(KEY_REFRESH_TOKEN, null)
        set(value) = sharedPreferences.edit().putString(KEY_REFRESH_TOKEN, value).apply()
}