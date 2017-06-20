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
import android.support.annotation.Nullable;

public interface ParameterizedDiffCallback<E> {
    
    /**
     * Called by the DiffUtil to decide whether two object represent the same Item.
     * <p>
     * For example, if your items have unique ids, this method should check their id equality.
     *
     * @param oldItem The item in the old list.
     * @param newItem The item in the new list associated with the same position as an old item.
     * @return True if the two items represent the same object or false if they are different.
     */
    boolean areItemsTheSame(@NonNull final E oldItem, @NonNull final E newItem);
    
    /**
     * Called by the DiffUtil when it wants to check whether two items have the same data.
     * DiffUtil uses this information to detect if the contents of an item has changed.
     * <p>
     * DiffUtil uses this method to check equality instead of {@link Object#equals(Object)}
     * so that you can change its behavior depending on your UI.
     * For example, if you are using DiffUtil with a
     * {@link android.support.v7.widget.RecyclerView.Adapter RecyclerView.Adapter}, you should
     * return whether the items' visual representations are the same.
     * <p>
     * This method is called only if {@link #areItemsTheSame(E, E)} returns
     * {@code true} for these items.
     *
     * @param oldItem The item in the old list.
     * @param newItem The item in the new list associated with the same position as an old item.
     * @return True if the contents of the items are the same or false if they are different.
     */
    boolean areContentsTheSame(@NonNull final E oldItem, @NonNull final E newItem);
    
    /**
     * When {@link #areItemsTheSame(E, E)} returns {@code true} for two items and
     * {@link #areContentsTheSame(E, E)} returns false for them, DiffUtil
     * calls this method to get a payload about the change.
     * <p>
     * For example, if you are using DiffUtil with {@link android.support.v7.widget.RecyclerView},
     * you can return the particular field that changed in the item and your
     * {@link android.support.v7.widget.RecyclerView.ItemAnimator ItemAnimator} can use that
     * information to run the correct animation.
     * <p>
     * Default implementation returns {@code null}.
     *
     * @param oldItem The item in the old list
     * @param newItem The item in the new list
     * @return A payload object that represents the change between the two items.
     */
    @Nullable
    Object getChangePayload(@NonNull final E oldItem, @NonNull final E newItem);
    
}
