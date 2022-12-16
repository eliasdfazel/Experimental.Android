/*
 * Copyright © 2022 By Geeks Empire.
 *
 * Created by Elias Fazel
 * Last modified 12/16/22, 7:42 AM
 *
 * Licensed Under MIT License.
 * https://opensource.org/licenses/MIT
 */

package co.geeksempire.experiment.Scrolls

import androidx.recyclerview.widget.RecyclerView
import co.geeksempire.experiment.databinding.ScrollsItemLayoutBinding

class ScrollsViewHolder (scrollsItemLayoutBinding: ScrollsItemLayoutBinding) : RecyclerView.ViewHolder(scrollsItemLayoutBinding.root) {
    val rootItem = scrollsItemLayoutBinding.root
    val colorItem = scrollsItemLayoutBinding.colorItem
}