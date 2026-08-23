package com.ultraclone.pro.data.repository

import android.content.Context
import com.ultraclone.pro.UltraCloneApp
import com.ultraclone.pro.data.model.ClonedApp
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

class CloneRepository(private val context: Context) {
    private val vAppManager get() = UltraCloneApp.instance.vAppManager
    private val _clonedApps = MutableStateFlow<List<ClonedApp>>(emptyList())
    val clonedApps: StateFlow<List<ClonedApp>> = _clonedApps.asStateFlow()

    init { refresh() }

    fun refresh() { _clonedApps.value = vAppManager.getAllClonedApps() }

    suspend fun cloneApps(packageNames: List<String>, onProgress: (Int, Int) -> Unit): List<ClonedApp> {
        val results = mutableListOf<ClonedApp>()
        packageNames.forEachIndexed { i, pkg -> onProgress(i + 1, packageNames.size); vAppManager.cloneApp(pkg)?.let { results.add(it) } }
        refresh(); return results
    }
    fun deleteClone(id: Long) { vAppManager.deleteClone(id); refresh() }
    fun launchClone(id: Long) { vAppManager.launchClone(id) }
    fun regenerateFingerprint(id: Long) { vAppManager.regenerateFingerprint(id) }
    fun toggleHidden(id: Long) { vAppManager.toggleHidden(id); refresh() }
    fun cleanCache(id: Long) { vAppManager.cleanCache(id); refresh() }
}
