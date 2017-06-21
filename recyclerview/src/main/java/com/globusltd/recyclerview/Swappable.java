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

import android.support.annotation.MainThread;
import android.support.annotation.NonNull;

/**
 * Interface describes component that supports replacing internal components
 * without interruption to the system.
 *
 * @param <E> Type of elements handled by component.
 */
@MainThread
public interface Swappable<E> {

    /**
     * Performs swapping the internal components.
     *
     * @param component The new component that should replace the old one.
     */
    void swap(@NonNull final E component);

}