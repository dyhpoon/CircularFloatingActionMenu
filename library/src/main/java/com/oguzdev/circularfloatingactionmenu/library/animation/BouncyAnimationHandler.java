package com.oguzdev.circularfloatingactionmenu.library.animation;

import android.animation.Animator;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.animation.PropertyValuesHolder;
import android.graphics.Point;
import android.view.View;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.view.animation.AnimationSet;
import android.view.animation.DecelerateInterpolator;
import android.view.animation.Interpolator;
import android.view.animation.OvershootInterpolator;

import com.oguzdev.circularfloatingactionmenu.library.FloatingActionMenu;

/**
 * Created by darrenpoon on 12/2/15.
 */
public class BouncyAnimationHandler extends MenuAnimationHandler {
    /** duration of animations, in milliseconds */
    protected static final int DURATION = 200;
    /** duration to wait between each of  */
    protected static final int LAG_BETWEEN_ITEMS = 80;
    /** holds the current state of animation */
    private boolean animating;

    public BouncyAnimationHandler() {
        setAnimating(false);
    }

    @Override
    public void animateMenuOpening(Point center) {
        super.animateMenuOpening(center);

        final Interpolator overshootInterpolator = new OvershootInterpolator();
        final Interpolator decelerateInterpolator = new DecelerateInterpolator();

        setAnimating(true);

        Animator lastAnimation = null;
        for (int i = 0; i < menu.getSubActionItems().size(); i++) {

            menu.getSubActionItems().get(i).view.setScaleX(0);
            menu.getSubActionItems().get(i).view.setScaleY(0);
            menu.getSubActionItems().get(i).view.setAlpha(0);

            PropertyValuesHolder pvhX = PropertyValuesHolder.ofFloat(View.TRANSLATION_X, menu.getSubActionItems().get(i).x - center.x + menu.getSubActionItems().get(i).width / 2);
            PropertyValuesHolder pvhY = PropertyValuesHolder.ofFloat(View.TRANSLATION_Y, menu.getSubActionItems().get(i).y - center.y + menu.getSubActionItems().get(i).height / 2);
            PropertyValuesHolder pvhsX = PropertyValuesHolder.ofFloat(View.SCALE_X, 1);
            PropertyValuesHolder pvhsY = PropertyValuesHolder.ofFloat(View.SCALE_Y, 1);
            PropertyValuesHolder pvhA = PropertyValuesHolder.ofFloat(View.ALPHA, 1);

            final ObjectAnimator overShootAnimation = ObjectAnimator.ofPropertyValuesHolder(menu.getSubActionItems().get(i).view, pvhX, pvhY);
            final ObjectAnimator decelerateAnimation = ObjectAnimator.ofPropertyValuesHolder(menu.getSubActionItems().get(i).view, pvhsX, pvhsY, pvhA);
            overShootAnimation.setDuration(DURATION);
            decelerateAnimation.setDuration(DURATION);
            overShootAnimation.setInterpolator(overshootInterpolator);
            decelerateAnimation.setInterpolator(decelerateInterpolator);
            AnimatorSet set = new AnimatorSet();
            set.playTogether(overShootAnimation, decelerateAnimation);
            set.addListener(new SubActionItemAnimationListener(menu.getSubActionItems().get(i), MenuAnimationHandler.ActionType.OPENING));

            if(i == 0) {
                lastAnimation = set;
            }

            // Put a slight lag between each of the menu items to make it asymmetric
            set.setStartDelay((menu.getSubActionItems().size() - i) * LAG_BETWEEN_ITEMS);
            set.start();
        }
        if(lastAnimation != null) {
            lastAnimation.addListener(new MenuAnimationHandler.LastAnimationListener());
        }

    }

    @Override
    public void animateMenuClosing(Point center) {
        super.animateMenuOpening(center);

        setAnimating(true);

        Animator lastAnimation = null;
        for (int i = 0; i < menu.getSubActionItems().size(); i++) {
            PropertyValuesHolder pvhX = PropertyValuesHolder.ofFloat(View.TRANSLATION_X, - (menu.getSubActionItems().get(i).x - center.x + menu.getSubActionItems().get(i).width / 2));
            PropertyValuesHolder pvhY = PropertyValuesHolder.ofFloat(View.TRANSLATION_Y, - (menu.getSubActionItems().get(i).y - center.y + menu.getSubActionItems().get(i).height / 2));
            PropertyValuesHolder pvhR = PropertyValuesHolder.ofFloat(View.ROTATION, -720);
            PropertyValuesHolder pvhsX = PropertyValuesHolder.ofFloat(View.SCALE_X, 0);
            PropertyValuesHolder pvhsY = PropertyValuesHolder.ofFloat(View.SCALE_Y, 0);
            PropertyValuesHolder pvhA = PropertyValuesHolder.ofFloat(View.ALPHA, 0);

            final ObjectAnimator animation = ObjectAnimator.ofPropertyValuesHolder(menu.getSubActionItems().get(i).view, pvhX, pvhY, pvhR, pvhsX, pvhsY, pvhA);
            animation.setDuration(DURATION);
            animation.setInterpolator(new AccelerateDecelerateInterpolator());
            animation.addListener(new SubActionItemAnimationListener(menu.getSubActionItems().get(i), MenuAnimationHandler.ActionType.CLOSING));

            if(i == 0) {
                lastAnimation = animation;
            }

            animation.setStartDelay((menu.getSubActionItems().size() - i) * LAG_BETWEEN_ITEMS);
            animation.start();
        }
        if(lastAnimation != null) {
            lastAnimation.addListener(new MenuAnimationHandler.LastAnimationListener());
        }
    }

    @Override
    public boolean isAnimating() {
        return animating;
    }

    @Override
    protected void setAnimating(boolean animating) {
        this.animating = animating;
    }

    protected class SubActionItemAnimationListener implements Animator.AnimatorListener {

        private FloatingActionMenu.Item subActionItem;
        private MenuAnimationHandler.ActionType actionType;

        public SubActionItemAnimationListener(FloatingActionMenu.Item subActionItem, MenuAnimationHandler.ActionType actionType) {
            this.subActionItem = subActionItem;
            this.actionType = actionType;
        }

        @Override
        public void onAnimationStart(Animator animation) {

        }

        @Override
        public void onAnimationEnd(Animator animation) {
            restoreSubActionViewAfterAnimation(subActionItem, actionType);
        }

        @Override
        public void onAnimationCancel(Animator animation) {
            restoreSubActionViewAfterAnimation(subActionItem, actionType);
        }

        @Override public void onAnimationRepeat(Animator animation) {}
    }
}
