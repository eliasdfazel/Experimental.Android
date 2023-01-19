/*
 * Copyright © 2023 By Geeks Empire.
 *
 * Created by Elias Fazel
 * Last modified 1/19/23, 10:13 AM
 *
 * Licensed Under MIT License.
 * https://opensource.org/licenses/MIT
 */

package co.geeksempire.experiment.Tests

import android.graphics.Color
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import co.geeksempire.experiment.R
import co.geeksempire.experiment.databinding.BlurryTestsLayoutBinding

class BlurryTests : AppCompatActivity() {

    lateinit var blurryTestsLayoutBinding: BlurryTestsLayoutBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        blurryTestsLayoutBinding = BlurryTestsLayoutBinding.inflate(layoutInflater)
        setContentView(blurryTestsLayoutBinding.root)

        window.decorView.setBackgroundColor(Color.CYAN)
        blurryTestsLayoutBinding.root.background = getDrawable(R.drawable.splash_screen_initial)


    }

}