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
import android.support.v7.util.DiffUtil;
import android.support.v7.util.ListUpdateCallback;

import com.globusltd.recyclerview.datasource.Datasource;
import com.globusltd.recyclerview.diff.DiffCallback;
import com.globusltd.recyclerview.diff.DiffCallbackFactory;

/**
 * Default observable datastore that handles data diffs and data change events.
 */
@MainThread
public class Datastore<E> {

    @NonNull
    private final Datasource<? extends E> mDatasource;

    @Nullable
    private final DiffCallbackFactory<? extends E> mDiffCallbackFactory;

    @NonNull
    private final ListUpdateCallback mListUpdateCallback;

    public Datastore(@Nullable final DiffCallbackFactory<? extends E> diffCallbackFactory,
                     @NonNull final ListUpdateCallback listUpdateCallback) {
        mDatasource = null; // TODO: initialize datasource
        mDiffCallbackFactory = diffCallbackFactory;
        mListUpdateCallback = listUpdateCallback;
    }

    public E get(@IntRange(from = 0) final int index) {
        return mDatasource.get(index);
    }

    /*public void swap(@Nullable final List<E> items) {
        final List<E> notNullableItems = (items != null ? items : Collections.<E>emptyList());
        if (mItems.isEmpty()) {
            addAll(notNullableItems);

        } else if (mDiffCallbackFactory != null) {
            final DiffCallback diffCallback = mDiffCallbackFactory
                    .createDiffCallback(mItems, notNullableItems);

            mItems.clear();
            mItems.addAll(notNullableItems);

            DiffUtil.calculateDiff(diffCallback, diffCallback.shouldDetectMoves())
                    .dispatchUpdatesTo(mListUpdateCallback);

        } else {
            mItems.clear();
            mItems.addAll(notNullableItems);
            final int itemCount = mItems.size();
            mListUpdateCallback.onChanged(0, itemCount, /* payload */ /*null);
        }
    }

    public void add(@NonNull final E e) {
        if (mItems.add(e)) {
            final int position = mItems.size() - 1;
            mListUpdateCallback.onInserted(position, 1);
        }
    }

    public void add(@IntRange(from = 0) final int position, @NonNull final E e) {
        mItems.add(position, e);
        mListUpdateCallback.onInserted(position, 1);
    }

    public void addAll(@NonNull final List<E> items) {
        final int positionStart = mItems.size();
        if (mItems.addAll(items)) {
            final int itemCount = items.size();
            mListUpdateCallback.onInserted(positionStart, itemCount);
        }
    }

    public void addAll(@IntRange(from = 0) final int position, @NonNull final List<E> items) {
        if (mItems.addAll(position, items)) {
            final int itemCount = items.size();
            mListUpdateCallback.onInserted(position, itemCount);
        }
    }

    public void move(@IntRange(from = 0) final int fromPosition,
                     @IntRange(from = 0) final int toPosition) {
        final E e = mItems.remove(fromPosition);
        mItems.add(toPosition, e);
        mListUpdateCallback.onMoved(fromPosition, toPosition);
    }

    @NonNull
    public E remove(@IntRange(from = 0) final int position) {
        final E e = mItems.remove(position);
        mListUpdateCallback.onRemoved(position, 1);
        return e;
    }

    public void removeRange(@IntRange(from = 0) final int fromPosition,
                            @IntRange(from = 0) final int itemCount) {
        for (int position = fromPosition + itemCount - 1; position >= fromPosition; position--) {
            mItems.remove(position);
        }
        mListUpdateCallback.onRemoved(fromPosition, itemCount);
    }

    public void clear() {
        final int itemCount = mItems.size();
        mItems.clear();
        mListUpdateCallback.onRemoved(0, itemCount);
    }*/

    public int size() {
        return mDatasource.size();
    }

}
