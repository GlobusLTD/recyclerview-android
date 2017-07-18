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

import android.support.annotation.IntRange;
import android.support.annotation.MainThread;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.util.ArrayMap;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.globusltd.recyclerview.util.ArrayPool;
import com.globusltd.recyclerview.util.Pool;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * {@link ViewHolderTracker} provides view holder attach/detach events to
 * the registered {@link ViewHolderBehavior} objects.
 */
@MainThread
public class ViewHolderTracker implements ViewHolderBehavior {
    
    @NonNull
    private final List<ViewHolderBehavior> mBehaviors;
    
    @NonNull
    private final Pool<ViewHolderOnLayoutChangeListener> mPool;
    
    @NonNull
    private final Map<RecyclerView.ViewHolder, ViewHolderOnLayoutChangeListener> mOnLayoutChangeListeners;
    
    public ViewHolderTracker() {
        mBehaviors = new ArrayList<>();
        mPool = new ArrayPool<>(new ViewHolderOnLayoutChangeListenerFactory());
        mOnLayoutChangeListeners = new ArrayMap<>();
    }
    
    /**
     * Add a new {@link ViewHolderBehavior} to the {@link ViewHolderTracker},
     * which will be called at the same times as the attach/detach methods of
     * adapter are called.
     *
     * @param viewHolderBehavior The interface to call.
     */
    public void registerViewHolderBehavior(@NonNull final ViewHolderBehavior viewHolderBehavior) {
        if (!mBehaviors.contains(viewHolderBehavior)) {
            mBehaviors.add(viewHolderBehavior);
        }
    }
    
    /**
     * Remove a {@link ViewHolderBehavior} object that was previously registered
     * with {@link #registerViewHolderBehavior(ViewHolderBehavior)}.
     */
    public void unregisterViewHolderBehavior(@NonNull final ViewHolderBehavior viewHolderBehavior) {
        mBehaviors.remove(viewHolderBehavior);
    }
    
    /**
     * {@inheritDoc}
     */
    @Override
    public void onAttachViewHolder(@NonNull final RecyclerView.ViewHolder viewHolder) {
        if (!mOnLayoutChangeListeners.containsKey(viewHolder)) {
            final ViewHolderOnLayoutChangeListener listener = mPool.obtain();
            listener.viewHolder = viewHolder;
            listener.position = viewHolder.getAdapterPosition();
            viewHolder.itemView.addOnLayoutChangeListener(listener);
            mOnLayoutChangeListeners.put(viewHolder, listener);
        }
        
        for (final ViewHolderBehavior behavior : mBehaviors) {
            behavior.onAttachViewHolder(viewHolder);
        }
    }
    
    /**
     * {@inheritDoc}
     */
    @Override
    public void onViewHolderPositionChanged(@NonNull final RecyclerView.ViewHolder viewHolder) {
        for (final ViewHolderBehavior behavior : mBehaviors) {
            behavior.onViewHolderPositionChanged(viewHolder);
        }
    }
    
    /**
     * {@inheritDoc}
     */
    @Override
    public void onDetachViewHolder(@NonNull final RecyclerView.ViewHolder viewHolder) {
        final int size = mBehaviors.size();
        for (int i = size - 1; i >= 0; i--) {
            final ViewHolderBehavior behavior = mBehaviors.get(i);
            behavior.onDetachViewHolder(viewHolder);
        }
        
        final ViewHolderOnLayoutChangeListener listener = mOnLayoutChangeListeners.remove(viewHolder);
        if (listener != null) {
            viewHolder.itemView.removeOnLayoutChangeListener(listener);
            
            listener.viewHolder = null;
            listener.position = RecyclerView.NO_POSITION;
            mPool.recycle(listener);
        }
    }
    
    private class ViewHolderOnLayoutChangeListenerFactory implements
            Pool.Factory<ViewHolderOnLayoutChangeListener> {
        
        @NonNull
        @Override
        public ViewHolderOnLayoutChangeListener create() {
            return new ViewHolderOnLayoutChangeListener();
        }
        
    }
    
    private class ViewHolderOnLayoutChangeListener implements View.OnLayoutChangeListener {
        
        @Nullable
        RecyclerView.ViewHolder viewHolder;
        
        @IntRange(from = RecyclerView.NO_POSITION)
        int position;
        
        @Override
        public void onLayoutChange(final View v, final int left, final int top,
                                   final int right, final int bottom,
                                   final int oldLeft, final int oldTop,
                                   final int oldRight, final int oldBottom) {
            final boolean hasSameView = (viewHolder != null && viewHolder.itemView == v);
            final boolean isPositionChanged = (viewHolder != null && viewHolder.getAdapterPosition() != position);
            if (hasSameView && isPositionChanged) {
                position = viewHolder.getAdapterPosition();
                onViewHolderPositionChanged(viewHolder);
            }
        }
        
    }
    
}