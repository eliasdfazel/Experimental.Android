/*
 * Copyright © 2023 By Geeks Empire.
 *
 * Created by Elias Fazel
 * Last modified 5/19/22, 8:27 AM
 *
 * Licensed Under MIT License.
 * https://opensource.org/licenses/MIT
 */

package net.geeksempire.loadingspin;

import net.geeksempire.loadingspin.sprite.Sprite;
import net.geeksempire.loadingspin.style.ChasingDots;
import net.geeksempire.loadingspin.style.Circle;
import net.geeksempire.loadingspin.style.CubeGrid;
import net.geeksempire.loadingspin.style.DoubleBounce;
import net.geeksempire.loadingspin.style.FadingCircle;
import net.geeksempire.loadingspin.style.FoldingCube;
import net.geeksempire.loadingspin.style.MultiplePulse;
import net.geeksempire.loadingspin.style.MultiplePulseRing;
import net.geeksempire.loadingspin.style.Pulse;
import net.geeksempire.loadingspin.style.PulseRing;
import net.geeksempire.loadingspin.style.RotatingCircle;
import net.geeksempire.loadingspin.style.RotatingPlane;
import net.geeksempire.loadingspin.style.ThreeBounce;
import net.geeksempire.loadingspin.style.WanderingCubes;
import net.geeksempire.loadingspin.style.Wave;

/**
 * Created by ybq.
 */
public class SpriteFactory {

    public static Sprite create(Style style) {
        Sprite sprite = null;
        switch (style) {
            case ROTATING_PLANE:
                sprite = new RotatingPlane();
                break;
            case DOUBLE_BOUNCE:
                sprite = new DoubleBounce();
                break;
            case WAVE:
                sprite = new Wave();
                break;
            case WANDERING_CUBES:
                sprite = new WanderingCubes();
                break;
            case PULSE:
                sprite = new Pulse();
                break;
            case CHASING_DOTS:
                sprite = new ChasingDots();
                break;
            case THREE_BOUNCE:
                sprite = new ThreeBounce();
                break;
            case CIRCLE:
                sprite = new Circle();
                break;
            case CUBE_GRID:
                sprite = new CubeGrid();
                break;
            case FADING_CIRCLE:
                sprite = new FadingCircle();
                break;
            case FOLDING_CUBE:
                sprite = new FoldingCube();
                break;
            case ROTATING_CIRCLE:
                sprite = new RotatingCircle();
                break;
            case MULTIPLE_PULSE:
                sprite = new MultiplePulse();
                break;
            case PULSE_RING:
                sprite = new PulseRing();
                break;
            case MULTIPLE_PULSE_RING:
                sprite = new MultiplePulseRing();
                break;
            default:
                break;
        }
        return sprite;
    }
}
