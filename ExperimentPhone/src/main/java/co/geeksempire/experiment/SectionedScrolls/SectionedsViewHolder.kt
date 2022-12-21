/*
 * Copyright © 2022 By Geeks Empire.
 *
 * Created by Elias Fazel
 * Last modified 12/21/22, 4:52 AM
 *
 * Licensed Under MIT License.
 * https://opensource.org/licenses/MIT
 */

package co.geeksempire.experiment.SectionedScrolls

import androidx.recyclerview.widget.RecyclerView
import co.geeksempire.experiment.databinding.SectionedScrollsItemLayoutBinding

class SectionedsViewHolder (sectionedScrollsItemLayoutBinding: SectionedScrollsItemLayoutBinding) : RecyclerView.ViewHolder(sectionedScrollsItemLayoutBinding.root) {
    val rootItem = sectionedScrollsItemLayoutBinding.root
    val itemSection = sectionedScrollsItemLayoutBinding.itemSection
}