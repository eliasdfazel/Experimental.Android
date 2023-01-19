/*
 * Copyright © 2023 By Geeks Empire.
 *
 * Created by Elias Fazel
 * Last modified 1/19/23, 10:29 AM
 *
 * Licensed Under MIT License.
 * https://opensource.org/licenses/MIT
 */

package co.geeksempire.experiment.Tests

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import co.geeksempire.experiment.databinding.WaitingTestsLayoutBinding

class WaitingTests : AppCompatActivity() {

    lateinit var waitingTestsLayoutBinding: WaitingTestsLayoutBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        waitingTestsLayoutBinding = WaitingTestsLayoutBinding.inflate(layoutInflater)
        setContentView(waitingTestsLayoutBinding.root)



    }

}