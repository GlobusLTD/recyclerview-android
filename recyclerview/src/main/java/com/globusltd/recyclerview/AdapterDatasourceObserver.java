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
import android.support.annotation.Nullable;
import android.support.v7.widget.RecyclerView;

import com.globusltd.recyclerview.datasource.Datasource;
import com.globusltd.recyclerview.datasource.DatasourceObserver;

/**
 * Observer class for watching changes to a {@link Datasource} and
 * dispatching them to the {@link RecyclerView.Adapter}.
 */
public class AdapterDatasourceObserver extends DatasourceObserver {

    @NonNull
    private final RecyclerView.Adapter<?> mAdapter;

    AdapterDatasourceObserver(@NonNull final RecyclerView.Adapter<?> adapter) {
        super();
        mAdapter = adapter;
    }
    
    /**
     * {@inheritDoc}
     */
    @Override
    public void onChanged() {
        mAdapter.notifyDataSetChanged();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void onItemRangeChanged(@IntRange(from = 0) final int positionStart,
                                   @IntRange(from = 0) final int itemCount,
                                   @Nullable final Object payload) {
        mAdapter.notifyItemRangeChanged(positionStart, itemCount, payload);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void onItemRangeInserted(@IntRange(from = 0) final int positionStart,
                                    @IntRange(from = 0) final int itemCount) {
        mAdapter.notifyItemRangeInserted(positionStart, itemCount);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void onItemRangeRemoved(@IntRange(from = 0) final int positionStart,
                                   @IntRange(from = 0) final int itemCount) {
        mAdapter.notifyItemRangeRemoved(positionStart, itemCount);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void onItemMoved(@IntRange(from = 0) final int fromPosition,
                            @IntRange(from = 0) final int toPosition) {
        mAdapter.notifyItemMoved(fromPosition, toPosition);
    }
    
}
