/*
 * Copyright © 2022 By Geeks Empire.
 *
 * Created by Elias Fazel
 * Last modified 11/30/22, 5:08 AM
 *
 * Licensed Under MIT License.
 * https://opensource.org/licenses/MIT
 */

package co.geeksempire.experiment.Games

import android.graphics.Color
import android.graphics.drawable.AnimationDrawable
import android.graphics.drawable.Drawable
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import co.geeksempire.experiment.Animations.GradientAnimations
import co.geeksempire.experiment.databinding.GradientGameLayoutBinding
import net.geekstools.imageview.customshapes.R

class GradientGame : AppCompatActivity() {

    val gradientAnimations: GradientAnimations by lazy {
        GradientAnimations(applicationContext)
    }

    val allShapes: ArrayList<Drawable?> by lazy {
        arrayListOf(
            getDrawable(R.drawable.beaker),
            getDrawable(R.drawable.bell),
            getDrawable(R.drawable.tooltip),
            getDrawable(R.drawable.ghost)
        )
    }

    var multipleGradientAnimation: AnimationDrawable? = null
    var multipleGradientFrame: Drawable? = null

    lateinit var gradientGameLayoutBinding: GradientGameLayoutBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        gradientGameLayoutBinding = GradientGameLayoutBinding.inflate(layoutInflater)
        setContentView(gradientGameLayoutBinding.root)

        window.decorView.setBackgroundColor(Color.CYAN)
        gradientGameLayoutBinding.root.background = getDrawable(co.geeksempire.experiment.R.drawable.splash_screen_initial)

        multipleGradientFrame = gradientAnimations.allDrawableAnimations.random()

        gradientGameLayoutBinding.selectedColor.setImageDrawable(multipleGradientFrame)
        gradientGameLayoutBinding.selectedColor.setShapeDrawable(allShapes.random())

        multipleGradientAnimation = gradientAnimations.multipleGradientAnimation(gradientGameLayoutBinding.backgroundView)

        gradientGameLayoutBinding.backgroundView.setOnClickListener { view ->
            gradientGameLayoutBinding.backgroundView.isEnabled = false

            multipleGradientAnimation?.let {

                if (it.current == multipleGradientFrame) {
                    Log.d(this@GradientGame.javaClass.simpleName, "Equal")

                    it.stop()
                    Toast.makeText(applicationContext, "You Win!", Toast.LENGTH_LONG).show()

                    Handler(Looper.getMainLooper()).postDelayed({

                        this@GradientGame.recreate()

                    }, 3333)

                } else {
                    Log.d(this@GradientGame.javaClass.simpleName, "Not Equal")

                    gradientGameLayoutBinding.backgroundView.isEnabled = true

                }

            }

        }

    }

    override fun onResume() {
        super.onResume()

        multipleGradientFrame?.let {
            gradientGameLayoutBinding.selectedColor.setImageDrawable(it)
        }

        multipleGradientAnimation?.start()

    }

    override fun onPause() {
        super.onPause()

        multipleGradientAnimation?.stop()

    }

}