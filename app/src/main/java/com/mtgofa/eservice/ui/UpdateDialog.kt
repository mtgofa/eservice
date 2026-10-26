package com.mtgofa.eservice.ui

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import com.mtgofa.eservice.Updater

/** The "new version" popup, shown as soon as [Updater] finds a newer GitHub release. */
@Composable
fun UpdateDialog() {
    val rel = Updater.release ?: return
    if (!Updater.prompt) return
    val context = LocalContext.current

    AlertDialog(
        onDismissRequest = { if (!Updater.downloading) Updater.later() },
        title = { Text("تحديث جديد ${rel.tagName}") },
        text = {
            Column(Modifier.heightIn(max = 320.dp).verticalScroll(rememberScrollState())) {
                Text(rel.notes.ifBlank { "يتوفر إصدار جديد من التطبيق." }, style = MaterialTheme.typography.bodyMedium)
                if (Updater.downloading) {
                    LinearProgressIndicator(Modifier.fillMaxWidth().padding(top = 16.dp))
                }
                if (Updater.message.isNotBlank()) {
                    Text(
                        Updater.message,
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                        modifier = Modifier.padding(top = 8.dp)
                    )
                }
            }
        },
        confirmButton = {
            Button(onClick = { Updater.install(context) }, enabled = !Updater.downloading) { Text("تحديث الآن") }
        },
        dismissButton = {
            TextButton(onClick = { Updater.later() }, enabled = !Updater.downloading) { Text("لاحقًا") }
        }
    )
}
