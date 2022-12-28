/*
 * Copyright © 2022 By Geeks Empire.
 *
 * Created by Elias Fazel
 * Last modified 12/28/22, 5:22 AM
 *
 * Licensed Under MIT License.
 * https://opensource.org/licenses/MIT
 */

package co.geeksempire.experiment.Animations

import android.animation.Animator
import android.animation.ValueAnimator
import android.content.Context
import android.graphics.drawable.GradientDrawable
import android.util.Log
import android.widget.ImageView
import co.geeksempire.experiment.R

interface AnimationInterface {
    fun animationEnded()
}
class GradientAnimations (private val context: Context,
                          private val allColors: IntArray,
                          private val animationInterface: AnimationInterface) {

    /**
     * Count Each Click
     * - If Correct -> Sum Up
     * - If Wrong -> Zero It
     **/
    var pointCounter = 0

    var maximumPoints = 7

    /// from Cloud
    var gradientDuration: Long = 1357

    /// from Cloud
    var gradientLayersColors = 3

    fun multipleGradient(instanceOfView: ImageView,
                         fromColor: Int = context.getColor(R.color.default_color_bright),
                         toColor: Int = context.getColor(R.color.cyberGreen)) {

        var gradientIndex = 0

        var previousColor = toColor

        val colorAnimator = ValueAnimator.ofArgb(fromColor, toColor).apply {
            duration = gradientDuration
            repeatCount = (gradientLayersColors - 1)
        }
        colorAnimator.start()

        colorAnimator.addUpdateListener {
            val currentColor = it.animatedValue as Int

            val gradientDrawableColors = IntArray(gradientLayersColors)

            repeat(gradientLayersColors) { index ->

                if (gradientIndex == 0) {

                }

                gradientDrawableColors[gradientIndex] = currentColor

                if (index < gradientIndex) {

                    gradientDrawableColors[index] = previousColor

                } else if (index > gradientIndex) {

                    gradientDrawableColors[index] = fromColor

                }

            }

            val aGradientDrawable = GradientDrawable(GradientDrawable.Orientation.TR_BL, gradientDrawableColors)
            instanceOfView.setImageDrawable(aGradientDrawable)

        }

        colorAnimator.addListener(object : Animator.AnimatorListener {

            override fun onAnimationStart(animator: Animator) {}

            override fun onAnimationEnd(animator: Animator) {
                Log.d(this@GradientAnimations.javaClass.simpleName, "Animation Ended")

                val newColor = randomNewColor(previousColor)

                multipleGradient(instanceOfView, previousColor, newColor)

                animationInterface.animationEnded()

            }

            override fun onAnimationCancel(animator: Animator) {}

            override fun onAnimationRepeat(animator: Animator) {
                Log.d(this@GradientAnimations.javaClass.simpleName, "Repeat Count: ${gradientIndex}")

                gradientIndex++

            }

        })

    }

    fun randomNewColor(previousColor: Int) : Int {

        val newColor = allColors.random()

        if (newColor == previousColor) {

            randomNewColor(previousColor)

        }

        return newColor
    }

}