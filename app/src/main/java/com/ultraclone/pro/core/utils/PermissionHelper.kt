package com.ultraclone.pro.core.utils

import android.Manifest
import android.app.Activity
import android.content.Context
import android.content.pm.PackageManager
import android.os.Build
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat

object PermissionHelper {
    fun hasPermissions(context: Context): Boolean {
        val perms = listOf(Manifest.permission.INTERNET, Manifest.permission.QUERY_ALL_PACKAGES)
        return perms.all { ContextCompat.checkSelfPermission(context, it) == PackageManager.PERMISSION_GRANTED }
    }

    fun requestPermissions(activity: Activity, code: Int = 1001) {
        val needed = listOf(Manifest.permission.INTERNET, Manifest.permission.QUERY_ALL_PACKAGES).filter {
            ContextCompat.checkSelfPermission(activity, it) != PackageManager.PERMISSION_GRANTED
        }
        if (needed.isNotEmpty()) ActivityCompat.requestPermissions(activity, needed.toTypedArray(), code)
    }
}
