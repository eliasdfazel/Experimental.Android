/*
 * Copyright © 2022 By Geeks Empire.
 *
 * Created by Elias Fazel
 * Last modified 11/22/22, 11:15 PM
 *
 * Licensed Under MIT License.
 * https://opensource.org/licenses/MIT
 */

package co.geeksempire.sachiel.signals.administrators.Utils.Display

import android.content.Context
import android.content.res.Resources
import android.graphics.Paint
import android.util.DisplayMetrics
import android.util.Log
import android.util.TypedValue
import androidx.appcompat.widget.AppCompatTextView
import kotlin.math.pow
import kotlin.math.sqrt

fun columnCount(context: Context, itemWidth: Int) : Int {

    var spanCount = (displayX(context) / dpToPixel(context, itemWidth.toFloat())).toInt()

    if (spanCount < 1) {
        spanCount = 1
    }

    return spanCount
}

fun dpToPixel(context: Context, dp: Float) : Float {

    val resources: Resources = context.resources

    val metrics = resources.displayMetrics

    return dp * (metrics.densityDpi.toFloat() / DisplayMetrics.DENSITY_DEFAULT)
}

fun pixelToDp(context: Context, px: Float) : Float {

    return px / (context.resources.displayMetrics.densityDpi.toFloat() / DisplayMetrics.DENSITY_DEFAULT)
}

fun Float.dpToPixel(context: Context) : Float {

    val resources: Resources = context.resources

    val metrics = resources.displayMetrics

    return this@dpToPixel * (metrics.densityDpi.toFloat() / DisplayMetrics.DENSITY_DEFAULT)
}

fun Float.pixelToDp(context: Context) : Float {

    val resources: Resources = context.resources

    val metrics = resources.displayMetrics

    return this@pixelToDp / (metrics.densityDpi.toFloat() / DisplayMetrics.DENSITY_DEFAULT)
}

fun pixelToFloat(context: Context, pixel: Float) : Float {

    return dpToFloat(context, pixelToDp(context, pixel))
}

fun dpToFloat(context: Context, dp: Float): Float {

    return TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dp.toFloat(), context.resources.displayMetrics)
}

fun dpToInteger(context: Context, dp: Int): Int {

    return TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dp.toFloat(), context.resources.displayMetrics).toInt()
}

fun Float.spToInteger(context: Context) : Float {

    return TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, this@spToInteger, context.resources.displayMetrics)
}

fun percentageOfDisplayDiagonal(context: Context, percentageAmount: Float) : Float {

    return ((displayDiagonal(context) * percentageAmount) / 100).toFloat()
}

fun percentageOfDisplayX(context: Context, percentageAmount: Float) : Float {

    return (((displayX(context)) * percentageAmount) / 100).toFloat()
}

/**
 * Get Touch Pixel Position
 * @param displayEnd = displayX() Or displayY()
 **/
fun Float.pixelToPercentage(displayEnd: Float) : Float {

    return (this@pixelToPercentage * 100) / displayEnd
}

/**
 * Get Percentage Pixel Position
 * @param displayEnd = displayX() Or displayY()
 **/
fun Float.percentageToPixel(displayEnd: Float) : Float {

    return (this@percentageToPixel * displayEnd) / 100
}

fun displayX(context: Context) : Int {

    return context.resources.displayMetrics.widthPixels
}

fun displayY(context: Context) : Int {

    return context.resources.displayMetrics.heightPixels
}

fun displayDiagonal(context: Context) : Double {

    return sqrt((displayX(context).toDouble().pow(2.0)) + (displayY(context).toDouble().pow(2.0)))
}

fun statusBarHeight(context: Context): Int {

    var statusBarHeight = 0

    val resourceId = context.resources.getIdentifier("status_bar_height", "dimen", "android")
    if (resourceId > 0) {
        statusBarHeight = context.resources.getDimensionPixelSize(resourceId)
    }

    return statusBarHeight
}

fun navigationBarHeight(context: Context) : Int {

    var navigationBarHeight = 0

    val resourceIdNavigationBar: Int =
        context.resources.getIdentifier("navigation_bar_height", "dimen", "android")
    if (resourceIdNavigationBar > 0) {
        navigationBarHeight = context.resources.getDimensionPixelSize(resourceIdNavigationBar)
    }

    return navigationBarHeight
}

fun Float.calculatePercentage(percentageAmount: Float) : Float {

    return ((this@calculatePercentage * percentageAmount) / 100).toFloat()
}

fun generateHashTag(inputText: String) : String {

    val hashTagBuilder = StringBuilder()

    inputText.split(" ").forEachIndexed { index, text ->

        hashTagBuilder.append("#${text}" + " ")

    }

    return hashTagBuilder.toString()
}

fun generatePassword(inputText: String) : String {

    val charList = inputText.reversed().toList().shuffled()

    val passwordBuilder = StringBuilder()

    charList.forEachIndexed { index, text ->

        passwordBuilder.append("$text")

    }

    return passwordBuilder.toString()
}

fun String.widthOfText(textSize: Float) : Float {

    val paint = Paint()
    paint.textSize = textSize

    return paint.measureText(this@widthOfText, 0, this@widthOfText.length)
}

fun renderedText(textView: AppCompatTextView, startLine: Int, endLine: Int): String {

    val displayedText = textView.text.toString().substring(textView.layout.getLineStart(startLine), textView.layout.getEllipsisCount(endLine))
    Log.d("Rendered Text", displayedText)

    return displayedText
}