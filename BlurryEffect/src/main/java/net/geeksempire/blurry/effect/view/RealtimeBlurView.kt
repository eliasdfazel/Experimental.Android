/*
 * Copyright © 2022 By Geeks Empire.
 *
 * Created by Elias Fazel
 * Last modified 11/11/22, 4:46 AM
 *
 * Licensed Under MIT License.
 * https://opensource.org/licenses/MIT
 */

package net.geeksempire.blurry.effect.view;

import android.app.Activity;
import android.content.Context;
import android.content.ContextWrapper;
import android.content.res.TypedArray;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.LinearGradient;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.RadialGradient;
import android.graphics.Rect;
import android.graphics.RectF;
import android.graphics.Shader;
import android.os.Build;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.View;
import android.view.ViewTreeObserver;

/**
 * A realtime blurring overlay (like iOS UIVisualEffectView). Just put it above
 * the view you want to blur and it doesn't have to be in the same ViewGroup
 * <ul>
 * <li>realtimeBlurRadius (10dp)</li>
 * <li>realtimeDownsampleFactor (4)</li>
 * <li>realtimeOverlayColor (#aaffffff)</li>
 * </ul>
 */
public class RealtimeBlurView extends View {

	private float mDownsampleFactor; // default 4

	private int firstOverlayColor; // default #aaffffff
	private int secondOvarlayColr; // default -666

	private int strokeWidth = 0;
	private int strokeColor = Color.TRANSPARENT;

	private float mBlurRadius; // default 10dp (0 < r <= 25)

	private final BlurImpl mBlurImpl;
	private boolean mDirty;
	private Bitmap mBitmapToBlur, mBlurredBitmap;
	private Canvas mBlurringCanvas;
	private boolean mIsRendering;
	private Paint paintInstance, paintStrokeInstance;
	private final Rect mRectSrc = new Rect();
	private final RectF mRectDst = new RectF();

	private Float topLeftCorner = 0f;
	private Float topRightCorner = 0f;
	private Float bottomLeftCorner = 0f;
	private Float bottomRightCorner = 0f;

	// mDecorView should be the root view of the activity (even if you are on a different window like a dialog)
	private View mDecorView;
	// If the view is on different root view (usually means we are on a PopupWindow),
	// we need to manually call invalidate() in onPreDraw(), otherwise we will not be able to see the changes
	private boolean mDifferentRoot;
	private static int RENDERING_COUNT;
	private static int BLUR_IMPL;

	int gradientType = 0;

	public static int GradientTypeNone = 0;
	public static int GradientTypeRadial = 1;

	public static int GradientTypeLinearTB = 2;
	public static int GradientTypeLinearLR = 3;


	public RealtimeBlurView(Context context, AttributeSet attrs) {
		super(context, attrs);

		mBlurImpl = getBlurImpl(); // provide your own by override getBlurImpl()

		TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.RealtimeBlurView);

		mBlurRadius = a.getDimension(R.styleable.RealtimeBlurView_realtimeBlurRadius,
				TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 10, context.getResources().getDisplayMetrics()));
		mDownsampleFactor = a.getFloat(R.styleable.RealtimeBlurView_realtimeDownSampleFactor, 4);

		firstOverlayColor = a.getColor(R.styleable.RealtimeBlurView_realtimeFirstColor, 0xAAFFFFFF);
		secondOvarlayColr = a.getColor(R.styleable.RealtimeBlurView_realtimeSecondColor, -666);

		strokeWidth = a.getInteger(R.styleable.RealtimeBlurView_realtimeStrokeWidth, 0);
		strokeColor = a.getColor(R.styleable.RealtimeBlurView_realtimeStrokeColor, Color.TRANSPARENT);

		topLeftCorner = a.getDimension(R.styleable.RealtimeBlurView_realtimeBlurTopLeft, 0f);
		topRightCorner = a.getDimension(R.styleable.RealtimeBlurView_realtimeBlurTopRight, 0f);
		bottomLeftCorner = a.getDimension(R.styleable.RealtimeBlurView_realtimeBlurBottomLeft, 0f);
		bottomRightCorner = a.getDimension(R.styleable.RealtimeBlurView_realtimeBlurBottomRight, 0f);

		if (a.getInteger(R.styleable.RealtimeBlurView_realtimeBlurGradientType, 0) == 0) {
			gradientType = GradientTypeNone;
		} else if (a.getInteger(R.styleable.RealtimeBlurView_realtimeBlurGradientType, 0) == 1) {
			gradientType = GradientTypeRadial;
		} else if (a.getInteger(R.styleable.RealtimeBlurView_realtimeBlurGradientType, 0) == 2) {
			gradientType = GradientTypeLinearTB;
		} else if (a.getInteger(R.styleable.RealtimeBlurView_realtimeBlurGradientType, 0) == 3) {
			gradientType = GradientTypeLinearLR;
		}

		a.recycle();

		paintInstance = new Paint();
		paintInstance.setStyle(Paint.Style.FILL);

		paintStrokeInstance = new Paint();
		paintStrokeInstance.setStyle(Paint.Style.STROKE);

	}

	protected BlurImpl getBlurImpl() {
		if (BLUR_IMPL == 0) {
			// try to use stock impl first
			if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1) {
				try {
					AndroidStockBlurImpl impl = new AndroidStockBlurImpl();
					Bitmap bmp = Bitmap.createBitmap(4, 4, Bitmap.Config.ARGB_8888);
					impl.prepare(getContext(), bmp, 4);
					impl.release();
					bmp.recycle();
					BLUR_IMPL = 3;
				} catch (Throwable e) {
				}
			}
		}
		if (BLUR_IMPL == 0) {
			try {
				getClass().getClassLoader().loadClass("androidx.renderscript.RenderScript");
				// initialize RenderScript to load jni impl
				// may throw unsatisfied link error
				AndroidXBlurImpl impl = new AndroidXBlurImpl();
				Bitmap bmp = Bitmap.createBitmap(4, 4, Bitmap.Config.ARGB_8888);
				impl.prepare(getContext(), bmp, 4);
				impl.release();
				bmp.recycle();
				BLUR_IMPL = 1;
			} catch (Throwable e) {
				// class not found or unsatisfied link
			}
		}
		if (BLUR_IMPL == 0) {
			try {
				getClass().getClassLoader().loadClass("android.support.v8.renderscript.RenderScript");
				// initialize RenderScript to load jni impl
				// may throw unsatisfied link error
				SupportLibraryBlurImpl impl = new SupportLibraryBlurImpl();
				Bitmap bmp = Bitmap.createBitmap(4, 4, Bitmap.Config.ARGB_8888);
				impl.prepare(getContext(), bmp, 4);
				impl.release();
				bmp.recycle();
				BLUR_IMPL = 2;
			} catch (Throwable e) {
				// class not found or unsatisfied link
			}
		}
		if (BLUR_IMPL == 0) {
			// fallback to empty impl, which doesn't have blur effect
			BLUR_IMPL = -1;
		}
		switch (BLUR_IMPL) {
			case 1:
				return new AndroidXBlurImpl();
			case 2:
				return new SupportLibraryBlurImpl();
			case 3:
				return new AndroidStockBlurImpl();
			default:
				return new EmptyBlurImpl();
		}
	}

	public void setBlurRadius(float radius) {
		if (mBlurRadius != radius) {
			mBlurRadius = radius;
			mDirty = true;
			invalidate();
		}
	}

	public void setDownsampleFactor(float factor) {
		if (factor <= 0) {
			throw new IllegalArgumentException("Downsample factor must be greater than 0.");
		}

		if (mDownsampleFactor != factor) {
			mDownsampleFactor = factor;
			mDirty = true; // may also change blur radius
			releaseBitmap();
			invalidate();
		}
	}

	public void setOverlayColor(int color) {
		if (firstOverlayColor != color) {
			firstOverlayColor = color;
			invalidate();
		}
	}

	public void setSecondOverlayColor(int color) {
		if (secondOvarlayColr != color) {
			secondOvarlayColr = color;
			invalidate();
		}
	}

	public void setStrokeWidth(int initialStrokeWidth) {
		if (strokeWidth != initialStrokeWidth) {
			strokeWidth = initialStrokeWidth;
			invalidate();
		}
	}

	public void setStrokeColor(int initialStrokeColor) {
		if (strokeColor != initialStrokeColor) {
			strokeColor = initialStrokeColor;
			invalidate();
		}
	}

	private void releaseBitmap() {
		if (mBitmapToBlur != null) {
			mBitmapToBlur.recycle();
			mBitmapToBlur = null;
		}
		if (mBlurredBitmap != null) {
			mBlurredBitmap.recycle();
			mBlurredBitmap = null;
		}
	}

	protected void release() {
		releaseBitmap();
		mBlurImpl.release();
	}

	protected boolean prepare() {
		if (mBlurRadius == 0) {
			release();
			return false;
		}

		float downsampleFactor = mDownsampleFactor;
		float radius = mBlurRadius / downsampleFactor;
		if (radius > 25) {
			downsampleFactor = downsampleFactor * radius / 25;
			radius = 25;
		}

		final int width = getWidth();
		final int height = getHeight();

		int scaledWidth = Math.max(1, (int) (width / downsampleFactor));
		int scaledHeight = Math.max(1, (int) (height / downsampleFactor));

		boolean dirty = mDirty;

		if (mBlurringCanvas == null || mBlurredBitmap == null
				|| mBlurredBitmap.getWidth() != scaledWidth
				|| mBlurredBitmap.getHeight() != scaledHeight) {
			dirty = true;
			releaseBitmap();

			boolean r = false;
			try {
				mBitmapToBlur = Bitmap.createBitmap(scaledWidth, scaledHeight, Bitmap.Config.ARGB_8888);
				if (mBitmapToBlur == null) {
					return false;
				}
				mBlurringCanvas = new Canvas(mBitmapToBlur);

				mBlurredBitmap = Bitmap.createBitmap(scaledWidth, scaledHeight, Bitmap.Config.ARGB_8888);
				if (mBlurredBitmap == null) {
					return false;
				}

				r = true;
			} catch (OutOfMemoryError e) {
				// Bitmap.createBitmap() may cause OOM error
				// Simply ignore and fallback
			} finally {
				if (!r) {
					release();
					return false;
				}
			}
		}

		if (dirty) {
			if (mBlurImpl.prepare(getContext(), mBitmapToBlur, radius)) {
				mDirty = false;
			} else {
				return false;
			}
		}

		return true;
	}

	protected void blur(Bitmap bitmapToBlur, Bitmap blurredBitmap) {
		mBlurImpl.blur(bitmapToBlur, blurredBitmap);
	}

	private final ViewTreeObserver.OnPreDrawListener preDrawListener = new ViewTreeObserver.OnPreDrawListener() {

		@Override
		public boolean onPreDraw() {

			final int[] locations = new int[2];

			Bitmap oldBmp = mBlurredBitmap;
			View decor = mDecorView;

			if (decor != null && isShown() && prepare()) {
				boolean redrawBitmap = mBlurredBitmap != oldBmp;
				oldBmp = null;
				decor.getLocationOnScreen(locations);
				int x = -locations[0];
				int y = -locations[1];

				getLocationOnScreen(locations);
				x += locations[0];
				y += locations[1];

				// just erase transparent
				mBitmapToBlur.eraseColor(firstOverlayColor & 0xffffff);
				mBitmapToBlur.eraseColor(secondOvarlayColr & 0xffffff);

				int rc = mBlurringCanvas.save();
				mIsRendering = true;
				RENDERING_COUNT++;
				try {
					mBlurringCanvas.scale(1.f * mBitmapToBlur.getWidth() / getWidth(), 1.f * mBitmapToBlur.getHeight() / getHeight());
					mBlurringCanvas.translate(-x, -y);
					if (decor.getBackground() != null) {
						decor.getBackground().draw(mBlurringCanvas);
					}
					decor.draw(mBlurringCanvas);
				} catch (StopException e) {
				} finally {
					mIsRendering = false;
					RENDERING_COUNT--;
					mBlurringCanvas.restoreToCount(rc);
				}

				blur(mBitmapToBlur, mBlurredBitmap);

				if (redrawBitmap || mDifferentRoot) {
					invalidate();
				}
			}

			return true;
		}
	};

	protected View getActivityDecorView() {
		Context ctx = getContext();
		for (int i = 0; i < 4 && ctx != null && !(ctx instanceof Activity) && ctx instanceof ContextWrapper; i++) {
			ctx = ((ContextWrapper) ctx).getBaseContext();
		}
		if (ctx instanceof Activity) {
			return ((Activity) ctx).getWindow().getDecorView();
		} else {
			return null;
		}
	}

	@Override
	protected void onAttachedToWindow() {
		super.onAttachedToWindow();
		mDecorView = getActivityDecorView();
		if (mDecorView != null) {
			mDecorView.getViewTreeObserver().addOnPreDrawListener(preDrawListener);
			mDifferentRoot = mDecorView.getRootView() != getRootView();
			if (mDifferentRoot) {
				mDecorView.postInvalidate();
			}
		} else {
			mDifferentRoot = false;
		}
	}

	@Override
	protected void onDetachedFromWindow() {
		if (mDecorView != null) {
			mDecorView.getViewTreeObserver().removeOnPreDrawListener(preDrawListener);
		}
		release();
		super.onDetachedFromWindow();
	}

	@Override
	public void draw(Canvas canvas) {
		if (mIsRendering) {
			// Quit here, don't draw views above me
			throw STOP_EXCEPTION;
		} else if (RENDERING_COUNT > 0) {
			// Doesn't support blurview overlap on another blurview
		} else {
			super.draw(canvas);
		}
	}

	Path clipPath = new Path();
	RectF rectF = new RectF();

	@Override
	protected void onDraw(Canvas canvas) {

		rectF.set(0,0, this.getWidth(), this.getHeight());

		float[] radii = {0, 0, 0, 0, 0, 0, 0, 0};

		radii[0] = topLeftCorner;
		radii[1] = topLeftCorner;

		radii[2] = topRightCorner;
		radii[3] = topRightCorner;

		radii[4] = bottomRightCorner;
		radii[5] = bottomRightCorner;

		radii[6] = bottomLeftCorner;
		radii[7] = bottomLeftCorner;

		clipPath.addRoundRect(rectF, radii, Path.Direction.CW);
		canvas.clipPath(clipPath);

		super.onDraw(canvas);

		drawBlurredBitmap(canvas, mBlurredBitmap, firstOverlayColor, secondOvarlayColr, strokeWidth, strokeColor);

	}

	/**
	 * Custom draw the blurred bitmap and color to define your own shape
	 **/
	protected void drawBlurredBitmap(Canvas canvas, Bitmap blurredBitmap, int mOverlayColor, int sOverlayColor, int strokeWidth, int strokeColor) {

		if (blurredBitmap != null) {

			mRectSrc.right = blurredBitmap.getWidth();
			mRectSrc.bottom = blurredBitmap.getHeight();

			mRectDst.right = getWidth();
			mRectDst.bottom = getHeight();

			canvas.drawBitmap(blurredBitmap, mRectSrc, mRectDst, null);

		}

		paintStrokeInstance.setStrokeJoin(Paint.Join.ROUND);
		paintStrokeInstance.setStrokeWidth(strokeWidth);
		paintStrokeInstance.setColor(strokeColor);

		if (gradientType == GradientTypeNone) {

			paintInstance.setColor(mOverlayColor);

		} else if (gradientType == GradientTypeLinearLR) {

			paintInstance.setShader(new LinearGradient(
					0, 0,
					getWidth(), 0,
					/* First Color */ sOverlayColor, /* Second Color */ mOverlayColor,
					Shader.TileMode.CLAMP));

		} else if (gradientType == GradientTypeLinearTB) {

			paintInstance.setShader(new LinearGradient(
					0, 0,
					0, getHeight(),
					/* First Color */ sOverlayColor, /* Second Color */ mOverlayColor,
					Shader.TileMode.CLAMP));

		} else if (gradientType == GradientTypeRadial) {

			paintInstance.setShader(new RadialGradient(getWidth() / 2f,
					getHeight() / 2f,
					Math.min(getWidth(), getHeight()) / 2f,
					sOverlayColor,
					mOverlayColor,
					Shader.TileMode.CLAMP));

		}

		canvas.drawRect(mRectDst, paintInstance);
		canvas.drawRect(mRectDst, paintStrokeInstance);

	}

	private static class StopException extends RuntimeException {
	}

	private static StopException STOP_EXCEPTION = new StopException();
}
