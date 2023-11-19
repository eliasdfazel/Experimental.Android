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

import android.R.id.text1
import android.R.layout.simple_list_item_1
import android.content.Context
import android.content.Intent
import android.widget.RemoteViews
import android.widget.RemoteViewsService


class WidgetDataProvider(private val context: Context, intent: Intent?) : RemoteViewsService.RemoteViewsFactory {

    var myListView: MutableList<String> = ArrayList()

    override fun onCreate() {
        initData()
    }

    override fun onDataSetChanged() {
        initData()
    }

    override fun onDestroy() {}

    override fun getCount(): Int {
        return myListView.size
    }

    override fun getViewAt(position: Int): RemoteViews {

        val remoteViews = RemoteViews(context.packageName, simple_list_item_1)

        remoteViews.setTextViewText(text1, myListView[position])

        return remoteViews
    }

    override fun getLoadingView(): RemoteViews? {
        return null
    }

    override fun getViewTypeCount(): Int {
        return 1
    }

    override fun getItemId(position: Int): Long {
        return position.toLong()
    }

    override fun hasStableIds(): Boolean {
        return true
    }

    private fun initData() {
        myListView.clear()


        for (i in 1..15) {

            myListView.add("ListView item $i")

        }

    }

}