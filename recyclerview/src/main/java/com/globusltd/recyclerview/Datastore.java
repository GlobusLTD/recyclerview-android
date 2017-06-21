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
import com.globusltd.recyclerview.datasource.ListDatasource;
import com.globusltd.recyclerview.datasource.ModifiableDatasource;
import com.globusltd.recyclerview.diff.DiffCallback;
import com.globusltd.recyclerview.diff.DiffCallbackFactory;

import java.io.IOException;

/**
 * Default observable datastore that handles data diffs and data change events.
 */
@MainThread
public class Datastore<E> implements ModifiableDatasource<E> {
    
    @Nullable
    private final DiffCallbackFactory<E> mDiffCallbackFactory;
    
    @NonNull
    private final ListUpdateCallback mListUpdateCallback;
    
    @NonNull
    private Datasource<? extends E> mDatasource;
    
    public Datastore(@Nullable final DiffCallbackFactory<E> diffCallbackFactory,
                     @NonNull final ListUpdateCallback listUpdateCallback) {
        mDiffCallbackFactory = diffCallbackFactory;
        mListUpdateCallback = listUpdateCallback;
        mDatasource = new ListDatasource<>();
    }
    
    @NonNull
    public E get(@IntRange(from = 0) final int index) {
        return mDatasource.get(index);
    }
    
    public void swap(@NonNull final Datasource<? extends E> datasource) throws IOException {
        final int itemCount = datasource.size();
        if (mDatasource.size() == 0) {
            mDatasource.close();
            mDatasource = datasource;
            mListUpdateCallback.onInserted(0, itemCount);
            
        } else if (mDiffCallbackFactory != null) {
            final DiffCallback diffCallback = mDiffCallbackFactory
                    .createDiffCallback(mDatasource, datasource);
            mDatasource.close();
            mDatasource = datasource;
            DiffUtil.calculateDiff(diffCallback, diffCallback.shouldDetectMoves())
                    .dispatchUpdatesTo(mListUpdateCallback);
            
        } else {
            mDatasource.close();
            mDatasource = datasource;
            mListUpdateCallback.onChanged(0, itemCount, null);
        }
    }
    
    @SuppressWarnings("unchecked")
    public void add(@NonNull final E e) {
        final int position = mDatasource.size();
        add(position, e);
    }
    
    @SuppressWarnings("unchecked")
    public void add(@IntRange(from = 0) final int position, @NonNull final E e) {
        if (mDatasource instanceof ModifiableDatasource) {
            ((ModifiableDatasource<E>) mDatasource).add(position, e);
            mListUpdateCallback.onInserted(position, 1);
            
        } else {
            throw new UnsupportedOperationException("Datasource instance does not support adding elements. " +
                    "Please provide instance of ModifiableDatasource via calling Datastore#swap.");
        }
    }
    
    public void addAll(@NonNull final Datasource<? extends E> datasource) throws IOException {
        final int position = mDatasource.size();
        addAll(position, datasource);
    }
    
    @SuppressWarnings("unchecked")
    public void addAll(@IntRange(from = 0) final int position,
                       @NonNull final Datasource<? extends E> datasource) throws IOException {
        if (mDatasource instanceof ModifiableDatasource) {
            final int itemCount = datasource.size();
            ((ModifiableDatasource<E>) mDatasource).addAll(position, datasource);
            mDatasource.close();
            mListUpdateCallback.onInserted(position, itemCount);
            
        } else {
            throw new UnsupportedOperationException("Datasource instance does not support adding elements. " +
                    "Please provide instance of ModifiableDatasource via calling Datastore#swap.");
        }
    }
    
    @SuppressWarnings("unchecked")
    public void move(@IntRange(from = 0) final int fromPosition,
                     @IntRange(from = 0) final int toPosition) {
        if (mDatasource instanceof ModifiableDatasource) {
            ((ModifiableDatasource<E>) mDatasource).move(fromPosition, toPosition);
            mListUpdateCallback.onMoved(fromPosition, toPosition);
            
        } else {
            throw new UnsupportedOperationException("Datasource instance does not support moving elements. " +
                    "Please provide instance of ModifiableDatasource via calling Datastore#swap.");
        }
    }
    
    @SuppressWarnings("unchecked")
    @NonNull
    public E remove(@IntRange(from = 0) final int position) {
        if (mDatasource instanceof ModifiableDatasource) {
            final E e = ((ModifiableDatasource<E>) mDatasource).remove(position);
            mListUpdateCallback.onRemoved(position, 1);
            return e;
        } else {
            throw new UnsupportedOperationException("Datasource instance does not support removing elements. " +
                    "Please provide instance of ModifiableDatasource via calling Datastore#swap.");
        }
    }
    
    @SuppressWarnings("unchecked")
    public void removeRange(@IntRange(from = 0) final int fromPosition,
                            @IntRange(from = 0) final int itemCount) {
        if (mDatasource instanceof ModifiableDatasource) {
            ((ModifiableDatasource<E>) mDatasource).removeRange(fromPosition, itemCount);
            mListUpdateCallback.onRemoved(fromPosition, itemCount);
        } else {
            throw new UnsupportedOperationException("Datasource instance does not support removing elements. " +
                    "Please provide instance of ModifiableDatasource via calling Datastore#swap.");
        }
    }
    
    public int size() {
        return mDatasource.size();
    }
    
    @Override
    public void close() throws IOException {
        mDatasource.close();
    }
    
}
