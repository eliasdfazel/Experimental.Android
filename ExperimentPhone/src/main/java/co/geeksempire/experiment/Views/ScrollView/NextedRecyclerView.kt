/*
 * Copyright © 2022 By Geeks Empire.
 *
 * Created by Elias Fazel
 * Last modified 11/19/22, 5:43 AM
 *
 * Licensed Under MIT License.
 * https://opensource.org/licenses/MIT
 */

package co.geeksempire.sachiel.signals.administrators.Utils.Views.Scrolls

import android.content.Context
import android.graphics.Canvas
import android.graphics.Path
import android.graphics.RectF
import android.util.AttributeSet
import androidx.recyclerview.widget.RecyclerView
import co.geeksempire.sachiel.signals.administrators.Utils.Display.dpToFloat

class NextedRecyclerView(context: Context, attributesSet: AttributeSet) : RecyclerView(context, attributesSet) {

    private lateinit var rectF: RectF

    private val path = Path()

    private var cornerRadius = dpToFloat(context, 19f)

    override fun onSizeChanged(w: Int, h: Int, oldw: Int, oldh: Int) {
        super.onSizeChanged(w, h, oldw, oldh)

        rectF = RectF(0f, 0f, w.toFloat(), h.toFloat())

        resetPath()
    }

    override fun draw(canvas: Canvas) {

        val save = canvas.save()

        canvas.clipPath(path)

        super.draw(canvas)

        canvas.restoreToCount(save)

    }

    override fun dispatchDraw(canvas: Canvas) {

        val save = canvas.save()

        canvas.clipPath(path)

        super.dispatchDraw(canvas)

        canvas.restoreToCount(save)

    }

    private fun resetPath() {

        path.reset()

        path.addRoundRect(rectF, cornerRadius, cornerRadius, Path.Direction.CW)

        path.close()

    }

}