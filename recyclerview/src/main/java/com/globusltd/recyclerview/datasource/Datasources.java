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

import com.globusltd.recyclerview.Datasource;
import com.globusltd.recyclerview.DatasourceObserver;

public class Datasources {

    private static final Datasource EMPTY_DATASOURCE = new EmptyDatasource<>();

    /**
     * Returns an empty datasource (immutable).
     *
     * <p>This example illustrates the type-safe way to obtain an empty datasource:
     * <pre>
     *     Datasource&lt;String&gt; s = Datasources.empty();
     * </pre>
     * Implementation note: Implementations of this method need not
     * create a separate <tt>Datasource</tt> object for each call. Using this
     * method is likely to have comparable cost to using the like-named
     * field. (Unlike this method, the field does not provide type safety.)
     *
     * @param <T> type of elements, if there were any, in the datasource.
     * @return an empty immutable datasource.
     *
     * @see #EMPTY_DATASOURCE
     */
    @SuppressWarnings("unchecked")
    public static <T> Datasource<T> empty() {
        return (Datasource<T>) EMPTY_DATASOURCE;
    }

    private static class EmptyDatasource<E> implements Datasource<E> {

        /**
         * {@inheritDoc}
         */
        @NonNull
        @Override
        public E get(@IntRange(from = 0) final int position) {
            throw new IndexOutOfBoundsException("Datasource has no element at position=" + position);
        }

        /**
         * {@inheritDoc}
         */
        @Override
        public int size() {
            return 0;
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

    private Datasources() {
        // No instances
    }

}
