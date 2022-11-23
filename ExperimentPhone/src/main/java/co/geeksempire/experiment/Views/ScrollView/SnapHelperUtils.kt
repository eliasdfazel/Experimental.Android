/*
 * Copyright © 2022 By Geeks Empire.
 *
 * Created by Elias Fazel
 * Last modified 10/27/22, 9:15 AM
 *
 * Licensed Under MIT License.
 * https://opensource.org/licenses/MIT
 */

package co.geeksempire.sachiel.signals.administrators.Utils.Views.Scrolls

import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.SnapHelper

fun snappedItemPosition(recyclerView: RecyclerView, snapHelper: SnapHelper) : Int {

    val layoutManager = recyclerView.layoutManager
    val snapView = snapHelper.findSnapView(layoutManager)

    return snapView?.let { layoutManager?.getPosition(it) } ?:0
}