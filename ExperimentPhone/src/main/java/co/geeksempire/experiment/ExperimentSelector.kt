/*
 * Copyright © 2022 By Geeks Empire.
 *
 * Created by Elias Fazel
 * Last modified 11/24/22, 3:20 AM
 *
 * Licensed Under MIT License.
 * https://opensource.org/licenses/MIT
 */

package co.geeksempire.experiment

import android.graphics.drawable.AnimationDrawable
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

        experimentSelectorLayoutBinding.root.background = getDrawable(R.drawable.gradient_animation)

        Handler(Looper.getMainLooper()).postDelayed({

//            val animationDrawable = AnimationDrawable()
//            animationDrawable.addFrame()

            val animDrawable = experimentSelectorLayoutBinding.root.background as AnimationDrawable
            animDrawable.setEnterFadeDuration(7)
            animDrawable.setExitFadeDuration(3333)
            animDrawable.start()

        }, 3333)



    }
}