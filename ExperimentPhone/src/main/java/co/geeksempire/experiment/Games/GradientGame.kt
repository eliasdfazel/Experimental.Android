/*
 * Copyright © 2022 By Geeks Empire.
 *
 * Created by Elias Fazel
 * Last modified 12/20/22, 4:55 AM
 *
 * Licensed Under MIT License.
 * https://opensource.org/licenses/MIT
 */

package co.geeksempire.experiment.Games

import android.graphics.Color
import android.graphics.drawable.Drawable
import android.graphics.drawable.GradientDrawable
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import co.geeksempire.experiment.Animations.AnimationInterface
import co.geeksempire.experiment.Animations.GradientAnimations
import co.geeksempire.experiment.R
import co.geeksempire.experiment.databinding.GradientGameLayoutBinding

class GradientGame : AppCompatActivity(), AnimationInterface {

    val allShapes: ArrayList<Drawable?> by lazy {
        arrayListOf(
            applicationContext.getDrawable(net.geekstools.imageview.customshapes.R.drawable.beaker),
            applicationContext.getDrawable(net.geekstools.imageview.customshapes.R.drawable.bell),
            applicationContext.getDrawable(net.geekstools.imageview.customshapes.R.drawable.tooltip),
            applicationContext.getDrawable(net.geekstools.imageview.customshapes.R.drawable.ghost)
        )
    }

    val allColors by lazy {
        intArrayOf(
            applicationContext.getColor(R.color.default_color_bright),
            applicationContext.getColor(R.color.default_color_game_bright),
            applicationContext.getColor(R.color.cyberGreen),
            applicationContext.getColor(R.color.purple),
            applicationContext.getColor(R.color.yellow),
            applicationContext.getColor(R.color.pink),
            applicationContext.getColor(R.color.default_color_bright),
        )
    }

    lateinit var gradientGameLayoutBinding: GradientGameLayoutBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        gradientGameLayoutBinding = GradientGameLayoutBinding.inflate(layoutInflater)
        setContentView(gradientGameLayoutBinding.root)

        window.decorView.setBackgroundColor(Color.CYAN)
        gradientGameLayoutBinding.root.background = getDrawable(co.geeksempire.experiment.R.drawable.splash_screen_initial)

        operateShape()

        gradientGameLayoutBinding.backgroundView.setImageDrawable(
            GradientDrawable(GradientDrawable.Orientation.TR_BL, intArrayOf(
                getColor(R.color.default_color_bright),
                getColor(R.color.default_color_bright),
                getColor(R.color.default_color_bright),
            ))
        )

        val gradientAnimations: GradientAnimations = GradientAnimations(applicationContext, allColors, this@GradientGame)

        gradientAnimations.multipleGradientLevelOne(instanceOfView = gradientGameLayoutBinding.backgroundView)

        gradientGameLayoutBinding.backgroundView.setOnClickListener { view ->
            gradientGameLayoutBinding.backgroundView.isEnabled = false

            operateShape()

        }

    }

    override fun animationEnded() {


    }

    private fun operateShape() {

//        gradientGameLayoutBinding.selectedColor.setShapeDrawable(allShapes.random())

        val firstColor = allColors.random()
        val secondColor = allColors.random()

        val listOfColor = if (IntRange(0, 100).random() in 0..50) {

            intArrayOf(
                firstColor,
                firstColor,
                secondColor
            )

        } else {

            intArrayOf(
                secondColor,
                firstColor,
                firstColor
            )

        }

        gradientGameLayoutBinding.selectedColor.setImageDrawable(GradientDrawable(GradientDrawable.Orientation.TR_BL, listOfColor))

    }

}