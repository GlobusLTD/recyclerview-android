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
import android.view.View;

/**
 * Simple data class that keeps identifiers of clickable views.
 */
public class ClickableViews {

    /**
     * Used to mark a view that has no ID.
     */
    public static final int NO_ID = View.NO_ID;

    /**
     * Use this to describe {@link android.support.v7.widget.RecyclerView.ViewHolder#itemView}.
     */
    public static final int ITEM_VIEW_ID = Integer.MIN_VALUE;

    private static final int[] EMPTY_ARRAY = new int[0];

    /**
     * Default clickable views info that makes no view clickable.
     */
    public static final ClickableViews NONE = new ClickableViews(NO_ID);

    /**
     * Default clickable views info that makes clickable only
     * {@link android.support.v7.widget.RecyclerView.ViewHolder#itemView}.
     */
    public static final ClickableViews ITEM_VIEW = new ClickableViews(ITEM_VIEW_ID);

    @IdRes
    private final int mDefaultViewId;

    @IdRes
    @NonNull
    private final int[] mClickableViewIds;

    public ClickableViews(@IdRes final int defaultViewId) {
        this(defaultViewId, EMPTY_ARRAY);
    }

    public ClickableViews(@IdRes final int defaultViewId,
                          @IdRes @NonNull final int... clickableViewIds) {
        mDefaultViewId = defaultViewId;
        mClickableViewIds = clickableViewIds;
    }

    /**
     * Returns identifier of default item view.
     * Use {@link #ITEM_VIEW_ID} to make root view clickable or
     * {@link #NO_ID} to make no view clickable.
     *
     * @return An identifier of view, or {@link #ITEM_VIEW_ID} or {@link #NO_ID}.
     */
    @IdRes
    int getDefaultViewId() {
        return mDefaultViewId;
    }

    /**
     * Returns array of clickable views ids at a given position.
     *
     * @return an array of view ids.
     */
    @IdRes
    @NonNull
    int[] getClickableViewIds() {
        return mClickableViewIds;
    }

}