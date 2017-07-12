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
package com.globusltd.recyclerview.datasource;

import android.support.annotation.IntRange;
import android.support.annotation.MainThread;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.annotation.RestrictTo;
import android.support.v7.util.DiffUtil;
import android.support.v7.util.ListUpdateCallback;

import com.globusltd.recyclerview.Datasource;
import com.globusltd.recyclerview.DatasourceObservable;
import com.globusltd.recyclerview.DatasourceObserver;
import com.globusltd.recyclerview.DatasourceSwappable;
import com.globusltd.recyclerview.diff.DiffCallback;
import com.globusltd.recyclerview.diff.DiffCallbackFactory;

@MainThread
@RestrictTo(RestrictTo.Scope.LIBRARY)
public class DatasourceProxy<E> implements Datasource<E>, DatasourceSwappable<E> {

    @Nullable
    private final DiffCallbackFactory<E> mDiffCallbackFactory;

    @NonNull
    private final DatasourceObservable mDatasourceObservable;

    @NonNull
    private final DatasourceObserver mDatasourceObserver;

    @NonNull
    private final ListUpdateCallback mListUpdateCallback;

    @NonNull
    private Datasource<? extends E> mDatasource;

    public DatasourceProxy(@NonNull final Datasource<? extends E> datasource,
                           @Nullable final DiffCallbackFactory<E> diffCallbackFactory) {
        mDatasource = datasource;
        mDiffCallbackFactory = diffCallbackFactory;

        mDatasourceObservable = new DatasourceObservable();
        mDatasourceObserver = new DatasourceObserverProxy();
        mListUpdateCallback = new DatasourceListUpdateCallback();

        mDatasource.registerDatasourceObserver(mDatasourceObserver);
    }

    @Nullable
    @Override
    public Datasource<? extends E> swap(@NonNull final Datasource<? extends E> datasource) {
        final Datasource<? extends E> oldDatasource = mDatasource;
        oldDatasource.unregisterDatasourceObserver(mDatasourceObserver);

        final int itemCount = datasource.size();
        if (oldDatasource.size() == 0) {
            mDatasource = datasource;
            mDatasource.registerDatasourceObserver(mDatasourceObserver);
            mDatasourceObservable.notifyItemRangeInserted(0, itemCount);

        } else if (mDiffCallbackFactory != null) {
            final DiffCallback diffCallback = mDiffCallbackFactory
                    .createDiffCallback(oldDatasource, datasource);
            mDatasource = datasource;
            mDatasource.registerDatasourceObserver(mDatasourceObserver);
            DiffUtil.calculateDiff(diffCallback, diffCallback.shouldDetectMoves())
                    .dispatchUpdatesTo(mListUpdateCallback);

        } else {
            mDatasource = datasource;
            mDatasource.registerDatasourceObserver(mDatasourceObserver);
            mDatasourceObservable.notifyItemRangeChanged(0, itemCount, null);
        }

        return oldDatasource;
    }

    @NonNull
    @Override
    public E get(@IntRange(from = 0) final int position) {
        return mDatasource.get(position);
    }

    @Override
    public int size() {
        return mDatasource.size();
    }

    @Override
    public void registerDatasourceObserver(@NonNull final DatasourceObserver observer) {
        mDatasourceObservable.registerObserver(observer);
    }

    @Override
    public void unregisterDatasourceObserver(@NonNull final DatasourceObserver observer) {
        mDatasourceObservable.unregisterObserver(observer);
    }

    private class DatasourceObserverProxy extends DatasourceObserver {

        @Override
        public void onChanged() {
            mDatasourceObservable.notifyChanged();
        }

        @Override
        public void onItemRangeChanged(@IntRange(from = 0) final int positionStart,
                                       @IntRange(from = 0) final int itemCount,
                                       @Nullable final Object payload) {
            mDatasourceObservable.notifyItemRangeChanged(positionStart, itemCount, payload);
        }

        @Override
        public void onItemRangeInserted(@IntRange(from = 0) final int positionStart,
                                        @IntRange(from = 0) final int itemCount) {
            mDatasourceObservable.notifyItemRangeInserted(positionStart, itemCount);
        }

        @Override
        public void onItemRangeRemoved(@IntRange(from = 0) final int positionStart,
                                       @IntRange(from = 0) final int itemCount) {
            mDatasourceObservable.notifyItemRangeRemoved(positionStart, itemCount);
        }

        @Override
        public void onItemMoved(@IntRange(from = 0) final int fromPosition,
                                @IntRange(from = 0) final int toPosition) {
            mDatasourceObservable.notifyItemMoved(fromPosition, toPosition);
        }

    }

    private class DatasourceListUpdateCallback implements ListUpdateCallback {

        @Override
        public void onInserted(final int position, final int count) {
            mDatasourceObservable.notifyItemRangeInserted(position, count);
        }

        @Override
        public void onRemoved(final int position, final int count) {
            mDatasourceObservable.notifyItemRangeRemoved(position, count);
        }

        @Override
        public void onMoved(final int fromPosition, final int toPosition) {
            mDatasourceObservable.notifyItemMoved(fromPosition, toPosition);
        }

        @Override
        public void onChanged(final int position, final int count, final Object payload) {
            mDatasourceObservable.notifyItemRangeChanged(position, count, payload);
        }

    }

}
