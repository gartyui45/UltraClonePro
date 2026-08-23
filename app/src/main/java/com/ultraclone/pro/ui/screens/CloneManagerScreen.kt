package com.ultraclone.pro.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import com.ultraclone.pro.data.repository.CloneRepository
import com.ultraclone.pro.ui.theme.Primary

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CloneManagerScreen(onCloneClick: (Long) -> Unit, onBack: () -> Unit) {
    val ctx = androidx.compose.ui.platform.LocalContext.current
    val repo = remember { CloneRepository(ctx) }; val clones by repo.clonedApps.collectAsState()
    Scaffold(
        topBar = { TopAppBar(title = { Text("Meus Clones") }, navigationIcon = { IconButton(onClick = onBack) { Icon(Icons.Default.ArrowBack, "Voltar") } }, colors = TopAppBarDefaults.topAppBarColors(containerColor = MaterialTheme.colorScheme.surface)) }
    ) { pad ->
        if (clones.isEmpty()) Box(Modifier.fillMaxSize().padding(pad), contentAlignment = Alignment.Center) { Column(horizontalAlignment = Alignment.CenterHorizontally) { Icon(Icons.Default.Inbox, null, Modifier.size(64.dp), tint = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.3f)); Text("Nenhum clone criado") } }
        else LazyColumn(Modifier.fillMaxSize().padding(pad).padding(16.dp), verticalArrangement = Arrangement.spacedBy(8.dp)) {
            items(clones, key = { it.id }) { c ->
                var menu by remember { mutableStateOf(false) }
                Card(onClick = { onCloneClick(c.id) }, colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant), modifier = Modifier.fillMaxWidth()) {
                    Row(Modifier.fillMaxWidth().padding(12.dp), Alignment.CenterVertically) {
                        Icon(Icons.Default.Apps, null, tint = Primary, Modifier.size(36.dp))
                        Column(Modifier.weight(1f).padding(horizontal = 12.dp)) { Row { Text(c.cloneLabel, fontWeight = FontWeight.Medium, maxLines = 1, overflow = TextOverflow.Ellipsis); if (c.isHidden) { Spacer(Modifier.width(4.dp)); Icon(Icons.Default.VisibilityOff, null, Modifier.size(16.dp), tint = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.5f) ) } }; Text(c.originalPackageName, style = MaterialTheme.typography.bodySmall, color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.5f)) }
                        IconButton(onClick = { repo.launchClone(c.id) }) { Icon(Icons.Default.PlayArrow, "Abrir", tint = Primary) }
                        Box { IconButton(onClick = { menu = true }) { Icon(Icons.Default.MoreVert, "Menu") }; DropdownMenu(menu, { menu = false }) { DropdownMenuItem({ repo.toggleHidden(c.id); menu = false }, text = { Text(if (c.isHidden) "Mostrar" else "Ocultar") }, leadingIcon = { Icon(if (c.isHidden) Icons.Default.Visibility else Icons.Default.VisibilityOff, null) }); DropdownMenuItem({ repo.deleteClone(c.id); menu = false }, text = { Text("Excluir") }, leadingIcon = { Icon(Icons.Default.Delete, null) }) } }
                    }
                }
            }
        }
    }
}
