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
import android.support.annotation.RestrictTo;
import android.support.v4.util.ArrayMap;
import android.support.v7.widget.RecyclerView;

import com.globusltd.recyclerview.Adapter;
import com.globusltd.recyclerview.util.ArrayPool;
import com.globusltd.recyclerview.util.Pool;

import java.util.Map;

@RestrictTo(RestrictTo.Scope.LIBRARY)
@MainThread
public class ItemClickBehavior<E> implements ViewHolderBehavior {

    @NonNull
    private final Adapter<E, ?> mAdapter;

    @NonNull
    private final Pool<ViewHolderClickInfo> mPool;

    @NonNull
    private final Map<RecyclerView.ViewHolder, ViewHolderClickInfo> mActiveViewHolders;

    @Nullable
    private OnItemClickListener<E> mOnItemClickListener;

    @Nullable
    private OnItemLongClickListener<E> mOnItemLongClickListener;

    public ItemClickBehavior(@NonNull final Adapter<E, ?> adapter) {
        mAdapter = adapter;
        mPool = new ArrayPool<>(new ViewHolderClickInfoFactory());
        mActiveViewHolders = new ArrayMap<>();
    }

    /**
     * Register a callback to be invoked when an item in this adapter has
     * been clicked.
     *
     * @param listener the callback that will be invoked.
     */
    public void setOnItemClickListener(@Nullable final OnItemClickListener<E> listener) {
        mOnItemClickListener = listener;
    }

    /**
     * Register a callback to be invoked when an item in this adapter has
     * been long clicked.
     *
     * @param listener the callback that will be invoked.
     */
    public void setOnLongItemClickListener(@Nullable final OnItemLongClickListener<E> listener) {
        mOnItemLongClickListener = listener;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void attachViewHolder(@NonNull final RecyclerView.ViewHolder holder) {
        final int position = holder.getAdapterPosition();
        if (position > RecyclerView.NO_POSITION && position < mAdapter.getItemCount()) {
            final ViewHolderClickInfo clickInfo = mActiveViewHolders.containsKey(holder) ?
                    mActiveViewHolders.get(holder) : mPool.obtain();
            mActiveViewHolders.put(holder, clickInfo);

            final ClickableInfo clickableInfo = mAdapter.getClickableInfo(position);
            if (mAdapter.areAllItemsEnabled() && mAdapter.isEnabled(position)) {
                clickInfo.position = position;
                // TODO: set click listener
            } else {
                // TODO: reset click listeners
                clickInfo.position = RecyclerView.NO_POSITION;
            }
        }
    }

    /**
     * {@inheritDoc}
     */
    public void detachViewHolder(@NonNull final RecyclerView.ViewHolder holder) {
        final ViewHolderClickInfo clickInfo = mActiveViewHolders.remove(holder);
        if (clickInfo != null) {
            final ClickableInfo clickableInfo = mAdapter.getClickableInfo(clickInfo.position);
            // TODO: reset click listeners
            clickInfo.position = RecyclerView.NO_POSITION;
            mPool.recycle(clickInfo);
        }
    }

    private class ViewHolderClickInfoFactory implements Pool.Factory<ViewHolderClickInfo> {

        @NonNull
        @Override
        public ViewHolderClickInfo create() {
            return new ViewHolderClickInfo();
        }

    }

    private class ViewHolderClickInfo {

        @IntRange(from = RecyclerView.NO_POSITION)
        int position = RecyclerView.NO_POSITION;

    }

}
