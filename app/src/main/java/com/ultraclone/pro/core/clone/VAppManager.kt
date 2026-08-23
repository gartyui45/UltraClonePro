package com.ultraclone.pro.core.clone

import android.content.Context
import android.content.SharedPreferences
import com.ultraclone.pro.data.model.ClonedApp
import java.io.File
import java.util.UUID

class VAppManager(private val context: Context) {
    private val prefs: SharedPreferences = context.getSharedPreferences("vapp_manager", Context.MODE_PRIVATE)
    private val clonesDir: File
    private val clones = mutableListOf<ClonedApp>()
    private var nextId: Long = 1

    init {
        clonesDir = File(context.getDir("ultraclone_virtual", Context.MODE_PRIVATE), "clones").also { it.mkdirs() }
    }

    fun initialize() { loadClones() }

    fun getAllClonedApps(): List<ClonedApp> = clones.toList()
    fun getClone(id: Long): ClonedApp? = clones.find { it.id == id }

    fun cloneApp(packageName: String): ClonedApp? {
        try {
            val pm = context.packageManager
            val appInfo = pm.getApplicationInfo(packageName, 0)
            val appName = pm.getApplicationLabel(appInfo).toString()
            val cloneId = nextId++
            val fingerprintId = UUID.randomUUID().toString()

            val cloneDir = File(clonesDir, "clone_$cloneId").also { it.mkdirs() }
            File(cloneDir, "data").mkdirs()
            File(cloneDir, "data/data/$packageName").mkdirs()
            File(cloneDir, "cache").mkdirs()
            File(cloneDir, "files").mkdirs()
            File(cloneDir, "obb").mkdirs()
            File(cloneDir, "termux/home").mkdirs()

            val apkFile = File(appInfo.sourceDir)
            apkFile.copyTo(File(cloneDir, "base.apk"), overwrite = true)

            val clone = ClonedApp(cloneId, packageName, "com.ultraclone.pro.clone.$cloneId", "$appName Clone", fingerprintId)
            clones.add(clone)
            saveClones()
            return clone
        } catch (e: Exception) { return null }
    }

    fun launchClone(cloneId: Long) {
        val clone = getClone(cloneId) ?: return
        try {
            val intent = android.content.Intent(context, VCloneService::class.java)
            intent.putExtra("clone_id", cloneId)
            intent.putExtra("original_package", clone.originalPackageName)
            context.startForegroundService(intent)
        } catch (e: Exception) { e.printStackTrace() }
    }

    fun deleteClone(cloneId: Long) {
        File(clonesDir, "clone_$cloneId").deleteRecursively()
        clones.removeAll { it.id == cloneId }
        saveClones()
    }

    fun regenerateFingerprint(id: Long) {
        val idx = clones.indexOfFirst { it.id == id }
        if (idx >= 0) clones[idx] = clones[idx].copy(fingerprintId = UUID.randomUUID().toString())
        saveClones()
    }

    fun toggleHidden(id: Long) {
        val idx = clones.indexOfFirst { it.id == id }
        if (idx >= 0) { clones[idx] = clones[idx].copy(isHidden = !clones[idx].isHidden); saveClones() }
    }

    fun cleanCache(id: Long) { File(clonesDir, "clone_$id/cache").deleteRecursively() }

    private fun loadClones() {
        val json = prefs.getString("clones_json", null) ?: return
        try {
            json.split(";;;").forEach { item ->
                val p = item.split("|")
                if (p.size >= 6) clones.add(ClonedApp(p[0].toLong(), p[1], p[2], p[3], p[4], p[5].toLong()))
            }
            nextId = (clones.maxOfOrNull { it.id } ?: 0) + 1
        } catch (e: Exception) { clones.clear(); prefs.edit().remove("clones_json").apply() }
    }

    private fun saveClones() {
        prefs.edit().putString("clones_json", clones.joinToString(";;;") {
            "${it.id}|${it.originalPackageName}|${it.clonePackageName}|${it.cloneLabel}|${it.fingerprintId}|${it.createdAt}"
        }).apply()
    }
}
