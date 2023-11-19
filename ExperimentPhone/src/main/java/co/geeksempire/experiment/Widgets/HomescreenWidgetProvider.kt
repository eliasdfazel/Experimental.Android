/*
 * Copyright © 2023 By Geeks Empire.
 *
 * Created by Elias Fazel
 * Last modified 11/19/23, 5:47 AM
 *
 * Licensed Under MIT License.
 * https://opensource.org/licenses/MIT
 */

package co.geeksempire.experiment.Widgets

import android.appwidget.AppWidgetHost
import android.appwidget.AppWidgetHostView
import android.appwidget.AppWidgetManager
import android.appwidget.AppWidgetProvider
import android.appwidget.AppWidgetProviderInfo
import android.content.Context
import android.os.Bundle
import android.util.Log
import android.widget.RemoteViews
import co.geeksempire.experiment.R

class HomescreenWidgetProvider : AppWidgetProvider() {

    override fun onEnabled(context: Context?) {
        super.onEnabled(context)
        Log.d(this@HomescreenWidgetProvider.javaClass.simpleName, "Enabled")
    }

    override fun onUpdate(context: Context?, appWidgetManager: AppWidgetManager?, appWidgetIds: IntArray?) {
        super.onUpdate(context, appWidgetManager, appWidgetIds)

        if (context != null) {

            appWidgetIds?.let { widgetId ->
                Log.d(this@HomescreenWidgetProvider.javaClass.simpleName, "Widget Id: ${widgetId.first()}")

                for (index in appWidgetIds.indices) {

                    val currentWidgetId = appWidgetIds[index]

                    val remoteViews = RemoteViews(context.packageName, R.layout.remote_widget_view)

                    appWidgetManager?.updateAppWidget(currentWidgetId, remoteViews)

                }

            }

        }

    }

    fun prepareWidget(context: Context, appWidgetManager: AppWidgetManager, appWidgetProviderInfo: AppWidgetProviderInfo, widgetId: Int) : AppWidgetHostView {

        val appWidgetHost = AppWidgetHost(context, System.currentTimeMillis().toInt())

        appWidgetHost.startListening()

        val hostView: AppWidgetHostView = appWidgetHost.createView(context, widgetId, appWidgetProviderInfo)
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

        return hostView;
    }

}