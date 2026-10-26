package com.mtgofa.eservice.data

import android.content.Context
import android.content.SharedPreferences
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue

data class User(val name: String, val email: String, val username: String)

/** The signed-in user + token, persisted so the app opens straight to home next time. */
object Session {
    private lateinit var prefs: SharedPreferences

    var user by mutableStateOf<User?>(null); private set
    val token: String? get() = prefs.getString("token", null)

    /** Last login identifier, kept only when "remember me" is on. */
    val rememberedLogin: String get() = prefs.getString("remembered_login", "") ?: ""

    fun init(context: Context) {
        prefs = context.getSharedPreferences("session", Context.MODE_PRIVATE)
        if (token != null) {
            user = User(
                name = prefs.getString("name", "") ?: "",
                email = prefs.getString("email", "") ?: "",
                username = prefs.getString("username", "") ?: ""
            )
        }
    }

    fun signIn(result: LoginResult, login: String, remember: Boolean) {
        prefs.edit()
            .putString("token", result.token)
            .putString("name", result.user.name)
            .putString("email", result.user.email)
            .putString("username", result.user.username)
            .putString("remembered_login", if (remember) login else "")
            .apply()
        user = result.user
    }

    fun signOut() {
        prefs.edit().remove("token").remove("name").remove("email").remove("username").apply()
        user = null
    }
}
