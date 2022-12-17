/*
 * Copyright © 2022 By Geeks Empire.
 *
 * Created by Elias Fazel
 * Last modified 12/17/22, 5:22 AM
 *
 * Licensed Under MIT License.
 * https://opensource.org/licenses/MIT
 */

package co.geeksempire.experiment.Scrolls

import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.RecyclerView
import co.geeksempire.experiment.Views.ScrollView.NextedScrollsAdapter
import co.geeksempire.experiment.databinding.ScrollsItemLayoutBinding
import java.util.*


class ScrollsAdapter (private val context: AppCompatActivity,
                      private val recyclerView: RecyclerView,
                      private val dataList: List<Int>) : NextedScrollsAdapter() {

    override fun getItemCount(): Int {

        return dataList.size
    }

    override fun getItemViewType(position: Int): Int {
        super.getItemViewType(position)

        return 0
    }

    override fun onCreateViewHolder(viewGroup: ViewGroup, viewType: Int): ScrollsViewHolder {

        return ScrollsViewHolder(ScrollsItemLayoutBinding.inflate(context.layoutInflater, viewGroup, false))
    }

    override fun onBindViewHolder(scrollsViewHolder: RecyclerView.ViewHolder, position: Int) {

        scrollsViewHolder as ScrollsViewHolder

        scrollsViewHolder.colorItem.setImageDrawable(ColorDrawable(dataList[position]))

        scrollsViewHolder.rootItem.setOnClickListener {

            recyclerView.smoothScrollToPosition(position)

        }

    }

    override fun onRowMoved(fromPosition: Int, toPosition: Int) {

        if (fromPosition < toPosition) {

            for (i in fromPosition until toPosition) {

                Collections.swap(dataList, i, i + 1)

            }

        } else {

            for (i in fromPosition downTo toPosition + 1) {

                Collections.swap(dataList, i, i - 1)

            }

        }

        notifyItemMoved(fromPosition, toPosition)

    }

    override fun onRowSelected(scrollsViewHolder: ScrollsViewHolder) {

        scrollsViewHolder.rootItem.setBackgroundColor(Color.WHITE)

    }

    override fun onRowClear(myViewHolder: ScrollsViewHolder) {

        myViewHolder.rootItem.setBackgroundColor(Color.TRANSPARENT)

    }

}