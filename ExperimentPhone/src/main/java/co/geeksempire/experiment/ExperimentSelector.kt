/*
 * Copyright © 2022 By Geeks Empire.
 *
 * Created by Elias Fazel
 * Last modified 11/29/22, 2:45 AM
 *
 * Licensed Under MIT License.
 * https://opensource.org/licenses/MIT
 */

package co.geeksempire.experiment

import android.content.Intent
import android.graphics.drawable.Drawable
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import co.geeksempire.experiment.Games.GradientGame
import co.geeksempire.experiment.databinding.ExperimentSelectorLayoutBinding
import net.geekstools.imageview.customshapes.R

class ExperimentSelector : AppCompatActivity() {

    val allShapes: ArrayList<Drawable?> by lazy {
        arrayListOf(
            applicationContext.getDrawable(R.drawable.beaker),
            applicationContext.getDrawable(R.drawable.android),
            applicationContext.getDrawable(R.drawable.bell),
            applicationContext.getDrawable(R.drawable.tooltip),
            applicationContext.getDrawable(R.drawable.ghost),
        )
    }

    lateinit var experimentSelectorLayoutBinding: ExperimentSelectorLayoutBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        experimentSelectorLayoutBinding = ExperimentSelectorLayoutBinding.inflate(layoutInflater)
        setContentView(experimentSelectorLayoutBinding.root)

        startActivity(Intent(this@ExperimentSelector, GradientGame::class.java))

    }
}