/*
 * Copyright © 2022 By Geeks Empire.
 *
 * Created by Elias Fazel
 * Last modified 11/27/22, 3:43 AM
 *
 * Licensed Under MIT License.
 * https://opensource.org/licenses/MIT
 */
package net.geeksempire.blurry.effect.view

import android.app.Activity
import android.content.Context
import android.content.ContextWrapper
import android.graphics.*
import android.os.Build
import android.util.AttributeSet
import android.util.TypedValue
import android.view.View
import android.view.ViewTreeObserver
import androidx.appcompat.widget.AppCompatImageView


/**
 * A realtime blurring overlay (like iOS UIVisualEffectView). Just put it above
 * the view you want to blur and it doesn't have to be in the same ViewGroup
 *
 *  * realtimeBlurRadius (10dp)
 *  * realtimeDownsampleFactor (4)
 *  * realtimeOverlayColor (#aaffffff)
 *
 */
class RealtimeBlurView(context: Context, attrs: AttributeSet?) : AppCompatImageView(context, attrs) {
    private var mDownsampleFactor // default 4
            : Float
    private var firstOverlayColor: Int = Color.WHITE
    private var secondOvarlayColr: Int = -666
    private var strokeWidth = 0
    private var strokeColor = Color.TRANSPARENT
    private var mBlurRadius // default 10dp (0 < r <= 25)
            : Float
    private val mBlurImpl: BlurImpl
    private var mDirty = false
    private var mBitmapToBlur: Bitmap? = null
    private var mBlurredBitmap: Bitmap? = null
    private var mBlurringCanvas: Canvas? = null
    private var mIsRendering = false
    private val paintInstance: Paint
    private val paintStrokeInstance: Paint
    private val mRectSrc = Rect()
    private val mRectDst = RectF()
    private var topLeftCorner = 0f
    private var topRightCorner = 0f
    private var bottomLeftCorner = 0f
    private var bottomRightCorner = 0f

    // mDecorView should be the root view of the activity (even if you are on a different window like a dialog)
    private var mDecorView: View? = null

    // If the view is on different root view (usually means we are on a PopupWindow),
    // we need to manually call invalidate() in onPreDraw(), otherwise we will not be able to see the changes
    private var mDifferentRoot = false
    var gradientType =
        0// fallback to empty impl, which doesn't have blur effect// class not found or unsatisfied link// initialize RenderScript to load jni impl

    // may throw unsatisfied link error
// class not found or unsatisfied link// initialize RenderScript to load jni impl
    // may throw unsatisfied link error
    // try to use stock impl first
    private val blurImpl: BlurImpl
        get() {
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
        if (mBlurRadius != radius) {
            mBlurRadius = radius
            mDirty = true
            invalidate()
        }
    }

    fun setDownsampleFactor(factor: Float) {
        require(factor > 0) { "Downsample factor must be greater than 0." }
        if (mDownsampleFactor != factor) {
            mDownsampleFactor = factor
            mDirty = true // may also change blur radius
            releaseBitmap()
            invalidate()
        }
    }

    fun setOverlayColor(color: Int) {
        if (firstOverlayColor != color) {
            firstOverlayColor = color
            invalidate()
        }
    }

    fun setSecondOverlayColor(color: Int) {
        if (secondOvarlayColr != color) {
            secondOvarlayColr = color
            invalidate()
        }
    }

    fun setStrokeWidth(initialStrokeWidth: Int) {
        if (strokeWidth != initialStrokeWidth) {
            strokeWidth = initialStrokeWidth
            invalidate()
        }
    }

    fun setStrokeColor(initialStrokeColor: Int) {
        if (strokeColor != initialStrokeColor) {
            strokeColor = initialStrokeColor
            invalidate()
        }
    }

    private fun releaseBitmap() {
        if (mBitmapToBlur != null) {
            mBitmapToBlur!!.recycle()
            mBitmapToBlur = null
        }
        if (mBlurredBitmap != null) {
            mBlurredBitmap!!.recycle()
            mBlurredBitmap = null
        }
    }

    protected fun release() {
        releaseBitmap()
        mBlurImpl.release()
    }

    protected fun prepare(): Boolean {
        if (mBlurRadius == 0f) {
            release()
            return false
        }
        var downsampleFactor = mDownsampleFactor
        var radius = mBlurRadius / downsampleFactor
        if (radius > 25) {
            downsampleFactor = downsampleFactor * radius / 25
            radius = 25f
        }
        val width = width
        val height = height
        val scaledWidth = Math.max(1, (width / downsampleFactor).toInt())
        val scaledHeight = Math.max(1, (height / downsampleFactor).toInt())
        var dirty = mDirty
        if (mBlurringCanvas == null || mBlurredBitmap == null || mBlurredBitmap!!.width != scaledWidth || mBlurredBitmap!!.height != scaledHeight) {
            dirty = true
            releaseBitmap()
            var r = false
            try {
                mBitmapToBlur = Bitmap.createBitmap(scaledWidth, scaledHeight, Bitmap.Config.ARGB_8888)

                if (mBitmapToBlur == null) {
                    return false
                }
                mBlurringCanvas = Canvas(mBitmapToBlur!!)

                mBlurredBitmap = Bitmap.createBitmap(scaledWidth, scaledHeight, Bitmap.Config.ARGB_8888)

                if (mBlurredBitmap == null) {
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
            mDirty = if (mBlurImpl.prepare(context, mBitmapToBlur, radius)) {
                false
            } else {
                return false
            }
        }
        return true
    }

    protected fun blur(bitmapToBlur: Bitmap?, blurredBitmap: Bitmap?) {
        mBlurImpl.blur(bitmapToBlur, blurredBitmap)
    }

    private val preDrawListener = ViewTreeObserver.OnPreDrawListener {
        val locations = IntArray(2)
        var oldBmp = mBlurredBitmap
        val decor = mDecorView
        if (decor != null && isShown && prepare()) {
            val redrawBitmap = mBlurredBitmap != oldBmp
            oldBmp = null
            decor.getLocationOnScreen(locations)
            var x = -locations[0]
            var y = -locations[1]
            getLocationOnScreen(locations)
            x += locations[0]
            y += locations[1]

            // just erase transparent
            mBitmapToBlur!!.eraseColor(firstOverlayColor and 0xffffff)
            mBitmapToBlur!!.eraseColor(secondOvarlayColr and 0xffffff)
            val rc = mBlurringCanvas!!.save()
            mIsRendering = true
            RENDERING_COUNT++
            try {
                mBlurringCanvas!!.scale(
                    1f * mBitmapToBlur!!.width / width,
                    1f * mBitmapToBlur!!.height / height
                )
                mBlurringCanvas!!.translate(-x.toFloat(), -y.toFloat())
                if (decor.background != null) {
                    decor.background.draw(mBlurringCanvas!!)
                }
                decor.draw(mBlurringCanvas)
            } catch (e: StopException) {
            } finally {
                mIsRendering = false
                RENDERING_COUNT--
                mBlurringCanvas!!.restoreToCount(rc)
            }
            blur(mBitmapToBlur, mBlurredBitmap)
            if (redrawBitmap || mDifferentRoot) {
                invalidate()
            }
        }
        true
    }
    protected val activityDecorView: View?
        protected get() {
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
        mDecorView = activityDecorView
        if (mDecorView != null) {
            mDecorView!!.viewTreeObserver.addOnPreDrawListener(preDrawListener)
            mDifferentRoot = mDecorView!!.rootView !== rootView
            if (mDifferentRoot) {
                mDecorView!!.postInvalidate()
            }
        } else {
            mDifferentRoot = false
        }
    }

    override fun onDetachedFromWindow() {
        if (mDecorView != null) {
            mDecorView!!.viewTreeObserver.removeOnPreDrawListener(preDrawListener)
        }
        release()
        super.onDetachedFromWindow()
    }

    override fun draw(canvas: Canvas) {
        if (mIsRendering) {
            // Quit here, don't draw views above me
            throw STOP_EXCEPTION
        } else if (RENDERING_COUNT > 0) {
            // Doesn't support blurview overlap on another blurview
        } else {
            super.draw(canvas)
        }
    }

    var clipPath = Path()
    var rectF = RectF()

    override fun onDraw(canvas: Canvas) {
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

        clipPath.addRoundRect(rectF, radii, Path.Direction.CW)

        canvas.clipPath(clipPath)

        super.onDraw(canvas)

        drawBlurredBitmap(
            canvas,
            mBlurredBitmap,
            firstOverlayColor,
            secondOvarlayColr,
            strokeWidth,
            strokeColor
        )

    }

    /**
     * Custom draw the blurred bitmap and color to define your own shape
     */
    private fun drawBlurredBitmap(
        canvas: Canvas,
        blurredBitmap: Bitmap?,
        mOverlayColor: Int,
        sOverlayColor: Int,
        strokeWidth: Int,
        strokeColor: Int
    ) {

        if (blurredBitmap != null) {

            // Blur
            mRectSrc.right = blurredBitmap.width
            mRectSrc.bottom = blurredBitmap.height
            mRectDst.right = width.toFloat()
            mRectDst.bottom = height.toFloat()

            canvas.drawBitmap(blurredBitmap, mRectSrc, mRectDst, null)

            // Stroke
            paintStrokeInstance.strokeJoin = Paint.Join.ROUND
            paintStrokeInstance.strokeWidth = strokeWidth.toFloat()
            paintStrokeInstance.color = strokeColor

            canvas.drawRect(mRectDst, paintStrokeInstance)

            // Gradient
            if (gradientType == GradientTypeNone) {

                paintInstance.color = mOverlayColor

            } else if (gradientType == GradientTypeLinearLR) {

                paintInstance.shader = LinearGradient(
                    0f, 0f,
                    width.toFloat(), 0f,  /* First Color */
                    sOverlayColor,  /* Second Color */mOverlayColor,
                    Shader.TileMode.CLAMP
                )

            } else if (gradientType == GradientTypeLinearTB) {

                paintInstance.shader = LinearGradient(
                    0f, 0f,
                    0f, height.toFloat(),  /* First Color */
                    sOverlayColor,  /* Second Color */mOverlayColor,
                    Shader.TileMode.CLAMP
                )

            } else if (gradientType == GradientTypeRadial) {

                paintInstance.shader = RadialGradient(
                    width / 2f,
                    height / 2f,
                    Math.min(width, height) / 2f,
                    sOverlayColor,
                    mOverlayColor,
                    Shader.TileMode.CLAMP
                )

            }

            canvas.drawRect(mRectDst, paintInstance)

        }

    }

    private class StopException : RuntimeException()

    init {
        mBlurImpl = blurImpl // provide your own by override getBlurImpl()
        val a = context.obtainStyledAttributes(attrs, R.styleable.RealtimeBlurView)
        mBlurRadius = a.getDimension(
            R.styleable.RealtimeBlurView_realtimeBlurRadius,
            TypedValue.applyDimension(
                TypedValue.COMPLEX_UNIT_DIP,
                10f,
                context.resources.displayMetrics
            )
        )
        mDownsampleFactor = a.getFloat(R.styleable.RealtimeBlurView_realtimeDownSampleFactor, 4f)
        firstOverlayColor = a.getColor(R.styleable.RealtimeBlurView_realtimeFirstColor, -0x55000001)
        secondOvarlayColr = a.getColor(R.styleable.RealtimeBlurView_realtimeSecondColor, -666)
        strokeWidth = a.getInteger(R.styleable.RealtimeBlurView_realtimeStrokeWidth, 0)
        strokeColor =
            a.getColor(R.styleable.RealtimeBlurView_realtimeStrokeColor, Color.TRANSPARENT)
        topLeftCorner = a.getDimension(R.styleable.RealtimeBlurView_realtimeBlurTopLeft, 0f)
        topRightCorner = a.getDimension(R.styleable.RealtimeBlurView_realtimeBlurTopRight, 0f)
        bottomLeftCorner = a.getDimension(R.styleable.RealtimeBlurView_realtimeBlurBottomLeft, 0f)
        bottomRightCorner = a.getDimension(R.styleable.RealtimeBlurView_realtimeBlurBottomRight, 0f)
        if (a.getInteger(R.styleable.RealtimeBlurView_realtimeBlurGradientType, 0) == 0) {
            gradientType = GradientTypeNone
        } else if (a.getInteger(R.styleable.RealtimeBlurView_realtimeBlurGradientType, 0) == 1) {
            gradientType = GradientTypeRadial
        } else if (a.getInteger(R.styleable.RealtimeBlurView_realtimeBlurGradientType, 0) == 2) {
            gradientType = GradientTypeLinearTB
        } else if (a.getInteger(R.styleable.RealtimeBlurView_realtimeBlurGradientType, 0) == 3) {
            gradientType = GradientTypeLinearLR
        }
        a.recycle()

        paintInstance = Paint(Paint.ANTI_ALIAS_FLAG)
        paintInstance.style = Paint.Style.FILL

        paintStrokeInstance = Paint()
        paintStrokeInstance.style = Paint.Style.STROKE

    }

    companion object {
        private var RENDERING_COUNT = 0
        private var BLUR_IMPL = 0
        var GradientTypeNone = 0
        var GradientTypeRadial = 1
        var GradientTypeLinearTB = 2
        var GradientTypeLinearLR = 3
        private val STOP_EXCEPTION = StopException()
    }
}