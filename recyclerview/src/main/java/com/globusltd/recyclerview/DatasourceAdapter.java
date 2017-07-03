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
import android.support.annotation.NonNull;

/**
 * Interface declares methods for a datasource based adapter.
 * <p>
 * Adapters provide a binding from an app-specific data set to views
 * that are displayed within a {@link android.support.v7.widget.RecyclerView}.
 */
public interface DatasourceAdapter<E> {
    
    /**
     * Returns an {@link Datasource} instance.
     *
     * @return An {@link Datasource} instance.
     */
    @NonNull
    Datasource<? extends E> getDatasource();
    
    /**
     * Returns the total number of items in the datasource held by the adapter.
     *
     * @return The total number of items in this adapter.
     */
    @IntRange(from = 0)
    int getItemCount();
    
    /**
     * Return the view type of the item at <code>position</code> for the purposes
     * of view recycling.
     * <p>
     * <p>The default implementation of this method returns 0, making the assumption of
     * a single view type for the adapter. Unlike ListView adapters, types need not
     * be contiguous. Consider using id resources to uniquely identify item view types.
     *
     * @param position position to query
     * @return integer value identifying the type of the view needed to represent the item at
     * <code>position</code>. Type codes need not be contiguous.
     */
    int getItemViewType(@IntRange(from = 0) final int position);
    
}