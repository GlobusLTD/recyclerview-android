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

import android.support.annotation.Nullable;
import android.support.v4.view.GestureDetectorCompat;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;

public class ItemClickHelper2<E> implements RecyclerView.OnChildAttachStateChangeListener,
        RecyclerView.OnItemTouchListener, GestureDetector.OnGestureListener {

    @Nullable
    private OnItemClickListener<E> mOnItemClickListener;

    @Nullable
    private OnItemLongClickListener<E> mOnItemLongClickListener;

    @Nullable
    private RecyclerView mRecyclerView;

    @Nullable
    private GestureDetectorCompat mGestureDetector;

    public ItemClickHelper2() {
    }

    /**
     * Register a callback to be invoked when view is clicked.
     *
     * @param onItemClickListener The callback that will run
     */
    public void setOnItemClickListener(@Nullable final OnItemClickListener<E> onItemClickListener) {
        mOnItemClickListener = onItemClickListener;
    }

    /**
     * Register a callback to be invoked when view is long clicked.
     *
     * @param itemLongClickListener The callback that will run
     */
    public void setOnItemLongClickListener(@Nullable final OnItemLongClickListener<E> itemLongClickListener) {
        mOnItemLongClickListener = itemLongClickListener;
    }

    /**
     * Attaches the ItemTouchHelper to the provided RecyclerView. If TouchHelper is already
     * attached to a RecyclerView, it will first detach from the previous one. You can call this
     * method with {@code null} to detach it from the current RecyclerView.
     *
     * @param recyclerView The RecyclerView instance to which you want to add this helper or
     *                     {@code null} if you want to remove ItemTouchHelper from the current
     *                     RecyclerView.
     */
    public void attachToRecyclerView(@Nullable final RecyclerView recyclerView) {
        if (mRecyclerView == recyclerView) {
            return; // Nothing to do
        }
        if (mRecyclerView != null) {
            mGestureDetector = null;
            mRecyclerView.removeOnItemTouchListener(this);
            mRecyclerView.removeOnChildAttachStateChangeListener(this);
        }
        mRecyclerView = recyclerView;
        if (mRecyclerView != null) {
            mGestureDetector = new GestureDetectorCompat(recyclerView.getContext(), this);
            mRecyclerView.addOnChildAttachStateChangeListener(this);
            mRecyclerView.addOnItemTouchListener(this);
        }
    }

    @Override
    public void onChildViewAttachedToWindow(final View view) {
        Log.i("1111", "onChildViewAttachedToWindow(" + view + ")");
    }

    @Override
    public void onChildViewDetachedFromWindow(final View view) {
        Log.i("1111", "onChildViewDetachedFromWindow(" + view + ")");
    }

    @Override
    public boolean onInterceptTouchEvent(final RecyclerView rv, final MotionEvent e) {
        Log.i("1111", "onInterceptTouchEvent(" + e + ")");
        return false;
    }

    @Override
    public void onTouchEvent(final RecyclerView rv, final MotionEvent e) {
        Log.i("1111", "onTouchEvent(" + e + ")");
    }

    @Override
    public void onRequestDisallowInterceptTouchEvent(final boolean disallowIntercept) {
        Log.i("1111", "onRequestDisallowInterceptTouchEvent(" + disallowIntercept + ")");
    }

    @Override
    public boolean onDown(final MotionEvent e) {
        return false;
    }

    @Override
    public void onShowPress(final MotionEvent e) {

    }

    @Override
    public boolean onSingleTapUp(final MotionEvent e) {
        return false;
    }

    @Override
    public boolean onScroll(final MotionEvent e1, final MotionEvent e2, final float distanceX, final float distanceY) {
        return false;
    }

    @Override
    public void onLongPress(final MotionEvent e) {

    }

    @Override
    public boolean onFling(final MotionEvent e1, final MotionEvent e2, final float velocityX, final float velocityY) {
        return false;
    }
}
