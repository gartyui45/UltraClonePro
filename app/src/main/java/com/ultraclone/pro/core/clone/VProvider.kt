package com.ultraclone.pro.core.clone

import android.content.ContentProvider
import android.content.ContentValues
import android.database.Cursor
import android.database.MatrixCursor
import android.net.Uri

class VProvider : ContentProvider() {
    override fun onCreate(): Boolean = true

    override fun query(uri: Uri, projection: Array<out String>?, selection: String?, selectionArgs: Array<out String>?, sortOrder: String?): Cursor? {
        val cloneId = uri.getQueryParameter("clone_id")?.toLongOrNull() ?: return null
        if (uri.lastPathSegment == "fingerprint" && context != null) {
            val fp = VFingerprintManager(context!!).getFingerprint(cloneId)
            val c = MatrixCursor(arrayOf("key", "value"))
            fp.forEach { (k, v) -> c.addRow(arrayOf(k, v)) }
            return c
        }
        return null
    }
    override fun getType(uri: Uri): String = "vnd.android.cursor.item/vnd.ultraclone.profile"
    override fun insert(uri: Uri, cv: ContentValues?) = null
    override fun delete(uri: Uri, s: String?, a: Array<out String>?) = 0
    override fun update(uri: Uri, cv: ContentValues?, s: String?, a: Array<out String>?) = 0
}
