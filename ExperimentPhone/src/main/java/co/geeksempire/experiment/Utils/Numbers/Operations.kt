/*
 * Copyright © 2023 By Geeks Empire.
 *
 * Created by Elias Fazel
 * Last modified 12/23/23, 7:15 AM
 *
 * Licensed Under MIT License.
 * https://opensource.org/licenses/MIT
 */

package co.geeksempire.experiment.Utils.Numbers

fun Float.roundTo(decimalPoint: Int = 3) : Float {

    val formattedString = if ((decimalPoint + 3) <= (this@roundTo.toString().length)) {
        this@roundTo.toString().substring(0, (decimalPoint + 3)).toFloat()
    } else {
        decimalPoint.toFloat()
    }

    return formattedString
}