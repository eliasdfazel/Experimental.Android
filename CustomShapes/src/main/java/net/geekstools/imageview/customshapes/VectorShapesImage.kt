/*
 * Copyright © 2022 By Geeks Empire.
 *
 * Created by Elias Fazel
 * Last modified 12/20/22, 9:34 AM
 *
 * Licensed Under MIT License.
 * https://opensource.org/licenses/MIT
 */
package net.geekstools.imageview.customshapes

import android.content.Context
import android.graphics.Canvas
import android.graphics.Path
import android.graphics.RectF
import android.util.AttributeSet
import androidx.appcompat.widget.AppCompatImageView
import net.geekstools.imageview.customshapes.utils.VectorPathParser

class VectorShapesImage(context: Context, attributeSet: AttributeSet) : AppCompatImageView(context, attributeSet) {

    var shapePath: String
    var scaleAmount = 37

    var rectF = RectF()

    var clipPath = Path()

    private val vectorPathParser = VectorPathParser()

    init {

        val typedArray = context.obtainStyledAttributes(attributeSet, R.styleable.ShapesImage)

        shapePath = typedArray.getString(R.styleable.ShapesImage_shapePath).toString()
        scaleAmount = typedArray.getInteger(R.styleable.ShapesImage_shapeScale, 10)

        typedArray.recycle()

        if (shapePath.isEmpty()) {
            throw NullPointerException("shapePath Must Not Be Empty")
        }

    }

    override fun onDraw(canvas: Canvas) {

        val path = vectorPathParser.parser(vectorPath = shapePath, scaleAmount = scaleAmount)

        clipPath.addPath(path)

        canvas.clipPath(clipPath)

        super.onDraw(canvas)

    }
}