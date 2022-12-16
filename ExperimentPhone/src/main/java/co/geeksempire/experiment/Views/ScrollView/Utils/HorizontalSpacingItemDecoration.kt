/*
 * Copyright © 2022 By Geeks Empire.
 *
 * Created by Elias Fazel
 * Last modified 12/16/22, 6:19 AM
 *
 * Licensed Under MIT License.
 * https://opensource.org/licenses/MIT
 */

package co.geeksempire.experiment.Views.ScrollView.Utils

import android.content.Context
import android.graphics.Rect
import android.view.View
import androidx.recyclerview.widget.RecyclerView
import co.geeksempire.sachiel.signals.administrators.Utils.Display.dpToPixel
import kotlin.math.roundToInt

class HorizontalSpacingItemDecoration(context: Context, space: Int = 19) : RecyclerView.ItemDecoration() {

    private val spaceInDp = dpToPixel(context, space.toFloat())

    override fun getItemOffsets(outRect: Rect, view: View, parent: RecyclerView, state: RecyclerView.State) {

        outRect.right = spaceInDp.roundToInt()

    }
}