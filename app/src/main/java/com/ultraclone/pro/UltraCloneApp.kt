package com.ultraclone.pro

import android.app.Application
import com.ultraclone.pro.core.clone.VAppManager
import com.ultraclone.pro.core.storage.StorageManager

class UltraCloneApp : Application() {
    lateinit var vAppManager: VAppManager
    lateinit var storageManager: StorageManager

    override fun onCreate() {
        super.onCreate()
        instance = this
        storageManager = StorageManager(this)
        vAppManager = VAppManager(this)
        vAppManager.initialize()
    }

    companion object {
        lateinit var instance: UltraCloneApp
            private set
    }
}
