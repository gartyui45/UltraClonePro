package com.ultraclone.pro.core.root

import android.content.Context
import java.io.File

class TermuxBridge(private val context: Context) {
    private val termuxDir get() = File(context.filesDir, "termux")

    fun isTermuxInstalled(): Boolean = File(termuxDir, "usr/bin/proot").exists()

    fun installTermuxBootstrap(): Boolean {
        termuxDir.mkdirs()
        File(termuxDir, "home").mkdirs()
        File(termuxDir, "usr").mkdirs()
        File(termuxDir, "usr/bin").mkdirs()
        return true
    }

    fun getProotCommand(cloneId: Long): List<String> {
        val cloneDir = context.filesDir.resolve("ultraclone_virtual/clones/clone_$cloneId").absolutePath
        return listOf("$termuxDir/usr/bin/proot", "-0", "-r", "$cloneDir/termux", "-b", "/dev", "-b", "/proc", "-w", "/home", "/usr/bin/env", "HOME=/home", "TERM=xterm-256color", "PATH=/usr/bin:/usr/local/bin", "/usr/bin/bash")
    }
}
