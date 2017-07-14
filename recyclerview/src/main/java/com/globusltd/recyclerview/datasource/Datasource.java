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

/**
 * Datasource is an abstraction over elements storage.
 *
 * @param <E> Type of elements handled by datastore.
 */
@MainThread
public interface Datasource<E> {

    /**
     * Returns the element at the specified position.
     *
     * @param position position of the element to return.
     * @return the element at the specified position.
     * @throws IndexOutOfBoundsException if the index is out of range
     *                                   (<tt>index &lt; 0 || index &gt;= size()</tt>)
     */
    @NonNull
    E get(@IntRange(from = 0) final int position);

    /**
     * Returns the number of elements in this datastore. If this list contains
     * more than <tt>Integer.MAX_VALUE</tt> elements, returns <tt>Integer.MAX_VALUE</tt>.
     *
     * @return The number of elements in this datastore.
     */
    int size();

    /**
     * Register a new observer to listen for data changes.
     * <p>
     * <p>The datasource may publish a variety of events describing specific changes.
     * Not all datasource may support all change types and some may fall back to a generic
     * {@link DatasourceObserver#onChanged()} "something changed" event
     * if more specific data is not available.</p>
     * <p>
     * <p>Components registering observers with a datasource are responsible for
     * {@link #unregisterDatasourceObserver(DatasourceObserver)
     * unregistering} those observers when finished.</p>
     *
     * @param observer Observer to register.
     * @see #unregisterDatasourceObserver(DatasourceObserver)
     */
    void registerDatasourceObserver(@NonNull final DatasourceObserver observer);

    /**
     * Unregister an observer currently listening for data changes.
     * <p>
     * <p>The unregistered observer will no longer receive events about changes
     * to the datasource.</p>
     *
     * @param observer Observer to unregister.
     * @see #registerDatasourceObserver(DatasourceObserver)
     */
    void unregisterDatasourceObserver(@NonNull final DatasourceObserver observer);

}
