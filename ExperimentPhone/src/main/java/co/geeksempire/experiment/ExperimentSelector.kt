/*
 * Copyright © 2022 By Geeks Empire.
 *
 * Created by Elias Fazel
 * Last modified 11/24/22, 1:57 AM
 *
 * Licensed Under MIT License.
 * https://opensource.org/licenses/MIT
 */

package co.geeksempire.experiment

import android.graphics.drawable.AnimationDrawable
import android.graphics.drawable.GradientDrawable
import android.os.Bundle
import android.os.Handler
import android.os.Looper
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

            val animationDrawable = AnimationDrawable()
//            animationDrawable.addFrame()


        }, 3333)



    }
}