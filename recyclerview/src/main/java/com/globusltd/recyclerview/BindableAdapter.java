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

import android.support.annotation.CallSuper;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;

/**
 * Base {@link RecyclerView.Adapter} that provides simple bindings.
 */
public abstract class BindableAdapter<VH extends RecyclerView.ViewHolder>
        extends RecyclerView.Adapter<VH> {
    
    @NonNull
    private final RecyclerViewBehaviorComposite mRecyclerViewBehaviorComposite;
    
    @NonNull
    private final ViewHolderTracker mViewHolderTracker;
    
    public BindableAdapter() {
        super();
        mRecyclerViewBehaviorComposite = new RecyclerViewBehaviorComposite();
        mViewHolderTracker = new ViewHolderTracker();
    }
    
    /**
     * Add a new {@link RecyclerViewBehavior} to the {@link RecyclerViewBehaviorComposite},
     * which will be called at the same times as the attach/detach methods of the
     * adapter are called.
     *
     * @param recyclerViewBehavior The interface to call.
     */
    public void registerRecyclerViewBehavior(@NonNull final RecyclerViewBehavior recyclerViewBehavior) {
        mRecyclerViewBehaviorComposite.registerRecyclerViewBehavior(recyclerViewBehavior);
    }
    
    /**
     * Remove a {@link RecyclerViewBehavior} object that was previously registered
     * with {@link #registerRecyclerViewBehavior(RecyclerViewBehavior)}.
     */
    public void unregisterRecyclerViewBehavior(@NonNull final RecyclerViewBehavior recyclerViewBehavior) {
        mRecyclerViewBehaviorComposite.unregisterRecyclerViewBehavior(recyclerViewBehavior);
    }
    
    /**
     * Add a new {@link ViewHolderBehavior} to the {@link ViewHolderTracker},
     * which will be called at the same times as the attach/detach methods of the
     * adapter are called.
     *
     * @param viewHolderBehavior The interface to call.
     */
    public void registerViewHolderBehavior(@NonNull final ViewHolderBehavior viewHolderBehavior) {
        mViewHolderTracker.registerViewHolderBehavior(viewHolderBehavior);
    }
    
    /**
     * Remove a {@link ViewHolderBehavior} object that was previously registered
     * with {@link #registerViewHolderBehavior(ViewHolderBehavior)}.
     */
    public void unregisterViewHolderBehavior(@NonNull final ViewHolderBehavior viewHolderBehavior) {
        mViewHolderTracker.unregisterViewHolderBehavior(viewHolderBehavior);
    }
    
    /**
     * {@inheritDoc}
     */
    @CallSuper
    @Override
    public void onViewAttachedToWindow(final VH holder) {
        super.onViewAttachedToWindow(holder);
        mViewHolderTracker.onAttachViewHolder(holder);
    }
    
    /**
     * {@inheritDoc}
     */
    @CallSuper
    @Override
    public void onViewDetachedFromWindow(final VH holder) {
        super.onViewDetachedFromWindow(holder);
        mViewHolderTracker.onDetachViewHolder(holder);
    }
    
    /**
     * {@inheritDoc}
     */
    @CallSuper
    @Override
    public void onAttachedToRecyclerView(final RecyclerView recyclerView) {
        super.onAttachedToRecyclerView(recyclerView);
        mRecyclerViewBehaviorComposite.onAttachedToRecyclerView(recyclerView);
    }
    
    /**
     * {@inheritDoc}
     */
    @CallSuper
    @Override
    public void onDetachedFromRecyclerView(final RecyclerView recyclerView) {
        mRecyclerViewBehaviorComposite.onDetachedFromRecyclerView(recyclerView);
        super.onDetachedFromRecyclerView(recyclerView);
    }
    
}
