/*
 * Copyright © 2022 By Geeks Empire.
 *
 * Created by Elias Fazel
 * Last modified 12/16/22, 6:18 AM
 *
 * Licensed Under MIT License.
 * https://opensource.org/licenses/MIT
 */

package co.geeksempire.experiment.Scrolls

import android.graphics.drawable.ColorDrawable
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.RecyclerView
import co.geeksempire.experiment.R
import co.geeksempire.experiment.databinding.ScrollsItemLayoutBinding

class ScrollsAdapter (private val context: AppCompatActivity) : RecyclerView.Adapter<ScrollsViewHolder>() {

    val allColors = intArrayOf(
        context.getColor(R.color.default_color_bright),
        context.getColor(R.color.yellow),
        context.getColor(R.color.default_color_game_bright),
        context.getColor(R.color.cyberGreen),
        context.getColor(R.color.default_color_game_dark),
        context.getColor(R.color.white),
        context.getColor(R.color.pink),
    )

    override fun getItemCount(): Int {

        return allColors.size
    }

    override fun getItemViewType(position: Int): Int {
        super.getItemViewType(position)

        return 0
    }

    override fun onCreateViewHolder(viewGroup: ViewGroup, viewType: Int): ScrollsViewHolder {

        return ScrollsViewHolder(ScrollsItemLayoutBinding.inflate(context.layoutInflater, viewGroup, false))
    }

    override fun onBindViewHolder(scrollsViewHolder: ScrollsViewHolder, position: Int) {

        scrollsViewHolder.colorItem.setImageDrawable(ColorDrawable(allColors.random()))

    }

}