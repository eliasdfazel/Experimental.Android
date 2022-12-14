/*
 * Copyright © 2022 By Geeks Empire.
 *
 * Created by Elias Fazel
 * Last modified 12/21/22, 3:19 AM
 *
 * Licensed Under MIT License.
 * https://opensource.org/licenses/MIT
 */

package co.geeksempire.experiment.Games

import android.graphics.drawable.GradientDrawable
import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.AppCompatImageView
import co.geeksempire.experiment.Animations.AnimationInterface
import co.geeksempire.experiment.Animations.GradientAnimations
import co.geeksempire.experiment.BuildConfig
import co.geeksempire.experiment.R
import co.geeksempire.experiment.databinding.GradientGameLayoutBinding


class GradientGame : AppCompatActivity(), AnimationInterface {

    /// from Cloud
    val allShapes by lazy {
        arrayListOf<String>(
            applicationContext.getString(R.string.leafPath),
            applicationContext.getString(R.string.birdPath),
        )
    }

    /// from Cloud
    val allColors by lazy {
        intArrayOf(
            applicationContext.getColor(R.color.default_color_bright),
            applicationContext.getColor(R.color.default_color_game_bright),
            applicationContext.getColor(R.color.cyberGreen),
            applicationContext.getColor(R.color.purple),
            applicationContext.getColor(R.color.yellow),
            applicationContext.getColor(R.color.pink),
            applicationContext.getColor(R.color.white),
            applicationContext.getColor(R.color.brown),
            applicationContext.getColor(R.color.brownLight),
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

        window.decorView.setBackgroundColor(getColor(R.color.dark))

        operateShape()

        gradientGameLayoutBinding.backgroundView.setImageDrawable(
            GradientDrawable(GradientDrawable.Orientation.TR_BL, intArrayOf(
                getColor(R.color.default_color_game_bright),
                getColor(R.color.default_color_bright),
                getColor(R.color.default_color_bright),
            ))
        )

        allShapes.shuffle()
        allColors.shuffle()

        gradientAnimations.multipleGradient(instanceOfView = gradientGameLayoutBinding.backgroundView)

        gradientGameLayoutBinding.backgroundView.setOnClickListener {

            if (gradientCheckpoint(gradientGameLayoutBinding.backgroundView, gradientGameLayoutBinding.selectedGradient)) {
                Log.d(this@GradientGame.javaClass.simpleName, "Winner!")

                gradientGameLayoutBinding.backgroundView.isEnabled = false

                operateShape()

                gradientAnimations.pointCounter++
                Log.d(this@GradientGame.javaClass.simpleName, "Winner Point; ${gradientAnimations.pointCounter}")

                if (gradientAnimations.pointCounter == gradientAnimations.maximumPoints) {
                    Log.d(this@GradientGame.javaClass.simpleName, "Won Completely!")

                }

            } else {
                Log.d(this@GradientGame.javaClass.simpleName, "Loser!")

                gradientAnimations.pointCounter = 0

            }

        }

    }

    override fun animationEnded() {


    }

    private fun operateShape() {

        gradientGameLayoutBinding.selectedGradient.changeShape(allShapes.random())

        gradientGameLayoutBinding.selectedGradient.setImageDrawable(GradientDrawable(GradientDrawable.Orientation.TR_BL, intArrayOf(
            getColor(R.color.default_color_game_bright),
            getColor(R.color.default_color_bright),
            getColor(R.color.default_color_bright),
        )))

        val gradientDrawable = gradientGameLayoutBinding.selectedGradient.drawable as GradientDrawable

        val firstColor = if (BuildConfig.DEBUG) {
            getColor(R.color.default_color_bright)
        } else {
            gradientAnimations.randomNewColor(gradientDrawable.colors!!.first())
        }
        val secondColor = if (BuildConfig.DEBUG) {
            getColor(R.color.default_color_game_bright)
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

        gradientGameLayoutBinding.selectedGradient.setImageDrawable(GradientDrawable(GradientDrawable.Orientation.TR_BL, listOfColor))

    }

    fun gradientCheckpoint(backgroundView: AppCompatImageView, shapeView: AppCompatImageView) : Boolean {

        val backgroundGradientDrawable: GradientDrawable = backgroundView.drawable as GradientDrawable
        val shapedGradientDrawable: GradientDrawable = shapeView.drawable as GradientDrawable

        return (backgroundGradientDrawable.colors.contentEquals(shapedGradientDrawable.colors))
    }

}