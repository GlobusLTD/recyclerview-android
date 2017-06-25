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
package com.globusltd.recyclerview.util;

import android.support.annotation.NonNull;

/**
 * Interface for managing a pool of objects.
 *
 * @param <T> The pooled type.
 */
public interface Pool<T> {

    /**
     * The pooled object factory.
     *
     * @param <T> The pooled type.
     */
    interface Factory<T> {

        /**
         * Creates a new object if pool has no free objects.
         *
         * @return Created object.
         */
        @NonNull
        T create();

    }

    /**
     * @return An instance from the pool.
     */
    @NonNull
    T obtain();

    /**
     * Release an instance to the pool.
     *
     * @param instance The instance to release.
     */
    void recycle(@NonNull final T instance);

}