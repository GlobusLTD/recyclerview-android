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
import com.globusltd.recyclerview.view.OnItemClickListener;
import com.globusltd.recyclerview.view.OnItemLongClickListener;

import java.util.Set;

@MainThread
@RestrictTo(RestrictTo.Scope.LIBRARY)
public class ChoiceModeOwner<E> implements OnItemClickListener<E>, OnItemLongClickListener<E>,
        RecyclerViewBehavior {
    
    private static final ChoiceMode DEFAULT_CHOICE_MODE = new NoneChoiceMode();
    
    @NonNull
    private ChoiceMode mChoiceMode = DEFAULT_CHOICE_MODE;
    
    @Nullable
    private OnItemClickListener<E> mOnItemClickListener;
    
    @Nullable
    private OnItemLongClickListener<E> mOnItemLongClickListener;
    
    @NonNull
    private final Set<RecyclerView> mAttachedRecyclerViews;
    
    public ChoiceModeOwner() {
        mAttachedRecyclerViews = new ArraySet<>();
    }
    
    /**
     * Sets a {@link ChoiceMode} implementation to handle selected items.
     *
     * @param choiceMode {@link ChoiceMode} implementation.
     */
    public void setChoiceMode(@NonNull final ChoiceMode choiceMode) {
        mChoiceMode = choiceMode;
        // TODO: attach/detach
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
        mAttachedRecyclerViews.add(recyclerView);
    }
    
    /**
     * {@inheritDoc}
     */
    @Override
    public void onDetachedFromRecyclerView(final RecyclerView recyclerView) {
        mAttachedRecyclerViews.remove(recyclerView);
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
    
}