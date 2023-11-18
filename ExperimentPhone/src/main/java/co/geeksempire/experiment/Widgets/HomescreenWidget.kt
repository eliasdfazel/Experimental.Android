/*
 * Copyright © 2023 By Geeks Empire.
 *
 * Created by Elias Fazel
 * Last modified 11/18/23, 9:35 AM
 *
 * Licensed Under MIT License.
 * https://opensource.org/licenses/MIT
 */

package co.geeksempire.experiment.Widgets

import android.app.PendingIntent
import android.appwidget.AppWidgetManager
import android.appwidget.AppWidgetProvider
import android.content.Context
import android.content.Intent
import android.os.Handler
import android.os.Looper
import android.widget.RemoteViews
import co.geeksempire.experiment.R
import co.geeksempire.experiment.Tests.BlurryTests

class HomescreenWidget : AppWidgetProvider() {

    override fun onEnabled(context: Context?) {
        super.onEnabled(context)
        println(">>> >> > Enabled")
    }

    override fun onUpdate(context: Context?, appWidgetManager: AppWidgetManager?, appWidgetIds: IntArray?) {
        super.onUpdate(context, appWidgetManager, appWidgetIds)

        if (context != null) {

            appWidgetIds?.let { widgetId ->
                println(">>> >> > Widget Id: ${widgetId.first()}")

                for (index in appWidgetIds.indices) {

                    val currentWidgetId = appWidgetIds[index]

                    val searchEngineIntent = Intent(context, BlurryTests::class.java)
                    searchEngineIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)

                    val searchEnginePendingIntent = PendingIntent.getActivity(context, 0, searchEngineIntent, PendingIntent.FLAG_UPDATE_CURRENT or  PendingIntent.FLAG_IMMUTABLE)

                    val remoteViews = RemoteViews(context.packageName, R.layout.remote_widget_view)

                    remoteViews.setOnClickPendingIntent(R.id.widgetList, searchEnginePendingIntent)

                    appWidgetManager?.updateAppWidget(currentWidgetId, remoteViews)

                    Handler(Looper.getMainLooper()).postDelayed({

                        repeat (3) {
                            println(">>> >> > Repeat $it")

                            remoteViews.addView(R.id.widgetList, RemoteViews(context.packageName, R.layout.remote_widget_item))

                        }

                        appWidgetManager?.notifyAppWidgetViewDataChanged(widgetId, R.id.widgetList)

                        appWidgetManager?.updateAppWidget(widgetId.first(), remoteViews)


                    }, 333)

                }

            }

        }

    }

}