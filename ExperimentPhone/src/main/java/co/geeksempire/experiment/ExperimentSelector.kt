/*
 * Copyright © 2024 By Geeks Empire.
 *
 * Created by Elias Fazel
 * Last modified 3/24/24, 12:50 PM
 *
 * Licensed Under MIT License.
 * https://opensource.org/licenses/MIT
 */

package co.geeksempire.experiment

import android.content.Context
import android.content.Intent
import android.content.pm.LauncherApps
import android.content.pm.PackageManager
import android.graphics.Color
import android.os.Bundle
import android.os.Process
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import co.geeksempire.experiment.databinding.ExperimentSelectorLayoutBinding
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.async
import kotlinx.coroutines.flow.asFlow
import kotlinx.coroutines.flow.filter
import kotlinx.coroutines.flow.withIndex

class ExperimentSelector : AppCompatActivity() {

    lateinit var experimentSelectorLayoutBinding: ExperimentSelectorLayoutBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        experimentSelectorLayoutBinding = ExperimentSelectorLayoutBinding.inflate(layoutInflater)
        setContentView(experimentSelectorLayoutBinding.root)

        window.decorView.setBackgroundColor(Color.CYAN)
        experimentSelectorLayoutBinding.root.background = getDrawable(R.drawable.splash_screen_initial)

        experimentSelectorLayoutBinding.experimenting.setOnClickListener {

            val launcherApps = getSystemService(Context.LAUNCHER_APPS_SERVICE) as LauncherApps

            launcherApps.startShortcut("com.google.android.keep", "manifest_new_note_shortcut",
                null, null, Process.myUserHandle())
            //manifest_new_note_shortcut
            //com.google.android.keep
        }

    }

    fun installedApplicationsQuery() = CoroutineScope(Dispatchers.IO + SupervisorJob()).async {

        val applicationInfoList = packageManager.queryIntentActivities(Intent().apply {
            action = Intent.ACTION_MAIN
            addCategory(Intent.CATEGORY_LAUNCHER)
        }, PackageManager.MATCH_ALL)

        applicationInfoList.asFlow()
            .filter {

                (packageManager.getLaunchIntentForPackage(it.activityInfo.packageName) != null)
            }
            .withIndex().collect {
                Log.d(this@ExperimentSelector.javaClass.simpleName, "${it.index}. Installed Application: ${it.value.activityInfo.applicationInfo.loadLabel(packageManager)} - ${it.value.activityInfo.packageName}")

                getShortcuts(it.value.activityInfo.packageName)

            }

    }

    fun getShortcuts(aPackageName: String) = CoroutineScope(Dispatchers.IO).async {

        val shortcutQuery = LauncherApps.ShortcutQuery()
        shortcutQuery.setQueryFlags(LauncherApps.ShortcutQuery.FLAG_MATCH_DYNAMIC or LauncherApps.ShortcutQuery.FLAG_MATCH_MANIFEST or LauncherApps.ShortcutQuery.FLAG_MATCH_PINNED)
        shortcutQuery.setPackage(aPackageName)

        val launcherApps = getSystemService(Context.LAUNCHER_APPS_SERVICE) as LauncherApps

        try {

            launcherApps.getShortcuts(shortcutQuery, Process.myUserHandle())?.forEach {

                println(it.id)
                //manifest_new_note_shortcut
                //com.google.android.keep

            }

//            launcherApps.startShortcut()

        } catch (e: SecurityException) {

        }

    }

}