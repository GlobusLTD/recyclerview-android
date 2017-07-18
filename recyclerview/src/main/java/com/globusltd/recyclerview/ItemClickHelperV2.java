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
package com.globusltd.recyclerview;

import android.os.Build;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.RecyclerView;
import android.view.MotionEvent;
import android.view.View;

import com.globusltd.recyclerview.view.EnchancedGestureDetector;
import com.globusltd.recyclerview.view.OnItemClickListener;
import com.globusltd.recyclerview.view.OnItemLongClickListener;

public class ItemClickHelperV2<E> implements RecyclerView.OnItemTouchListener {
    
    @NonNull
    private final ClickableAdapter<E> mClickableAdapter;
    
    @Nullable
    private OnItemClickListener<E> mOnItemClickListener;
    
    @Nullable
    private OnItemLongClickListener<E> mOnItemLongClickListener;
    
    @Nullable
    private RecyclerView mRecyclerView;
    
    @Nullable
    private EnchancedGestureDetector mGestureDetector;
    
    @Nullable
    private ViewHolderTracker mViewHolderTracker;
    
    public ItemClickHelperV2(@NonNull final ClickableAdapter<E> clickableAdapter) {
        mClickableAdapter = clickableAdapter;
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
            //mRecyclerView.removeOnChildAttachStateChangeListener(this);
        }
        mRecyclerView = recyclerView;
        if (mRecyclerView != null) {
            final EnchancedGestureDetector.OnGestureListener onGestureListener = onCreateGestureListener(mRecyclerView);
            mGestureDetector = new EnchancedGestureDetector(mRecyclerView.getContext(), onGestureListener);
            mGestureDetector.setIsLongpressEnabled(mOnItemLongClickListener != null);
            mRecyclerView.addOnItemTouchListener(this);
        }
    }
    
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
    
    @Override
    public void onRequestDisallowInterceptTouchEvent(final boolean disallowIntercept) {
    }
    
    public boolean performClick(@NonNull final RecyclerView.ViewHolder viewHolder) {
        final int position = viewHolder.getAdapterPosition();
        final E item = mClickableAdapter.get(position);
        return (mOnItemClickListener != null && mOnItemClickListener.onItemClick(viewHolder.itemView, item, position));
    }
    
    public boolean performLongPress(@NonNull final RecyclerView.ViewHolder viewHolder) {
        final int position = viewHolder.getAdapterPosition();
        final E item = mClickableAdapter.get(position);
        return (mOnItemLongClickListener != null && mOnItemLongClickListener.onItemLongClick(viewHolder.itemView, item, position));
    }
    
    @NonNull
    public EnchancedGestureDetector.OnGestureListener onCreateGestureListener(@NonNull final RecyclerView recyclerView) {
        return new DefaultGestureListener(recyclerView);
    }
    
    private class DefaultGestureListener implements EnchancedGestureDetector.OnGestureListener {
        
        @NonNull
        private final RecyclerView mHostView;
        
        @Nullable
        private View mTargetChild;
        
        DefaultGestureListener(@NonNull final RecyclerView recyclerView) {
            mHostView = recyclerView;
        }
        
        @Override
        public boolean onDown(@NonNull final MotionEvent event) {
            final float x = event.getX();
            final float y = event.getY();
            final View itemView = mHostView.findChildViewUnder(x, y);
            if (itemView != null) {
                mTargetChild = itemView;
                
                /* TODO: find the closest clickable view to event coordinates
                final RecyclerView.ViewHolder viewHolder = mHostView.findContainingViewHolder(itemView);
                final int position = (viewHolder != null ? viewHolder.getAdapterPosition() : RecyclerView.NO_POSITION);
                final int viewType = (viewHolder != null ? viewHolder.getItemViewType() : RecyclerView.INVALID_TYPE);
                
                final RecyclerView.Adapter<?> adapter = mHostView.getAdapter();
                if (position > RecyclerView.NO_POSITION && adapter == mClickableAdapter && mClickableAdapter.isEnabled(position)) {
                    final ClickableViews clickableViews = mClickableAdapter.getClickableViews(position, viewType);
                    
                }*/
            }
            return (mTargetChild != null);
        }
        
        @Override
        public void onShowPress(@NonNull final MotionEvent event) {
            if (mTargetChild != null) {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                    final float x = event.getX() - mTargetChild.getLeft();
                    final float y = event.getY() - mTargetChild.getTop();
                    mTargetChild.drawableHotspotChanged(x, y);
                }
                mTargetChild.setPressed(true);
            }
        }
        
        @Override
        public boolean onSingleTapUp(@NonNull final MotionEvent event) {
            if (mTargetChild != null) {
                final RecyclerView.ViewHolder viewHolder = mHostView.findContainingViewHolder(mTargetChild);
                final boolean handled = (viewHolder != null && performClick(viewHolder));
                mTargetChild.setPressed(false);
                mTargetChild = null;
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
            if (mTargetChild != null) {
                final RecyclerView.ViewHolder viewHolder = mHostView.findContainingViewHolder(mTargetChild);
                if (viewHolder != null) {
                    performLongPress(viewHolder);
                }
            }
        }
        
        @Override
        public boolean onFling(@NonNull final MotionEvent e1, @NonNull final MotionEvent e2,
                               final float velocityX, final float velocityY) {
            
            return false;
        }
        
        @Override
        public void onUp(@NonNull final MotionEvent e) {
            if (mTargetChild != null) {
                mTargetChild.setPressed(false);
                mTargetChild = null;
            }
        }
        
    }
    
    
}
