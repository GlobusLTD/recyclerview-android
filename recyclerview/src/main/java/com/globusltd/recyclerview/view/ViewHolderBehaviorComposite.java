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

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;

import com.globusltd.recyclerview.ViewHolderBehavior;

import java.util.ArrayList;
import java.util.List;

public class ViewHolderBehaviorComposite implements ViewHolderBehavior {

    @NonNull
    private final List<ViewHolderBehavior> mBehaviors;

    public ViewHolderBehaviorComposite() {
        mBehaviors = new ArrayList<>();
    }

    public void addViewHolderBehavior(@NonNull final ViewHolderBehavior viewHolderBehavior) {
        if (!mBehaviors.contains(viewHolderBehavior)) {
            mBehaviors.add(viewHolderBehavior);
        }
    }

    public void removeViewHolderBehavior(@NonNull final ViewHolderBehavior viewHolderBehavior) {
        mBehaviors.remove(viewHolderBehavior);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void onAttachViewHolder(@NonNull final RecyclerView.ViewHolder viewHolder) {
        for (final ViewHolderBehavior behavior : mBehaviors) {
            behavior.onAttachViewHolder(viewHolder);
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void onDetachViewHolder(@NonNull final RecyclerView.ViewHolder viewHolder) {
        final int size = mBehaviors.size();
        for (int i = size - 1; i >= 0; i--) {
            final ViewHolderBehavior behavior = mBehaviors.get(i);
            behavior.onDetachViewHolder(viewHolder);
        }
    }

}
