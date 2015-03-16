package com.oguzdev.circularfloatingactionmenu.library.subActionButtonAnimation;

import android.animation.Animator;

import com.oguzdev.circularfloatingactionmenu.library.OnBounceListener;
import com.oguzdev.circularfloatingactionmenu.library.SubActionButton;

/**
 * Created by darrenpoon on 12/2/15.
 */
public abstract class SubActionButtonAnimationHandler {

    public abstract void animateOnTouchSubActionButton(SubActionButton button, OnBounceListener listener);

    public abstract boolean isAnimating();

    protected abstract void setAnimating(boolean animating);

    public class LastAnimationListener implements Animator.AnimatorListener {

        public OnBounceListener mBounceListener;

        public LastAnimationListener(OnBounceListener listener) {
            mBounceListener = listener;
        }

        @Override
        public void onAnimationStart(Animator animation) {
            setAnimating(true);
        }

        @Override
        public void onAnimationEnd(Animator animation) {
            setAnimating(false);
            if (mBounceListener != null) {
                mBounceListener.onBounce();
            }
        }

        @Override
        public void onAnimationCancel(Animator animation) {
            setAnimating(false);
        }

        @Override
        public void onAnimationRepeat(Animator animation) {
            setAnimating(true);
        }
    }

}
