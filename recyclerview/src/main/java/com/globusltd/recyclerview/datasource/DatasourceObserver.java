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

/**
 * Observer base class for watching changes to a {@link Datasource}.
 * See {@link Datasource#registerDatasourceObserver(DatasourceObserver)}.
 */
@MainThread
public abstract class DatasourceObserver {
    
    /**
     * Called when the data in the datasource have been changed.
     */
    public void onChanged() {
        // Do nothing
    }
    
    /**
     * Called when the data in the datasource have been changed.
     * Note that method can be called multiple times.
     *
     * @param positionStart Position of the first data item that has changed.
     * @param itemCount Number of the data items that have changed.
     * @param payload Optional parameter, use null to identify a "full" update.
     */
    public void onItemRangeChanged(@IntRange(from = 0) final int positionStart,
                                   @IntRange(from = 0) final int itemCount,
                                   @Nullable final Object payload) {
        // Do nothing
    }
    
    /**
     * Called when the new data have been inserted to the datasource.
     * Note that method can be called multiple times.
     *
     * @param positionStart Position of the first data item that was inserted.
     * @param itemCount Number of the data items inserted.
     */
    public void onItemRangeInserted(@IntRange(from = 0) final int positionStart,
                                    @IntRange(from = 0) final int itemCount) {
        // Do nothing
    }
    
    /**
     * Called when the new data have been removed from the datasource.
     * Note that method can be called multiple times.
     *
     * @param positionStart Position of the first data item that was removed.
     * @param itemCount Number of the data items removed.
     */
    public void onItemRangeRemoved(@IntRange(from = 0) final int positionStart,
                                   @IntRange(from = 0) final int itemCount) {
        // Do nothing
    }
    
    /**
     * Called when the data have been moved to the another position in the datasource.
     * Note that method can be called multiple times.
     *
     * @param fromPosition Previous position of the data item.
     * @param toPosition New position of the data item.
     */
    public void onItemMoved(@IntRange(from = 0) final int fromPosition,
                            @IntRange(from = 0) final int toPosition) {
        // Do nothing
    }

}