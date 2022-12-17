/*
 * Copyright © 2022 By Geeks Empire.
 *
 * Created by Elias Fazel
 * Last modified 12/17/22, 3:45 AM
 *
 * Licensed Under MIT License.
 * https://opensource.org/licenses/MIT
 */

package co.geeksempire.experiment.Views.ScrollView.LayoutManager

import android.content.Context
import android.graphics.PointF
import android.os.Handler
import android.os.Looper
import android.util.DisplayMetrics
import android.view.View
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.LinearSmoothScroller
import androidx.recyclerview.widget.RecyclerView
import kotlin.math.abs
import kotlin.math.min

class NextedLayoutManager (private val context: Context,
                           private val recyclerView: RecyclerView,
                           val nextedLayoutManagerFactory: NextedLayoutManagerFactory) : LinearLayoutManager(context, nextedLayoutManagerFactory.layoutOrientation, false) {

    var velocityMillisecondPerInch = 23f

    init {


        Handler(Looper.getMainLooper()).postDelayed({

            when (nextedLayoutManagerFactory.layoutOrientation) {
                VERTICAL -> {

                    val midpoint = height / 2f

                    val d0 = 0f
                    val d1: Float = nextedLayoutManagerFactory.shrinkDistance * midpoint

                    val s0 = 1f
                    val s1: Float = 1f - nextedLayoutManagerFactory.shrinkAmount

                    for (i in 0..childCount) {

                        val child: View? = getChildAt(i)
                        child?.let {

                            val childMidpoint = (getDecoratedTop(child) + getDecoratedBottom(child)) / 2f
                            val d = min(d1, abs(midpoint - childMidpoint))
                            val scale = s0 + (s1 - s0) * (d - d0) / (d1 - d0)

                            child.scaleX = scale
                            child.scaleY = scale

                            child.alpha = scale

                        }

                    }

              //      scrollToPosition(2)

                }
                HORIZONTAL -> {

                    val midpoint = width / 2f

                    val d0 = 0f
                    val d1: Float = nextedLayoutManagerFactory.shrinkDistance * midpoint

                    val s0 = 1f
                    val s1: Float = 1f - nextedLayoutManagerFactory.shrinkAmount

                    val child: View? = getChildAt(1)
                    child?.let {

                        val childMidpoint = (getDecoratedRight(child) + getDecoratedLeft(child)) / 2f

                        val d = min(d1, abs(midpoint - childMidpoint))
                        val scale = s0 + (s1 - s0) * (d - d0) / (d1 - d0)

                        child.scaleX = scale
                        child.scaleY = scale

                        child.alpha = scale

                    }

                    scrollToPosition(2)

                }
            }


        }, 13)

        recyclerView.addOnScrollListener(object : RecyclerView.OnScrollListener() {

            override fun onScrollStateChanged(recyclerView: RecyclerView, newState: Int) {
                super.onScrollStateChanged(recyclerView, newState)

                when (newState) {
                    RecyclerView.SCROLL_STATE_IDLE -> {



                    }
                    RecyclerView.SCROLL_STATE_DRAGGING -> {



                    }
                    RecyclerView.SCROLL_STATE_SETTLING -> {



                    }
                }

            }

        })

    }

    override fun smoothScrollToPosition(recyclerView: RecyclerView?, state: RecyclerView.State?, position: Int) {

        val smoothScroller: LinearSmoothScroller = object : LinearSmoothScroller(context) {

            override fun computeScrollVectorForPosition(targetPosition: Int): PointF? {

                return this@NextedLayoutManager.computeScrollVectorForPosition(targetPosition)
            }

            override fun calculateSpeedPerPixel(displayMetrics: DisplayMetrics): Float {

                return velocityMillisecondPerInch / displayMetrics.densityDpi
            }

        }

        smoothScroller.targetPosition = position
        startSmoothScroll(smoothScroller)

    }

    override fun scrollHorizontallyBy(dx: Int, recycler: RecyclerView.Recycler?, recycleViewState: RecyclerView.State?): Int {

        val scrolled = super.scrollHorizontallyBy(dx, recycler, recycleViewState)

        val midpoint = width / 2f

        val d0 = 0f
        val d1: Float = nextedLayoutManagerFactory.shrinkDistance * midpoint

        val s0 = 1f
        val s1: Float = 1f - nextedLayoutManagerFactory.shrinkAmount

        for (i in 0 until childCount) {

            val child: View? = getChildAt(i)
            child?.let {

                val childMidpoint = (getDecoratedRight(child) + getDecoratedLeft(child)) / 2f

                val d = min(d1, abs(midpoint - childMidpoint))
                val scale = s0 + (s1 - s0) * (d - d0) / (d1 - d0)

                child.scaleX = scale
                child.scaleY = scale

                child.alpha = scale

            }

        }

        return scrolled
    }

    override fun scrollVerticallyBy(dy: Int, recycler: RecyclerView.Recycler?, recycleViewState: RecyclerView.State?) : Int {

        val scrolled = super.scrollVerticallyBy(dy, recycler, recycleViewState)

        val midpoint = height / 2f

        val d0 = 0f
        val d1: Float =nextedLayoutManagerFactory. shrinkDistance * midpoint

        val s0 = 1f
        val s1: Float = 1f - nextedLayoutManagerFactory.shrinkAmount

        for (i in 0..childCount) {

            val child: View? = getChildAt(i)
            child?.let {

                val childMidpoint = (getDecoratedTop(child) + getDecoratedBottom(child)) / 2f
                val d = min(d1, abs(midpoint - childMidpoint))
                val scale = s0 + (s1 - s0) * (d - d0) / (d1 - d0)

//                val divideFactor: Double = (i.toDouble().div(5.toDouble()))
//                val scale = (1 - divideFactor).toFloat()

                child.scaleX = scale
                child.scaleY = scale

                child.alpha = scale

            }

        }

        return scrolled
    }

}