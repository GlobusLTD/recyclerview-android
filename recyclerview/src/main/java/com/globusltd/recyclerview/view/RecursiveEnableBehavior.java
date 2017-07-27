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

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;

/**
 * This is a class to recursively enable or disable {@link RecyclerView.ViewHolder}'s item view
 * and its child views according to enabled state returned by {@link Callback#isEnabled(int)}.
 */
public class RecursiveEnableBehavior extends SimpleEnableBehavior {

    public RecursiveEnableBehavior(@NonNull final Callback callback) {
        super(callback);
    }

    /**
     * Recursively applies enabled state to the {@link RecyclerView.ViewHolder#itemView} and
     * its child views.
     *
     * @param viewHolder {@link RecyclerView.ViewHolder} instance.
     * @param enabled    {@code true} if view should be enabled, {@code false} otherwise.
     */
    @Override
    protected void onEnabledChanged(@NonNull final RecyclerView.ViewHolder viewHolder,
                                    final boolean enabled) {
        setEnabledRecursively(viewHolder.itemView, enabled);
    }

    private void setEnabledRecursively(@NonNull final View view, final boolean enabled) {
        view.setEnabled(enabled);

        if (view instanceof ViewGroup) {
            final ViewGroup viewGroup = (ViewGroup) view;
            final int childCount = viewGroup.getChildCount();
            for (int index = 0; index < childCount; index++) {
                final View child = viewGroup.getChildAt(index);
                setEnabledRecursively(child, enabled);
            }
        }
    }

}
