/*
 * Copyright 2017 Globus Ltd.
 * <p/>
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * <p/>
 * http://www.apache.org/licenses/LICENSE-2.0
 * <p/>
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.globusltd.recyclerview.view;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.view.GestureDetectorCompat;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;

/**
 * Detects various gestures and events using the supplied {@link MotionEvent}s.
 * The {@link OnGestureListener} callback will notify users when a particular
 * motion event has occurred. This class should only be used with {@link MotionEvent}s
 * reported via touch (don't use for trackball events).
 * <p>
 * To use this class:
 * <ul>
 * <li>Create an instance of the {@code GestureDetector} for your {@link View}
 * <li>In the {@link View#onTouchEvent(MotionEvent)} method ensure you call
 * {@link #onTouchEvent(MotionEvent)}. The methods defined in your callback
 * will be executed when the events occur.
 * </ul>
 */
public final class EnchancedGestureDetector implements GestureDetector.OnGestureListener {

    /**
     * The listener that is used to notify when gestures occur.
     * If you want to listen for all the different gestures then implement
     * this interface. If you only want to listen for a subset it might
     * be easier to extend {@link SimpleOnGestureListener}.
     */
    public interface OnGestureListener {

        /**
         * Notified when a tap occurs with the down {@link MotionEvent}
         * that triggered it. This will be triggered immediately for
         * every down event. All other events should be preceded by this.
         *
         * @param e The down motion event.
         */
        boolean onDown(@NonNull final MotionEvent e);

        /**
         * The user has performed a down {@link MotionEvent} and not performed
         * a move or up yet. This event is commonly used to provide visual
         * feedback to the user to let them know that their action has been
         * recognized i.e. highlight an element.
         *
         * @param e The down motion event
         */
        void onShowPress(@NonNull final MotionEvent e);

        /**
         * Notified when a tap occurs with the up {@link MotionEvent}
         * that triggered it.
         *
         * @param e The up motion event that completed the first tap
         * @return true if the event is consumed, else false
         */
        boolean onSingleTapUp(@NonNull final MotionEvent e);

        /**
         * Notified when a long press occurs with the initial on down {@link MotionEvent}
         * that trigged it.
         *
         * @param e The initial on down motion event that started the longpress.
         */
        void onLongPress(@NonNull final MotionEvent e);

        /**
         * Notified of a fling event when it occurs with the initial on down {@link MotionEvent}
         * and the matching up {@link MotionEvent}. The calculated velocity is supplied along
         * the x and y axis in pixels per second.
         *
         * @param e1        The first down motion event that started the fling.
         * @param e2        The move motion event that triggered the current onFling.
         * @param velocityX The velocity of this fling measured in pixels per second
         *                  along the x axis.
         * @param velocityY The velocity of this fling measured in pixels per second
         *                  along the y axis.
         * @return true if the event is consumed, else false
         */
        boolean onFling(@NonNull final MotionEvent e1, @NonNull final MotionEvent e2,
                        final float velocityX, final float velocityY);

        /**
         * Notified when a scroll begins with the initial on down {@link MotionEvent}
         *
         * @param e The first down motion event that started the scrolling.{@code e1}
         * @return true if the event is consumed, else false.
         */
        boolean onScrollBegin(@NonNull final MotionEvent e);

        /**
         * Notified when a scroll occurs with the initial on down {@link MotionEvent} and the
         * current move {@link MotionEvent}. The distance in x and y is also supplied for
         * convenience.
         *
         * @param first     The first down motion event that started the scrolling.
         * @param previous  The move motion event that happen before the current event.
         * @param current   The move motion event that triggered the current onScroll.
         * @param distanceX The distance along the X axis that has been scrolled since the last
         *                  call to onScroll. This is NOT the distance between {@code e1}
         *                  and {@code e2}.
         * @param distanceY The distance along the Y axis that has been scrolled since the last
         *                  call to onScroll. This is NOT the distance between {@code e1}
         *                  and {@code e2}.
         * @return true if the event is consumed, else false
         */
        boolean onScroll(@NonNull final MotionEvent first, @NonNull final MotionEvent previous,
                         @NonNull final MotionEvent current, final float distanceX, final float distanceY);

        /**
         * The user has performed a move or up {@link MotionEvent}.
         * This event is commonly used to provide visual feedback to the user
         * to let them know that their primary action has been cancelled
         * i.e. remove highlight from an element.
         */
        void onHidePress();

        void onUp(@NonNull final MotionEvent e);

    }

    private static final int MIN_FLING_VELOCITY = 350;
    private static final float VELOCITY_REDUCE_FACTOR = 1.5f;

    @NonNull
    private final GestureDetectorCompat mGestureDetector;

    @NonNull
    private final OnGestureListener mListener;

    private boolean mAlwaysInTapRegion;
    private boolean mScrollInProgress = false;
    private boolean mHandleFling = false;

    @Nullable
    private MotionEvent mPreviousMotionEvent;

    /**
     * Creates a EnchancedGestureDetector with the supplied listener.
     *
     * @param context the application's context
     * @param listener the listener invoked for all the callbacks, this must
     * not be null.
     */
    public EnchancedGestureDetector(@NonNull final Context context,
                                    @NonNull final OnGestureListener listener) {
        mGestureDetector = new GestureDetectorCompat(context, this);
        mListener = listener;
    }

    /**
     * Set whether longpress is enabled, if this is enabled when a user
     * presses and holds down you get a longpress event and nothing further.
     * If it's disabled the user can press and hold down and then later
     * moved their finger and you will get scroll events. By default
     * longpress is enabled.
     *
     * @param enabled whether longpress should be enabled.
     */
    public void setLongpressEnabled(final boolean enabled) {
        mGestureDetector.setIsLongpressEnabled(enabled);
    }

    /**
     * Analyzes the given motion event and if applicable triggers the
     * appropriate callbacks on the {@link OnGestureListener} supplied.
     *
     * @param e The current motion event.
     * @return true if the {@link OnGestureListener} consumed the event,
     *              else false.
     */
    public boolean onTouchEvent(@NonNull final MotionEvent e) {
        switch (e.getAction()) {
            case MotionEvent.ACTION_UP:
            case MotionEvent.ACTION_CANCEL:
            case MotionEvent.ACTION_OUTSIDE:
                mScrollInProgress = false;
                final boolean handled = mGestureDetector.onTouchEvent(e);
                mListener.onHidePress();
                if (!handled) {
                    mListener.onUp(e);
                }
                return handled;

            default:
                return mGestureDetector.onTouchEvent(e);
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean onDown(@NonNull final MotionEvent e) {
        mAlwaysInTapRegion = true;
        mHandleFling = true;
        return mListener.onDown(e);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean onFling(@NonNull final MotionEvent e1, @NonNull final MotionEvent e2,
                           final float velocityX, final float velocityY) {
        mListener.onHidePress();
        mListener.onUp(e1);
        return mHandleFling &&
                (Math.abs(velocityX) > MIN_FLING_VELOCITY || Math.abs(velocityY) > MIN_FLING_VELOCITY) &&
                mListener.onFling(e1, e2, velocityX / VELOCITY_REDUCE_FACTOR, velocityY / VELOCITY_REDUCE_FACTOR);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void onLongPress(@NonNull final MotionEvent e) {
        mListener.onLongPress(e);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean onScroll(@NonNull final MotionEvent e1, @NonNull final MotionEvent e2,
                            final float distanceX, final float distanceY) {
        if (!mScrollInProgress) {
            if (mAlwaysInTapRegion) {
                mListener.onHidePress();
            }
            mAlwaysInTapRegion = false;
            mScrollInProgress = mListener.onScrollBegin(e1);
            mPreviousMotionEvent = MotionEvent.obtain(e1);
            return mScrollInProgress;
        }

        if (mPreviousMotionEvent != null) {
            final boolean res = mListener.onScroll(e1, mPreviousMotionEvent, e2,
                    distanceX, distanceY);
            mPreviousMotionEvent.recycle();
            mPreviousMotionEvent = MotionEvent.obtain(e2);
            return res;
        }

        return false;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void onShowPress(@NonNull final MotionEvent e) {
        mListener.onShowPress(e);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean onSingleTapUp(@NonNull final MotionEvent e) {
        return mListener.onSingleTapUp(e);
    }

    /**
     * A convenience class to extend when you only want to listen for a subset
     * of all the gestures. This implements all methods in the
     * {@link OnGestureListener} but does nothing and
     * return {@code false} for all applicable methods.
     */
    public static class SimpleOnGestureListener implements OnGestureListener {

        /**
         * {@inheritDoc}
         */
        @Override
        public boolean onDown(@NonNull final MotionEvent e) {
            return false;
        }

        /**
         * {@inheritDoc}
         */
        @Override
        public void onShowPress(@NonNull final MotionEvent e) {
        }

        /**
         * {@inheritDoc}
         */
        @Override
        public boolean onSingleTapUp(@NonNull final MotionEvent e) {
            return false;
        }

        /**
         * {@inheritDoc}
         */
        @Override
        public void onLongPress(@NonNull final MotionEvent e) {
        }

        /**
         * {@inheritDoc}
         */
        @Override
        public boolean onFling(@NonNull final MotionEvent e1, @NonNull final MotionEvent e2,
                               final float velocityX, final float velocityY) {
            return false;
        }

        /**
         * {@inheritDoc}
         */
        @Override
        public boolean onScrollBegin(@NonNull final MotionEvent e) {
            return false;
        }

        /**
         * {@inheritDoc}
         */
        @Override
        public boolean onScroll(@NonNull final MotionEvent first,
                                @NonNull final MotionEvent previous,
                                @NonNull final MotionEvent current,
                                final float distanceX, final float distanceY) {
            return false;
        }

        /**
         * {@inheritDoc}
         */
        @Override
        public void onHidePress() {
        }

        /**
         * {@inheritDoc}
         */
        @Override
        public void onUp(@NonNull final MotionEvent e) {
        }

    }

}
