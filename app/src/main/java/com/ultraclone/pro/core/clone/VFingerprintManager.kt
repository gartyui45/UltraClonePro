package com.ultraclone.pro.core.clone

import android.content.Context
import android.content.SharedPreferences
import java.util.UUID

class VFingerprintManager(private val context: Context) {
    private val prefs: SharedPreferences = context.getSharedPreferences("fingerprints", Context.MODE_PRIVATE)

    fun generateFingerprint(cloneId: Long): Map<String, String> {
        val map = mapOf(
            "android_id" to UUID.randomUUID().toString().take(16),
            "serial" to "UCP" + (1..8).map { "0123456789ABCDEF".random() }.joinToString(""),
            "model" to listOf("SM-G998B", "Pixel 7 Pro", "M2101K6G").random(),
            "manufacturer" to listOf("samsung", "google", "xiaomi").random()
        )
        map.forEach { (k, v) -> prefs.edit().putString("${k}_$cloneId", v).apply() }
        return map
    }

    fun getFingerprint(cloneId: Long): Map<String, String> {
        return listOf("android_id", "serial", "model", "manufacturer").associateWith {
            prefs.getString("${it}_$cloneId", "") ?: ""
        }
    }

    fun removeFingerprint(cloneId: Long) {
        listOf("android_id", "serial", "model", "manufacturer").forEach { prefs.edit().remove("${it}_$cloneId").apply() }
    }
}
