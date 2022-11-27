/*
 * Copyright © 2022 By Geeks Empire.
 *
 * Created by Elias Fazel
 * Last modified 11/27/22, 12:22 AM
 *
 * Licensed Under MIT License.
 * https://opensource.org/licenses/MIT
 */

package co.geeksempire.experiment.Animations

import android.content.Context
import android.graphics.drawable.AnimationDrawable
import android.graphics.drawable.GradientDrawable
import android.view.View
import android.view.ViewGroup
import co.geeksempire.experiment.R

class GradientAnimations (private val context: Context) {

    val allColors = intArrayOf(
        context.getColor(R.color.default_color_bright),
        context.getColor(R.color.default_color_game_bright),
        context.getColor(R.color.cyberGreen),
        context.getColor(R.color.purple),
        context.getColor(R.color.yellow),
        context.getColor(R.color.pink),
        context.getColor(R.color.default_color_bright),
    )

    fun multipleGradientAnimation(instanceOfView: View) : AnimationDrawable {

        instanceOfView as ViewGroup

        val gradientDuration: Int = 2222
        val gradientExitDuration: Int = 1111

        val animationDrawable = AnimationDrawable().apply {
            repeat(7) { loopCount ->


            }
            setEnterFadeDuration(1)
            setExitFadeDuration(gradientExitDuration)

            addFrame(GradientDrawable(GradientDrawable.Orientation.TR_BL, intArrayOf(allColors[0], allColors[1])), gradientDuration)
            addFrame(GradientDrawable(GradientDrawable.Orientation.TR_BL, intArrayOf(allColors[2], allColors[0])), gradientDuration)
            addFrame(GradientDrawable(GradientDrawable.Orientation.TR_BL, intArrayOf(allColors[3], allColors[2])), gradientDuration)
            addFrame(GradientDrawable(GradientDrawable.Orientation.TR_BL, intArrayOf(allColors[4], allColors[3])), gradientDuration)
            addFrame(GradientDrawable(GradientDrawable.Orientation.TR_BL, intArrayOf(allColors[5], allColors[4])), gradientDuration)
            addFrame(GradientDrawable(GradientDrawable.Orientation.TR_BL, intArrayOf(allColors[1], allColors[5])), gradientDuration)

        }

        instanceOfView.background = animationDrawable

        return animationDrawable
    }

}