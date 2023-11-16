/*
 * Copyright © 2023 By Geeks Empire.
 *
 * Created by Elias Fazel
 * Last modified 11/16/23, 3:52 AM
 *
 * Licensed Under MIT License.
 * https://opensource.org/licenses/MIT
 */

package co.geeksempire.experiment.Views.Progress

import android.content.Context
import android.graphics.Canvas
import android.graphics.Path
import android.util.AttributeSet
import android.widget.ProgressBar
import co.geeksempire.blurry.effect.utils.VectorPathParser

class NextedProgress(context: Context, attributesSet: AttributeSet) : ProgressBar(context, attributesSet) {

    val clipPath = Path()

    private val vectorPathParser = VectorPathParser()

    override fun onDraw(canvas: Canvas) {

        val path = vectorPathParser.parser(vectorPath = "M17,8C8,10 5.9,16.17 3.82,21.34L5.71,22L6.66,19.7C7.14,19.87 7.64,20 8,20C19,20 22,3 22,3C21,5 14,5.25 9,6.25C4,7.25 2,11.5 2,13.5C2,15.5 3.75,17.25 3.75,17.25C7,8 17,8 17,8Z", scaleAmount = 37)

        clipPath.addPath(path)

        canvas?.clipPath(clipPath)

        super.onDraw(canvas)

    }

}