package com.ultraclone.pro.data.model

data class ClonedApp(
    val id: Long = 0,
    val originalPackageName: String,
    val clonePackageName: String,
    val cloneLabel: String,
    val fingerprintId: String,
    val createdAt: Long = System.currentTimeMillis(),
    val lastOpened: Long = 0,
    val storageUsedBytes: Long = 0,
    val isHidden: Boolean = false,
    val isRunning: Boolean = false
)
