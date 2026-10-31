package com.example.umkmfinance.data.local

import android.content.Context

class TokenManager(context: Context) {

    private val preferences = context.getSharedPreferences(
        "umkm_finance_preferences",
        Context.MODE_PRIVATE
    )

    fun saveToken(token: String) {
        preferences.edit()
            .putString(KEY_TOKEN, token)
            .apply()
    }

    fun getToken(): String? {
        return preferences.getString(KEY_TOKEN, null)
    }

    fun saveUserId(userId: Long) {
        preferences.edit()
            .putLong(KEY_USER_ID, userId)
            .apply()
    }

    fun getUserId(): Long {
        return preferences.getLong(KEY_USER_ID, -1L)
    }

    fun saveUsername(username: String) {
        preferences.edit()
            .putString(KEY_USERNAME, username)
            .apply()
    }

    fun getUsername(): String? {
        return preferences.getString(KEY_USERNAME, null)
    }

    fun saveRole(role: String) {
        preferences.edit()
            .putString(KEY_ROLE, role)
            .apply()
    }

    fun getRole(): String? {
        return preferences.getString(KEY_ROLE, null)
    }

    fun clear() {
        preferences.edit().clear().apply()
    }

    companion object {
        private const val KEY_TOKEN = "token"
        private const val KEY_USER_ID = "user_id"
        private const val KEY_USERNAME = "username"
        private const val KEY_ROLE = "role"
    }
}