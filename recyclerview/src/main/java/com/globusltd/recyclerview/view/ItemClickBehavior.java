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

import android.support.annotation.IdRes;
import android.support.annotation.IntRange;
import android.support.annotation.MainThread;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.util.ArrayMap;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.globusltd.recyclerview.Adapter;
import com.globusltd.recyclerview.ViewHolderBehavior;
import com.globusltd.recyclerview.util.ArrayPool;
import com.globusltd.recyclerview.util.Pool;

import java.util.Map;

/**
 * Class that manages attaching and detaching click listeners
 * when ViewHolder is bond to the data.
 *
 * @param <E> Type of elements handled by click listeners.
 */
@MainThread
public class ItemClickBehavior<E, VH extends RecyclerView.ViewHolder>
        implements ViewHolderBehavior<VH> {
    
    @NonNull
    private final Adapter<E, VH> mAdapter;
    
    @NonNull
    private final Pool<ViewHolderClickListener> mPool;
    
    @NonNull
    private final Map<VH, ViewHolderClickListener> mActiveViewHolders;
    
    @Nullable
    private OnItemClickListener<E> mOnItemClickListener;
    
    @Nullable
    private OnItemLongClickListener<E> mOnItemLongClickListener;
    
    public ItemClickBehavior(@NonNull final Adapter<E, VH> adapter) {
        mAdapter = adapter;
        mPool = new ArrayPool<>(new ViewHolderClickListenerFactory());
        mActiveViewHolders = new ArrayMap<>();
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
     * {@inheritDoc}
     */
    @Override
    public void onAttachViewHolder(@NonNull final VH holder) {
        final int position = holder.getAdapterPosition();
        if (isPositionAvailable(position)) {
            final ViewHolderClickListener listener = mActiveViewHolders.containsKey(holder) ?
                    mActiveViewHolders.get(holder) : mPool.obtain();
            mActiveViewHolders.put(holder, listener);
            
            final int viewType = mAdapter.getItemViewType(position);
            listener.position = position;
            listener.viewType = viewType;
            
            final ClickableViews clickableViews = mAdapter.getClickableViews(position, viewType);
            final boolean isEnabled = mAdapter.isEnabled(position);
            
            final View.OnClickListener onClickListener = (isEnabled ? listener : null);
            setClickable(holder.itemView, clickableViews.getDefaultViewId(), onClickListener);
            for (final int viewId : clickableViews.getClickableViewIds()) {
                setClickable(holder.itemView, viewId, onClickListener);
            }
            
            final View.OnLongClickListener onLongClickListener = (isEnabled ? listener : null);
            setLongClickable(holder.itemView, clickableViews.getDefaultViewId(), onLongClickListener);
        }
    }
    
    /**
     * {@inheritDoc}
     */
    public void onDetachViewHolder(@NonNull final VH holder) {
        final ViewHolderClickListener listener = mActiveViewHolders.remove(holder);
        if (listener != null && isPositionAvailable(listener.position)) {
            final ClickableViews clickableViews = mAdapter.getClickableViews(listener.position, listener.viewType);
            
            setClickable(holder.itemView, clickableViews.getDefaultViewId(), null);
            setLongClickable(holder.itemView, clickableViews.getDefaultViewId(), null);
            for (final int viewId : clickableViews.getClickableViewIds()) {
                setClickable(holder.itemView, viewId, null);
            }
            
            listener.position = RecyclerView.NO_POSITION;
            listener.viewType = RecyclerView.INVALID_TYPE;
            
            mPool.recycle(listener);
        }
    }
    
    private void setClickable(@NonNull final View itemView, @IdRes final int viewId,
                              @Nullable final View.OnClickListener listener) {
        final View view = findViewById(itemView, viewId);
        if (view != null) {
            view.setOnClickListener(listener);
            view.setClickable(listener != null);
        }
    }
    
    private void setLongClickable(@NonNull final View itemView, @IdRes final int viewId,
                                  @Nullable final View.OnLongClickListener listener) {
        final View view = (findViewById(itemView, viewId));
        if (view != null) {
            view.setOnLongClickListener(listener);
            view.setLongClickable(listener != null);
        }
    }
    
    @Nullable
    private View findViewById(@NonNull final View itemView, @IdRes final int viewId) {
        switch (viewId) {
            case View.NO_ID:
                return null;
            
            case ClickableViews.ITEM_VIEW_ID:
                return itemView;
            
            default:
                return itemView.findViewById(viewId);
        }
    }
    
    private boolean isPositionAvailable(@IntRange(from = RecyclerView.NO_POSITION) final int position) {
        return position > RecyclerView.NO_POSITION && position < mAdapter.getItemCount();
    }
    
    private class ViewHolderClickListenerFactory implements Pool.Factory<ViewHolderClickListener> {
        
        @NonNull
        @Override
        public ViewHolderClickListener create() {
            return new ViewHolderClickListener();
        }
        
    }
    
    private class ViewHolderClickListener implements View.OnClickListener, View.OnLongClickListener {
        
        //@IntRange(from = RecyclerView.NO_POSITION)
        int position;// = RecyclerView.NO_POSITION;
        
        @IntRange(from = RecyclerView.INVALID_TYPE)
        int viewType = RecyclerView.INVALID_TYPE;
        
        @Override
        public void onClick(final View view) {
            // The position check below is a possible solution for the bug which causes
            // ViewHolder.getAdapterPosition to return NO_POSITION for a visible item.
            if (isPositionAvailable(position)) {
                if (mOnItemClickListener != null) {
                    final E item = mAdapter.getDatasource().get(position);
                    mOnItemClickListener.onItemClick(view, item, position);
                }
            }
        }
        
        @Override
        public boolean onLongClick(final View view) {
            // The position check below is a possible solution for the bug which causes
            // ViewHolder.getAdapterPosition to return NO_POSITION for a visible item.
            if (isPositionAvailable(position)) {
                if (mOnItemLongClickListener != null) {
                    final E item = mAdapter.getDatasource().get(position);
                    return mOnItemLongClickListener.onItemLongClick(view, item, position);
                }
            }
            return false;
        }
        
    }
    
}
