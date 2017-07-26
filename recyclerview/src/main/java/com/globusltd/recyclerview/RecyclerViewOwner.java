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
import android.support.annotation.Nullable;
import android.support.annotation.RestrictTo;
import android.support.v7.widget.RecyclerView;

@MainThread
@RestrictTo(RestrictTo.Scope.LIBRARY)
public abstract class RecyclerViewOwner {

    @Nullable
    private RecyclerView mRecyclerView;

    /**
     * Attaches the RecyclerViewOwner to the provided RecyclerView. If RecyclerViewOwner is already
     * attached to a RecyclerView, it will first detach from the previous one. You can call this
     * method with {@code null} to detach it from the current RecyclerView.
     *
     * @param recyclerView The RecyclerView instance to which you want to add this helper or
     *                     {@code null} if you want to remove RecyclerViewOwner from the current
     *                     RecyclerView.
     */
    public void setRecyclerView(@Nullable final RecyclerView recyclerView) {
        if (mRecyclerView == recyclerView) {
            return; // Nothing to do
        }
        if (mRecyclerView != null) {
            onDetachedFromRecyclerView(mRecyclerView);
        }
        mRecyclerView = recyclerView;
        if (mRecyclerView != null) {
            onAttachedToRecyclerView(mRecyclerView);
        }
    }

    /**
     * Returns a RecyclerView instance this class is bond to.
     */
    @Nullable
    protected RecyclerView getRecyclerView() {
        return mRecyclerView;
    }

    /**
     * Called by RecyclerViewOwner when RecyclerView is attached.
     *
     * @param recyclerView The RecyclerView instance.
     * @see #onDetachedFromRecyclerView(RecyclerView)
     */
    protected abstract void onAttachedToRecyclerView(@NonNull final RecyclerView recyclerView);

    /**
     * Called by RecyclerViewOwner when RecyclerView is detached.
     *
     * @param recyclerView The RecyclerView instance.
     * @see #onAttachedToRecyclerView(RecyclerView)
     */
    protected abstract void onDetachedFromRecyclerView(@NonNull final RecyclerView recyclerView);

}
