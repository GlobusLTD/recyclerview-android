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
package com.globusltd.recyclerview.diff;

import android.support.annotation.NonNull;

import com.globusltd.recyclerview.datasource.Datasource;

/**
 * Factory that allows to create {@link DiffCallback} for two {@link Datasource}s.
 *
 * @param <E> The type of an item that will be handled.
 */
public interface DiffCallbackFactory<E> {

    @NonNull
    DiffCallback createDiffCallback(@NonNull final Datasource<? extends E> oldDatasource,
                                    @NonNull final Datasource<? extends E> newDatasource);

}