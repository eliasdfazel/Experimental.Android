/*
 * Copyright © 2022 By Geeks Empire.
 *
 * Created by Elias Fazel
 * Last modified 12/21/22, 4:58 AM
 *
 * Licensed Under MIT License.
 * https://opensource.org/licenses/MIT
 */

package co.geeksempire.experiment.SectionedScrolls

import android.graphics.drawable.ColorDrawable
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.RecyclerView
import co.geeksempire.experiment.R
import co.geeksempire.experiment.Scrolls.ScrollsViewHolder
import co.geeksempire.experiment.databinding.ScrollsItemLayoutBinding
import co.geeksempire.experiment.databinding.SectionedScrollsItemLayoutBinding

class SectionedScrollsAdapter (private val context: AppCompatActivity,
                               private val recyclerView: RecyclerView,
                               private val dataList: List<Int>) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    val viewTypeSection = 1
    val viewTypeItem = 2

    override fun getItemCount(): Int {

        return dataList.size
    }

    override fun getItemViewType(position: Int): Int {
        super.getItemViewType(position)

        return when (dataList[position]) {
            context.getColor(R.color.transparent) -> {
                viewTypeSection
            }
            else -> {
                viewTypeItem
            }
        }
    }

    override fun onCreateViewHolder(viewGroup: ViewGroup, viewType: Int): RecyclerView.ViewHolder {


        return when (viewType) {
            viewTypeSection -> {

                SectionedsViewHolder(SectionedScrollsItemLayoutBinding.inflate(context.layoutInflater, viewGroup, false))

            }
            viewTypeItem -> {

                ScrollsViewHolder(ScrollsItemLayoutBinding.inflate(context.layoutInflater, viewGroup, false))

            }
            else -> {

                ScrollsViewHolder(ScrollsItemLayoutBinding.inflate(context.layoutInflater, viewGroup, false))

            }
        }
    }

    override fun onBindViewHolder(scrollsViewHolder: RecyclerView.ViewHolder, position: Int) {

        when (scrollsViewHolder) {
            is ScrollsViewHolder -> {

                scrollsViewHolder.colorItem.setImageDrawable(ColorDrawable(dataList[position]))

                scrollsViewHolder.rootItem.setOnClickListener {

                    recyclerView.smoothScrollToPosition(position)

                }

            }
            is SectionedsViewHolder -> {

                scrollsViewHolder.itemSection.text = dataList[position - 1].toString()

                scrollsViewHolder.rootItem.setOnClickListener {

                    recyclerView.smoothScrollToPosition(position)

                }

            }
        }

    }

}