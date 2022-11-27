/*
 * Copyright © 2022 By Geeks Empire.
 *
 * Created by Elias Fazel
 * Last modified 11/27/22, 3:45 AM
 *
 * Licensed Under MIT License.
 * https://opensource.org/licenses/MIT
 */

package co.geeksempire.experiment

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import co.geeksempire.experiment.databinding.ExperimentSelectorLayoutBinding

class ExperimentSelector : AppCompatActivity() {

    lateinit var experimentSelectorLayoutBinding: ExperimentSelectorLayoutBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        experimentSelectorLayoutBinding = ExperimentSelectorLayoutBinding.inflate(layoutInflater)
        setContentView(experimentSelectorLayoutBinding.root)

        experimentSelectorLayoutBinding.root.setOnClickListener {



        }

        experimentSelectorLayoutBinding.loadingView.setOnClickListener {



        }

    }
}