/*
 * Copyright © 2022 By Geeks Empire.
 *
 * Created by Elias Fazel
 * Last modified 11/23/22, 3:09 AM
 *
 * Licensed Under MIT License.
 * https://opensource.org/licenses/MIT
 */

package co.geeksempire.experiment

import android.animation.ValueAnimator
import android.graphics.drawable.GradientDrawable
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.util.Log
import android.view.animation.Animation
import android.view.animation.OvershootInterpolator
import android.view.animation.RotateAnimation
import androidx.appcompat.app.AppCompatActivity
import co.geeksempire.experiment.databinding.ExperimentSelectorLayoutBinding

class ExperimentSelector : AppCompatActivity() {

    lateinit var experimentSelectorLayoutBinding: ExperimentSelectorLayoutBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        experimentSelectorLayoutBinding = ExperimentSelectorLayoutBinding.inflate(layoutInflater)
        setContentView(experimentSelectorLayoutBinding.root)

        experimentSelectorLayoutBinding.root.background = GradientDrawable(GradientDrawable.Orientation.TR_BL, intArrayOf(getColor(R.color.default_color), getColor(R.color.default_color_game)))

        Handler(Looper.getMainLooper()).postDelayed({

            val gradientAnimatorOne = ValueAnimator.ofArgb(getColor(R.color.default_color), getColor(R.color.default_color_game))
            gradientAnimatorOne.duration = 7777
            gradientAnimatorOne.addUpdateListener {
                val animationValue = it.animatedValue as Int

                experimentSelectorLayoutBinding.root.background = GradientDrawable(GradientDrawable.Orientation.TR_BL,
                    intArrayOf(animationValue, getColor(R.color.premiumDark))
                )

                if (animationValue == getColor(R.color.default_color_game)) {



                }

            }

            gradientAnimatorOne.start()

        }, 3333)

        Handler(Looper.getMainLooper()).postDelayed({

            val allColors = intArrayOf(
                getColor(R.color.default_color_bright),
                getColor(R.color.default_color_game_bright),
                getColor(R.color.cyberGreen),
                getColor(R.color.purple),
                getColor(R.color.yellow),
                getColor(R.color.pink),
                getColor(R.color.default_color_bright),
            )

            val colorsSet = arrayListOf(
                intArrayOf(allColors[0], allColors[1]),
                intArrayOf(allColors[1], allColors[2]),
                intArrayOf(allColors[2], allColors[3]),
                intArrayOf(allColors[3], allColors[4]),
                intArrayOf(allColors[4], allColors[5]),
                intArrayOf(allColors[5], allColors[6]),
            )

            experimentSelectorLayoutBinding.loadingView.setImageDrawable(GradientDrawable(GradientDrawable.Orientation.TR_BL, intArrayOf(getColor(R.color.default_color), getColor(R.color.default_color_game))))

            val rotateAnimation = RotateAnimation(0f, 360f,
                Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 0.5f).apply {
                duration = 3333
                repeatCount = Animation.INFINITE
                interpolator = OvershootInterpolator()
                repeatMode = Animation.REVERSE
            }

            experimentSelectorLayoutBinding.loadingView.startAnimation(rotateAnimation)

            var animationCounter = 0

            rotateAnimation.setAnimationListener(object : Animation.AnimationListener {

                override fun onAnimationStart(animation: Animation?) {
                    Log.d(this@ExperimentSelector.javaClass.toString(), "Start")
                }

                override fun onAnimationRepeat(animation: Animation?) {
                    Log.d(this@ExperimentSelector.javaClass.toString(), "Repeat")

                    experimentSelectorLayoutBinding.loadingView.setImageDrawable(GradientDrawable(GradientDrawable.Orientation.TR_BL, colorsSet[animationCounter]))

                    animationCounter++

                    if (animationCounter == colorsSet.size) {

                        animationCounter = 0

                        colorsSet.shuffle()

                    }

                }

                override fun onAnimationEnd(animation: Animation?) {
                    Log.d(this@ExperimentSelector.javaClass.toString(), "End")
                }

            })

        }, 3000)

    }
}