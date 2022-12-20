/*
 * Copyright © 2022 By Geeks Empire.
 *
 * Created by Elias Fazel
 * Last modified 12/20/22, 7:18 AM
 *
 * Licensed Under MIT License.
 * https://opensource.org/licenses/MIT
 */
package net.geekstools.imageview.customshapes

import android.content.Context
import android.graphics.Canvas
import android.graphics.Path
import android.util.AttributeSet
import androidx.appcompat.widget.AppCompatImageView
import net.geekstools.imageview.customshapes.utils.VectorPathParser

open class ShapesImage(context: Context, attributeSet: AttributeSet?) : AppCompatImageView(context, attributeSet) {

    var clipPath = Path()

    var shapePath: String
    var scaleAmount = 37

    private val vectorPathParser = VectorPathParser()

    init {

        val typedArray = context.obtainStyledAttributes(attributeSet, R.styleable.ShapesImage)

        shapePath = typedArray.getString(R.styleable.ShapesImage_shapePath).toString()
        scaleAmount = typedArray.getInteger(R.styleable.ShapesImage_scaleAmount, 37)

        typedArray.recycle()

    }

    override fun draw(canvas: Canvas?) {
        super.draw(canvas)
    }

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)

        /* Start - Custom Shape */
        val path = vectorPathParser.parser(vectorPath = shapePath, scaleAmount = scaleAmount)

        clipPath.addPath(path)
        /* End - Custom Shape */

        canvas.clipPath(clipPath)

    }

}