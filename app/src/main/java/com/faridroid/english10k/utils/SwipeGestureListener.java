package com.faridroid.english10k.utils;

import android.view.MotionEvent;
import android.view.GestureDetector.SimpleOnGestureListener;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Toast;

import com.faridroid.english10k.FlashcardGameActivity;

/*
 * detecta los gestos de deslizamiento (swipe). Puedes personalizar lo que
 * ocurre cuando el usuario desliza a la izquierda o a la derecha en los
 * métodos onSwipeLeft() y onSwipeRight().
 * */
public class SwipeGestureListener extends SimpleOnGestureListener {

    private static final int SWIPE_THRESHOLD = 100;
    private static final int SWIPE_VELOCITY_THRESHOLD = 100;
    private FlashcardGameActivity flashcardGameActivity;
    private View viewToAnimate;

    public SwipeGestureListener(FlashcardGameActivity flashcardGameActivity, View viewToAnimate) {
        this.flashcardGameActivity = flashcardGameActivity;
        this.viewToAnimate = viewToAnimate;
    }

    @Override
    public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX, float velocityY) {
        boolean result = false;
        try {
            float diffX = e2.getX() - e1.getX();
            float diffY = e2.getY() - e1.getY();
            if (Math.abs(diffX) > Math.abs(diffY)) {
                if (Math.abs(diffX) > SWIPE_THRESHOLD && Math.abs(velocityX) > SWIPE_VELOCITY_THRESHOLD) {
                    if (diffX > 0) {
                        onSwipeRight();
                    } else {
                        onSwipeLeft();
                    }
                    result = true;
                }
            }
        } catch (Exception exception) {
            exception.printStackTrace();
        }
        return result;
    }

    public void onSwipeRight() {
        // Animación de salida
        Animation slideOutRight = AnimationUtils.loadAnimation(flashcardGameActivity, flashcardGameActivity.getResources().getIdentifier("slide_out_right", "anim", flashcardGameActivity.getPackageName()));

        viewToAnimate.startAnimation(slideOutRight);
        slideOutRight.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {
            }

            @Override
            public void onAnimationEnd(Animation animation) {

                // Animación de entrada
                Animation slideInLeft = AnimationUtils.loadAnimation(flashcardGameActivity, flashcardGameActivity.getResources().getIdentifier("slide_in_left", "anim", flashcardGameActivity.getPackageName()));
                viewToAnimate.startAnimation(slideInLeft);
                // Cambiar la palabra después de la animación de salida
                flashcardGameActivity.onSwipeRight();
            }

            @Override
            public void onAnimationRepeat(Animation animation) {
            }
        });
    }

    public void onSwipeLeft() {
        // Animación de salida
        Animation slideOutLeft = AnimationUtils.loadAnimation(flashcardGameActivity, flashcardGameActivity.getResources().getIdentifier("slide_out_left", "anim", flashcardGameActivity.getPackageName()));

        viewToAnimate.startAnimation(slideOutLeft);
        slideOutLeft.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {
            }

            @Override
            public void onAnimationEnd(Animation animation) {
                // Animación de entrada
                Animation slideInRight = AnimationUtils.loadAnimation(flashcardGameActivity, flashcardGameActivity.getResources().getIdentifier("slide_in_right", "anim", flashcardGameActivity.getPackageName()));
                viewToAnimate.startAnimation(slideInRight);

                // Cambiar la palabra después de la animación de salida
                flashcardGameActivity.onSwipeLeft();
            }

            @Override
            public void onAnimationRepeat(Animation animation) {
            }
        });
    }

}
