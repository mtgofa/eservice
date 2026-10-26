package com.mtgofa.eservice.data

import com.mtgofa.eservice.BuildConfig
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.withContext
import org.json.JSONObject
import java.net.HttpURLConnection
import java.net.URL

data class LoginResult(val token: String, val user: User)

class AuthException(message: String) : Exception(message)

/**
 * Login against `POST {API_BASE_URL}/login` (set `apiBaseUrl` in gradle.properties).
 *
 *   request  -> {"login": "<email or username>", "password": "..."}
 *   response -> {"token": "...", "user": {"name": "...", "email": "...", "username": "..."}}
 *   error    -> any non-2xx, optional {"message": "..."} shown to the user
 *
 * With no API configured the app runs in demo mode: admin / admin@eservice.app + 123456.
 */
object AuthApi {
    val demoMode get() = BuildConfig.API_BASE_URL.isBlank()

    const val DEMO_USERNAME = "admin"
    const val DEMO_EMAIL = "admin@eservice.app"
    const val DEMO_PASSWORD = "123456"

    suspend fun login(login: String, password: String): LoginResult =
        if (demoMode) demoLogin(login, password) else withContext(Dispatchers.IO) { remoteLogin(login, password) }

    private suspend fun demoLogin(login: String, password: String): LoginResult {
        delay(700)
        val ok = (login.equals(DEMO_USERNAME, true) || login.equals(DEMO_EMAIL, true)) && password == DEMO_PASSWORD
        if (!ok) throw AuthException("بيانات الدخول غير صحيحة")
        return LoginResult("demo-token", User("مدير النظام", DEMO_EMAIL, DEMO_USERNAME))
    }

    private fun remoteLogin(login: String, password: String): LoginResult {
        val c = (URL(BuildConfig.API_BASE_URL.trimEnd('/') + "/login").openConnection() as HttpURLConnection).apply {
            requestMethod = "POST"
            connectTimeout = 15_000
            readTimeout = 30_000
            doOutput = true
            setRequestProperty("Content-Type", "application/json; charset=utf-8")
            setRequestProperty("Accept", "application/json")
            setRequestProperty("User-Agent", "eService-Android")
        }
        try {
            val body = JSONObject().put("login", login).put("password", password).toString()
            try {
                c.outputStream.use { it.write(body.toByteArray()) }
            } catch (e: Exception) {
                throw AuthException("تعذّر الاتصال بالخادم، تحقق من الإنترنت")
            }
            val code = c.responseCode
            val text = (if (code in 200..299) c.inputStream else c.errorStream)?.bufferedReader()?.readText().orEmpty()
            val json = runCatching { JSONObject(text) }.getOrNull()
            if (code !in 200..299) {
                val msg = json?.optString("message")?.takeIf { it.isNotBlank() }
                throw AuthException(msg ?: if (code == 401 || code == 422) "بيانات الدخول غير صحيحة" else "خطأ من الخادم ($code)")
            }
            val token = json?.optString("token").orEmpty()
            if (token.isBlank()) throw AuthException("رد غير متوقع من الخادم")
            val u = json?.optJSONObject("user")
            return LoginResult(
                token,
                User(
                    name = u?.optString("name").orEmpty(),
                    email = u?.optString("email").orEmpty(),
                    username = u?.optString("username").orEmpty().ifBlank { login }
                )
            )
        } finally {
            c.disconnect()
        }
    }
}
