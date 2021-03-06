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
package com.globusltd.recyclerview.lifecycle;

import android.support.annotation.MainThread;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.util.SparseBooleanArray;

import com.globusltd.recyclerview.ViewHolderObserver;

/**
 * This is a class to provide common lifecycle events to the {@link RecyclerView.ViewHolder}.
 */
@MainThread
public class LifecycleBehavior implements ViewHolderObserver {

    @NonNull
    private final LifecycleComposite mLifecycleComposite;

    @NonNull
    private final SparseBooleanArray mLifecycleCallbacksTypes;

    public LifecycleBehavior(@NonNull final LifecycleComposite lifecycleComposite) {
        mLifecycleComposite = lifecycleComposite;
        mLifecycleCallbacksTypes = new SparseBooleanArray();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void onAttached(@NonNull final RecyclerView.ViewHolder viewHolder) {
        if (isLifecycleCallbacks(viewHolder)) {
            final LifecycleCallbacks lifecycleCallbacks = (LifecycleCallbacks) viewHolder;
            mLifecycleComposite.registerLifecycleCallbacks(lifecycleCallbacks);
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void onPositionChanged(@NonNull final RecyclerView.ViewHolder viewHolder) {
        // Do nothing
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void onChanged(@NonNull final RecyclerView.ViewHolder viewHolder) {
        if (isLifecycleCallbacks(viewHolder)) {
            final LifecycleCallbacks lifecycleCallbacks = (LifecycleCallbacks) viewHolder;
            mLifecycleComposite.unregisterLifecycleCallbacks(lifecycleCallbacks);
            mLifecycleComposite.registerLifecycleCallbacks(lifecycleCallbacks);
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void onDetached(@NonNull final RecyclerView.ViewHolder viewHolder) {
        if (isLifecycleCallbacks(viewHolder)) {
            final LifecycleCallbacks lifecycleCallbacks = (LifecycleCallbacks) viewHolder;
            mLifecycleComposite.unregisterLifecycleCallbacks(lifecycleCallbacks);
        }
    }

    private boolean isLifecycleCallbacks(@NonNull final RecyclerView.ViewHolder viewHolder) {
        final int viewType = viewHolder.getItemViewType();
        if (mLifecycleCallbacksTypes.indexOfKey(viewType) >= 0) {
            return mLifecycleCallbacksTypes.get(viewType);

        } else {
            final boolean isLifecycleCallbacks = LifecycleCallbacks.class.isInstance(viewHolder);
            mLifecycleCallbacksTypes.put(viewType, isLifecycleCallbacks);
            return isLifecycleCallbacks;
        }
    }

}