package com.ultraclone.pro.core.clone

import android.app.Service
import android.content.Intent
import android.os.IBinder

class VEngineService : Service() {
    override fun onBind(intent: Intent?) = null
    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int = START_STICKY
}
