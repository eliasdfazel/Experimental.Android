/*
 * Copyright © 2022 By Geeks Empire.
 *
 * Created by Elias Fazel
 * Last modified 12/20/22, 9:38 AM
 *
 * Licensed Under MIT License.
 * https://opensource.org/licenses/MIT
 */

package net.geekstools.imageview.customshapes;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.graphics.Rect;
import android.graphics.RectF;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.widget.ImageView;

import androidx.appcompat.widget.AppCompatImageView;


/**
 * An {@link ImageView} that draws its contents inside a mask.
 */

public class ShapesMaskImage extends AppCompatImageView {

    private Paint mMaskedPaint;
    private Rect mBounds;
    private RectF mBoundsF;
    private Drawable mMaskDrawable;
    private boolean mCacheValid = false;
    private Bitmap mCacheBitmap;
    private int mCachedWidth;
    private int mCachedHeight;

    public ShapesMaskImage(Context context) {
        this(context, null);
    }

    public ShapesMaskImage(Context context, AttributeSet attrs) {
        super(context, attrs);

        // Attribute initialization
        final TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.ShapesImage);

        mMaskDrawable = a.getDrawable(R.styleable.ShapesImage_shapeDrawable);
        if (mMaskDrawable != null) {
            mMaskDrawable.setCallback(this);
        }

        a.recycle();
        setUpPaints();
    }

    private void setUpPaints() {
        Paint mBlackPaint = new Paint();
        mBlackPaint.setColor(0xff000000);
        mMaskedPaint = new Paint();
        mMaskedPaint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.SRC_IN));
        mCacheBitmap = Bitmap.createBitmap(1, 1, Bitmap.Config.ARGB_8888);
    }

    @Override
    protected boolean setFrame(int l, int t, int r, int b) {
        final boolean changed = super.setFrame(l, t, r, b);
        mBounds = new Rect(0, 0, r - l, b - t);
        mBoundsF = new RectF(mBounds);
        if (mMaskDrawable != null) {
            mMaskDrawable.setBounds(mBounds);
        }
        if (changed) {
            mCacheValid = false;
        }
        return changed;
    }

    @Override
    protected void onDraw(Canvas canvas) {
        if (mBounds == null) {
            return;
        }
        int width = mBounds.width();
        int height = mBounds.height();
        if (width == 0 || height == 0) {
            return;
        }

        if (!mCacheValid || width != mCachedWidth || height != mCachedHeight) {
            // Need to redraw the cache
            if (width == mCachedWidth && height == mCachedHeight) {
                // Have a correct-sized bitmap cache already allocated. Just erase it.
                mCacheBitmap.eraseColor(0);
            } else {
                // Allocate a new bitmap with the correct dimensions.
                mCacheBitmap.recycle();
                //noinspection AndroidLintDrawAllocation
                mCacheBitmap = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888);
                mCachedWidth = width;
                mCachedHeight = height;
            }

            Canvas cacheCanvas = new Canvas(mCacheBitmap);
            if (mMaskDrawable != null) {
                int sc = cacheCanvas.save();
                mMaskDrawable.draw(cacheCanvas);
                cacheCanvas.saveLayer(mBoundsF, mMaskedPaint,
                        Canvas.ALL_SAVE_FLAG);
                super.onDraw(cacheCanvas);
                cacheCanvas.restoreToCount(sc);
            } else {
                super.onDraw(cacheCanvas);
            }
        }
        // Draw from cache
        canvas.drawBitmap(mCacheBitmap, mBounds.left, mBounds.top, null);
    }

    @Override
    protected void drawableStateChanged() {
        super.drawableStateChanged();
        if (mMaskDrawable != null && mMaskDrawable.isStateful()) {
            mMaskDrawable.setState(getDrawableState());
        }
        if (isDuplicateParentStateEnabled()) {
            this.postInvalidateOnAnimation();
        }
    }

    @Override
    public void invalidateDrawable(Drawable who) {
        if (who == mMaskDrawable) {
            invalidate();
        } else {
            super.invalidateDrawable(who);
        }
    }

    @Override
    protected boolean verifyDrawable(Drawable who) {
        return who == mMaskDrawable || super.verifyDrawable(who);
    }

    /**
     * Sets the Drawable
     *
     * @param drawable Drawable object
     */
    public void setShapeDrawable(Drawable drawable) {
        this.mMaskDrawable = drawable;
        if (mMaskDrawable != null) {
            mMaskDrawable.setCallback(this);
        }
        setUpPaints();
    }

}