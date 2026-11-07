package com.aura.tv.data.apps

import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.content.pm.ResolveInfo
import com.aura.tv.domain.models.InstalledApp
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class AppRepository(private val context: Context) {

    suspend fun loadInstalledApps(): List<InstalledApp> = withContext(Dispatchers.IO) {
        val pm = context.packageManager
        val selfPackage = context.packageName

        // Discover Leanback apps first
        val leanbackIntent = Intent(Intent.ACTION_MAIN, null).apply {
            addCategory(Intent.CATEGORY_LEANBACK_LAUNCHER)
        }
        val leanbackApps = pm.queryIntentActivities(leanbackIntent, 0)

        // Discover Standard launcher apps as fallback
        val standardIntent = Intent(Intent.ACTION_MAIN, null).apply {
            addCategory(Intent.CATEGORY_LAUNCHER)
        }
        val standardApps = pm.queryIntentActivities(standardIntent, 0)

        val seen = mutableSetOf<String>()
        val result = mutableListOf<InstalledApp>()

        fun processResolveInfo(info: ResolveInfo, isLeanback: Boolean) {
            val pkg = info.activityInfo.packageName
            if (pkg == selfPackage || seen.contains(pkg)) return
            seen.add(pkg)

            val label = try {
                info.loadLabel(pm).toString()
            } catch (e: Exception) {
                pkg
            }

            val icon = try {
                info.loadIcon(pm)
            } catch (e: Exception) {
                null
            }

            result.add(
                InstalledApp(
                    packageName = pkg,
                    activityName = info.activityInfo.name,
                    label = label,
                    icon = icon,
                    isLeanback = isLeanback
                )
            )
        }

        for (info in leanbackApps) {
            processResolveInfo(info, isLeanback = true)
        }

        for (info in standardApps) {
            processResolveInfo(info, isLeanback = false)
        }

        result.sortedBy { it.label.lowercase() }
    }

    fun launchApp(app: InstalledApp): Boolean {
        return try {
            val intent = if (app.isLeanback) {
                context.packageManager.getLeanbackLaunchIntentForPackage(app.packageName)
                    ?: context.packageManager.getLaunchIntentForPackage(app.packageName)
            } else {
                context.packageManager.getLaunchIntentForPackage(app.packageName)
            }
            if (intent != null) {
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
                context.startActivity(intent)
                true
            } else {
                false
            }
        } catch (e: Exception) {
            false
        }
    }
}
