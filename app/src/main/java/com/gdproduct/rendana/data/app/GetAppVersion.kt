package com.gdproduct.rendana.data.app

import android.content.Context
import android.content.pm.PackageManager

fun Context.GetAppVersion(): String {
    return packageManager
        .getPackageInfo(packageName, 0)
        .versionName ?: "Unknown"
}