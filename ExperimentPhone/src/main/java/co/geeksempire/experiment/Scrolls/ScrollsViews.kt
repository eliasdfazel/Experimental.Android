/*
 * Copyright © 2022 By Geeks Empire.
 *
 * Created by Elias Fazel
 * Last modified 12/17/22, 5:33 AM
 *
 * Licensed Under MIT License.
 * https://opensource.org/licenses/MIT
 */

package co.geeksempire.experiment.Scrolls

import android.os.Bundle
import android.view.animation.AnimationUtils
import android.view.animation.LayoutAnimationController
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.PagerSnapHelper
import androidx.recyclerview.widget.RecyclerView
import co.geeksempire.experiment.R
import co.geeksempire.experiment.Views.ScrollView.LayoutManager.NextedItemAnimation
import co.geeksempire.experiment.Views.ScrollView.LayoutManager.NextedLayoutManager
import co.geeksempire.experiment.Views.ScrollView.LayoutManager.NextedLayoutManagerFactory
import co.geeksempire.experiment.Views.ScrollView.LayoutManager.NextedTouchHelper
import co.geeksempire.experiment.Views.ScrollView.Utils.HorizontalSpacingItemDecoration
import co.geeksempire.experiment.databinding.ScrollsLayoutBinding
import co.geeksempire.geeksempire.layoutmanager.Curve.CurveLayoutManager
import co.geeksempire.geeksempire.layoutmanager.Curve.FanLayoutManagerSettings


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
            adapter = ScrollsAdapter(this@ScrollsViews, scrollsLayoutBinding.curvedRecyclerView, intArrayOf(
                getColor(R.color.transparent),
                getColor(R.color.transparent),
                getColor(R.color.default_color_bright),
                getColor(R.color.yellow),
                getColor(R.color.default_color_game_bright),
                getColor(R.color.cyberGreen),
                getColor(R.color.dark_gray),
                getColor(R.color.default_color_game_dark),
                getColor(R.color.green),
                getColor(R.color.white),
                getColor(R.color.pink),
                getColor(R.color.dark),
                getColor(R.color.transparent),
                getColor(R.color.transparent),
            ).toList())


            PagerSnapHelper().attachToRecyclerView(scrollsLayoutBinding.curvedRecyclerView)

            smoothScrollToPosition(2)
        }

        scrollsLayoutBinding.sizedRecyclerView.apply {

            val scrollAdapter = ScrollsAdapter(this@ScrollsViews, scrollsLayoutBinding.sizedRecyclerView, intArrayOf(
                getColor(R.color.transparent),
                getColor(R.color.transparent),
                getColor(R.color.default_color_bright),
                getColor(R.color.yellow),
                getColor(R.color.default_color_game_bright),
                getColor(R.color.cyberGreen),
                getColor(R.color.dark_gray),
                getColor(R.color.default_color_game_dark),
                getColor(R.color.green),
                getColor(R.color.white),
                getColor(R.color.pink),
                getColor(R.color.dark),
                getColor(R.color.transparent),
                getColor(R.color.transparent),
            ).toList())

            layoutManager = NextedLayoutManager(applicationContext, scrollsLayoutBinding.sizedRecyclerView,
                NextedLayoutManagerFactory(
                    layoutOrientation = RecyclerView.HORIZONTAL,
                    shrinkAmount = 0.19f,
                    shrinkDistance = 1f
                )
            )
            adapter = scrollAdapter

            PagerSnapHelper().attachToRecyclerView(scrollsLayoutBinding.sizedRecyclerView)

        }

        scrollsLayoutBinding.nextedRecyclerView.apply {

            val scrollsAdapter = ScrollsAdapter(this@ScrollsViews, scrollsLayoutBinding.nextedRecyclerView, intArrayOf(
                getColor(R.color.transparent),
                getColor(R.color.transparent),
                getColor(R.color.default_color_bright),
                getColor(R.color.yellow),
                getColor(R.color.default_color_game_bright),
                getColor(R.color.cyberGreen),
                getColor(R.color.dark_gray),
                getColor(R.color.default_color_game_dark),
                getColor(R.color.green),
                getColor(R.color.white),
                getColor(R.color.pink),
                getColor(R.color.dark),
                getColor(R.color.transparent),
                getColor(R.color.transparent),
            ).toList())

            val itemTouchHelper = ItemTouchHelper(NextedTouchHelper(scrollsAdapter))
            itemTouchHelper.attachToRecyclerView(scrollsLayoutBinding.nextedRecyclerView)

            addItemDecoration(HorizontalSpacingItemDecoration(applicationContext))
            itemAnimator = NextedItemAnimation()
            layoutAnimation = LayoutAnimationController(AnimationUtils.loadAnimation(applicationContext, R.anim.fade_in), 0.7f)
            layoutManager = NextedLayoutManager(applicationContext, scrollsLayoutBinding.nextedRecyclerView,
                NextedLayoutManagerFactory(
                    layoutOrientation = RecyclerView.VERTICAL,
                    shrinkAmount = 0.47f,
                    shrinkDistance = 1f,
                    snapIt = true
                )
            )
            adapter = scrollsAdapter

        }

    }

}

interface ItemTouchHelperContract {
    fun onRowMoved(fromPosition: Int, toPosition: Int)
    fun onRowSelected(myViewHolder: ScrollsViewHolder)
    fun onRowClear(myViewHolder: ScrollsViewHolder)
}