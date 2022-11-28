/*
 * Copyright © 2022 By Geeks Empire.
 *
 * Created by Elias Fazel
 * Last modified 11/28/22, 7:15 AM
 *
 * Licensed Under MIT License.
 * https://opensource.org/licenses/MIT
 */

package co.geeksempire.experiment

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import co.geeksempire.experiment.Animations.GradientAnimations
import co.geeksempire.experiment.databinding.ExperimentSelectorLayoutBinding

class ExperimentSelector : AppCompatActivity() {

    lateinit var experimentSelectorLayoutBinding: ExperimentSelectorLayoutBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        experimentSelectorLayoutBinding = ExperimentSelectorLayoutBinding.inflate(layoutInflater)
        setContentView(experimentSelectorLayoutBinding.root)

//        experimentSelectorLayoutBinding.root.background = GradientDrawable(GradientDrawable.Orientation.TR_BL,
//            intArrayOf(
//                getColor(R.color.default_color_bright),
//                getColor(R.color.default_color_game_bright),
//                ColorsModification().mixColors(getColor(R.color.default_color_bright), getColor(R.color.default_color_game_bright), 0.5f),
//                getColor(R.color.default_color_game_bright))
//        )

        val gradientAnimations = GradientAnimations(applicationContext)
        gradientAnimations.multipleGradientAnimation(experimentSelectorLayoutBinding.root).start()

//        gradientAnimations.multipleGradientAnimation(experimentSelectorLayoutBinding.root).start()

        experimentSelectorLayoutBinding.root.setOnClickListener {



        }

    }
}