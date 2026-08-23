package com.ultraclone.pro.data.model

import android.graphics.drawable.Drawable

data class InstalledApp(
    val packageName: String,
    val appName: String,
    val versionName: String,
    val versionCode: Long,
    val icon: Drawable?,
    val category: AppCategory,
    val isSystemApp: Boolean,
    val apkPath: String
)
