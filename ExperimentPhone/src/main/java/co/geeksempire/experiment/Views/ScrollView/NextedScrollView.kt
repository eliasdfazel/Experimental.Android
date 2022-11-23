/*
 * Copyright © 2022 By Geeks Empire.
 *
 * Created by Elias Fazel
 * Last modified 11/22/22, 11:19 PM
 *
 * Licensed Under MIT License.
 * https://opensource.org/licenses/MIT
 */

package co.geeksempire.sachiel.signals.administrators.Utils.Views.Scrolls

import android.content.Context
import android.content.res.TypedArray
import android.util.AttributeSet
import android.util.Log
import androidx.core.widget.NestedScrollView
import co.geeksempire.experiment.R

class NextedScrollView(context: Context, attributesSet: AttributeSet) : NestedScrollView(context, attributesSet) {

    var typedArray: TypedArray = context.obtainStyledAttributes(attributesSet, R.styleable.NextedScrollView)

    val flingVelocityFraction: Int = typedArray.getInt(R.styleable.NextedScrollView_flingVelocityFraction, 5)

    override fun fling(velocityY: Int) {
        super.fling(velocityY / flingVelocityFraction)

        Log.d(this@NextedScrollView.javaClass.simpleName, "Velocity Fraction: ${flingVelocityFraction}")
    }

}