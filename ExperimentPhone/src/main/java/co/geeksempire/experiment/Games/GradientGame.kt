/*
 * Copyright © 2022 By Geeks Empire.
 *
 * Created by Elias Fazel
 * Last modified 11/29/22, 2:45 AM
 *
 * Licensed Under MIT License.
 * https://opensource.org/licenses/MIT
 */

package co.geeksempire.experiment.Games

import android.graphics.drawable.Drawable
import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import co.geeksempire.experiment.Animations.GradientAnimations
import co.geeksempire.experiment.Animations.RotateAnimations
import co.geeksempire.experiment.databinding.GradientGameLayoutBinding
import net.geekstools.imageview.customshapes.R

class GradientGame : AppCompatActivity() {

    val allShapes: ArrayList<Drawable?> by lazy {
        arrayListOf(
            applicationContext.getDrawable(R.drawable.beaker),
            applicationContext.getDrawable(R.drawable.android),
            applicationContext.getDrawable(R.drawable.bell),
            applicationContext.getDrawable(R.drawable.tooltip),
            applicationContext.getDrawable(R.drawable.ghost),
        )
    }


    lateinit var gradientGameLayoutBinding: GradientGameLayoutBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        gradientGameLayoutBinding = GradientGameLayoutBinding.inflate(layoutInflater)
        setContentView(gradientGameLayoutBinding.root)

        val gradientAnimations = GradientAnimations(applicationContext)
        val multipleGradientAnimation = gradientAnimations.multipleGradientAnimation(gradientGameLayoutBinding.backgroundView).apply {
            start()
        }


        var multipleGradientFrame: Drawable = gradientAnimations.allDrawableAnimations.random()

        gradientGameLayoutBinding.selectedColor.setShapeDrawable(allShapes.random())
        gradientGameLayoutBinding.selectedColor.setImageDrawable(multipleGradientFrame)

        RotateAnimations(applicationContext)
            .multipleColorsRotation(gradientGameLayoutBinding.loadingView)

        gradientGameLayoutBinding.root.setOnClickListener {

            multipleGradientFrame = gradientAnimations.allDrawableAnimations.random()

            gradientGameLayoutBinding.selectedColor.setShapeDrawable(allShapes.random())
            gradientGameLayoutBinding.selectedColor.setImageDrawable(multipleGradientFrame)

            if (multipleGradientAnimation.current == multipleGradientFrame) {
                Log.d(this@GradientGame.javaClass.simpleName, "Equal")



            } else {
                Log.d(this@GradientGame.javaClass.simpleName, "Not Equal")



            }

        }

    }

}