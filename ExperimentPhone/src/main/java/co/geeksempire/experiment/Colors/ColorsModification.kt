/*
 * Copyright © 2022 By Geeks Empire.
 *
 * Created by Elias Fazel
 * Last modified 11/28/22, 6:48 AM
 *
 * Licensed Under MIT License.
 * https://opensource.org/licenses/MIT
 */

package co.geeksempire.experiment.Colors

import android.graphics.Color

class ColorsModification {

    /**
     * @param colorRatio colorRatio 1 for colorOne
     * @param colorRatio colorRatio 0 for colorTwo
     **/
    fun mixColors(colorOne: Int, colorTwo: Int, colorRatio: Float /* 0 -> 1 */): Int {
        /* ratio = 1 >> color1 */
        /* ratio = 0 >> color2 */
        val inverseRation = 1f - colorRatio

        val r = Color.red(colorOne) * colorRatio + Color.red(colorTwo) * inverseRation
        val g = Color.green(colorOne) * colorRatio + Color.green(colorTwo) * inverseRation
        val b = Color.blue(colorOne) * colorRatio + Color.blue(colorTwo) * inverseRation

        return Color.rgb(r.toInt(), g.toInt(), b.toInt())
    }

}