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

import android.support.annotation.IntRange;
import android.support.annotation.MainThread;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.RecyclerView;
import android.view.MotionEvent;
import android.view.View;

import com.globusltd.recyclerview.RecyclerViewOwner;

@MainThread
public class ItemClickHelper<E> extends RecyclerViewOwner {

    @NonNull
    private final Callback<E> mCallback;

    @NonNull
    private final RecyclerView.OnItemTouchListener mOnItemTouchListener;

    @Nullable
    private OnItemClickListener<E> mOnItemClickListener;

    @Nullable
    private OnItemLongClickListener<E> mOnItemLongClickListener;

    @Nullable
    private EnchancedGestureDetector mGestureDetector;

    public ItemClickHelper(@NonNull final Callback<E> callback) {
        mCallback = callback;
        mOnItemTouchListener = new OnItemTouchListener();
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
        if (mGestureDetector != null) {
            mGestureDetector.setIsLongpressEnabled(itemLongClickListener != null);
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void onAttachedToRecyclerView(@NonNull final RecyclerView recyclerView) {
        final EnchancedGestureDetector.OnGestureListener onGestureListener = onCreateGestureListener(recyclerView);
        mGestureDetector = new EnchancedGestureDetector(recyclerView.getContext(), onGestureListener);
        mGestureDetector.setIsLongpressEnabled(mOnItemLongClickListener != null);
        recyclerView.addOnItemTouchListener(mOnItemTouchListener);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void onDetachedFromRecyclerView(@NonNull final RecyclerView recyclerView) {
        recyclerView.removeOnItemTouchListener(mOnItemTouchListener);
        mGestureDetector = null;
    }

    private boolean performClick(@NonNull final RecyclerView.ViewHolder viewHolder,
                                 @NonNull final View view) {
        final int position = viewHolder.getAdapterPosition();
        final E item = mCallback.get(position);
        return (mOnItemClickListener != null && mOnItemClickListener.onItemClick(view, item, position));
    }

    private boolean performLongPress(@NonNull final RecyclerView.ViewHolder viewHolder,
                                     @NonNull final View view) {
        if (viewHolder.itemView == view) {
            final int position = viewHolder.getAdapterPosition();
            final E item = mCallback.get(position);
            return (mOnItemLongClickListener != null && mOnItemLongClickListener.onItemLongClick(view, item, position));
        }
        return false;
    }

    private class OnItemTouchListener extends RecyclerView.SimpleOnItemTouchListener {

        @Override
        public boolean onInterceptTouchEvent(final RecyclerView rv, final MotionEvent e) {
            return (mOnItemClickListener != null || mOnItemLongClickListener != null) &&
                    (mGestureDetector != null && mGestureDetector.onTouchEvent(e));
        }

        @Override
        public void onTouchEvent(final RecyclerView rv, final MotionEvent e) {
            if (mGestureDetector != null) {
                mGestureDetector.onTouchEvent(e);
            }
        }

    }

    @NonNull
    private EnchancedGestureDetector.OnGestureListener onCreateGestureListener(@NonNull final RecyclerView recyclerView) {
        return new DefaultGestureListener(recyclerView);
    }

    private class DefaultGestureListener implements EnchancedGestureDetector.OnGestureListener {

        @NonNull
        private final ClickableViewFinder mViewFinder;

        @Nullable
        private ClickableViewFinder.Target mTarget;

        DefaultGestureListener(@NonNull final RecyclerView recyclerView) {
            mViewFinder = new ClickableViewFinder(recyclerView, mCallback);
        }

        @Override
        public boolean onDown(@NonNull final MotionEvent event) {
            mTarget = mViewFinder.findTarget(event.getX(), event.getY());
            return (mTarget != null);
        }

        @Override
        public void onShowPress(@NonNull final MotionEvent event) {
            if (mTarget != null) {
                mTarget.setPressed(true, event.getX(), event.getY());
            }
        }

        @Override
        public boolean onSingleTapUp(@NonNull final MotionEvent event) {
            if (mTarget != null) {
                final boolean handled = performClick(mTarget.getViewHolder(), mTarget.getView());
                mTarget.setPressed(false);
                mTarget = null;
                return handled;
            }
            return false;
        }

        @Override
        public boolean onScrollBegin(@NonNull final MotionEvent e) {
            return false;
        }

        @Override
        public boolean onScroll(@NonNull final MotionEvent first, @NonNull final MotionEvent previous,
                                @NonNull final MotionEvent current, final float distanceX, final float distanceY) {
            return false;
        }

        @Override
        public void onLongPress(@NonNull final MotionEvent e) {
            if (mTarget != null) {
                performLongPress(mTarget.getViewHolder(), mTarget.getView());
            }
        }

        @Override
        public boolean onFling(@NonNull final MotionEvent e1, @NonNull final MotionEvent e2,
                               final float velocityX, final float velocityY) {
            return false;
        }

        @Override
        public void onUp(@NonNull final MotionEvent e) {
            if (mTarget != null) {
                mTarget.setPressed(false);
                mTarget = null;
            }
        }

    }

    public interface Callback<E> {

        /**
         * Returns the element at the specified position.
         *
         * @param position position of the element to return.
         * @return the element at the specified position.
         * @throws IndexOutOfBoundsException if the index is out of range
         *                                   (<tt>index &lt; 0 || index &gt;= size()</tt>)
         */
        @NonNull
        E get(@IntRange(from = 0) final int position);

        /**
         * Returns information about all of the clickable views at specified position.
         * Note that it's better to preallocate ClickableViews instance for each view type.
         *
         * @param position an index of the item.
         * @return A {@link ClickableViews} instance.
         */
        @NonNull
        ClickableViews getClickableViews(@IntRange(from = 0) final int position, final int viewType);

    }

}
