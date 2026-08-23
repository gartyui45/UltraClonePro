package com.ultraclone.pro.core.clone

import android.content.Context
import android.content.Intent

class VProcess(private val context: Context) {
    fun launchCloneProcess(cloneId: Long, packageName: String) {
        val intent = Intent(context, VCloneService::class.java)
        intent.putExtra("clone_id", cloneId)
        intent.putExtra("package_name", packageName)
        context.startService(intent)
    }
}
