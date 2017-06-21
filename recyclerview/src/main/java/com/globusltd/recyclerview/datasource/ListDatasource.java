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

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Datasource implementation that uses {@link List} of elements as
 * the underlying data storage.
 */
@MainThread
public class ListDatasource<E> implements ModifiableDatasource<E> {
    
    @NonNull
    private final List<E> mItems;
    
    public ListDatasource() {
        this(Collections.<E>emptyList());
    }
    
    public ListDatasource(@NonNull final List<E> items) {
        mItems = new ArrayList<>(items);
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
     * {@inheritDoc}
     */
    @Override
    public void add(@NonNull final E e) {
        mItems.add(e);
    }
    
    /**
     * {@inheritDoc}
     */
    @Override
    public void add(@IntRange(from = 0) final int position, @NonNull final E e) {
        mItems.add(position, e);
    }
    
    /**
     * {@inheritDoc}
     */
    @Override
    public void addAll(@NonNull final Datasource<? extends E> datasource) {
        final int position = mItems.size();
        addAll(position, datasource);
    }
    
    /**
     * {@inheritDoc}
     */
    @Override
    public void addAll(@IntRange(from = 0) final int position,
                       @NonNull final Datasource<? extends E> datasource) {
        final int itemCount = datasource.size();
        for (int i = 0; i < itemCount; i++) {
            final int toPosition = position + i;
            final E item = datasource.get(position);
            mItems.add(toPosition, item);
        }
    }
    
    /**
     * {@inheritDoc}
     */
    @Override
    public void move(@IntRange(from = 0) final int fromPosition,
                     @IntRange(from = 0) final int toPosition) {
        final E item = mItems.remove(fromPosition);
        mItems.add(toPosition, item);
    }
    
    /**
     * {@inheritDoc}
     */
    @NonNull
    @Override
    public E remove(@IntRange(from = 0) final int position) {
        return mItems.remove(position);
    }
    
    /**
     * {@inheritDoc}
     */
    @Override
    public void removeRange(@IntRange(from = 0) final int fromPosition,
                            @IntRange(from = 0) final int itemCount) {
        for (int position = fromPosition + itemCount - 1; position >= fromPosition; position--) {
            mItems.remove(position);
        }
    }
    
    /**
     * {@inheritDoc}
     */
    @Override
    public void clear() {
        mItems.clear();
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
    public void close() throws IOException {
        // Do nothing
    }
    
}
