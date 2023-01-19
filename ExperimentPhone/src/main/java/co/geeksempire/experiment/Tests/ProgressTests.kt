/*
 * Copyright © 2023 By Geeks Empire.
 *
 * Created by Elias Fazel
 * Last modified 1/19/23, 10:17 AM
 *
 * Licensed Under MIT License.
 * https://opensource.org/licenses/MIT
 */

package co.geeksempire.experiment.Tests

import android.graphics.Color
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import co.geeksempire.experiment.R
import co.geeksempire.experiment.databinding.ProgressTestsLayoutBinding
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.delay

class ProgressTests  : AppCompatActivity() {

    lateinit var progressTestsLayoutBinding: ProgressTestsLayoutBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        progressTestsLayoutBinding = ProgressTestsLayoutBinding.inflate(layoutInflater)
        setContentView(progressTestsLayoutBinding.root)

        window.decorView.setBackgroundColor(Color.CYAN)
        progressTestsLayoutBinding.root.background = getDrawable(R.drawable.splash_screen_initial)

        progressTestsLayoutBinding.progressBar.setOnClickListener {

            setUpdateProgress()

        }


    }

    fun setUpdateProgress() = CoroutineScope(Dispatchers.Main).async {


        repeat(100) {

            delay(111)

            progressTestsLayoutBinding.progressBar.setProgress(it, true)

        }

    }

}