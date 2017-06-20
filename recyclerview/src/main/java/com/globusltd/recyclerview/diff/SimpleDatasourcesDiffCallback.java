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

import com.globusltd.recyclerview.datasource.Datasource;

/**
 * A callback class used by DiffUtil while calculating the diff between two {@link Datasource}s.
 *
 * @param <E> type of entity in the datasource.
 */
public abstract class SimpleDatasourcesDiffCallback<E> extends DiffCallback
        implements ParameterizedDiffCallback<E> {

    @NonNull
    private final Datasource<? extends E> mOldDatasource;

    @NonNull
    private final Datasource<? extends E> mNewDatasource;

    public SimpleDatasourcesDiffCallback(@NonNull final Datasource<? extends E> oldDatasource,
                                         @NonNull final Datasource<? extends E> newDatasource) {
        this(oldDatasource, newDatasource, true);
    }

    public SimpleDatasourcesDiffCallback(@NonNull final Datasource<? extends E> oldDatasource,
                                         @NonNull final Datasource<? extends E> newDatasource,
                                         final boolean shouldDetectMoves) {
        super(shouldDetectMoves);
        mOldDatasource = oldDatasource;
        mNewDatasource = newDatasource;
    }
    
    @Override
    public int getOldListSize() {
        return mOldDatasource.size();
    }
    
    @Override
    public int getNewListSize() {
        return mNewDatasource.size();
    }
    
    @Override
    public final boolean areItemsTheSame(final int oldItemPosition, final int newItemPosition) {
        return areItemsTheSame(mOldDatasource.get(oldItemPosition), mNewDatasource.get(newItemPosition));
    }
    
    @Override
    public final boolean areContentsTheSame(final int oldItemPosition, final int newItemPosition) {
        return areContentsTheSame(mOldDatasource.get(oldItemPosition), mNewDatasource.get(newItemPosition));
    }
    
    @Nullable
    @Override
    public final Object getChangePayload(final int oldItemPosition, final int newItemPosition) {
        return getChangePayload(mOldDatasource.get(oldItemPosition), mNewDatasource.get(newItemPosition));
    }
    
    /**
     * When {@link #areItemsTheSame(E, E)} returns {@code true} for two items and
     * {@link #areContentsTheSame(E, E)} returns false for them, DiffUtil
     * calls this method to get a payload about the change.
     * <p>
     * For example, if you are using DiffUtil with {@link android.support.v7.widget.RecyclerView},
     * you can return the particular field that changed in the item and your
     * {@link android.support.v7.widget.RecyclerView.ItemAnimator} can use that
     * information to run the correct animation.
     * <p>
     * Default implementation returns {@code null}.
     *
     * @param oldItem The item in the old datasource.
     * @param newItem The item in the new datasource.
     * @return A payload object that represents the change between the two datasources.
     */
    @Nullable
    public Object getChangePayload(@NonNull final E oldItem, @NonNull final E newItem) {
        return null;
    }
    
}