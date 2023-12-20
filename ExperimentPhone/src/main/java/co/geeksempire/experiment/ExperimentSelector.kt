/*
 * Copyright © 2023 By Geeks Empire.
 *
 * Created by Elias Fazel
 * Last modified 12/20/23, 5:33 AM
 *
 * Licensed Under MIT License.
 * https://opensource.org/licenses/MIT
 */

package co.geeksempire.experiment

import android.content.Intent
import android.content.pm.PackageManager
import android.content.pm.ResolveInfo
import android.graphics.Color
import android.os.Bundle
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

        installedApplicationsQuery()

    }

    fun installedApplicationsQuery() = CoroutineScope(Dispatchers.IO + SupervisorJob()).async {

        val applicationInfoList = packageManager.queryIntentActivities(Intent().apply {
            action = Intent.ACTION_MAIN
            addCategory(Intent.CATEGORY_LAUNCHER)
        }, PackageManager.MATCH_UNINSTALLED_PACKAGES).sortedWith(ResolveInfo.DisplayNameComparator(packageManager))

        applicationInfoList.asFlow()
            .filter {

                (packageManager.getLaunchIntentForPackage(it.activityInfo.packageName) != null)
            }
            .withIndex().collect {
                Log.d(this@ExperimentSelector.javaClass.simpleName, "${it.index}. Installed Application: ${it.value.activityInfo.applicationInfo.loadLabel(packageManager)}")
            }

    }

}