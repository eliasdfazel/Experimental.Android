/*
 * Copyright © 2023 By Geeks Empire.
 *
 * Created by Elias Fazel
 * Last modified 1/19/23, 10:41 AM
 *
 * Licensed Under MIT License.
 * https://opensource.org/licenses/MIT
 */

package co.geeksempire.experiment.Animations

import android.animation.ValueAnimator
import android.content.Context
import android.graphics.drawable.GradientDrawable
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

        val colorAnimator = ValueAnimator.ofArgb(allColors[0], allColors[2]).apply {
            duration = 1111
            repeatCount = Animation.INFINITE
        }
        colorAnimator.start()
        colorAnimator.addUpdateListener {
            val currentColor = it.animatedValue as Int

            val gradientDrawableColors = IntArray(2)

            gradientDrawableColors[0] = allColors[0]
            gradientDrawableColors[1] = currentColor

            val aGradientDrawable = GradientDrawable(GradientDrawable.Orientation.TR_BL, gradientDrawableColors)
            instanceOfView.setImageDrawable(aGradientDrawable)

        }

    }

}