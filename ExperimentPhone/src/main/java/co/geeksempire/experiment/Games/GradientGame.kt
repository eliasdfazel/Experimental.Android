/*
 * Copyright © 2022 By Geeks Empire.
 *
 * Created by Elias Fazel
 * Last modified 12/8/22, 8:02 AM
 *
 * Licensed Under MIT License.
 * https://opensource.org/licenses/MIT
 */

package co.geeksempire.experiment.Games

import android.animation.ValueAnimator
import android.graphics.Color
import android.graphics.drawable.AnimationDrawable
import android.graphics.drawable.Drawable
import android.graphics.drawable.GradientDrawable
import android.os.Bundle
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

    var multipleGradientAnimator: ValueAnimator? = null

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

        gradientGameLayoutBinding.backgroundView.setImageDrawable(
            GradientDrawable(GradientDrawable.Orientation.TR_BL, intArrayOf(
                getColor(co.geeksempire.experiment.R.color.default_color_bright),
                getColor(co.geeksempire.experiment.R.color.default_color_bright),
                getColor(co.geeksempire.experiment.R.color.default_color_bright),
            ))
        )
        gradientAnimations.multipleGradientLevelOne(gradientGameLayoutBinding.backgroundView)

        gradientGameLayoutBinding.backgroundView.setOnClickListener { view ->
            gradientGameLayoutBinding.backgroundView.isEnabled = false

//            multipleGradientAnimation?.let {
//
//                if (it.current == multipleGradientFrame) {
//                    Log.d(this@GradientGame.javaClass.simpleName, "Equal")
//
//                    Toast.makeText(applicationContext, "You Win!", Toast.LENGTH_LONG).show()
//
//                    it.stop()
//
//                    Handler(Looper.getMainLooper()).postDelayed({
//
//                        this@GradientGame.recreate()
//
//                    }, 3333)
//
//                } else {
//                    Log.d(this@GradientGame.javaClass.simpleName, "Not Equal")
//
//                    gradientGameLayoutBinding.backgroundView.isEnabled = true
//
//                }
//
//            }

        }

    }

    override fun onResume() {
        super.onResume()

//        multipleGradientFrame?.let {
//            gradientGameLayoutBinding.selectedColor.setImageDrawable(it)
//        }
//
//        multipleGradientAnimation?.start()

    }

    override fun onPause() {
        super.onPause()

//        multipleGradientAnimation?.stop()

    }

}