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
package com.globusltd.recyclerview.choice;

import android.os.Bundle;
import android.support.annotation.IntRange;
import android.support.annotation.MainThread;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.RecyclerView;

/**
 * {@link ChoiceMode} that allows up to one choice.
 */
@MainThread
public class SingleChoiceMode extends ObservableChoiceMode {

    private static final String KEY_SINGLE_CHOICE_MODE = "single_choice_mode";
    private static final String KEY_CHECKED_ID = "checked_id";
    
    /**
     * Single choice mode callback.
     */
    @Nullable
    private SimpleChoiceModeListener mChoiceModeListener;
    
    /**
     * Running state of which ID are currently checked.
     */
    private long mCheckedId = RecyclerView.NO_ID;

    public SingleChoiceMode() {
        this(null);
    }
    
    public SingleChoiceMode(@Nullable final Bundle savedInstanceState) {
        super();
        final Bundle state = (savedInstanceState != null ?
                savedInstanceState.getBundle(KEY_SINGLE_CHOICE_MODE) : null);
        if (state != null) {
            mCheckedId = state.getLong(KEY_CHECKED_ID, RecyclerView.NO_ID);
        }
    }
    
    /**
     * Sets single choice mode callback.
     */
    public void setChoiceModeListener(@Nullable final SimpleChoiceModeListener listener) {
        mChoiceModeListener = listener;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean requiresStableIds() {
        return true;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean requiresLongpress() {
        return false;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean isActivated() {
        return true;
    }
    
    /**
     * {@inheritDoc}
     */
    @IntRange(from = 0, to = 1)
    @Override
    public int getCheckedItemCount() {
        return (mCheckedId != RecyclerView.NO_ID ? 1 : 0);
    }
    
    /**
     * {@inheritDoc}
     */
    @Override
    public boolean isItemChecked(final long itemId) {
        return (mCheckedId == itemId);
    }
    
    /**
     * {@inheritDoc}
     */
    @Override
    public void setItemChecked(final long itemId, final boolean checked) {
        setItemCheckedInternal(itemId, checked, false);
    }
    
    private void setItemCheckedInternal(final long itemId, final boolean checked,
                                        final boolean fromUser) {
        final long checkedId = mCheckedId;
        mCheckedId = (checked ? itemId : RecyclerView.NO_ID);
    
        if (mChoiceModeListener != null) {
            mChoiceModeListener.onItemCheckedStateChanged(itemId, checked, fromUser);
        }
    
        notifyItemCheckedChanged(checkedId, fromUser);
        notifyItemCheckedChanged(mCheckedId, fromUser);
    }
    
    /**
     * Returns an identifier of checked item or
     * {@link RecyclerView#NO_ID} if no item is checked.
     */
    public long getCheckedItem() {
        return mCheckedId;
    }
    
    /**
     * {@inheritDoc}
     */
    @Override
    public void clearChoices() {
        final long itemId = mCheckedId;
        mCheckedId = RecyclerView.NO_ID;
        notifyItemCheckedChanged(itemId, false);
    }
    
    /**
     * {@inheritDoc}
     */
    @Override
    public boolean onClick(final long itemId) {
        if (!isItemChecked(itemId)) {
            setItemCheckedInternal(itemId, true, true);
        }
        return true;
    }
    
    /**
     * {@inheritDoc}
     */
    @Override
    public boolean onLongClick(final long itemId) {
        // Consume event when it happens
        return true;
    }

    /**
     * Call this method to retrieve per-instance state before UI component being killed
     * so that the state can be restored via constructor.
     *
     * @param outState Bundle in which to place your saved state.
     */
    public void onSaveInstanceState(@NonNull final Bundle outState) {
        final Bundle state = new Bundle();
        state.putLong(KEY_CHECKED_ID, mCheckedId);
        outState.putBundle(KEY_SINGLE_CHOICE_MODE, state);
    }
    
}