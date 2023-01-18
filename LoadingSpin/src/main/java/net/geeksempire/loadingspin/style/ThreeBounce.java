/*
 * Copyright © 2023 By Geeks Empire.
 *
 * Created by Elias Fazel
 * Last modified 5/19/22, 8:27 AM
 *
 * Licensed Under MIT License.
 * https://opensource.org/licenses/MIT
 */

package net.geeksempire.loadingspin.style;

import android.animation.ValueAnimator;
import android.graphics.Rect;

import net.geeksempire.loadingspin.animation.SpriteAnimatorBuilder;
import net.geeksempire.loadingspin.sprite.CircleSprite;
import net.geeksempire.loadingspin.sprite.Sprite;
import net.geeksempire.loadingspin.sprite.SpriteContainer;

/**
 * Created by ybq.
 */
public class ThreeBounce extends SpriteContainer {

    @Override
    public Sprite[] onCreateChild() {
        return new Sprite[]{
                new Bounce(),
                new Bounce(),
                new Bounce()
        };
    }

    @Override
    public void onChildCreated(Sprite... sprites) {
        super.onChildCreated(sprites);
        sprites[1].setAnimationDelay(160);
        sprites[2].setAnimationDelay(320);
    }

    @Override
    protected void onBoundsChange(Rect bounds) {
        super.onBoundsChange(bounds);
        bounds = clipSquare(bounds);
        int radius = bounds.width() / 8;
        int top = bounds.centerY() - radius;
        int bottom = bounds.centerY() + radius;

        for (int i = 0; i < getChildCount(); i++) {
            int left = bounds.width() * i / 3
                    + bounds.left;
            getChildAt(i).setDrawBounds(
                    left, top, left + radius * 2, bottom
            );
        }
    }

    private class Bounce extends CircleSprite {

        Bounce() {
            setScale(0f);
        }

        @Override
        public ValueAnimator onCreateAnimation() {
            float fractions[] = new float[]{0f, 0.4f, 0.8f, 1f};
            return new SpriteAnimatorBuilder(this).scale(fractions, 0f, 1f, 0f, 0f).
                    duration(1400).
                    easeInOut(fractions)
                    .build();
        }
    }
}
