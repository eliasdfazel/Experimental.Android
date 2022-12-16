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

import android.view.View;

import androidx.recyclerview.widget.RecyclerView;

import java.lang.ref.WeakReference;

/**
 * @author alex yarovoi
 * @version 1.0
 */
public class FanChildDrawingOrderCallback implements RecyclerView.ChildDrawingOrderCallback {

    private WeakReference<RecyclerView.LayoutManager> mLayoutManagerWeakReference;

    public FanChildDrawingOrderCallback(RecyclerView.LayoutManager layoutManager) {
        mLayoutManagerWeakReference = new WeakReference<>(layoutManager);
    }

    @Override
    public int onGetChildDrawingOrder(int childCount, int i) {
        RecyclerView.LayoutManager layoutManager = mLayoutManagerWeakReference.get();
        if (layoutManager != null) {
            View startView = layoutManager.getChildAt(0);
            int position = layoutManager.getPosition(startView);

            boolean isStartFromBelow = true;

            if (position % 2 == 0) {
                isStartFromBelow = true;
            } else {
                isStartFromBelow = false;
            }

            int result = 0;
            if (isStartFromBelow) {
                if (i % 2 == 0) {
                    //front
                    result = i == 0 ? 0 : i - 1;
                } else {
                    //bellow
                    result = i + 1 >= childCount ? i : i + 1;
                }
            } else {
                if (i % 2 == 0) {
                    //front
                    result = i + 1 >= childCount ? i : i + 1;
                } else {
                    //bellow
                    result = i - 1;
                }
            }
//                Log.e("ORDER", "Result = " + result + "; CC = " + childCount + "; i = " + i + "; pos = " + position);

            return result;
        }
        return i;
    }
}
