package com.shebang.dog.goo.util

import android.Manifest
import android.app.Activity
import android.content.pm.PackageManager.PERMISSION_GRANTED
import androidx.core.content.ContextCompat

object PermissionGranter {
    private val permissions = listOf(
        Manifest.permission.ACCESS_FINE_LOCATION,
        Manifest.permission.ACCESS_COARSE_LOCATION,
        Manifest.permission.INTERNET
    )

    private const val REQUEST_CODE = 34

    fun requestPermission(activity: Activity) {
        permissions
            .filter { ContextCompat.checkSelfPermission(activity, it) != PERMISSION_GRANTED }
            .takeUnless { it.isEmpty() }
            ?.also { activity.requestPermissions(it.toTypedArray(), REQUEST_CODE) }
    }

}