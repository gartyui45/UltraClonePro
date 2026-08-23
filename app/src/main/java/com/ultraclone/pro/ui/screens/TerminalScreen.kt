package com.ultraclone.pro.ui.screens

import android.widget.TextView
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.viewinterop.AndroidView
import com.ultraclone.pro.core.root.TermuxBridge

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TerminalScreen(cloneId: Long, onBack: () -> Unit) {
    val ctx = androidx.compose.ui.platform.LocalContext.current
    val bridge = remember { TermuxBridge(ctx) }
    var output by remember { mutableStateOf("") }; var cmd by remember { mutableStateOf("") }
    var installed by remember { mutableStateOf(bridge.isTermuxInstalled()) }

    Scaffold(
        topBar = { TopAppBar(title = { Text("Terminal #$cloneId") }, navigationIcon = { IconButton(onClick = onBack) { Icon(Icons.Default.ArrowBack, "Voltar") } }) }
    ) { pad ->
        Column(Modifier.fillMaxSize().padding(pad)) {
            Box(Modifier.weight(1f).fillMaxWidth().padding(8.dp)) {
                AndroidView({ TextView(it).apply { textSize = 12f; typeface = android.graphics.Typeface.MONOSPACE; setTextColor(-16777216); setBackgroundColor(-14540254); text = "UltraClone Terminal #$cloneId\nProot emulado\n\n" } }, update = { it.append(output) }, modifier = Modifier.fillMaxSize())
            }
            Surface(tonalElevation = 4.dp) {
                Row(Modifier.fillMaxWidth().padding(8.dp), Alignment.CenterVertically) {
                    Text("$ ", style = TextStyle(fontFamily = FontFamily.Monospace, fontSize = 14.sp, color = MaterialTheme.colorScheme.primary))
                    BasicTextField(cmd, { cmd = it }, Modifier.weight(1f), textStyle = TextStyle(fontFamily = FontFamily.Monospace, fontSize = 14.sp), singleLine = true)
                    IconButton({ output = when(cmd) { "help" -> "Comandos: su, whoami, id, ls, pwd, df, clear, exit\n"; "whoami" -> "root\n"; "id" -> "uid=0(root)\n"; "pwd" -> "/home\n"; "su" -> "root@ultraclone:~# \n"; "ls" -> "bin dev etc home proc usr\n"; "clear" -> ""; else -> "sh: ${cmd}: not found\n" }; cmd = "" }) { Icon(Icons.Default.Send, "Executar") }
                }
            }
            if (!installed) Button(onClick = { installed = bridge.installTermuxBootstrap(); output = if (installed) "OK\n" else "FAIL\n" }, modifier = Modifier.fillMaxWidth().padding(8.dp)) { Icon(Icons.Default.Download, null); Text("Instalar Termux+Proot") }
        }
    }
}
