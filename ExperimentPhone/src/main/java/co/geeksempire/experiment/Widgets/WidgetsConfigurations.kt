/*
 * Copyright © 2023 By Geeks Empire.
 *
 * Created by Elias Fazel
 * Last modified 11/16/23, 4:17 AM
 *
 * Licensed Under MIT License.
 * https://opensource.org/licenses/MIT
 */

package co.geeksempire.experiment.Widgets

import android.appwidget.AppWidgetHost
import android.appwidget.AppWidgetManager
import android.appwidget.AppWidgetProviderInfo
import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import co.geeksempire.experiment.databinding.WidgetConfigurationsLayoutBinding
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.async

class WidgetsConfigurations : AppCompatActivity() {


    lateinit var widgetsConfigurationsLayoutBinding: WidgetConfigurationsLayoutBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        widgetsConfigurationsLayoutBinding = WidgetConfigurationsLayoutBinding.inflate(layoutInflater)
        setContentView(widgetsConfigurationsLayoutBinding.root)

        loadInstalledWidgets()

    }

    fun loadInstalledWidgets() = CoroutineScope(SupervisorJob() + Dispatchers.IO).async {

        val widgetManager = AppWidgetManager.getInstance(applicationContext)

        val widgetHost = AppWidgetHost(applicationContext, System.currentTimeMillis().toInt())

        val widgetProviders = widgetManager.installedProviders as ArrayList<AppWidgetProviderInfo>

        widgetProviders.forEach { appWidgetProviderInfo ->
            Log.d(this@WidgetsConfigurations.javaClass.simpleName, "Widget: ${appWidgetProviderInfo.loadLabel(packageManager)}")



        }

    }

}