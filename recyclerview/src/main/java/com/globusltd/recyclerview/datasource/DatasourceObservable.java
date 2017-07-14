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
import android.support.annotation.Nullable;

import com.globusltd.recyclerview.util.Observable;

/**
 * {@link DatasourceObservable} provides methods for registering, unregistering
 * and dispatching data changes in the datasources to the registered {@link DatasourceObserver}s.
 */
@MainThread
public class DatasourceObservable extends Observable<DatasourceObserver> {
    
    public DatasourceObservable() {
        super();
    }
    
    /**
     * Notifies the registered observers that the data in the datasource have been changed.
     * Note that method can be called multiple times.
     */
    public void notifyChanged() {
        final int size = mObservers.size();
        for (int i = size - 1; i >= 0; i--) {
            mObservers.get(i).onChanged();
        }
    }
    
    /**
     * Notifies the registered observers that the data in the datasource have been changed.
     * Note that method can be called multiple times.
     *
     * @param positionStart Position of the first data item that has changed.
     * @param itemCount     Number of the data items that have changed.
     * @param payload       Optional parameter, use null to identify a "full" update.
     */
    public void notifyItemRangeChanged(@IntRange(from = 0) final int positionStart,
                                       @IntRange(from = 0) final int itemCount,
                                       @Nullable final Object payload) {
        final int size = mObservers.size();
        for (int i = size - 1; i >= 0; i--) {
            mObservers.get(i).onItemRangeChanged(positionStart, itemCount, payload);
        }
    }
    
    /**
     * Notifies the registered observers that the new data have been inserted to the datasource.
     * Note that method can be called multiple times.
     *
     * @param positionStart Position of the first data item that was inserted.
     * @param itemCount     Number of the data items inserted.
     */
    public void notifyItemRangeInserted(@IntRange(from = 0) final int positionStart,
                                        @IntRange(from = 0) final int itemCount) {
        final int size = mObservers.size();
        for (int i = size - 1; i >= 0; i--) {
            mObservers.get(i).onItemRangeInserted(positionStart, itemCount);
        }
    }
    
    /**
     * Notifies the registered observers that the data have been removed from the datasource.
     * Note that method can be called multiple times.
     *
     * @param positionStart Position of the first data item that was removed.
     * @param itemCount     Number of the data items removed.
     */
    public void notifyItemRangeRemoved(@IntRange(from = 0) final int positionStart,
                                       @IntRange(from = 0) final int itemCount) {
        final int size = mObservers.size();
        for (int i = size - 1; i >= 0; i--) {
            mObservers.get(i).onItemRangeRemoved(positionStart, itemCount);
        }
    }
    
    /**
     * Notifies the registered observers that the data have been moved to the another position
     * in the datasource.
     * Note that method can be called multiple times.
     *
     * @param fromPosition Previous position of the data item.
     * @param toPosition   New position of the data item.
     */
    public void notifyItemMoved(@IntRange(from = 0) final int fromPosition,
                                @IntRange(from = 0) final int toPosition) {
        final int size = mObservers.size();
        for (int i = size - 1; i >= 0; i--) {
            mObservers.get(i).onItemMoved(fromPosition, toPosition);
        }
    }
    
}
