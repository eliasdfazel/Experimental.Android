/*
 * Copyright © 2022 By Geeks Empire.
 *
 * Created by Elias Fazel
 * Last modified 11/24/22, 4:24 AM
 *
 * Licensed Under MIT License.
 * https://opensource.org/licenses/MIT
 */

package co.geeksempire.experiment.Animations

import android.content.Context
import android.graphics.drawable.AnimationDrawable
import android.graphics.drawable.GradientDrawable
import android.view.View
import android.widget.ImageView
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

    fun multipleGradientAnimation(instanceOfView: View) {

        instanceOfView as ImageView

        val animationDrawable = AnimationDrawable().apply {
            setEnterFadeDuration(7)
            setExitFadeDuration(3333)

            addFrame(GradientDrawable(GradientDrawable.Orientation.TR_BL, intArrayOf(allColors[0], allColors[1])), 3333)
            addFrame(GradientDrawable(GradientDrawable.Orientation.TR_BL, intArrayOf(allColors[1], allColors[2])), 3333)
            addFrame(GradientDrawable(GradientDrawable.Orientation.TR_BL, intArrayOf(allColors[2], allColors[3])), 3333)
            addFrame(GradientDrawable(GradientDrawable.Orientation.TR_BL, intArrayOf(allColors[3], allColors[4])), 3333)
            addFrame(GradientDrawable(GradientDrawable.Orientation.TR_BL, intArrayOf(allColors[4], allColors[5])), 3333)

        }

        instanceOfView.background = animationDrawable

        animationDrawable.start()

    }

}