/*
 * Copyright © 2023 By Geeks Empire.
 *
 * Created by Elias Fazel
 * Last modified 1/19/23, 9:56 AM
 *
 * Licensed Under MIT License.
 * https://opensource.org/licenses/MIT
 */

package net.geeksempire.loadingspin.style;

import android.animation.ValueAnimator;

import net.geeksempire.loadingspin.animation.SpriteAnimatorBuilder;
import net.geeksempire.loadingspin.sprite.CircleLayoutContainer;
import net.geeksempire.loadingspin.sprite.CircleSprite;
import net.geeksempire.loadingspin.sprite.Sprite;

/**
 * Created by ybq.
 */
public class Circle extends CircleLayoutContainer {

    @Override
    public Sprite[] onCreateChild() {
        Dot[] dots = new Dot[23];

        for (int i = 0; i < dots.length; i++) {

            dots[i] = new Dot();

            dots[i].setAnimationDelay(1200 / 7 * i);

        }

        return dots;
    }

    private class Dot extends CircleSprite {

        Dot() {
            setScale(0f);
        }

        @Override
        public ValueAnimator onCreateAnimation() {
            float fractions[] = new float[]{0f, 0.5f, 1f};
            return new SpriteAnimatorBuilder(this).
                    scale(fractions, 0f, 1f, 0f).
                    duration(1200).
                    easeInOut(fractions)
                    .build();
        }
    }
}
