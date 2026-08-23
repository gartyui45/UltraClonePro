package com.ultraclone.pro.data.repository

import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import com.ultraclone.pro.data.model.AppCategory
import com.ultraclone.pro.data.model.InstalledApp
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class AppRepository(private val context: Context) {
    suspend fun getInstalledApps(): List<InstalledApp> = withContext(Dispatchers.IO) {
        val pm = context.packageManager
        val intent = Intent(Intent.ACTION_MAIN).apply { addCategory(Intent.CATEGORY_LAUNCHER) }
        val activities = pm.queryIntentActivities(intent, 0)
        val unique = mutableSetOf<String>()
        activities.mapNotNull { res ->
            val pkg = res.activityInfo.packageName
            if (pkg in unique || pkg == context.packageName) return@mapNotNull null
            unique.add(pkg)
            try {
                val info = pm.getApplicationInfo(pkg, 0)
                InstalledApp(pkg, pm.getApplicationLabel(info).toString(),
                    pm.getPackageInfo(pkg, 0).versionName ?: "1.0",
                    if (android.os.Build.VERSION.SDK_INT >= 28) pm.getPackageInfo(pkg, 0).longVersionCode else pm.getPackageInfo(pkg, 0).versionCode.toLong(),
                    info.loadIcon(pm), AppCategory.fromPackageName(pkg),
                    (info.flags and android.content.pm.ApplicationInfo.FLAG_SYSTEM) != 0, info.sourceDir ?: "")
            } catch (e: Exception) { null }
        }.sortedBy { it.appName }
    }
}
