/*
 * Copyright © 2023 By Geeks Empire.
 *
 * Created by Elias Fazel
 * Last modified 11/16/23, 3:53 AM
 *
 * Licensed Under MIT License.
 * https://opensource.org/licenses/MIT
 */

package co.geeksempire.blurry.effect.library

import android.app.Activity
import android.content.Context
import android.content.ContextWrapper
import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.LinearGradient
import android.graphics.Paint
import android.graphics.Path
import android.graphics.PorterDuff
import android.graphics.PorterDuffXfermode
import android.graphics.RadialGradient
import android.graphics.Rect
import android.graphics.RectF
import android.graphics.Shader
import android.os.Build
import android.util.AttributeSet
import android.util.TypedValue
import android.view.View
import android.view.ViewTreeObserver
import co.geeksempire.blurry.effect.blurImplementation.AndroidXBlurImpl
import co.geeksempire.blurry.effect.blurImplementation.SupportLibraryBlurImpl
import co.geeksempire.blurry.effect.utils.VectorPathParser
import net.geeksempire.blurry.effect.view.R
import kotlin.math.min


open class OverlayBlur(context: Context, attributeSet: AttributeSet) : View(context, attributeSet) {

    private var downSampleFactor = 0f // default 4

    private var firstColor = 0 // default #aaffffff
    private var secondColor = 0 // default -666

    /**
     * Default 10dp (0 < radius <= 25)
     */
    private var blurRadius = 0f // default 10dp (0 < r <= 25)

    private var topLeftCorner = 0f
    private var topRightCorner = 0f
    private var bottomLeftCorner = 0f
    private var bottomRightCorner = 0f

    var gradientType = 0

    var GradientTypeNone = 0
    var GradientTypeRadial = 1

    var GradientTypeLinearTB = 2
    var GradientTypeLinearLR = 3
    var GradientTypeLinearTilt = 4

    var shapePath: String?
    var scaleAmount = 37

    var clipPath = Path()
    var rectF = RectF()

    private var blurImpl: BlurImpl? = null

    private var dirty = false
    private var bitmapToBlur: Bitmap? = null
    private var blurredBitmap: Bitmap? = null

    private var blurringCanvas: Canvas? = null
    private var isRendering = false

    private var paintInstance: Paint = Paint()

    private var rectSrc = Rect()
    private var rectDst = Rect()

    // mDecorView should be the root view of the activity (even if you are on a different window like a dialog)
    private var decorView: View? = null

    // If the view is on different root view (usually means we are on a PopupWindow),
    // we need to manually call invalidate() in onPreDraw(), otherwise we will not be able to see the changes
    private var differentRoot = false

    private var RENDERING_COUNT = 0
    private var BLUR_IMPL = 0

    private val vectorPathParser = VectorPathParser()

    init {

        blurImpl = getBlurImpl() // provide your own by override getBlurImpl()


        val typedArray = context.obtainStyledAttributes(attributeSet, R.styleable.RealtimeBlurView)

        blurRadius = typedArray.getDimension(
            R.styleable.RealtimeBlurView_realtimeBlurRadius, TypedValue.applyDimension(
                TypedValue.COMPLEX_UNIT_DIP, 10f, context.resources.displayMetrics
            )
        )

        downSampleFactor =
            typedArray.getFloat(R.styleable.RealtimeBlurView_realtimeDownSampleFactor, 4f)

        firstColor = typedArray.getColor(R.styleable.RealtimeBlurView_realtimeFirstColor, -0x55000001)
        secondColor = typedArray.getColor(R.styleable.RealtimeBlurView_realtimeSecondColor, -666)

        topLeftCorner = typedArray.getDimension(R.styleable.RealtimeBlurView_realtimeBlurTopLeft, 0f)
        topRightCorner = typedArray.getDimension(R.styleable.RealtimeBlurView_realtimeBlurTopRight, 0f)
        bottomLeftCorner = typedArray.getDimension(R.styleable.RealtimeBlurView_realtimeBlurBottomLeft, 0f)
        bottomRightCorner = typedArray.getDimension(R.styleable.RealtimeBlurView_realtimeBlurBottomRight, 0f)

        shapePath = typedArray.getString(R.styleable.RealtimeBlurView_realtimeShapePath)
        scaleAmount = typedArray.getInteger(R.styleable.RealtimeBlurView_realtimeScaleAmount, 10)

        if (typedArray.getInteger(R.styleable.RealtimeBlurView_realtimeBlurGradientType, 0) == 0) {
            gradientType = GradientTypeNone
        } else if (typedArray.getInteger(
                R.styleable.RealtimeBlurView_realtimeBlurGradientType,
                0
            ) == 1
        ) {
            gradientType = GradientTypeRadial
        } else if (typedArray.getInteger(
                R.styleable.RealtimeBlurView_realtimeBlurGradientType,
                0
            ) == 2
        ) {
            gradientType = GradientTypeLinearTB
        } else if (typedArray.getInteger(
                R.styleable.RealtimeBlurView_realtimeBlurGradientType,
                0
            ) == 3
        ) {
            gradientType = GradientTypeLinearLR
        } else if (typedArray.getInteger(
                R.styleable.RealtimeBlurView_realtimeBlurGradientType,
                0
            ) == 4
        ) {
            gradientType = GradientTypeLinearTilt
        }

        typedArray.recycle()

        paintInstance = Paint()

    }

    private fun getBlurImpl(): BlurImpl? {
        if (BLUR_IMPL == 0) {
            // try to use stock impl first
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1) {
                try {
                    val impl = AndroidStockBlurImpl()
                    val bmp = Bitmap.createBitmap(4, 4, Bitmap.Config.ARGB_8888)
                    impl.prepare(context, bmp, 4f)
                    impl.release()
                    bmp.recycle()
                    BLUR_IMPL = 3
                } catch (e: Throwable) {
                }
            }
        }
        if (BLUR_IMPL == 0) {
            try {
                javaClass.classLoader.loadClass("androidx.renderscript.RenderScript")
                // initialize RenderScript to load jni impl
                // may throw unsatisfied link error
                val impl = AndroidXBlurImpl()
                val bmp = Bitmap.createBitmap(4, 4, Bitmap.Config.ARGB_8888)
                impl.prepare(context, bmp, 4f)
                impl.release()
                bmp.recycle()
                BLUR_IMPL = 1
            } catch (e: Throwable) {
                // class not found or unsatisfied link
            }
        }
        if (BLUR_IMPL == 0) {
            try {
                javaClass.classLoader.loadClass("android.support.v8.renderscript.RenderScript")
                // initialize RenderScript to load jni impl
                // may throw unsatisfied link error
                val impl = SupportLibraryBlurImpl()
                val bmp = Bitmap.createBitmap(4, 4, Bitmap.Config.ARGB_8888)
                impl.prepare(context, bmp, 4f)
                impl.release()
                bmp.recycle()
                BLUR_IMPL = 2
            } catch (e: Throwable) {
                // class not found or unsatisfied link
            }
        }
        if (BLUR_IMPL == 0) {
            // fallback to empty impl, which doesn't have blur effect
            BLUR_IMPL = -1
        }
        return when (BLUR_IMPL) {
            1 -> AndroidXBlurImpl()
            2 -> SupportLibraryBlurImpl()
            3 -> AndroidStockBlurImpl()
            else -> EmptyBlurImpl()
        }
    }

    fun setBlurRadius(radius: Float) {
        if (blurRadius != radius) {
            blurRadius = radius
            dirty = true
            invalidate()
        }
    }

    fun setDownSampleFactor(factor: Float) {
        require(factor > 0) { "Downsample factor must be greater than 0." }
        if (downSampleFactor != factor) {
            downSampleFactor = factor
            dirty = true // may also change blur radius
            releaseBitmap()
            invalidate()
        }
    }

    fun setFirstColor(color: Int) {
        if (firstColor != color) {
            firstColor = color
            invalidate()
        }
    }

    fun setSecondColor(color: Int) {
        if (firstColor != color) {
            secondColor = color
            invalidate()
        }
    }

    private fun releaseBitmap() {
        if (bitmapToBlur != null) {
            bitmapToBlur?.recycle()
            bitmapToBlur = null
        }
        if (blurredBitmap != null) {
            blurredBitmap!!.recycle()
            blurredBitmap = null
        }
    }

    private fun release() {
        releaseBitmap()
        blurImpl?.release()
    }

    private fun prepare(): Boolean {
        if (blurRadius == 0f) {
            release()
            return false
        }
        var downSampleFactor = downSampleFactor
        var radius = blurRadius / downSampleFactor
        if (radius > 25) {
            downSampleFactor = downSampleFactor * radius / 25
            radius = 25f
        }
        val width = width
        val height = height
        val scaledWidth = Math.max(1, (width / downSampleFactor).toInt())
        val scaledHeight = Math.max(1, (height / downSampleFactor).toInt())
        var dirty: Boolean = dirty
        if (blurringCanvas == null || blurredBitmap == null || blurredBitmap!!.width != scaledWidth || blurredBitmap!!.height != scaledHeight) {
            dirty = true
            releaseBitmap()
            var r = false
            try {
                bitmapToBlur =
                    Bitmap.createBitmap(scaledWidth, scaledHeight, Bitmap.Config.ARGB_8888)
                if (bitmapToBlur == null) {
                    return false
                }
                blurringCanvas = Canvas(bitmapToBlur!!)
                blurredBitmap =
                    Bitmap.createBitmap(scaledWidth, scaledHeight, Bitmap.Config.ARGB_8888)
                if (blurredBitmap == null) {
                    return false
                }
                r = true
            } catch (e: OutOfMemoryError) {
                // Bitmap.createBitmap() may cause OOM error
                // Simply ignore and fallback
            } finally {
                if (!r) {
                    release()
                    return false
                }
            }
        }
        if (dirty) {
            if (blurImpl?.prepare(context, bitmapToBlur, radius)!!) {
                dirty = false
            } else {
                return false
            }
        }
        return true
    }

    private fun blur(bitmapToBlur: Bitmap?, blurredBitmap: Bitmap?) {
        blurImpl?.blur(bitmapToBlur, blurredBitmap)
    }

    private val preDrawListener: ViewTreeObserver.OnPreDrawListener = ViewTreeObserver.OnPreDrawListener {
        val locations = IntArray(2)
        var oldBmp = blurredBitmap
        val decor: View? = decorView
        if (decor != null && isShown && prepare()) {
            val redrawBitmap = blurredBitmap != oldBmp
            oldBmp = null
            decor.getLocationOnScreen(locations)
            var x = -locations[0]
            var y = -locations[1]
            getLocationOnScreen(locations)
            x += locations[0]
            y += locations[1]

            // just erase transparent
            bitmapToBlur?.eraseColor(firstColor and 0xffffff)
            val rc: Int = blurringCanvas?.save()!!
            isRendering = true
            RENDERING_COUNT++
            if(blurringCanvas != null && bitmapToBlur != null) {
                try {
                    blurringCanvas!!.scale(
                        1f * bitmapToBlur!!.getWidth() / width,
                        1f * bitmapToBlur!!.getHeight() / height
                    )
                    blurringCanvas!!.translate(-x.toFloat(), -y.toFloat())
                    if (decor.background != null) {
                        decor.background.draw(blurringCanvas!!)
                    }
                    decor.draw(blurringCanvas!!)
                } catch (e: Exception) {

                } finally {
                    isRendering = false
                    RENDERING_COUNT--
                    blurringCanvas!!.restoreToCount(rc)
                }
            }
            blur(bitmapToBlur, blurredBitmap)
            if (redrawBitmap || differentRoot) {
                invalidate()
            }
        }
        true
    }

    private fun getActivityDecorView(): View? {
        var ctx = context
        var i = 0
        while (i < 4 && ctx != null && ctx !is Activity && ctx is ContextWrapper) {
            ctx = ctx.baseContext
            i++
        }
        return if (ctx is Activity) {
            ctx.window.decorView
        } else {
            null
        }
    }

    override fun onAttachedToWindow() {
        super.onAttachedToWindow()
        decorView = getActivityDecorView()
        if (decorView != null) {
            decorView!!.viewTreeObserver.addOnPreDrawListener(preDrawListener)
            differentRoot = decorView!!.rootView !== rootView
            if (differentRoot) {
                decorView!!.postInvalidate()
            }
        } else {
            differentRoot = false
        }
    }

    override fun onDetachedFromWindow() {
        decorView?.viewTreeObserver?.removeOnPreDrawListener(preDrawListener)
        release()
        super.onDetachedFromWindow()
    }

    override fun draw(canvas: Canvas) {
        if (isRendering) {
            // Quit here, don't draw views above me
        } else if (RENDERING_COUNT > 0) {
            // Doesn't support blurview overlap on another blurview
        } else {
            super.draw(canvas)
        }
    }

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)

        /* Start - Custom Shape */
        if (shapePath != null) {

            val path = vectorPathParser.parser(vectorPath = shapePath!!, scaleAmount = scaleAmount)

            clipPath.addPath(path)

        } else {

            /* Start - Round Rectangle */
            rectF[0f, 0f, this.width.toFloat()] = this.height.toFloat()

            val radii = floatArrayOf(0f, 0f, 0f, 0f, 0f, 0f, 0f, 0f)
            radii[0] = topLeftCorner
            radii[1] = topLeftCorner
            radii[2] = topRightCorner
            radii[3] = topRightCorner
            radii[4] = bottomRightCorner
            radii[5] = bottomRightCorner
            radii[6] = bottomLeftCorner
            radii[7] = bottomLeftCorner

            clipPath.addRoundRect(rectF, radii, Path.Direction.CCW)
            /* End - Round Rectangle */

        }
        /* End - Custom Shape */

        canvas.clipPath(clipPath)

        blurredBitmap?.let { drawBlurredBitmap(canvas, it, firstColor, secondColor) }

    }

    /**
     * Custom draw the blurred bitmap and color to define your own shape
     *
     * @param canvas
     * @param blurredBitmap
     * @param firstColor
     */
    private fun drawBlurredBitmap(canvas: Canvas, blurredBitmap: Bitmap, firstColor: Int, secondColor: Int) {

        /* Start - Draw Blurred Effect */
        rectSrc.right = blurredBitmap.width
        rectSrc.bottom = blurredBitmap.height
        rectDst.right = width
        rectDst.bottom = height

        canvas.drawBitmap(blurredBitmap, rectSrc, rectDst, null)
        /* End - Draw Blurred Effect */

        /* Start - Gradient Effect */
        when (gradientType) {
            GradientTypeNone -> {
                paintInstance.color = firstColor
            }
            GradientTypeLinearLR -> {
                paintInstance.shader = LinearGradient(
                    0f, 0f,
                    width.toFloat(), 0f,  /* First Color */
                    firstColor,  /* Second Color */
                    secondColor,
                    Shader.TileMode.CLAMP
                )
            }
            GradientTypeLinearTB -> {
                paintInstance.shader = LinearGradient(
                    0f, 0f,
                    0f, height.toFloat(),  /* First Color */
                    firstColor,  /* Second Color */
                    secondColor,
                    Shader.TileMode.CLAMP
                )
            }
            GradientTypeLinearTilt -> {
                paintInstance.shader = LinearGradient(
                    width.toFloat(), 0f,
                    0f, height.toFloat(),  /* First Color */
                    firstColor,  /* Second Color */
                    secondColor,
                    Shader.TileMode.CLAMP
                )
            }
            GradientTypeRadial -> {
                paintInstance.shader = RadialGradient(
                    width / 2f,
                    height / 2f,
                    min(width, height) / 2f,
                    firstColor,
                    secondColor,
                    Shader.TileMode.CLAMP
                )
            }
        }

        canvas.drawRect(rectDst, paintInstance)
        /* End - Gradient Effect */

        /* Start - Frosted Text */
        canvas.drawText("XYZ", (width / 2).toFloat(), (height / 2).toFloat(), paintInstance.apply {
            color = Color.argb(255, 0, 128, 255)
            textSize = 393f
            textAlign = Paint.Align.CENTER
//            xfermode = PorterDuffXfermode(PorterDuff.Mode.CLEAR)
//            xfermode = PorterDuffXfermode(PorterDuff.Mode.ADD)
//            xfermode = PorterDuffXfermode(PorterDuff.Mode.XOR)
//            xfermode = PorterDuffXfermode(PorterDuff.Mode.DST_IN)
//            xfermode = PorterDuffXfermode(PorterDuff.Mode.SRC_OVER)
//            xfermode = PorterDuffXfermode(PorterDuff.Mode.SCREEN)
//            xfermode = PorterDuffXfermode(PorterDuff.Mode.LIGHTEN)
            xfermode = PorterDuffXfermode(PorterDuff.Mode.MULTIPLY)
        })
        /* End - Frosted Text */

    }

}