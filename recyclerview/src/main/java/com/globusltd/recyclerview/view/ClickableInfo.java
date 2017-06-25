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
package com.globusltd.recyclerview.view;

import android.support.annotation.IdRes;
import android.support.annotation.NonNull;

public class ClickableInfo {

    public static final ClickableInfo NO_INFO = new ClickableInfo(/* TODO: Default id */ -1);

    private static final int[] EMPTY_ARRAY = new int[0];

    @IdRes
    private final int mDefaultClickableViewId;

    @IdRes
    @NonNull
    private final int[] mClickableViewIds;

    public ClickableInfo(@IdRes final int defaultClickableViewId) {
        this(defaultClickableViewId, EMPTY_ARRAY);
    }

    public ClickableInfo(@IdRes final int defaultClickableViewId,
                         @IdRes @NonNull final int[] clickableViewIds) {
        mDefaultClickableViewId = defaultClickableViewId;
        mClickableViewIds = clickableViewIds;
    }

    /**
     * Returns identifier of default item view.
     * Use {@link DEFAULT_CLICKABLE_VIEW_ID} to make root view clickable.
     *
     * @return An identifier of view or {@link DEFAULT_CLICKABLE_VIEW_ID}
     */
    @IdRes
    public int getDefaultClickableViewId() {
        return mDefaultClickableViewId;
    }

    /**
     * Returns array of clickable views ids at a given position.
     * Use {@link #EMPTY_ARRAY} to make root view clickable.
     *
     * @return an array of view ids. Use {@link #EMPTY_ARRAY}
     * if only root view should be clickable.
     */
    @IdRes
    @NonNull
    public int[] getClickableViewIds() {
        return mClickableViewIds;
    }

}
