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

import com.globusltd.recyclerview.view.ClickableViews;

public interface ClickableAdapter<E> extends ParameterizedAdapter<E> {

    /**
     * Returns true if the item at the specified position is clickable.
     * <p/>
     * The result is unspecified if position is invalid. An {@link IndexOutOfBoundsException}
     * should be thrown in that case for fast failure.
     *
     * @param position an index of the item.
     * @return {@code true} if item is enabled, {@code false} otherwise.
     */
    boolean isEnabled(/*@IntRange(from = 0) */ final int position);

    /**
     * Returns information about all of the clickable views at specified position.
     * Note that it's better to preallocate ClickableViews instance for each view type
     * and enable/disable clicks by providing enabled flag via {@link #isEnabled(int)}.
     *
     * @param position an index of the item.
     * @return A {@link ClickableViews} instance.
     * @see #isEnabled(int)
     */
    @NonNull
    ClickableViews getClickableViews(/*@IntRange(from = 0) */ final int position, final int viewType);

}
