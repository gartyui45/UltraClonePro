package com.ultraclone.pro.core.clone

import android.content.Context
import java.io.File

class VEnvironment(private val context: Context) {
    fun getCloneDir(cloneId: Long) = File(context.getDir("ultraclone_virtual", Context.MODE_PRIVATE), "clones/clone_$cloneId")
}
