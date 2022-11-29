/*
 * Copyright © 2022 By Geeks Empire.
 *
 * Created by Elias Fazel
 * Last modified 11/29/22, 5:19 AM
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

public class ShapesImage extends AppCompatImageView {
    public static final int CUSTOM = 0;

    private Paint mMaskedPaint;

    private Rect mBounds;
    private RectF mBoundsF;

    private Drawable mskDrawable;

    private boolean mCacheValid = false;

    private Bitmap mCacheBitmap;

    private int mCachedWidth;
    private int mCachedHeight;

    public ShapesImage(Context context) {
        this(context, null);
    }

    public ShapesImage(Context context, AttributeSet attributeSet) {
        super(context, attributeSet);

        // Attribute initialization
        final TypedArray typedArray = context.obtainStyledAttributes(attributeSet, R.styleable.ShapesImage);

        mskDrawable = typedArray.getDrawable(R.styleable.ShapesImage_shapeDrawable);
        if (mskDrawable != null) {
            mskDrawable.setCallback(this);
        }

        typedArray.recycle();
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
        if (mskDrawable != null) {
            mskDrawable.setBounds(mBounds);
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
            if (mskDrawable != null) {
                int sc = cacheCanvas.save();
                mskDrawable.draw(cacheCanvas);
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
        if (mskDrawable != null && mskDrawable.isStateful()) {
            mskDrawable.setState(getDrawableState());
        }
        if (isDuplicateParentStateEnabled()) {
            this.postInvalidateOnAnimation();
        }
    }

    @Override
    public void invalidateDrawable(Drawable who) {
        if (who == mskDrawable) {
            invalidate();
        } else {
            super.invalidateDrawable(who);
        }
    }

    @Override
    protected boolean verifyDrawable(Drawable who) {
        return who == mskDrawable || super.verifyDrawable(who);
    }

    /**
     * Sets the Drawable
     *
     * @param drawable Drawable object
     */
    public void setShapeDrawable(Drawable drawable) {
        this.mskDrawable = drawable;
        if (mskDrawable != null) {
            mskDrawable.setCallback(this);
        }
        setUpPaints();
    }

}