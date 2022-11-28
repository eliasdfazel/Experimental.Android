/*
 * Copyright © 2022 By Geeks Empire.
 *
 * Created by Elias Fazel
 * Last modified 11/28/22, 3:52 AM
 *
 * Licensed Under MIT License.
 * https://opensource.org/licenses/MIT
 */

package co.geeksempire.blurry.effect.library;

import android.app.Activity;
import android.content.Context;
import android.content.ContextWrapper;
import android.content.res.TypedArray;
import android.graphics.Bitmap;
import android.graphics.Canvas;
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

import net.geeksempire.blurry.effect.view.R;

import co.geeksempire.blurry.effect.blurImplementation.AndroidXBlurImpl;
import co.geeksempire.blurry.effect.blurImplementation.SupportLibraryBlurImpl;

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

	private float downSampleFactor; // default 4

	private int firstColor; // default #aaffffff
	private int secondColor; // default -666

	/**
	 * Default 10dp (0 < radius <= 25)
	 **/
	private float blurRadius; // default 10dp (0 < r <= 25)

	private Float topLeftCorner = 19f;
	private Float topRightCorner = 19f;
	private Float bottomLeftCorner = 19f;
	private Float bottomRightCorner = 19f;

	int gradientType = 0;

	public static int GradientTypeNone = 0;
	public static int GradientTypeRadial = 1;

	public static int GradientTypeLinearTB = 2;
	public static int GradientTypeLinearLR = 3;
	public static int GradientTypeLinearTilt = 4;

	Path clipPath = new Path();
	RectF rectF = new RectF();

	private final BlurImpl mBlurImpl;

	private boolean mDirty;
	private Bitmap mBitmapToBlur, blurredBitmap;
	private Canvas mBlurringCanvas;
	private boolean mIsRendering;
	private Paint paintInstance;
	private final Rect mRectSrc = new Rect(), mRectDst = new Rect();
	// mDecorView should be the root view of the activity (even if you are on a different window like a dialog)
	private View mDecorView;
	// If the view is on different root view (usually means we are on a PopupWindow),
	// we need to manually call invalidate() in onPreDraw(), otherwise we will not be able to see the changes
	private boolean mDifferentRoot;
	private static int RENDERING_COUNT;
	private static int BLUR_IMPL;

	public RealtimeBlurView(Context context, AttributeSet attributeSet) {
		super(context, attributeSet);

		mBlurImpl = getBlurImpl(); // provide your own by override getBlurImpl()

		TypedArray typedArray = context.obtainStyledAttributes(attributeSet, R.styleable.RealtimeBlurView);

		blurRadius = typedArray.getDimension(R.styleable.RealtimeBlurView_realtimeBlurRadius, TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 10, context.getResources().getDisplayMetrics()));

		downSampleFactor = typedArray.getFloat(R.styleable.RealtimeBlurView_realtimeDownSampleFactor, 4);

		firstColor = typedArray.getColor(R.styleable.RealtimeBlurView_realtimeFirstColor, 0xAAFFFFFF);
		secondColor = typedArray.getColor(R.styleable.RealtimeBlurView_realtimeSecondColor, -666);

		topLeftCorner = typedArray.getDimension(R.styleable.RealtimeBlurView_realtimeBlurTopLeft, 19f);
		topRightCorner = typedArray.getDimension(R.styleable.RealtimeBlurView_realtimeBlurTopRight, 19f);
		bottomLeftCorner = typedArray.getDimension(R.styleable.RealtimeBlurView_realtimeBlurBottomLeft, 19f);
		bottomRightCorner = typedArray.getDimension(R.styleable.RealtimeBlurView_realtimeBlurBottomRight, 19f);

		if (typedArray.getInteger(R.styleable.RealtimeBlurView_realtimeBlurGradientType, 0) == 0) {
			gradientType = GradientTypeNone;
		} else if (typedArray.getInteger(R.styleable.RealtimeBlurView_realtimeBlurGradientType, 0) == 1) {
			gradientType = GradientTypeRadial;
		} else if (typedArray.getInteger(R.styleable.RealtimeBlurView_realtimeBlurGradientType, 0) == 2) {
			gradientType = GradientTypeLinearTB;
		} else if (typedArray.getInteger(R.styleable.RealtimeBlurView_realtimeBlurGradientType, 0) == 3) {
			gradientType = GradientTypeLinearLR;
		} else if (typedArray.getInteger(R.styleable.RealtimeBlurView_realtimeBlurGradientType, 0) == 4) {
			gradientType = GradientTypeLinearTilt;
		}

		typedArray.recycle();

		paintInstance = new Paint();
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
		if (blurRadius != radius) {
			blurRadius = radius;
			mDirty = true;
			invalidate();
		}
	}

	public void setDownSampleFactor(float factor) {
		if (factor <= 0) {
			throw new IllegalArgumentException("Downsample factor must be greater than 0.");
		}

		if (downSampleFactor != factor) {
			downSampleFactor = factor;
			mDirty = true; // may also change blur radius
			releaseBitmap();
			invalidate();
		}
	}

	public void setFirstColor(int color) {
		if (firstColor != color) {
			firstColor = color;
			invalidate();
		}
	}

	public void setSecondColor(int color) {
		if (firstColor != color) {
			secondColor = color;
			invalidate();
		}
	}

	private void releaseBitmap() {
		if (mBitmapToBlur != null) {
			mBitmapToBlur.recycle();
			mBitmapToBlur = null;
		}
		if (blurredBitmap != null) {
			blurredBitmap.recycle();
			blurredBitmap = null;
		}
	}

	protected void release() {
		releaseBitmap();
		mBlurImpl.release();
	}

	protected boolean prepare() {
		if (blurRadius == 0) {
			release();
			return false;
		}

		float downsampleFactor = downSampleFactor;
		float radius = blurRadius / downsampleFactor;
		if (radius > 25) {
			downsampleFactor = downsampleFactor * radius / 25;
			radius = 25;
		}

		final int width = getWidth();
		final int height = getHeight();

		int scaledWidth = Math.max(1, (int) (width / downsampleFactor));
		int scaledHeight = Math.max(1, (int) (height / downsampleFactor));

		boolean dirty = mDirty;

		if (mBlurringCanvas == null || blurredBitmap == null
				|| blurredBitmap.getWidth() != scaledWidth
				|| blurredBitmap.getHeight() != scaledHeight) {
			dirty = true;
			releaseBitmap();

			boolean r = false;
			try {
				mBitmapToBlur = Bitmap.createBitmap(scaledWidth, scaledHeight, Bitmap.Config.ARGB_8888);
				if (mBitmapToBlur == null) {
					return false;
				}
				mBlurringCanvas = new Canvas(mBitmapToBlur);

				blurredBitmap = Bitmap.createBitmap(scaledWidth, scaledHeight, Bitmap.Config.ARGB_8888);
				if (blurredBitmap == null) {
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
			Bitmap oldBmp = blurredBitmap;
			View decor = mDecorView;
			if (decor != null && isShown() && prepare()) {
				boolean redrawBitmap = blurredBitmap != oldBmp;
				oldBmp = null;
				decor.getLocationOnScreen(locations);
				int x = -locations[0];
				int y = -locations[1];

				getLocationOnScreen(locations);
				x += locations[0];
				y += locations[1];

				// just erase transparent
				mBitmapToBlur.eraseColor(firstColor & 0xffffff);

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

				blur(mBitmapToBlur, blurredBitmap);

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

//		clipPath.addRoundRect(rectF, radii, Path.Direction.CW);

		clipPath.moveTo(0,0);
		clipPath.lineTo(0, getHeight()/2);
		clipPath.lineTo(getWidth(), getHeight()/4);
		clipPath.lineTo(getWidth(), 0);

		canvas.clipPath(clipPath);

		super.onDraw(canvas);

		drawBlurredBitmap(canvas, blurredBitmap, firstColor, secondColor);
	}

	/**
	 * Custom draw the blurred bitmap and color to define your own shape
	 *
	 * @param canvas
	 * @param blurredBitmap
	 * @param firstColor
	 */
	protected void drawBlurredBitmap(Canvas canvas, Bitmap blurredBitmap, int firstColor, int secondColor) {

		if (blurredBitmap != null) {
			mRectSrc.right = blurredBitmap.getWidth();
			mRectSrc.bottom = blurredBitmap.getHeight();
			mRectDst.right = getWidth();
			mRectDst.bottom = getHeight();

			canvas.drawBitmap(blurredBitmap, mRectSrc, mRectDst, null);
		}

		if (gradientType == GradientTypeNone) {

			paintInstance.setColor(firstColor);

		} else if (gradientType == GradientTypeLinearLR) {

			paintInstance.setShader(new LinearGradient(
					0, 0,
					getWidth(), 0,
					/* First Color */ firstColor,
					/* Second Color */ secondColor,
					Shader.TileMode.CLAMP));

		} else if (gradientType == GradientTypeLinearTB) {

			paintInstance.setShader(new LinearGradient(
					0, 0,
					0, getHeight(),
					/* First Color */ firstColor,
					/* Second Color */ secondColor,
					Shader.TileMode.CLAMP));

		} else if (gradientType == GradientTypeLinearTilt) {

			paintInstance.setShader(new LinearGradient(
					getWidth(), 0,
					0, getHeight(),
					/* First Color */ firstColor,
					/* Second Color */ secondColor,
					Shader.TileMode.CLAMP));

		} else if (gradientType == GradientTypeRadial) {

			paintInstance.setShader(new RadialGradient(getWidth() / 2f,
					getHeight() / 2f,
					Math.min(getWidth(), getHeight()) / 2f,
					firstColor,
					secondColor,
					Shader.TileMode.CLAMP));

		}


		canvas.drawRect(mRectDst, paintInstance);

	}

	private static class StopException extends RuntimeException {
	}

	private static StopException STOP_EXCEPTION = new StopException();
}
