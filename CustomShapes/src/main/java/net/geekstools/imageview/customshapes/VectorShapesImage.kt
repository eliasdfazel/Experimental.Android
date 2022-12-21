/*
 * Copyright © 2022 By Geeks Empire.
 *
 * Created by Elias Fazel
 * Last modified 12/21/22, 2:24 AM
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

class VectorShapesImage(context: Context, attributeSet: AttributeSet) : AppCompatImageView(context, attributeSet) {

    var shapePath: String
    var scaleAmount = 37

    var clipPath = Path()

    private val vectorPathParser = VectorPathParser()

    var shapedCanvas: Canvas = Canvas()

    init {

        val typedArray = context.obtainStyledAttributes(attributeSet, R.styleable.ShapesImage)

        shapePath = typedArray.getString(R.styleable.ShapesImage_shapePath).toString()
        scaleAmount = typedArray.getInteger(R.styleable.ShapesImage_shapeScale, 37)

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

        shapedCanvas = canvas

    }

    fun changeShape(newShapePath: String, newScaleAmount: Int = 10) {

        clipPath.reset()

        this@VectorShapesImage.shapePath = newShapePath
        this@VectorShapesImage.scaleAmount = newScaleAmount

        val path = vectorPathParser.parser(vectorPath = shapePath, scaleAmount = scaleAmount)

        clipPath.addPath(path)

        shapedCanvas.clipPath(clipPath)

        invalidate()

    }

}