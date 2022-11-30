/*
 * Copyright © 2022 By Geeks Empire.
 *
 * Created by Elias Fazel
 * Last modified 11/30/22, 6:30 AM
 *
 * Licensed Under MIT License.
 * https://opensource.org/licenses/MIT
 */

package co.geeksempire.experiment.Animations

import android.content.Context
import android.graphics.drawable.AnimationDrawable
import android.graphics.drawable.GradientDrawable
import android.widget.ImageView
import co.geeksempire.experiment.R

interface AnimationInterface {
    fun loopCounter(maximumLoop: Int, loopAmount: Int)
}
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

    val allDrawableAnimations = arrayListOf<GradientDrawable>(
        GradientDrawable(GradientDrawable.Orientation.TR_BL,
            intArrayOf(
                allColors[1],
                allColors[1],
                allColors[1]
            )),
        GradientDrawable(GradientDrawable.Orientation.TR_BL,
            intArrayOf(
                allColors[0],
                allColors[1],
                allColors[1]
            )),
        GradientDrawable(GradientDrawable.Orientation.TR_BL,
            intArrayOf(
                allColors[0],
                allColors[0],
                allColors[1]
            )),
        GradientDrawable(GradientDrawable.Orientation.TR_BL,
            intArrayOf(
                allColors[0],
                allColors[0],
                allColors[0]
            )),
        GradientDrawable(GradientDrawable.Orientation.TR_BL,
            intArrayOf(
                allColors[2],
                allColors[0],
                allColors[0]
            )),
        GradientDrawable(GradientDrawable.Orientation.TR_BL,
            intArrayOf(
                allColors[2],
                allColors[2],
                allColors[0]
            )),
        GradientDrawable(GradientDrawable.Orientation.TR_BL,
            intArrayOf(
                allColors[2],
                allColors[2],
                allColors[2]
            )),
        GradientDrawable(GradientDrawable.Orientation.TR_BL,
            intArrayOf(
                allColors[3],
                allColors[2],
                allColors[2]
            )),
        GradientDrawable(GradientDrawable.Orientation.TR_BL,
            intArrayOf(
                allColors[3],
                allColors[3],
                allColors[2]
            )),
        GradientDrawable(GradientDrawable.Orientation.TR_BL,
            intArrayOf(
                allColors[3],
                allColors[3],
                allColors[3]
            )),
        GradientDrawable(GradientDrawable.Orientation.TR_BL,
            intArrayOf(
                allColors[4],
                allColors[3],
                allColors[3]
            )),
        GradientDrawable(GradientDrawable.Orientation.TR_BL,
            intArrayOf(
                allColors[4],
                allColors[4],
                allColors[3]
            )),
        GradientDrawable(GradientDrawable.Orientation.TR_BL,
            intArrayOf(
                allColors[4],
                allColors[4],
                allColors[4]
            )),
        GradientDrawable(GradientDrawable.Orientation.TR_BL,
            intArrayOf(
                allColors[5],
                allColors[4],
                allColors[4]
            )),
        GradientDrawable(GradientDrawable.Orientation.TR_BL,
            intArrayOf(
                allColors[5],
                allColors[5],
                allColors[4]
            )),
        GradientDrawable(GradientDrawable.Orientation.TR_BL,
            intArrayOf(
                allColors[5],
                allColors[5],
                allColors[5]
            )),
        GradientDrawable(GradientDrawable.Orientation.TR_BL,
            intArrayOf(
                allColors[6],
                allColors[5],
                allColors[5]
            )),
        GradientDrawable(GradientDrawable.Orientation.TR_BL,
            intArrayOf(
                allColors[6],
                allColors[6],
                allColors[5]
            )),
        GradientDrawable(GradientDrawable.Orientation.TR_BL,
            intArrayOf(
                allColors[6],
                allColors[6],
                allColors[6]
            )),
        GradientDrawable(GradientDrawable.Orientation.TR_BL,
            intArrayOf(
                allColors[1],
                allColors[6],
                allColors[6]
            )),
        GradientDrawable(GradientDrawable.Orientation.TR_BL,
            intArrayOf(
                allColors[1],
                allColors[1],
                allColors[6]
            )),
        GradientDrawable(GradientDrawable.Orientation.TR_BL,
            intArrayOf(
                allColors[1],
                allColors[1],
                allColors[1]
            )),
    )

    val gradientDuration: Int = 888
    val gradientExitDuration: Int = 888

    val maximumLoop: Int = 7

    fun multipleGradientAnimation(instanceOfView: ImageView, animationInterface: AnimationInterface) : AnimationDrawable {



        val animationDrawable = AnimationDrawable().apply {
            setEnterFadeDuration(1)
            setExitFadeDuration(gradientExitDuration)

            addFrame(allDrawableAnimations[0], gradientDuration)
            addFrame(allDrawableAnimations[1], gradientDuration)
            addFrame(allDrawableAnimations[2], gradientDuration)
            addFrame(allDrawableAnimations[3], gradientDuration)
            addFrame(allDrawableAnimations[4], gradientDuration)
            addFrame(allDrawableAnimations[5], gradientDuration)
            addFrame(allDrawableAnimations[6], gradientDuration)
            addFrame(allDrawableAnimations[7], gradientDuration)
            addFrame(allDrawableAnimations[8], gradientDuration)
            addFrame(allDrawableAnimations[9], gradientDuration)
            addFrame(allDrawableAnimations[10], gradientDuration)
            addFrame(allDrawableAnimations[11], gradientDuration)
            addFrame(allDrawableAnimations[12], gradientDuration)
            addFrame(allDrawableAnimations[13], gradientDuration)
            addFrame(allDrawableAnimations[14], gradientDuration)
            addFrame(allDrawableAnimations[15], gradientDuration)
            addFrame(allDrawableAnimations[16], gradientDuration)
            addFrame(allDrawableAnimations[17], gradientDuration)
            addFrame(allDrawableAnimations[18], gradientDuration)
            addFrame(allDrawableAnimations[19], gradientDuration)
            addFrame(allDrawableAnimations[20], gradientDuration)
            addFrame(allDrawableAnimations[21], gradientDuration)
        }

        instanceOfView.setImageDrawable(animationDrawable)

        return animationDrawable
    }

}