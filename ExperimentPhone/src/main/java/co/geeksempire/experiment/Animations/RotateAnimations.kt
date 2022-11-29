/*
 * Copyright © 2022 By Geeks Empire.
 *
 * Created by Elias Fazel
 * Last modified 11/29/22, 2:45 AM
 *
 * Licensed Under MIT License.
 * https://opensource.org/licenses/MIT
 */

package co.geeksempire.experiment.Animations

import android.content.Context
import android.graphics.drawable.GradientDrawable
import android.util.Log
import android.view.animation.Animation
import android.view.animation.OvershootInterpolator
import android.view.animation.RotateAnimation
import android.widget.ImageView
import co.geeksempire.experiment.R

class RotateAnimations (private val context: Context) {

    val allColors = intArrayOf(
        context.getColor(R.color.default_color_bright),
        context.getColor(R.color.default_color_game_bright),
        context.getColor(R.color.cyberGreen),
        context.getColor(R.color.purple),
        context.getColor(R.color.yellow),
        context.getColor(R.color.pink),
        context.getColor(R.color.default_color_bright),
    )

    val colorsSet = arrayListOf(
        intArrayOf(allColors[0], allColors[1]),
        intArrayOf(allColors[1], allColors[2]),
        intArrayOf(allColors[2], allColors[3]),
        intArrayOf(allColors[3], allColors[4]),
        intArrayOf(allColors[4], allColors[5]),
        intArrayOf(allColors[5], allColors[6]),
    )

    fun multipleColorsRotation(instanceOfView: ImageView) {

        instanceOfView.setImageDrawable(GradientDrawable(GradientDrawable.Orientation.TR_BL, intArrayOf(context.getColor(
            R.color.default_color
        ), context.getColor(R.color.default_color_game))))

        val rotateAnimation = RotateAnimation(0f, 360f,
            Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 0.5f).apply {
            duration = 3333
            repeatCount = Animation.INFINITE
            interpolator = OvershootInterpolator()
            repeatMode = Animation.REVERSE
        }

        instanceOfView.startAnimation(rotateAnimation)

        var animationCounter = 0

        rotateAnimation.setAnimationListener(object : Animation.AnimationListener {

            override fun onAnimationStart(animation: Animation?) {
                Log.d(this@RotateAnimations.javaClass.toString(), "Start")
            }

            override fun onAnimationRepeat(animation: Animation?) {
                Log.d(this@RotateAnimations.javaClass.toString(), "Repeat")

                instanceOfView.setImageDrawable(GradientDrawable(GradientDrawable.Orientation.TR_BL, colorsSet[animationCounter]))

                animationCounter++

                if (animationCounter == colorsSet.size) {

                    animationCounter = 0

                    colorsSet.shuffle()

                }

            }

            override fun onAnimationEnd(animation: Animation?) {
                Log.d(this@RotateAnimations.javaClass.toString(), "End")
            }

        })

    }

}