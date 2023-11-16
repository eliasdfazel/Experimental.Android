/*
 * Copyright © 2023 By Geeks Empire.
 *
 * Created by Elias Fazel
 * Last modified 11/16/23, 4:18 AM
 *
 * Licensed Under MIT License.
 * https://opensource.org/licenses/MIT
 */

package co.geeksempire.experiment

import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import co.geeksempire.experiment.Widgets.WidgetsConfigurations
import co.geeksempire.experiment.databinding.ExperimentSelectorLayoutBinding

class ExperimentSelector : AppCompatActivity() {

    lateinit var experimentSelectorLayoutBinding: ExperimentSelectorLayoutBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        experimentSelectorLayoutBinding = ExperimentSelectorLayoutBinding.inflate(layoutInflater)
        setContentView(experimentSelectorLayoutBinding.root)

        window.decorView.setBackgroundColor(Color.CYAN)
        experimentSelectorLayoutBinding.root.background = getDrawable(R.drawable.splash_screen_initial)

        startActivity(Intent(this@ExperimentSelector, WidgetsConfigurations::class.java))

    }

}