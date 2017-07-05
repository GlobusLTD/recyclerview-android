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
package com.globusltd.recyclerview.view;

import android.support.annotation.MainThread;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;

import com.globusltd.recyclerview.RecyclerViewBehavior;

import java.util.ArrayList;
import java.util.List;

/**
 * {@link RecyclerViewBehaviorComposite} provides RecyclerView attach/detach events to
 * the registered {@link RecyclerViewBehavior} objects.
 */
@MainThread
public class RecyclerViewBehaviorComposite implements RecyclerViewBehavior {
    
    @NonNull
    private final List<RecyclerViewBehavior> mBehaviors;
    
    public RecyclerViewBehaviorComposite() {
        mBehaviors = new ArrayList<>();
    }
    
    /**
     * Add a new {@link RecyclerViewBehavior} to the {@link RecyclerViewBehaviorComposite},
     * which will be called at the same times as the attach/detach methods of
     * adapter are called.
     *
     * @param recyclerViewBehavior The interface to call.
     */
    public void registerRecyclerViewBehavior(@NonNull final RecyclerViewBehavior recyclerViewBehavior) {
        if (!mBehaviors.contains(recyclerViewBehavior)) {
            mBehaviors.add(recyclerViewBehavior);
        }
    }
    
    /**
     * Remove a {@link RecyclerViewBehavior} object that was previously registered
     * with {@link #registerRecyclerViewBehavior(RecyclerViewBehavior)}.
     */
    public void unregisterRecyclerViewBehavior(@NonNull final RecyclerViewBehavior recyclerViewBehavior) {
        mBehaviors.remove(recyclerViewBehavior);
    }
    
    /**
     * {@inheritDoc}
     */
    @Override
    public void onAttachedToRecyclerView(final RecyclerView recyclerView) {
        for (final RecyclerViewBehavior behavior : mBehaviors) {
            behavior.onAttachedToRecyclerView(recyclerView);
        }
    }
    
    /**
     * {@inheritDoc}
     */
    @Override
    public void onDetachedFromRecyclerView(final RecyclerView recyclerView) {
        final int size = mBehaviors.size();
        for (int i = size - 1; i >= 0; i--) {
            final RecyclerViewBehavior behavior = mBehaviors.get(i);
            behavior.onDetachedFromRecyclerView(recyclerView);
        }
    }
    
}
