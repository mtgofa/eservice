package com.mtgofa.eservice.ui

import android.util.Patterns
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.systemBarsPadding
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.AlternateEmail
import androidx.compose.material.icons.rounded.ErrorOutline
import androidx.compose.material.icons.rounded.Lock
import androidx.compose.material.icons.rounded.Person
import androidx.compose.material.icons.rounded.Science
import androidx.compose.material.icons.rounded.Verified
import androidx.compose.material.icons.rounded.Visibility
import androidx.compose.material.icons.rounded.VisibilityOff
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Checkbox
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusDirection
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.mtgofa.eservice.data.AuthApi
import com.mtgofa.eservice.data.AuthException
import com.mtgofa.eservice.data.Session
import kotlinx.coroutines.launch

@Composable
fun LoginScreen() {
    val scope = rememberCoroutineScope()
    val focus = LocalFocusManager.current

    var login by rememberSaveable { mutableStateOf(Session.rememberedLogin) }
    var password by rememberSaveable { mutableStateOf("") }
    var rememberMe by rememberSaveable { mutableStateOf(Session.rememberedLogin.isNotEmpty()) }
    var showPassword by rememberSaveable { mutableStateOf(false) }
    var submitted by rememberSaveable { mutableStateOf(false) }
    var loading by remember { mutableStateOf(false) }
    var error by remember { mutableStateOf<String?>(null) }

    val isEmail = "@" in login
    val loginError = when {
        login.isBlank() -> "أدخل البريد الإلكتروني أو اسم المستخدم"
        isEmail && !Patterns.EMAIL_ADDRESS.matcher(login.trim()).matches() -> "البريد الإلكتروني غير صحيح"
        else -> null
    }
    val passwordError = when {
        password.isEmpty() -> "أدخل كلمة المرور"
        password.length < 6 -> "كلمة المرور 6 أحرف على الأقل"
        else -> null
    }

    fun submit() {
        submitted = true
        error = null
        if (loginError != null || passwordError != null || loading) return
        focus.clearFocus()
        loading = true
        scope.launch {
            try {
                val result = AuthApi.login(login.trim(), password)
                Session.signIn(result, login.trim(), rememberMe)
            } catch (e: AuthException) {
                error = e.message
            } catch (e: Exception) {
                error = "تعذّر الاتصال بالخادم، حاول مرة أخرى"
            } finally {
                loading = false
            }
        }
    }

    Box(
        Modifier
            .fillMaxSize()
            .background(
                Brush.verticalGradient(
                    0f to MaterialTheme.colorScheme.primary,
                    0.42f to MaterialTheme.colorScheme.primary,
                    0.42f to MaterialTheme.colorScheme.background,
                )
            )
    ) {
        Column(
            Modifier
                .fillMaxSize()
                .systemBarsPadding()
                .imePadding()
                .verticalScroll(rememberScrollState())
                .padding(horizontal = 20.dp, vertical = 24.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Spacer(Modifier.height(24.dp))
            Surface(
                shape = RoundedCornerShape(24.dp),
                color = MaterialTheme.colorScheme.onPrimary.copy(alpha = 0.15f),
                modifier = Modifier.size(84.dp)
            ) {
                Box(contentAlignment = Alignment.Center) {
                    Icon(Icons.Rounded.Verified, null, tint = MaterialTheme.colorScheme.onPrimary, modifier = Modifier.size(46.dp))
                }
            }
            Spacer(Modifier.height(16.dp))
            Text("الخدمات الإلكترونية", style = MaterialTheme.typography.displaySmall, color = MaterialTheme.colorScheme.onPrimary)
            Spacer(Modifier.height(4.dp))
            Text(
                "سجّل الدخول للمتابعة",
                style = MaterialTheme.typography.bodyLarge,
                color = MaterialTheme.colorScheme.onPrimary.copy(alpha = 0.85f)
            )
            Spacer(Modifier.height(28.dp))

            Card(
                modifier = Modifier.widthIn(max = 480.dp).fillMaxWidth(),
                shape = MaterialTheme.shapes.large,
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                elevation = CardDefaults.cardElevation(defaultElevation = 6.dp)
            ) {
                Column(Modifier.padding(20.dp), verticalArrangement = Arrangement.spacedBy(14.dp)) {
                    Text("تسجيل الدخول", style = MaterialTheme.typography.headlineSmall)

                    OutlinedTextField(
                        value = login,
                        onValueChange = { login = it; error = null },
                        modifier = Modifier.fillMaxWidth(),
                        label = { Text("البريد الإلكتروني أو اسم المستخدم") },
                        leadingIcon = { Icon(if (isEmail) Icons.Rounded.AlternateEmail else Icons.Rounded.Person, null) },
                        singleLine = true,
                        enabled = !loading,
                        isError = submitted && loginError != null,
                        supportingText = if (submitted && loginError != null) {
                            { Text(loginError) }
                        } else null,
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Email, imeAction = ImeAction.Next, autoCorrectEnabled = false),
                        keyboardActions = KeyboardActions(onNext = { focus.moveFocus(FocusDirection.Down) }),
                        shape = MaterialTheme.shapes.small
                    )

                    OutlinedTextField(
                        value = password,
                        onValueChange = { password = it; error = null },
                        modifier = Modifier.fillMaxWidth(),
                        label = { Text("كلمة المرور") },
                        leadingIcon = { Icon(Icons.Rounded.Lock, null) },
                        trailingIcon = {
                            IconButton(onClick = { showPassword = !showPassword }) {
                                Icon(
                                    if (showPassword) Icons.Rounded.VisibilityOff else Icons.Rounded.Visibility,
                                    contentDescription = if (showPassword) "إخفاء كلمة المرور" else "إظهار كلمة المرور"
                                )
                            }
                        },
                        singleLine = true,
                        enabled = !loading,
                        isError = submitted && passwordError != null,
                        supportingText = if (submitted && passwordError != null) {
                            { Text(passwordError) }
                        } else null,
                        visualTransformation = if (showPassword) VisualTransformation.None else PasswordVisualTransformation(),
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password, imeAction = ImeAction.Done, autoCorrectEnabled = false),
                        keyboardActions = KeyboardActions(onDone = { submit() }),
                        shape = MaterialTheme.shapes.small
                    )

                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Checkbox(checked = rememberMe, onCheckedChange = { rememberMe = it }, enabled = !loading)
                        Text("تذكرني", style = MaterialTheme.typography.bodyMedium)
                    }

                    error?.let {
                        Surface(color = MaterialTheme.colorScheme.errorContainer, shape = MaterialTheme.shapes.small) {
                            Row(Modifier.fillMaxWidth().padding(12.dp), verticalAlignment = Alignment.CenterVertically) {
                                Icon(Icons.Rounded.ErrorOutline, null, tint = MaterialTheme.colorScheme.onErrorContainer)
                                Spacer(Modifier.size(8.dp))
                                Text(it, color = MaterialTheme.colorScheme.onErrorContainer, style = MaterialTheme.typography.bodyMedium)
                            }
                        }
                    }

                    Button(
                        onClick = { submit() },
                        enabled = !loading,
                        modifier = Modifier.fillMaxWidth().height(52.dp),
                        shape = MaterialTheme.shapes.small
                    ) {
                        if (loading) {
                            CircularProgressIndicator(
                                strokeWidth = 2.dp,
                                color = MaterialTheme.colorScheme.onPrimary,
                                modifier = Modifier.size(22.dp)
                            )
                        } else {
                            Text("دخول")
                        }
                    }
                }
            }

            if (AuthApi.demoMode) {
                Spacer(Modifier.height(16.dp))
                Surface(
                    color = MaterialTheme.colorScheme.secondaryContainer,
                    shape = MaterialTheme.shapes.small,
                    modifier = Modifier.widthIn(max = 480.dp).fillMaxWidth()
                ) {
                    Row(Modifier.padding(12.dp), verticalAlignment = Alignment.CenterVertically) {
                        Icon(Icons.Rounded.Science, null, tint = MaterialTheme.colorScheme.onSecondaryContainer)
                        Spacer(Modifier.size(8.dp))
                        Text(
                            "وضع تجريبي: ${AuthApi.DEMO_USERNAME} أو ${AuthApi.DEMO_EMAIL} — كلمة المرور ${AuthApi.DEMO_PASSWORD}",
                            style = MaterialTheme.typography.bodySmall,
                            color = MaterialTheme.colorScheme.onSecondaryContainer,
                            textAlign = TextAlign.Start
                        )
                    }
                }
            }
        }
    }
}
