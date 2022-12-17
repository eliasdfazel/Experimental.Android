/*
 * Copyright © 2022 By Geeks Empire.
 *
 * Created by Elias Fazel
 * Last modified 12/17/22, 5:20 AM
 *
 * Licensed Under MIT License.
 * https://opensource.org/licenses/MIT
 */

package co.geeksempire.experiment.Views.ScrollView.LayoutManager

import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.RecyclerView
import co.geeksempire.experiment.Scrolls.ItemTouchHelperContract
import co.geeksempire.experiment.Scrolls.ScrollsViewHolder
import co.geeksempire.experiment.Views.ScrollView.NextedScrollsAdapter

class NextedTouchHelper (scrollsAdapter: NextedScrollsAdapter) : ItemTouchHelper.Callback() {

    private var itemTouchHelperContract: ItemTouchHelperContract = scrollsAdapter

    override fun isLongPressDragEnabled(): Boolean {

        return true
    }

    override fun isItemViewSwipeEnabled(): Boolean {

        return false
    }


    override fun onSwiped(viewHolder: RecyclerView.ViewHolder, i: Int) {

    }

    override fun getMovementFlags(recyclerView: RecyclerView, viewHolder: RecyclerView.ViewHolder): Int {

        val dragFlags = ItemTouchHelper.UP or ItemTouchHelper.DOWN

        return ItemTouchHelper.Callback.makeMovementFlags(dragFlags, 0)
    }

    override fun onMove(recyclerView: RecyclerView, viewHolder: RecyclerView.ViewHolder, target: RecyclerView.ViewHolder): Boolean {

        itemTouchHelperContract.onRowMoved(viewHolder.adapterPosition, target.adapterPosition)

        return true
    }

    override fun onSelectedChanged(viewHolder: RecyclerView.ViewHolder?, actionState: Int) {

        if (actionState != ItemTouchHelper.ACTION_STATE_IDLE) {
            if (viewHolder is ScrollsViewHolder) {

                val myViewHolder: ScrollsViewHolder = viewHolder

                itemTouchHelperContract.onRowSelected(myViewHolder)

            }
        }

        super.onSelectedChanged(viewHolder, actionState)
    }

    override fun clearView(recyclerView: RecyclerView, viewHolder: RecyclerView.ViewHolder) {
        super.clearView(recyclerView, viewHolder)

        if (viewHolder is ScrollsViewHolder) {

            val myViewHolder: ScrollsViewHolder = viewHolder

            itemTouchHelperContract.onRowClear(myViewHolder)

        }
    }

}