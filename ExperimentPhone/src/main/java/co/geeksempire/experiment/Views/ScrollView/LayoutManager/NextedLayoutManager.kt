/*
 * Copyright © 2022 By Geeks Empire.
 *
 * Created by Elias Fazel
 * Last modified 12/17/22, 5:38 AM
 *
 * Licensed Under MIT License.
 * https://opensource.org/licenses/MIT
 */

package co.geeksempire.experiment.Views.ScrollView.LayoutManager

import android.content.Context
import android.os.Handler
import android.os.Looper
import android.view.View
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import kotlin.math.abs
import kotlin.math.min

class NextedLayoutManager (private val context: Context,
                           private val recyclerView: RecyclerView,
                           val nextedLayoutManagerFactory: NextedLayoutManagerFactory) : LinearLayoutManager(context, nextedLayoutManagerFactory.layoutOrientation, false) {

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

                    Handler(Looper.getMainLooper()).postDelayed({

                        recyclerView.smoothScrollToPosition(2)

                    }, 13)

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

                    Handler(Looper.getMainLooper()).postDelayed({

                        recyclerView.smoothScrollToPosition(2)

                    }, 13)

                }
            }


        }, 13)

        recyclerView.addOnScrollListener(object : RecyclerView.OnScrollListener() {

            override fun onScrollStateChanged(recyclerView: RecyclerView, newState: Int) {
                super.onScrollStateChanged(recyclerView, newState)

                when (newState) {
                    RecyclerView.SCROLL_STATE_IDLE -> {

                        if (nextedLayoutManagerFactory.snapIt) {

                            recyclerView.smoothScrollToPosition(findFirstCompletelyVisibleItemPosition())

                        }

                    }
                    RecyclerView.SCROLL_STATE_DRAGGING -> {



                    }
                    RecyclerView.SCROLL_STATE_SETTLING -> {



                    }
                }

            }

        })

    }

    override fun scrollHorizontallyBy(dx: Int, recycler: RecyclerView.Recycler?, recycleViewState: RecyclerView.State?): Int {

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

        return super.scrollHorizontallyBy(dx, recycler, recycleViewState)
    }

    override fun scrollVerticallyBy(dy: Int, recycler: RecyclerView.Recycler?, recycleViewState: RecyclerView.State?) : Int {

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

                child.scaleX = scale
                child.scaleY = scale

                child.alpha = scale

            }

        }

        return super.scrollVerticallyBy(dy, recycler, recycleViewState)
    }

}