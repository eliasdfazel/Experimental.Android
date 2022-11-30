/*
 * Copyright © 2022 By Geeks Empire.
 *
 * Created by Elias Fazel
 * Last modified 11/30/22, 4:47 AM
 *
 * Licensed Under MIT License.
 * https://opensource.org/licenses/MIT
 */

package co.geeksempire.experiment.Views.ViewGroup

import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Path
import android.graphics.RectF
import android.util.AttributeSet
import androidx.constraintlayout.widget.ConstraintLayout
import co.geeksempire.sachiel.signals.administrators.Utils.Display.dpToFloat

class NextedConstraintLayout(context: Context, attributesSet: AttributeSet) : ConstraintLayout(context, attributesSet) {

    private lateinit var rectF: RectF

    private val path = Path()

    private var cornerRadius = dpToFloat(context, 19f)

    init {
        this@NextedConstraintLayout.setBackgroundColor(Color.BLACK)
    }

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

    override fun onDraw(canvas: Canvas?) {
        super.onDraw(canvas)

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