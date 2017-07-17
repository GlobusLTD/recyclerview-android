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
import android.support.v7.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

/**
 * {@link ViewHolderBehaviorComposite} provides view holder attach/detach events to
 * the registered {@link ViewHolderBehavior} objects.
 */
@MainThread
public class ViewHolderBehaviorComposite<VH extends RecyclerView.ViewHolder>
        implements ViewHolderBehavior<VH> {
    
    @NonNull
    private final List<ViewHolderBehavior<VH>> mBehaviors;
    
    public ViewHolderBehaviorComposite() {
        mBehaviors = new ArrayList<>();
    }
    
    /**
     * Add a new {@link ViewHolderBehavior} to the {@link ViewHolderBehaviorComposite},
     * which will be called at the same times as the attach/detach methods of
     * adapter are called.
     *
     * @param viewHolderBehavior The interface to call.
     */
    public void registerViewHolderBehavior(@NonNull final ViewHolderBehavior<VH> viewHolderBehavior) {
        if (!mBehaviors.contains(viewHolderBehavior)) {
            mBehaviors.add(viewHolderBehavior);
        }
    }
    
    /**
     * Remove a {@link ViewHolderBehavior} object that was previously registered
     * with {@link #registerViewHolderBehavior(ViewHolderBehavior)}.
     */
    public void unregisterViewHolderBehavior(@NonNull final ViewHolderBehavior<VH> viewHolderBehavior) {
        mBehaviors.remove(viewHolderBehavior);
    }
    
    /**
     * {@inheritDoc}
     */
    @Override
    public void onAttachViewHolder(@NonNull final VH viewHolder) {
        for (final ViewHolderBehavior<VH> behavior : mBehaviors) {
            behavior.onAttachViewHolder(viewHolder);
        }
    }
    
    /**
     * {@inheritDoc}
     */
    @Override
    public void onPositionChanged(@NonNull final VH viewHolder) {
        for (final ViewHolderBehavior<VH> behavior : mBehaviors) {
            behavior.onPositionChanged(viewHolder);
        }
    }
    
    /**
     * {@inheritDoc}
     */
    @Override
    public void onDetachViewHolder(@NonNull final VH viewHolder) {
        final int size = mBehaviors.size();
        for (int i = size - 1; i >= 0; i--) {
            final ViewHolderBehavior<VH> behavior = mBehaviors.get(i);
            behavior.onDetachViewHolder(viewHolder);
        }
    }
    
}