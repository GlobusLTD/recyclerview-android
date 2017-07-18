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
package com.globusltd.recyclerview.choice;

import android.support.annotation.IntRange;
import android.support.annotation.MainThread;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.annotation.RestrictTo;
import android.support.v4.util.ArraySet;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewParent;

import com.globusltd.recyclerview.RecyclerViewBehavior;
import com.globusltd.recyclerview.ViewHolderBehavior;
import com.globusltd.recyclerview.view.OnItemClickListener;
import com.globusltd.recyclerview.view.OnItemLongClickListener;

import java.util.Set;

@MainThread
@RestrictTo(RestrictTo.Scope.LIBRARY)
public class ChoiceModeOwner<E> implements OnItemClickListener<E>, OnItemLongClickListener<E>,
        RecyclerViewBehavior, ViewHolderBehavior {
    
    @NonNull
    private ChoiceMode mChoiceMode;
    
    @Nullable
    private OnItemClickListener<E> mOnItemClickListener;
    
    @Nullable
    private OnItemLongClickListener<E> mOnItemLongClickListener;
    
    @NonNull
    private final Set<RecyclerView> mAttachedRecyclerViews;
    
    @NonNull
    private final ChoiceModeObserver mChoiceModeObserver;
    
    public ChoiceModeOwner(@NonNull final ChoiceMode choiceMode) {
        mChoiceMode = choiceMode;
        mAttachedRecyclerViews = new ArraySet<>();
        mChoiceModeObserver = new Observer();
    }
    
    /**
     * Sets a {@link ChoiceMode} implementation to handle selected items.
     *
     * @param choiceMode {@link ChoiceMode} implementation.
     */
    public void setChoiceMode(@NonNull final ChoiceMode choiceMode) {
        mChoiceMode.unregisterChoiceModeObserver(mChoiceModeObserver);
        mChoiceMode = choiceMode;
        if (!mAttachedRecyclerViews.isEmpty()) {
            mChoiceMode.registerChoiceModeObserver(mChoiceModeObserver);
            // TODO: invalidate visible view holders
        }
    }
    
    /**
     * Register a callback to be invoked when view is clicked and choice mode is inactive.
     *
     * @param onItemClickListener The callback that will run.
     */
    public void setOnItemClickListener(@Nullable final OnItemClickListener<E> onItemClickListener) {
        mOnItemClickListener = onItemClickListener;
    }
    
    /**
     * {@inheritDoc}
     */
    @Override
    public boolean onItemClick(@NonNull final View view, @NonNull final E item,
                               @IntRange(from = 0) final int position) {
        final RecyclerView.ViewHolder viewHolder = findViewHolderByView(view);
        return (viewHolder != null && mChoiceMode.onClick(viewHolder.getItemId())) ||
                (mOnItemClickListener != null && mOnItemClickListener.onItemClick(view, item, position));
    }
    
    /**
     * Register a callback to be invoked when view is long clicked and choice mode is inactive.
     *
     * @param onItemLongClickListener The callback that will run.
     */
    public void setOnItemLongClickListener(@Nullable final OnItemLongClickListener<E> onItemLongClickListener) {
        mOnItemLongClickListener = onItemLongClickListener;
    }
    
    /**
     * {@inheritDoc}
     */
    @Override
    public boolean onItemLongClick(@NonNull final View view, @NonNull final E item,
                                   @IntRange(from = 0) final int position) {
        final RecyclerView.ViewHolder viewHolder = findViewHolderByView(view);
        return (viewHolder != null && mChoiceMode.onLongClick(viewHolder.getItemId())) ||
                (mOnItemLongClickListener != null && mOnItemLongClickListener.onItemLongClick(view, item, position));
    }
    
    /**
     * {@inheritDoc}
     */
    @Override
    public void onAttachedToRecyclerView(final RecyclerView recyclerView) {
        if (mAttachedRecyclerViews.isEmpty() && mAttachedRecyclerViews.add(recyclerView)) {
            mChoiceMode.registerChoiceModeObserver(mChoiceModeObserver);
        } else {
            mAttachedRecyclerViews.add(recyclerView);
        }
    }
    
    @Override
    public void onAttachViewHolder(@NonNull final RecyclerView.ViewHolder viewHolder) {
        onViewHolderPositionChanged(viewHolder);
        // TODO: set view holder checked
    }
    
    @Override
    public void onViewHolderPositionChanged(@NonNull final RecyclerView.ViewHolder viewHolder) {
        // TODO: set view holder checked
    }
    
    @Override
    public void onDetachViewHolder(@NonNull final RecyclerView.ViewHolder viewHolder) {
        // TODO: reset view holder checked
    }
    
    /**
     * {@inheritDoc}
     */
    @Override
    public void onDetachedFromRecyclerView(final RecyclerView recyclerView) {
        if (mAttachedRecyclerViews.remove(recyclerView) && mAttachedRecyclerViews.isEmpty()) {
            mChoiceMode.unregisterChoiceModeObserver(mChoiceModeObserver);
        }
    }
    
    @Nullable
    private RecyclerView.ViewHolder findViewHolderByView(@NonNull final View view) {
        for (final RecyclerView recyclerView : mAttachedRecyclerViews) {
            final ViewParent viewParent = view.getParent();
            final RecyclerView.ViewHolder viewHolder = (viewParent == recyclerView ?
                    recyclerView.getChildViewHolder(view) : null);
            if (viewHolder != null) {
                return viewHolder;
            }
        }
        return null;
    }
    
    private class Observer extends ChoiceModeObserver {
        
        @Override
        public void onItemCheckedChanged(final long itemId, final boolean fromUser) {
            // TODO: handle item checked changed
        }
        
    }
    
}