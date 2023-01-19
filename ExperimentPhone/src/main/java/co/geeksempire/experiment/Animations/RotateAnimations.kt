/*
 * Copyright © 2023 By Geeks Empire.
 *
 * Created by Elias Fazel
 * Last modified 1/19/23, 10:43 AM
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
import android.view.animation.Animation
import android.view.animation.OvershootInterpolator
import android.view.animation.RotateAnimation
import android.widget.ImageView
import co.geeksempire.experiment.R

class RotateAnimations (private val context: Context) {

    private val allColors = intArrayOf(
        context.getColor(R.color.default_color_bright),
        context.getColor(R.color.default_color_game_bright),
        context.getColor(R.color.cyberGreen),
        context.getColor(R.color.purple),
        context.getColor(R.color.yellow),
        context.getColor(R.color.pink),
        context.getColor(R.color.default_color_bright),
    )

    /// from Cloud
    var gradientDuration: Long = 1357

    /// from Cloud
    var gradientLayersColors = 3

    fun multipleColorsRotation(instanceOfView: ImageView) {

        instanceOfView.setImageDrawable(GradientDrawable(GradientDrawable.Orientation.TR_BL, intArrayOf(
            allColors[0], allColors[2]
        )))

        val rotateAnimation = RotateAnimation(0f, 360f,
            Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 0.5f).apply {
            duration = 3333
            repeatCount = Animation.INFINITE
            interpolator = OvershootInterpolator()
            repeatMode = Animation.REVERSE
        }

        instanceOfView.startAnimation(rotateAnimation)

        multipleGradient(instanceOfView, allColors[0], allColors[1])

    }



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
                Log.d(this@RotateAnimations.javaClass.simpleName, "Animation Ended")

                val newColor = randomNewColor(previousColor)

                multipleGradient(instanceOfView, previousColor, newColor)



            }

            override fun onAnimationCancel(animator: Animator) {}

            override fun onAnimationRepeat(animator: Animator) {
                Log.d(this@RotateAnimations.javaClass.simpleName, "Repeat Count: ${gradientIndex}")

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