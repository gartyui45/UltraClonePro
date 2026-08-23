package com.ultraclone.pro.core.clone

import android.content.Context

class VPackageManager(private val context: Context) {
    private val realPm = context.packageManager
    fun getPackageInfo(pkg: String) = try { realPm.getPackageInfo(pkg, 0) } catch (e: Exception) { null }
}
