package com.ultraclone.pro.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
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
import com.ultraclone.pro.data.model.AppCategory
import com.ultraclone.pro.data.model.InstalledApp
import com.ultraclone.pro.data.repository.AppRepository
import com.ultraclone.pro.data.repository.CloneRepository
import com.ultraclone.pro.ui.theme.Primary
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AppSelectorScreen(onBack: () -> Unit) {
    val ctx = androidx.compose.ui.platform.LocalContext.current
    val appRepo = remember { AppRepository(ctx) }; val cloneRepo = remember { CloneRepository(ctx) }
    val scope = rememberCoroutineScope()
    var apps by remember { mutableStateOf<List<InstalledApp>>(emptyList()) }
    var filtered by remember { mutableStateOf<List<InstalledApp>>(emptyList()) }
    var query by remember { mutableStateOf("") }; var cat by remember { mutableStateOf(AppCategory.ALL) }
    var selected by remember { mutableStateOf<Set<String>>(emptySet()) }
    var cloning by remember { mutableStateOf(false) }; var progress by remember { mutableIntStateOf(0) }; var total by remember { mutableIntStateOf(0) }

    LaunchedEffect(Unit) { apps = appRepo.getInstalledApps(); filtered = apps }
    LaunchedEffect(query, cat, apps) {
        filtered = apps.filter { a ->
            (query.isBlank() || a.appName.contains(query, true) || a.packageName.contains(query, true)) &&
            (cat == AppCategory.ALL || a.category == cat)
        }
    }

    Scaffold(
        topBar = { TopAppBar(title = { Text("Selecionar Apps") }, navigationIcon = { IconButton(onClick = onBack) { Icon(Icons.Default.ArrowBack, "Voltar") } }, colors = TopAppBarDefaults.topAppBarColors(containerColor = MaterialTheme.colorScheme.surface)) },
        bottomBar = if (selected.isNotEmpty()) {
            { Surface(tonalElevation = 8.dp) { Row(Modifier.fillMaxWidth().padding(16.dp), Arrangement.SpaceBetween, Alignment.CenterVertically) { Text("${selected.size} selecionado(s)"); Button({ cloning = true; total = selected.size; scope.launch { cloneRepo.cloneApps(selected.toList()) { a, b -> progress = a; total = b }; cloning = false; selected = emptySet() } }, enabled = !cloning && selected.size <= 30, colors = ButtonDefaults.buttonColors(containerColor = Primary)) { if (cloning) { CircularProgressIndicator(Modifier.size(18.dp), 2.dp, MaterialTheme.colorScheme.onPrimary); Spacer(Modifier.width(8.dp)) } Text("Clonar (${selected.size})") } } } }
        } else null
    ) { pad ->
        Column(Modifier.fillMaxSize().padding(pad)) {
            OutlinedTextField(query, { query = it }, placeholder = { Text("Buscar apps...") }, leadingIcon = { Icon(Icons.Default.Search, null) }, modifier = Modifier.fillMaxWidth().padding(horizontal = 16.dp, vertical = 8.dp), singleLine = true, shape = MaterialTheme.shapes.medium)
            LazyRow(Modifier.padding(horizontal = 16.dp), Arrangement.spacedBy(8.dp)) { items(AppCategory.entries) { FilterChip(cat == it, { cat = it }, label = { Text(it.displayName) }, colors = FilterChipDefaults.filterChipColors(selectedContainerColor = Primary, selectedLabelColor = MaterialTheme.colorScheme.onPrimary)) } }
            if (cloning) { LinearProgressIndicator(progress = { progress.toFloat() / total.coerceAtLeast(1) }, modifier = Modifier.fillMaxWidth().padding(horizontal = 16.dp)); Text("Clonando $progress de $total...", modifier = Modifier.padding(horizontal = 16.dp)) }
            LazyColumn(Modifier.fillMaxSize()) {
                items(filtered, key = { it.packageName }) { app ->
                    Card(onClick = { selected = if (app.packageName in selected) selected - app.packageName else if (selected.size < 30) selected + app.packageName else selected }, colors = CardDefaults.cardColors(containerColor = if (app.packageName in selected) MaterialTheme.colorScheme.primary.copy(alpha = 0.12f) else MaterialTheme.colorScheme.surfaceVariant), modifier = Modifier.fillMaxWidth().padding(horizontal = 16.dp, vertical = 2.dp)) {
                        Row(Modifier.fillMaxWidth().padding(12.dp), Alignment.CenterVertically) {
                            Icon(Icons.Default.Android, null, tint = Primary, Modifier.size(40.dp))
                            Column(Modifier.weight(1f).padding(horizontal = 12.dp)) { Text(app.appName, fontWeight = FontWeight.Medium, maxLines = 1, overflow = TextOverflow.Ellipsis); Text(app.packageName, style = MaterialTheme.typography.bodySmall, color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.5f), maxLines = 1) }
                            Checkbox(app.packageName in selected, { selected = if (app.packageName in selected) selected - app.packageName else selected + app.packageName })
                        }
                    }
                }
            }
        }
    }
}
