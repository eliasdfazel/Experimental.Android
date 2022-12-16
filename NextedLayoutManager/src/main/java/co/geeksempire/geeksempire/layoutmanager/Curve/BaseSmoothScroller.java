/*
 * Copyright © 2022 By Geeks Empire.
 *
 * Created by Elias Fazel
 * Last modified 5/19/22, 8:27 AM
 *
 * Licensed Under MIT License.
 * https://opensource.org/licenses/MIT
 */

package co.geeksempire.geeksempire.layoutmanager.Curve;

import android.content.Context;
import android.graphics.PointF;

import androidx.recyclerview.widget.LinearSmoothScroller;
import androidx.recyclerview.widget.RecyclerView;

/**
 * @author alex yarovoi
 * @version 1.0
 */
abstract class BaseSmoothScroller extends LinearSmoothScroller {
    BaseSmoothScroller(Context context) {
        super(context);
    }

    @Override
    public PointF computeScrollVectorForPosition(int targetPosition) {
        RecyclerView.LayoutManager layoutManager = getLayoutManager();
        // calculate vector for position
        if (layoutManager != null && layoutManager instanceof CurveLayoutManager) {
            if (getChildCount() == 0) {
                return null;
            }
            final int firstChildPos = layoutManager.getPosition(layoutManager.getChildAt(0));
            final int direction = targetPosition < firstChildPos ? -1 : 1;
            return new PointF(direction, 0);
        }
        return new PointF();
    }
}
