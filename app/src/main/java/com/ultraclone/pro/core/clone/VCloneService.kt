package com.ultraclone.pro.core.clone

import android.app.Service
import android.content.Intent
import android.os.IBinder

class VCloneService : Service() {
    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        intent?.let {
            val cloneId = it.getLongExtra("clone_id", -1)
            val pkg = it.getStringExtra("original_package") ?: ""
            if (cloneId >= 0) {
                // Em produção: carregar APK do sandbox e lançar Activity
            }
        }
        return START_NOT_STICKY
    }
    override fun onBind(intent: Intent?) = null
}
