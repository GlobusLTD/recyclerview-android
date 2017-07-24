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

public class EnchancedGestureDetector implements GestureDetector.OnGestureListener {

    public interface OnGestureListener {

        boolean onDown(@NonNull final MotionEvent e);

        void onShowPress(@NonNull final MotionEvent e);

        boolean onSingleTapUp(@NonNull final MotionEvent e);

        void onLongPress(@NonNull final MotionEvent e);

        boolean onFling(@NonNull final MotionEvent e1, @NonNull final MotionEvent e2,
                        final float velocityX, final float velocityY);

        boolean onScrollBegin(@NonNull final MotionEvent e);

        boolean onScroll(@NonNull final MotionEvent first, @NonNull final MotionEvent previous,
                         @NonNull final MotionEvent current, final float distanceX, final float distanceY);

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

    public EnchancedGestureDetector(@NonNull final Context context,
                                    @NonNull final OnGestureListener listener) {
        mGestureDetector = new GestureDetectorCompat(context, this);
        mListener = listener;
    }

    public void setIsLongpressEnabled(final boolean enabled) {
        mGestureDetector.setIsLongpressEnabled(enabled);
    }

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

    @Override
    public boolean onDown(@NonNull final MotionEvent e) {
        mAlwaysInTapRegion = true;
        mHandleFling = true;
        return mListener.onDown(e);
    }

    @Override
    public boolean onFling(@NonNull final MotionEvent e1, @NonNull final MotionEvent e2,
                           final float velocityX, final float velocityY) {
        mListener.onHidePress();
        mListener.onUp(e1);
        return mHandleFling &&
                (Math.abs(velocityX) > MIN_FLING_VELOCITY || Math.abs(velocityY) > MIN_FLING_VELOCITY) &&
                mListener.onFling(e1, e2, velocityX / VELOCITY_REDUCE_FACTOR, velocityY / VELOCITY_REDUCE_FACTOR);
    }

    @Override
    public void onLongPress(@NonNull final MotionEvent e) {
        mListener.onLongPress(e);
    }

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

    @Override
    public void onShowPress(@NonNull final MotionEvent e) {
        mListener.onShowPress(e);
    }

    @Override
    public boolean onSingleTapUp(@NonNull final MotionEvent e) {
        return mListener.onSingleTapUp(e);
    }

}
