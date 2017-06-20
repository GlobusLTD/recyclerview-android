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
import android.view.View;

/**
 * Interface definition for a callback to be invoked when an item in this
 * AdapterView has been clicked and held.
 */
@MainThread
public interface OnItemLongClickListener<E> {

    /**
     * Callback method to be invoked when an item in this AdapterView has
     * been clicked and held.
     *
     * @param view     the view that was clicked and held (this will be a view provided by the adapter).
     * @param item     the data entity that was clicked and held.
     * @param position the position of the data entity in the adapter.
     * @return true if the callback consumed the long click, false otherwise.
     */
    boolean onItemLongClick(@NonNull final View view, final E item, final int position);

}
