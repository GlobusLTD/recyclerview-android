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

import android.app.Activity;
import android.database.Cursor;
import android.database.MergeCursor;
import android.support.annotation.IntRange;
import android.support.annotation.MainThread;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;

import java.io.Closeable;

/**
 * Datasource implementation that uses {@link Cursor} as the underlying data storage.
 * <p>
 * Note that {@link CursorDatasource} does not allow to merge cursors,
 * but you are able to create that datasource with {@link MergeCursor} instance
 * when you want to merge a few cursors to the new one.
 * <p>
 * {@link CursorDatasource} implements {@link Closeable} interface to release
 * the underlaying cursor instance. Make sure you close this datasource when
 * you don't need its data anymore, for example, when {@link Activity#onDestroy()} or
 * {@link Fragment#onDestroyView()} are called.
 * <p>This example illustrates the one of possible ways to release a datasource:
 * <pre>
 *     @Override
 *     public void onDestroy() {
 *         super.onDestroy();
 *
 *         // Unbind all active view holders
 *         mRecyclerView.setAdapter(null);
 *
 *         // Unbind datasource from the adapter and release resources
 *         Datasource&lt;MyObject&gt; oldDatasource = mAdapter.swap(Datasources.empty());
 *         if (oldDatasource instanceof Closeable) {
 *             ((Closeable) oldDatasource).close();
 *         }
 *     }
 * </pre>
 *
 * @see Cursor
 * @see MergeCursor
 */
@MainThread
public class CursorDatasource implements Datasource<Cursor>, Closeable {
    
    @Nullable
    private final Cursor mCursor;
    
    public CursorDatasource(@Nullable final Cursor cursor) {
        mCursor = cursor;
    }
    
    /**
     * {@inheritDoc}
     */
    @NonNull
    @Override
    public Cursor get(@IntRange(from = 0) final int position) {
        if (mCursor != null) {
            mCursor.moveToPosition(position);
            return mCursor;
        }
        throw new IndexOutOfBoundsException("The underlying cursor is null");
    }
    
    /**
     * {@inheritDoc}
     */
    @Override
    public int size() {
        return (mCursor != null ? mCursor.getCount() : 0);
    }
    
    /**
     * {@inheritDoc}
     */
    @Override
    public void close() {
        if (mCursor != null) {
            mCursor.close();
        }
    }
    
    /**
     * {@inheritDoc}
     */
    @Override
    public void registerDatasourceObserver(@NonNull final DatasourceObserver observer) {
        // Do nothing
    }
    
    /**
     * {@inheritDoc}
     */
    @Override
    public void unregisterDatasourceObserver(@NonNull final DatasourceObserver observer) {
        // Do nothing
    }
    
}
