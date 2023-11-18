/*
 * Copyright © 2023 By Geeks Empire.
 *
 * Created by Elias Fazel
 * Last modified 11/18/23, 9:29 AM
 *
 * Licensed Under MIT License.
 * https://opensource.org/licenses/MIT
 */

package co.geeksempire.experiment.Widgets

import android.appwidget.AppWidgetHost
import android.appwidget.AppWidgetManager
import android.appwidget.AppWidgetProviderInfo
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import co.geeksempire.experiment.databinding.WidgetConfigurationsLayoutBinding
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.async
import kotlinx.coroutines.withContext

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
        Log.d(this@WidgetsConfigurations.javaClass.simpleName, "Widgets: ${widgetProviders.size}")

        widgetProviders.forEach { appWidgetProviderInfo ->
            Log.d(this@WidgetsConfigurations.javaClass.simpleName, "Widget: ${appWidgetProviderInfo.loadLabel(packageManager)}")
        }

       withContext(Dispatchers.Main) {

           createWidget(applicationContext, widgetsConfigurationsLayoutBinding.widgetWrapper,
               widgetManager, widgetHost, widgetProviders[1], 1)

           widgetsConfigurationsLayoutBinding.widgetAction.setOnClickListener {
               println("Widget Clicked")

               val intent = Intent(AppWidgetManager.ACTION_APPWIDGET_BIND).apply {
                   putExtra(AppWidgetManager.EXTRA_APPWIDGET_ID, 1)
                   putExtra(AppWidgetManager.EXTRA_APPWIDGET_PROVIDER, widgetProviders.first())
//                   putExtra(AppWidgetManager.EXTRA_APPWIDGET_OPTIONS, options)
               }
               startActivityForResult(intent, 666)

           }

       }

    }

    fun createWidget(context: Context, widgetView: ViewGroup, appWidgetManager: AppWidgetManager, appWidgetHost: AppWidgetHost, appWidgetProviderInfo: AppWidgetProviderInfo, widgetId: Int) {
        widgetView.removeAllViews()

        appWidgetHost.startListening()

        val hostView = appWidgetHost.createView(context, widgetId, appWidgetProviderInfo)
        hostView.setAppWidget(widgetId, appWidgetProviderInfo)

        val widgetWidth = 313
        val widgetHeight = 313

        hostView.minimumWidth = widgetWidth
        hostView.minimumHeight = widgetHeight

        val bundle = Bundle()
        bundle.putInt(AppWidgetManager.OPTION_APPWIDGET_MIN_WIDTH, widgetWidth)
        bundle.putInt(AppWidgetManager.OPTION_APPWIDGET_MIN_HEIGHT, widgetHeight)
        bundle.putInt(AppWidgetManager.OPTION_APPWIDGET_MAX_WIDTH, 313)
        bundle.putInt(AppWidgetManager.OPTION_APPWIDGET_MAX_HEIGHT, 313)

        appWidgetManager.bindAppWidgetIdIfAllowed(widgetId, appWidgetProviderInfo.provider, bundle)

        widgetView.addView(hostView)
    }

}