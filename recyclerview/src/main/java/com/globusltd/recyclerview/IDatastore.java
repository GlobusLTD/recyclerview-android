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

import java.util.List;

/**
 * IDatastore stores elements.
 *
 * @param <E> Type of elements handled by datastore.
 */
@MainThread
@Deprecated
interface IDatastore<E> {

    /**
     * Returns the element at the specified position.
     *
     * @param index index of the element to return.
     * @return the element at the specified position.
     * @throws IndexOutOfBoundsException if the index is out of range
     *                                   (<tt>index &lt; 0 || index &gt;= size()</tt>)
     */
    E get(@IntRange(from = 0) final int index);

    /**
     * Returns the unmodifiable list of elements stored in this datastore.
     *
     * @return Unmodifiable list of the elements stored in this datastore.
     */
    //@NonNull
    //S getAll();

    /**
     * Replace elements stored in this datastore by new elements.
     *
     * @param items a new elements.
     */
    //void swap(@Nullable final S items);

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
     * Adds all data entities to the end.
     *
     * @param items a non-null {@link List} of data entities.
     */
    void addAll(@NonNull final List<E> items);

    /**
     * Adds all data entities after the specified position.
     *
     * @param items a non-null {@link List} of data entities.
     */
    void addAll(@IntRange(from = 0) final int position, @NonNull final List<E> items);

    /**
     * Moves entity from one position to another.
     *
     * @param fromPosition an initial index.
     * @param toPosition   a new index.
     */
    void move(@IntRange(from = 0) final int fromPosition, @IntRange(from = 0) final int toPosition);

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
    void removeRange(@IntRange(from = 0) final int fromPosition, @IntRange(from = 0) final int itemCount);

    /**
     * Removes all entities.
     */
    void clear();

    /**
     * Returns the number of elements in this datastore. If this list contains
     * more than <tt>Integer.MAX_VALUE</tt> elements, returns <tt>Integer.MAX_VALUE</tt>.
     *
     * @return The number of elements in this datastore.
     */
    int size();

}
