package com.oguzdev.circularfloatingactionmenu.library.subActionButtonAnimation;

import android.animation.ObjectAnimator;
import android.animation.PropertyValuesHolder;
import android.animation.ValueAnimator;
import android.view.View;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.view.animation.OvershootInterpolator;

import com.oguzdev.circularfloatingactionmenu.library.SubActionButton;

/**
 * Created by darrenpoon on 12/2/15.
 */
public class DefaultSABAnimationHandler extends SubActionButtonAnimationHandler {

    private static final int DURATION = 100;
    private boolean animating;

    public DefaultSABAnimationHandler() {
        setAnimating(false);
    }

    @Override
    public void animateOnTouchSubActionButton(SubActionButton button) {
        setAnimating(true);
        PropertyValuesHolder pvhsX = PropertyValuesHolder.ofFloat(View.SCALE_X, 1.25f);
        PropertyValuesHolder pvhsY = PropertyValuesHolder.ofFloat(View.SCALE_Y, 1.25f);

        final ObjectAnimator bounce = ObjectAnimator.ofPropertyValuesHolder(button, pvhsX, pvhsY)
                .setDuration(DURATION);
        bounce.setRepeatCount(1);
        bounce.setRepeatMode(ValueAnimator.REVERSE);
        bounce.setInterpolator(new AccelerateDecelerateInterpolator());
        bounce.start();

        bounce.addListener(new LastAnimationListener());
    }

    @Override
    public boolean isAnimating() {
        return animating;
    }

    @Override
    public void setAnimating(boolean animating) {
        this.animating = animating;
    }
}
