/*
 * Copyright © 2023 By Geeks Empire.
 *
 * Created by Elias Fazel
 * Last modified 1/19/23, 9:51 AM
 *
 * Licensed Under MIT License.
 * https://opensource.org/licenses/MIT
 */
package net.geeksempire.loadingspin.sprite

import android.animation.ValueAnimator
import android.graphics.*

/**
 * Created by ybq.
 */
open class CircleSprite : ShapeSprite() {
    override fun onCreateAnimation(): ValueAnimator? {
        return null
    }

    override fun drawShape(canvas: Canvas, paint: Paint) {
        if (getDrawBounds() != null) {

            paint.shader =  LinearGradient(
                0f, 0f,
                0f, getDrawBounds().height().toFloat(),
                Color.CYAN,
                Color.MAGENTA,
                Shader.TileMode.CLAMP
            )

            val radius = Math.min(getDrawBounds().width(), getDrawBounds().height()) / 2
            canvas.drawCircle(
                getDrawBounds().centerX().toFloat(),
                getDrawBounds().centerY().toFloat(),
                radius.toFloat(), paint
            )
        }
    }
}