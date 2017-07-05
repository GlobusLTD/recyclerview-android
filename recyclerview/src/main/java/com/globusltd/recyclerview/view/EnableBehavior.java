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

import com.globusltd.recyclerview.Adapter;
import com.globusltd.recyclerview.ViewHolderBehavior;

@MainThread
public class EnableBehavior<VH extends RecyclerView.ViewHolder> implements ViewHolderBehavior<VH> {
    
    @NonNull
    private final Adapter<?, ?> mAdapter;
    
    public EnableBehavior(@NonNull final Adapter<?, ?> adapter) {
        mAdapter = adapter;
    }
    
    /**
     * {@inheritDoc}
     */
    @Override
    public void onAttachViewHolder(@NonNull final VH viewHolder) {
        final int position = viewHolder.getAdapterPosition();
        if (position > RecyclerView.NO_POSITION && position < mAdapter.getItemCount()) {
            final boolean isEnabled = mAdapter.isEnabled(position);
            viewHolder.itemView.setEnabled(isEnabled);
        } else {
            viewHolder.itemView.setEnabled(false);
        }
    }
    
    /**
     * {@inheritDoc}
     */
    @Override
    public void onDetachViewHolder(@NonNull final VH viewHolder) {
        viewHolder.itemView.setEnabled(false);
    }
    
}