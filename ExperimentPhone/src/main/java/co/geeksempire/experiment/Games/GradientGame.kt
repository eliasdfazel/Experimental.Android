/*
 * Copyright © 2022 By Geeks Empire.
 *
 * Created by Elias Fazel
 * Last modified 12/20/22, 5:42 AM
 *
 * Licensed Under MIT License.
 * https://opensource.org/licenses/MIT
 */

package co.geeksempire.experiment.Games

import android.graphics.Color
import android.graphics.drawable.Drawable
import android.graphics.drawable.GradientDrawable
import android.os.Bundle
import android.util.Log
import android.widget.ImageView
import androidx.appcompat.app.AppCompatActivity
import co.geeksempire.experiment.Animations.AnimationInterface
import co.geeksempire.experiment.Animations.GradientAnimations
import co.geeksempire.experiment.BuildConfig
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
            applicationContext.getColor(R.color.default_color_dark),
            applicationContext.getColor(R.color.black),
            applicationContext.getColor(R.color.green),
            applicationContext.getColor(R.color.default_color_game_dark),
        )
    }

    val gradientAnimations: GradientAnimations by lazy {
        GradientAnimations(applicationContext, allColors, this@GradientGame)
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
                getColor(R.color.cyberGreen),
                getColor(R.color.default_color_bright),
                getColor(R.color.default_color_bright),
            ))
        )


        gradientAnimations.multipleGradientLevelOne(instanceOfView = gradientGameLayoutBinding.backgroundView)

        gradientGameLayoutBinding.backgroundView.setOnClickListener { view ->

            if (gradientCheckpoint(gradientGameLayoutBinding.backgroundView, gradientGameLayoutBinding.selectedColor)) {
                Log.d(this@GradientGame.javaClass.simpleName, "Winner!")

                gradientGameLayoutBinding.backgroundView.isEnabled = false

                operateShape()

            } else {
                Log.d(this@GradientGame.javaClass.simpleName, "Loser!")

            }

        }

    }

    override fun animationEnded() {


    }

    private fun operateShape() {

        gradientGameLayoutBinding.selectedColor.setShapeDrawable(allShapes.random())

        gradientGameLayoutBinding.selectedColor.refreshDrawableState()
        gradientGameLayoutBinding.selectedColor.invalidate()

        gradientGameLayoutBinding.selectedColor.setImageDrawable(GradientDrawable(GradientDrawable.Orientation.TR_BL, intArrayOf(
            getColor(R.color.cyberGreen),
            getColor(R.color.default_color_bright),
            getColor(R.color.default_color_bright),
        )))

        val gradientDrawable = gradientGameLayoutBinding.selectedColor.drawable as GradientDrawable

        val firstColor = if (BuildConfig.DEBUG) {
            getColor(R.color.default_color_bright)
        } else {
            gradientAnimations.randomNewColor(gradientDrawable.colors!!.first())
        }
        val secondColor = if (BuildConfig.DEBUG) {
            getColor(R.color.cyberGreen)
        } else {
            gradientAnimations.randomNewColor(gradientDrawable.colors!!.last())
        }

        val listOfColor = if (BuildConfig.DEBUG) {
            intArrayOf(
                secondColor,
                firstColor,
                firstColor
            )
        } else {
            if (IntRange(0, 100).random() in 0..33) {

                intArrayOf(
                    firstColor,
                    firstColor,
                    secondColor
                )

            } else if (IntRange(0, 100).random() in 35..79) {

                intArrayOf(
                    secondColor,
                    firstColor,
                    firstColor
                )

            } else {

                intArrayOf(
                    secondColor,
                    secondColor,
                    firstColor
                )

            }
        }

        gradientGameLayoutBinding.selectedColor.setImageDrawable(GradientDrawable(GradientDrawable.Orientation.TR_BL, listOfColor))

    }

    fun gradientCheckpoint(backgroundView: ImageView, shapeView: ImageView) : Boolean {

        val firstGradientDrawable: GradientDrawable = backgroundView.drawable as GradientDrawable
        val secondGradientDrawable: GradientDrawable = shapeView.drawable as GradientDrawable

        return (firstGradientDrawable.colors.contentEquals(secondGradientDrawable.colors))
    }

}