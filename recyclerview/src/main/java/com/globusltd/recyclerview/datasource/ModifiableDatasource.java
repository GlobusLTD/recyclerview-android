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
import android.support.annotation.NonNull;

import java.io.IOException;

/**
 * Datasource that supports modifications of the dataset.
 *
 * @param <E> Type of elements handled by datastore.
 */
public interface ModifiableDatasource<E> extends Datasource<E> {
    
    /**
     * Adds a data entity to the end.
     *
     * @param e a data entity
     */
    void add(@NonNull final E e);
    
    /**
     * Adds a data entity to a given position.
     *
     * @param position an index in the data set.
     * @param e        a data entity.
     */
    void add(@IntRange(from = 0) final int position, @NonNull final E e);
    
    /**
     * Adds all data entities to the end and closes input datasource.
     *
     * @param items a non-null {@link Datasource} of data entities.
     *
     * @throws IOException if an I/O error occurs.
     */
    void addAll(@NonNull final Datasource<? extends E> items) throws IOException;
    
    /**
     * Adds all data entities after the specified position and closes input datasource.
     *
     * @param items a non-null {@link Datasource} of data entities.
     */
    void addAll(@IntRange(from = 0) final int position,
                @NonNull final Datasource<? extends E> items) throws IOException;
    
    /**
     * Moves entity from one position to another.
     *
     * @param fromPosition an initial index.
     * @param toPosition   a new index.
     */
    void move(@IntRange(from = 0) final int fromPosition,
              @IntRange(from = 0) final int toPosition);
    
    /**
     * Removes entity at a given position.
     *
     * @param position an index in the list of entities.
     * @return removed entity.
     */
    @NonNull
    E remove(@IntRange(from = 0) final int position);
    
    /**
     * Removes a range of elements.
     *
     * @param fromPosition Position of the first item that be removed.
     * @param itemCount    Number of items removed from the data set.
     */
    void removeRange(@IntRange(from = 0) final int fromPosition,
                     @IntRange(from = 0) final int itemCount);
    
}
