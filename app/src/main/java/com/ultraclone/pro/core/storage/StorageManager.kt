package com.ultraclone.pro.core.storage

import android.content.Context
import java.io.File

class StorageManager(private val context: Context) {
    private val baseDir get() = context.getDir("ultraclone_virtual", Context.MODE_PRIVATE)

    fun getTotalStorageUsed(): Long {
        if (!baseDir.exists()) return 0
        return baseDir.walkTopDown().filter { it.isFile }.sumOf { it.length() }
    }

    fun getFormattedSize(bytes: Long): String = when {
        bytes < 1024 -> "$bytes B"
        bytes < 1048576 -> String.format("%.1f KB", bytes / 1024.0)
        bytes < 1073741824 -> String.format("%.1f MB", bytes / 1048576.0)
        else -> String.format("%.1f GB", bytes / 1.073741824E9)
    }
}
