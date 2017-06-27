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
import android.support.annotation.RestrictTo;
import android.support.v7.widget.RecyclerView;

/**
 * Interface describes {@link RecyclerView.ViewHolder} attachable behavior.
 */
@MainThread
public interface ViewHolderBehavior<A extends RecyclerView.Adapter<VH>,
        VH extends RecyclerView.ViewHolder> {
    
    /**
     * Called when a view created by this adapter has been attached to a data.
     * <p>
     * Keep in mind that same data may be attached to the same ViewHolder multiple times.
     *
     * @param viewHolder Holder of the view being attached.
     * @see #onDetachViewHolder(A, VH)
     */
    void onAttachViewHolder(@NonNull final A adapter, @NonNull final VH viewHolder);
    
    /**
     * Called when a view created by this adapter has been detached from its data.
     *
     * @param viewHolder Holder of the view being detached.
     * @see #onAttachViewHolder(A, VH)
     */
    void onDetachViewHolder(@NonNull final A adapter, @NonNull final VH viewHolder);
    
}