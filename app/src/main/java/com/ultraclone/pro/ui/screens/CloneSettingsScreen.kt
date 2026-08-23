package com.ultraclone.pro.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.ultraclone.pro.data.repository.CloneRepository
import com.ultraclone.pro.ui.theme.Primary

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CloneSettingsScreen(cloneId: Long, onTerminalClick: () -> Unit, onBack: () -> Unit) {
    val ctx = androidx.compose.ui.platform.LocalContext.current
    val repo = remember { CloneRepository(ctx) }; val clones by repo.clonedApps.collectAsState()
    val clone = clones.find { it.id == cloneId }
    Scaffold(
        topBar = { TopAppBar(title = { Text(clone?.cloneLabel ?: "Config") }, navigationIcon = { IconButton(onClick = onBack) { Icon(Icons.Default.ArrowBack, "Voltar") } }) }
    ) { pad ->
        if (clone == null) return@Scaffold
        Column(Modifier.fillMaxSize().padding(pad).padding(16.dp)) {
            Card(Modifier.fillMaxWidth(), colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant)) {
                Column(Modifier.padding(16.dp)) {
                    Text("Informações", fontWeight = FontWeight.SemiBold)
                    Spacer(Modifier.height(8.dp))
                    listOf("App original" to clone.originalPackageName, "Fingerprint" to clone.fingerprintId.take(12)+"...").forEach { (l, v) -> Row(Modifier.fillMaxWidth().padding(vertical = 2.dp), Arrangement.SpaceBetween) { Text(l); Text(v, color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.6f)) } }
                }
            }
            Spacer(Modifier.height(16.dp))
            SettingItem(Icons.Default.Refresh, "Regenerar Fingerprint", "Nova identidade para o clone") { repo.regenerateFingerprint(cloneId) }
            SettingItem(Icons.Default.Terminal, "Terminal (Proot)", "Shell com root emulado") { onTerminalClick() }
            SettingItem(Icons.Default.DeleteSweep, "Limpar Cache", "Remove temporários") { repo.cleanCache(cloneId) }
            SettingItem(if (clone.isHidden) Icons.Default.Visibility else Icons.Default.VisibilityOff, if (clone.isHidden) "Mostrar" else "Ocultar (Stealth)", "") { repo.toggleHidden(cloneId) }
            Spacer(Modifier.weight(1f))
            Button(onClick = { repo.launchClone(cloneId) }, modifier = Modifier.fillMaxWidth(), colors = ButtonDefaults.buttonColors(containerColor = Primary)) { Icon(Icons.Default.PlayArrow, null); Spacer(Modifier.width(8.dp)); Text("Abrir Clone") }
            OutlinedButton(onClick = { repo.deleteClone(cloneId); onBack() }, modifier = Modifier.fillMaxWidth(), colors = ButtonDefaults.outlinedButtonColors(contentColor = MaterialTheme.colorScheme.error)) { Icon(Icons.Default.Delete, null); Spacer(Modifier.width(8.dp)); Text("Excluir") }
        }
    }
}

@Composable
fun SettingItem(icon: androidx.compose.ui.graphics.vector.ImageVector, title: String, subtitle: String, onClick: () -> Unit) {
    Card(onClick = onClick, modifier = Modifier.fillMaxWidth().padding(vertical = 4.dp), colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant)) {
        Row(Modifier.fillMaxWidth().padding(16.dp), Alignment.CenterVertically) {
            Icon(icon, null, tint = Primary); Spacer(Modifier.width(16.dp))
            Column(Modifier.weight(1f)) { Text(title, fontWeight = FontWeight.Medium); if (subtitle.isNotEmpty()) Text(subtitle, style = MaterialTheme.typography.bodySmall, color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.5f)) }
            Icon(Icons.Default.ChevronRight, null, tint = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.3f))
        }
    }
}
