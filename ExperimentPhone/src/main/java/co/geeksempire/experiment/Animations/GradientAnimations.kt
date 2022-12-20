/*
 * Copyright © 2022 By Geeks Empire.
 *
 * Created by Elias Fazel
 * Last modified 12/20/22, 4:45 AM
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
     * Count Each Click {
     * - If Correct -> Sum Up
     * - If Wrong -> Zero It
     * }
     **/
    var pointCounter = 0

    fun multipleGradientLevelOne(instanceOfView: ImageView,
                                 fromColor: Int = context.getColor(R.color.default_color_bright),
                                 toColor: Int = context.getColor(R.color.cyberGreen)) {

        var gradientIndex = 0

        var previousColor = 0

        val colorAnimator = ValueAnimator.ofArgb(fromColor, toColor)
        colorAnimator.duration = 1357
        colorAnimator.repeatCount = 2
        colorAnimator.start()

        colorAnimator.addUpdateListener {
            val currentColor = it.animatedValue as Int

            val aGradientDrawable = if (gradientIndex == 0) {

                previousColor = currentColor

                GradientDrawable(GradientDrawable.Orientation.TR_BL, intArrayOf(
                    currentColor,
                    fromColor,
                    fromColor,
                ))

            } else if (gradientIndex == 1) {

                GradientDrawable(GradientDrawable.Orientation.TR_BL, intArrayOf(
                    previousColor,
                    currentColor,
                    fromColor,
                ))

            } else if (gradientIndex == 2) {

                GradientDrawable(GradientDrawable.Orientation.TR_BL, intArrayOf(
                    previousColor,
                    previousColor,
                    currentColor
                ))

            } else {

                GradientDrawable(GradientDrawable.Orientation.TR_BL, intArrayOf(
                    context.getColor(R.color.black),
                    context.getColor(R.color.black),
                    context.getColor(R.color.black)
                ))

            }

            instanceOfView.setImageDrawable(aGradientDrawable)

        }

        colorAnimator.addListener(object : Animator.AnimatorListener {

            override fun onAnimationStart(animator: Animator) {}

            override fun onAnimationEnd(animator: Animator) {
                Log.d(this@GradientAnimations.javaClass.simpleName, "Animation Ended")


                multipleGradientLevelOne(instanceOfView, previousColor, allColors.random())

                animationInterface.animationEnded()

            }

            override fun onAnimationCancel(animator: Animator) {}

            override fun onAnimationRepeat(animator: Animator) {
                Log.d(this@GradientAnimations.javaClass.simpleName, "Repeat Count: ${gradientIndex}")

                gradientIndex++

            }

        })

    }

}