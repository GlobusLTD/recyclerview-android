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

/**
 * Interface describes {@link RecyclerView.ViewHolder} attachable behavior.
 */
@MainThread
public interface ViewHolderBehavior<VH extends RecyclerView.ViewHolder> {
    
    /**
     * Called when a view created by adapter has been attached to a data.
     * <p>
     * Keep in mind that same data may be attached to the same ViewHolder multiple times.
     *
     * @param viewHolder Holder of the view being attached.
     * @see #onDetachViewHolder(VH)
     */
    void onAttachViewHolder(@NonNull final VH viewHolder);
    
    /**
     * Called when a view holder position has been changed,
     * but data item associated with this view holder has not been changed.
     *
     * @param viewHolder A view holder whose position has been changed.
     */
    void onPositionChanged(@NonNull final VH viewHolder);
    
    /**
     * Called when a view created by adapter has been detached from its data.
     *
     * @param viewHolder Holder of the view being detached.
     * @see #onAttachViewHolder(VH)
     */
    void onDetachViewHolder(@NonNull final VH viewHolder);
    
}