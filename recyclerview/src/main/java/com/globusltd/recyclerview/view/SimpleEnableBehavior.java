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

import android.support.annotation.IntRange;
import android.support.annotation.MainThread;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;

import com.globusltd.recyclerview.ViewHolderObserver;

/**
 * This is a class to enable or disable {@link RecyclerView.ViewHolder}'s item view
 * according to enabled state returned by {@link Callback#isEnabled(int)}.
 */
@MainThread
public class SimpleEnableBehavior implements ViewHolderObserver {

    /**
     * This interface is the contract between SimpleEnableBehavior and your application.
     * It lets you control which enabled state is enabled per each ViewHolder.
     * <p>
     * To control in which enabled state view can be, you should override
     * {@link #isEnabled(int)} and return boolean value.
     */
    public interface Callback {

        /**
         * Returns true if the item at the specified position is clickable.
         * <p/>
         * The result is unspecified if position is invalid. An {@link IndexOutOfBoundsException}
         * should be thrown in that case for fast failure.
         *
         * @param position an index of the item.
         * @return {@code true} if item is enabled, {@code false} otherwise.
         */
        boolean isEnabled(@IntRange(from = 0) final int position);

    }

    @NonNull
    private final Callback mCallback;

    public SimpleEnableBehavior(@NonNull final Callback callback) {
        mCallback = callback;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void onAttached(@NonNull final RecyclerView.ViewHolder viewHolder) {
        updateItemViewEnabled(viewHolder);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void onPositionChanged(@NonNull final RecyclerView.ViewHolder viewHolder) {
        updateItemViewEnabled(viewHolder);
    }

    private void updateItemViewEnabled(@NonNull final RecyclerView.ViewHolder viewHolder) {
        final int position = viewHolder.getAdapterPosition();
        if (position > RecyclerView.NO_POSITION) {
            final boolean isEnabled = mCallback.isEnabled(position);
            onEnabledChanged(viewHolder, isEnabled);
        } else {
            onEnabledChanged(viewHolder, false);
        }
    }

    /**
     * Called when enabled state is changed. Default implementation enables or disables
     * {@link RecyclerView.ViewHolder#itemView}.
     *
     * @param viewHolder {@link RecyclerView.ViewHolder} instance.
     * @param enabled    {@code true} if view should be enabled, {@code false} otherwise.
     */
    protected void onEnabledChanged(@NonNull final RecyclerView.ViewHolder viewHolder,
                                    final boolean enabled) {
        viewHolder.itemView.setEnabled(enabled);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void onDetached(@NonNull final RecyclerView.ViewHolder viewHolder) {
        onEnabledChanged(viewHolder, false);
    }

}