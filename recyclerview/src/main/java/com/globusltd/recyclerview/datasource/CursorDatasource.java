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

import android.database.Cursor;
import android.database.MergeCursor;
import android.support.annotation.IntRange;
import android.support.annotation.MainThread;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import java.io.IOException;

/**
 * Datasource implementation that uses {@link Cursor} as the underlying data storage.
 * <p>
 * Note that {@link CursorDatasource} does not allow to merge cursors,
 * but you are able to create that datasource with {@link MergeCursor} instance
 * when you want to merge a few cursors to the new one.
 *
 * @see Cursor
 * @see MergeCursor
 */
@MainThread
public class CursorDatasource implements Datasource<Cursor> {
    
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
    public void close() throws IOException {
        if (mCursor != null) {
            mCursor.close();
        }
    }
    
}
