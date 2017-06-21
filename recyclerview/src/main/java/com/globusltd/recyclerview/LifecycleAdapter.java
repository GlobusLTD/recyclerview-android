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

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.ViewGroup;

import com.globusltd.recyclerview.lifecycle.LifecycleCallbacks;
import com.globusltd.recyclerview.lifecycle.LifecycleComposite;

import java.util.List;

/**
 * {@link RecyclerView.Adapter} provides activity or fragment lifecycle events
 * to {@link RecyclerView.ViewHolder}s. {@link LifecycleAdapter} uses decorator pattern.
 *
 * @param <VH> Type of {@link RecyclerView.ViewHolder}.
 * @see LifecycleCallbacks
 * @see LifecycleComposite
 */
public final class LifecycleAdapter<VH extends RecyclerView.ViewHolder & LifecycleCallbacks>
        extends RecyclerView.Adapter<VH> {

    @NonNull
    private final RecyclerView.Adapter<VH> mAdapter;

    @NonNull
    private final LifecycleComposite mLifecycleComposite;

    public LifecycleAdapter(@NonNull final RecyclerView.Adapter<VH> adapter,
                            @NonNull final LifecycleComposite lifecycleComposite) {
        super();
        mAdapter = adapter;
        setHasStableIds(mAdapter.hasStableIds());
        mLifecycleComposite = lifecycleComposite;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void onAttachedToRecyclerView(final RecyclerView recyclerView) {
        mAdapter.onAttachedToRecyclerView(recyclerView);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void registerAdapterDataObserver(final RecyclerView.AdapterDataObserver observer) {
        mAdapter.registerAdapterDataObserver(observer);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public long getItemId(final int position) {
        return mAdapter.getItemId(position);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public int getItemCount() {
        return mAdapter.getItemCount();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public int getItemViewType(final int position) {
        return mAdapter.getItemViewType(position);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public VH onCreateViewHolder(final ViewGroup parent, final int viewType) {
        return mAdapter.createViewHolder(parent, viewType);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void onViewAttachedToWindow(final VH holder) {
        mAdapter.onViewAttachedToWindow(holder);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void onBindViewHolder(final VH holder, final int position) {
        mAdapter.onBindViewHolder(holder, position);
        mLifecycleComposite.registerLifecycleCallbacks(holder);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void onBindViewHolder(final VH holder, final int position, final List<Object> payloads) {
        mAdapter.onBindViewHolder(holder, position, payloads);
        mLifecycleComposite.registerLifecycleCallbacks(holder);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void onViewRecycled(final VH holder) {
        mAdapter.onViewRecycled(holder);
        mLifecycleComposite.unregisterLifecycleCallbacks(holder);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void onViewDetachedFromWindow(final VH holder) {
        mAdapter.onViewDetachedFromWindow(holder);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean onFailedToRecycleView(final VH holder) {
        return mAdapter.onFailedToRecycleView(holder);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void unregisterAdapterDataObserver(final RecyclerView.AdapterDataObserver observer) {
        mAdapter.unregisterAdapterDataObserver(observer);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void onDetachedFromRecyclerView(final RecyclerView recyclerView) {
        mAdapter.onDetachedFromRecyclerView(recyclerView);
    }

}
