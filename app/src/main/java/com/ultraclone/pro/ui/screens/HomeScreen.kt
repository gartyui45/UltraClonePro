package com.ultraclone.pro.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.ultraclone.pro.data.repository.CloneRepository
import com.ultraclone.pro.ui.theme.Primary

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen(onNavigateToSelector: () -> Unit, onNavigateToManager: () -> Unit) {
    val ctx = androidx.compose.ui.platform.LocalContext.current
    val repo = remember { CloneRepository(ctx) }
    val clones by repo.clonedApps.collectAsState()

    Scaffold(
        topBar = { TopAppBar(title = { Text("UltraClone Pro", fontWeight = FontWeight.Bold) }, colors = TopAppBarDefaults.topAppBarColors(containerColor = MaterialTheme.colorScheme.surface)) },
        floatingActionButton = { ExtendedFloatingActionButton(onClick = onNavigateToSelector, containerColor = Primary) { Icon(Icons.Default.Add, null); Spacer(Modifier.width(8.dp)); Text("Clonar Apps") } }
    ) { pad ->
        Column(Modifier.fillMaxSize().padding(pad).padding(16.dp)) {
            Row(Modifier.fillMaxWidth(), Arrangement.spacedBy(12.dp)) {
                Card(Modifier.weight(1f), colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant)) {
                    Column(Modifier.padding(16.dp)) { Icon(Icons.Default.Apps, null, tint = Primary); Text("${clones.size}", fontSize = androidx.compose.ui.unit.sp(24), fontWeight = FontWeight.Bold); Text("Clones") }
                }
            }
            Spacer(Modifier.height(24.dp))
            if (clones.isEmpty()) {
                Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        Icon(Icons.Default.Android, null, Modifier.size(64.dp), tint = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.3f))
                        Spacer(Modifier.height(16.dp))
                        Text("Nenhum clone criado", style = MaterialTheme.typography.bodyLarge, color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.5f))
                        Text("Toque no botão + para clonar seus apps", style = MaterialTheme.typography.bodyMedium, color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.3f), textAlign = TextAlign.Center)
                    }
                }
            } else {
                clones.filter { !it.isHidden }.take(5).forEach { c ->
                    Card(Modifier.fillMaxWidth().padding(vertical = 4.dp), onClick = onNavigateToManager, colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant)) {
                        Row(Modifier.fillMaxWidth().padding(12.dp), verticalAlignment = Alignment.CenterVertically) {
                            Icon(Icons.Default.Apps, null, tint = Primary, modifier = Modifier.size(32.dp))
                            Column(Modifier.weight(1f).padding(start = 12.dp)) { Text(c.cloneLabel, fontWeight = FontWeight.Medium); Text(c.originalPackageName, style = MaterialTheme.typography.bodySmall, color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.5f)) }
                        }
                    }
                }
                if (clones.count { !it.isHidden } > 5) TextButton(onClick = onNavigateToManager) { Text("Ver todos") }
            }
        }
    }
}
