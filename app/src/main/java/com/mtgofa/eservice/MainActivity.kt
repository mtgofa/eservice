package com.mtgofa.eservice

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.togetherWith
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Modifier
import com.mtgofa.eservice.data.Session
import com.mtgofa.eservice.ui.EServiceTheme
import com.mtgofa.eservice.ui.HomeScreen
import com.mtgofa.eservice.ui.LoginScreen
import com.mtgofa.eservice.ui.UpdateDialog

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            EServiceTheme {
                Surface(Modifier.fillMaxSize(), color = MaterialTheme.colorScheme.background) {
                    LaunchedEffect(Unit) { Updater.check(auto = true) }
                    AnimatedContent(
                        targetState = Session.user,
                        transitionSpec = { fadeIn() togetherWith fadeOut() },
                        contentKey = { it != null },
                        label = "auth"
                    ) { user ->
                        if (user == null) LoginScreen() else HomeScreen(user)
                    }
                    UpdateDialog()
                }
            }
        }
    }

    override fun onResume() {
        super.onResume()
        // back from "Install unknown apps" settings -> continue the pending update
        Updater.onResume(this)
    }
}
