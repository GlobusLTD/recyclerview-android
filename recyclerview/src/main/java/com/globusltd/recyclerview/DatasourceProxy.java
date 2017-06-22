package com.globusltd.recyclerview;

import android.support.annotation.IntRange;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.globusltd.recyclerview.diff.DiffCallbackFactory;

class DatasourceProxy<E> implements Datasource<E>, DatasourceSwappable<E> {
    
    @Nullable
    private final DiffCallbackFactory<E> mDiffCallbackFactory;
    
    @NonNull
    private final DatasourceObservable mDatasourceObservable;
    
    DatasourceProxy(@Nullable final DiffCallbackFactory<E> diffCallbackFactory) {
        mDiffCallbackFactory = diffCallbackFactory;
        mDatasourceObservable = new DatasourceObservable();
    }
    
    @Nullable
    @Override
    public Datasource<? extends E> swap(@NonNull final Datasource<? extends E> datasource) {
        return null;
    }
    
    @NonNull
    @Override
    public E get(@IntRange(from = 0) final int position) {
        return null;
    }
    
    @Override
    public int size() {
        return 0;
    }
    
    @Override
    public void registerDatasourceObserver(@NonNull final DatasourceObserver observer) {
        mDatasourceObservable.registerObserver(observer);
    }
    
    @Override
    public void unregisterDatasourceObserver(@NonNull final DatasourceObserver observer) {
        mDatasourceObservable.unregisterObserver(observer);
    }
    
}
