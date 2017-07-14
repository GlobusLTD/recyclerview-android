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
package com.globusltd.recyclerview.datasource;

import android.support.annotation.IntRange;
import android.support.annotation.MainThread;
import android.support.annotation.NonNull;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Datasource implementation that uses {@link ArrayList} of the elements as
 * the underlying data storage.
 */
@MainThread
public class ListDatasource<E> implements Datasource<E> {
    
    @NonNull
    private final List<E> mItems;
    
    @NonNull
    private final DatasourceObservable mDatasourceObservable;
    
    public ListDatasource() {
        this(Collections.<E>emptyList());
    }
    
    public ListDatasource(@NonNull final List<? extends E> items) {
        mItems = new ArrayList<>(items);
        mDatasourceObservable = new DatasourceObservable();
    }
    
    /**
     * {@inheritDoc}
     */
    @NonNull
    @Override
    public E get(@IntRange(from = 0) final int position) {
        return mItems.get(position);
    }
    
    /**
     * Adds a data entity to the end.
     *
     * @param e a data entity
     */
    public void add(@NonNull final E e) {
        if (mItems.add(e)) {
            final int position = mItems.size() - 1;
            mDatasourceObservable.notifyItemRangeInserted(position, 1);
        }
    }
    
    /**
     * Adds a data entity to a given position.
     *
     * @param position an index in the data set.
     * @param e        a data entity.
     */
    public void add(@IntRange(from = 0) final int position, @NonNull final E e) {
        mItems.add(position, e);
        mDatasourceObservable.notifyItemRangeInserted(position, 1);
    }
    
    /**
     * Adds all data entities to the end.
     *
     * @param items a non-null {@link List} of data entities.
     */
    public void addAll(@NonNull final List<? extends E> items) {
        final int positionStart = mItems.size();
        if (mItems.addAll(items)) {
            final int itemCount = items.size();
            mDatasourceObservable.notifyItemRangeInserted(positionStart, itemCount);
        }
    }
    
    /**
     * Adds all data entities after the specified position.
     *
     * @param position position at which to insert the first element
     *                 from the specified collection.
     * @param items    a non-null {@link List} of data entities.
     */
    public void addAll(@IntRange(from = 0) final int position,
                       @NonNull final List<? extends E> items) {
        if (mItems.addAll(position, items)) {
            final int itemCount = items.size();
            mDatasourceObservable.notifyItemRangeInserted(position, itemCount);
        }
    }
    
    /**
     * Moves entity from one position to another.
     *
     * @param fromPosition an initial index.
     * @param toPosition   a new index.
     */
    public void move(@IntRange(from = 0) final int fromPosition,
                     @IntRange(from = 0) final int toPosition) {
        final E e = mItems.remove(fromPosition);
        mItems.add(toPosition, e);
        mDatasourceObservable.notifyItemMoved(fromPosition, toPosition);
    }
    
    /**
     * Removes entity at a given position.
     *
     * @param position an index in the list of entities.
     * @return removed entity.
     */
    @NonNull
    public E remove(@IntRange(from = 0) final int position) {
        final E e = mItems.remove(position);
        mDatasourceObservable.notifyItemRangeRemoved(position, 1);
        return e;
    }
    
    /**
     * Removes a range of elements.
     *
     * @param fromPosition Position of the first item that be removed.
     * @param itemCount    Number of items removed from the data set.
     */
    public void removeRange(@IntRange(from = 0) final int fromPosition,
                            @IntRange(from = 1) final int itemCount) {
        for (int position = fromPosition + itemCount - 1; position >= fromPosition; position--) {
            mItems.remove(position);
        }
        mDatasourceObservable.notifyItemRangeRemoved(fromPosition, itemCount);
    }
    
    /**
     * Removes all of the elements from this datastore.
     * The datastore will be empty after this call returns.
     */
    public void clear() {
        final int itemCount = mItems.size();
        mItems.clear();
        mDatasourceObservable.notifyItemRangeRemoved(0, itemCount);
    }
    
    /**
     * {@inheritDoc}
     */
    @Override
    public int size() {
        return mItems.size();
    }
    
    /**
     * {@inheritDoc}
     */
    @Override
    public void registerDatasourceObserver(@NonNull final DatasourceObserver observer) {
        mDatasourceObservable.registerObserver(observer);
    }
    
    /**
     * {@inheritDoc}
     */
    @Override
    public void unregisterDatasourceObserver(@NonNull final DatasourceObserver observer) {
        mDatasourceObservable.unregisterObserver(observer);
    }
    
}
