/*
 * Copyright © 2022 By Geeks Empire.
 *
 * Created by Elias Fazel
 * Last modified 12/17/22, 5:11 AM
 *
 * Licensed Under MIT License.
 * https://opensource.org/licenses/MIT
 */

package co.geeksempire.experiment.Views.ScrollView

import androidx.recyclerview.widget.RecyclerView
import co.geeksempire.experiment.Scrolls.ItemTouchHelperContract


abstract class NextedScrollsAdapter : RecyclerView.Adapter<RecyclerView.ViewHolder>(), ItemTouchHelperContract