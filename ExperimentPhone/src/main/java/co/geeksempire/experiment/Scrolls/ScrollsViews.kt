/*
 * Copyright © 2022 By Geeks Empire.
 *
 * Created by Elias Fazel
 * Last modified 12/16/22, 6:28 AM
 *
 * Licensed Under MIT License.
 * https://opensource.org/licenses/MIT
 */

package co.geeksempire.experiment.Scrolls

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import co.geeksempire.experiment.databinding.ScrollsLayoutBinding

class ScrollsViews : AppCompatActivity() {

    lateinit var scrollsLayoutBinding: ScrollsLayoutBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        scrollsLayoutBinding = ScrollsLayoutBinding.inflate(layoutInflater)
        setContentView(scrollsLayoutBinding.root)

        /*    val curveLayoutManager = CurveLayoutManager(applicationContext,
                FanLayoutManagerSettings.newBuilder(applicationContext).apply {
                    withFanRadius(true)
                    withSelectedAnimation(false)
                    withViewWidthDp(279f)
                    withViewHeightDp(439f)
                }.build())
            storefrontMoviesLayoutBinding.newContentRecyclerView.layoutManager = curveLayoutManager*/

    }

}