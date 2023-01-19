/*
 * Copyright © 2023 By Geeks Empire.
 *
 * Created by Elias Fazel
 * Last modified 1/19/23, 9:39 AM
 *
 * Licensed Under MIT License.
 * https://opensource.org/licenses/MIT
 */
package net.geeksempire.loadingspin.sprite

import android.animation.ValueAnimator
import android.graphics.Canvas
import android.graphics.Paint

/**
 * Created by ybq.
 */
open class RectSprite : ShapeSprite() {

    override fun onCreateAnimation(): ValueAnimator? {
        return null
    }

    override fun drawShape(canvas: Canvas, paint: Paint) {
        if (getDrawBounds() != null) {

            canvas.drawRect(getDrawBounds(), paint)

        }
    }
}