/*
 * Copyright © 2022 By Geeks Empire.
 *
 * Created by Elias Fazel
 * Last modified 12/16/22, 10:54 AM
 *
 * Licensed Under MIT License.
 * https://opensource.org/licenses/MIT
 */

package co.geeksempire.experiment.Scrolls

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.PagerSnapHelper
import androidx.recyclerview.widget.RecyclerView
import co.geeksempire.experiment.R
import co.geeksempire.experiment.databinding.ScrollsLayoutBinding
import co.geeksempire.geeksempire.layoutmanager.Curve.CurveLayoutManager
import co.geeksempire.geeksempire.layoutmanager.Curve.FanLayoutManagerSettings
import co.geeksempire.geeksempire.layoutmanager.NextedLayoutManager
import co.geeksempire.geeksempire.layoutmanager.Scale.ScaleLayoutManager

class ScrollsViews : AppCompatActivity() {

    lateinit var scrollsLayoutBinding: ScrollsLayoutBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        scrollsLayoutBinding = ScrollsLayoutBinding.inflate(layoutInflater)
        setContentView(scrollsLayoutBinding.root)

        window.decorView.setBackgroundColor(getColor(R.color.cyan))
        scrollsLayoutBinding.root.setBackgroundColor(getColor(R.color.default_color))

        scrollsLayoutBinding.curvedRecyclerView.apply {
            layoutManager = CurveLayoutManager(applicationContext,
                FanLayoutManagerSettings.newBuilder(applicationContext).apply {
                    withFanRadius(true)
                    withSelectedAnimation(true)
                    withViewHeightDp(101f)
                    withViewWidthDp(101f)
                }.build())
            adapter = ScrollsAdapter(this@ScrollsViews)


            PagerSnapHelper().attachToRecyclerView(scrollsLayoutBinding.curvedRecyclerView)
        }

        scrollsLayoutBinding.sizedRecyclerView.apply {
            layoutManager = ScaleLayoutManager(applicationContext, RecyclerView.HORIZONTAL)
            adapter = ScrollsAdapter(this@ScrollsViews)

            PagerSnapHelper().attachToRecyclerView(scrollsLayoutBinding.sizedRecyclerView)
        }

        scrollsLayoutBinding.nextedRecyclerView.apply {
            layoutManager = NextedLayoutManager(applicationContext, scrollsLayoutBinding.nextedRecyclerView, RecyclerView.VERTICAL)
            adapter = ScrollsAdapter(this@ScrollsViews)

            PagerSnapHelper().attachToRecyclerView(scrollsLayoutBinding.nextedRecyclerView)
        }

    }

}