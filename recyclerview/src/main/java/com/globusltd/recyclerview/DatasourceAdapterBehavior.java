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
import android.support.v4.util.ArraySet;
import android.support.v7.widget.RecyclerView;

import java.util.Set;

@MainThread
public class DatasourceAdapterBehavior implements RecyclerViewBehavior {
    
    @NonNull
    private final Datasource<?> mDatasource;
    
    @NonNull
    private final DatasourceObserver mDatasourceObserver;
    
    @NonNull
    private final Set<RecyclerView> mAttachedRecyclerViews;
    
    public DatasourceAdapterBehavior(@NonNull final Datasource<?> datasource,
                                     @NonNull final DatasourceObserver datasourceObserver) {
        mDatasource = datasource;
        mDatasourceObserver = datasourceObserver;
        mAttachedRecyclerViews = new ArraySet<>();
    }
    
    /**
     * {@inheritDoc}
     */
    @Override
    public void onAttachedToRecyclerView(final RecyclerView recyclerView) {
        if (mAttachedRecyclerViews.isEmpty() && mAttachedRecyclerViews.add(recyclerView)) {
            mDatasource.registerDatasourceObserver(mDatasourceObserver);
        } else {
            mAttachedRecyclerViews.add(recyclerView);
        }
    }
    
    /**
     * {@inheritDoc}
     */
    @Override
    public void onDetachedFromRecyclerView(final RecyclerView recyclerView) {
        if (mAttachedRecyclerViews.remove(recyclerView) && mAttachedRecyclerViews.isEmpty()) {
            mDatasource.unregisterDatasourceObserver(mDatasourceObserver);
        }
    }
    
}
