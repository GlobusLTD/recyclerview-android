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
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.globusltd.collections.LongArrayList;

/**
 * {@link ChoiceMode} that allows any number of items to be chosen
 */
public class MultipleChoiceMode  extends ObservableChoiceMode {

    private static final String KEY_MULTIPLE_CHOICE_MODE = "multiple_choice_mode";
    private static final String KEY_CHECKED_IDS = "checked_ids";

    /**
     * Multiple choice mode callback.
     */
    @Nullable
    private SimpleChoiceModeListener mChoiceModeListener;

    /**
     * Running state of which IDs are currently checked.
     * If there is a value for a given key, the checked state for that ID is true.
     */
    @NonNull
    private final LongArrayList mCheckedIds;

    public MultipleChoiceMode() {
        this(null);
    }

    public MultipleChoiceMode(@Nullable final Bundle savedInstanceState) {
        super();
        mCheckedIds = new LongArrayList();

        final Bundle state = (savedInstanceState != null ?
                savedInstanceState.getBundle(KEY_MULTIPLE_CHOICE_MODE) : null);
        if (state != null) {
            final LongArrayList checkedIdStates = state.getParcelable(KEY_CHECKED_IDS);
            if (checkedIdStates == null) {
                throw new IllegalArgumentException("Did you put checked id states to the saved state?");
            }

            final int count = checkedIdStates.size();
            mCheckedIds.clear();
            for (int i = 0; i < count; i++) {
                mCheckedIds.add(checkedIdStates.get(i));
            }
        }
    }

    /**
     * Sets multiple choice mode callback.
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
    @Override
    public int getCheckedItemCount() {
        return mCheckedIds.size();
    }
    /**
     * {@inheritDoc}
     */
    @Override
    public boolean isItemChecked(final long itemId) {
        return mCheckedIds.contains(itemId);
    }

    @Override
    public void setItemChecked(final long itemId, final boolean checked) {
        setItemCheckedInternal(itemId, checked, false);
    }

    private void setItemCheckedInternal(final long itemId, final boolean checked,
                                        final boolean fromUser) {
        final int indexOf = mCheckedIds.indexOf(itemId);
        if (indexOf > -1) {
            mCheckedIds.removeAt(indexOf);
        }
        if (checked) {
            mCheckedIds.add(itemId);
        }

        if (mChoiceModeListener != null) {
            mChoiceModeListener.onItemCheckedStateChanged(itemId, checked, fromUser);
        }
        notifyItemCheckedChanged(itemId, fromUser);
    }

    /**
     * Returns an unsorted {@link LongArrayList} of checked item ids.
     * Don't modify it without copying.
     */
    @NonNull
    public LongArrayList getCheckedItems() {
        return mCheckedIds;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void clearChoices() {
        mCheckedIds.clear();
        notifyAllItemsCheckedChanged(false);
    }

    @Override
    public boolean onClick(final long itemId) {
        final boolean checked = !isItemChecked(itemId);
        setItemCheckedInternal(itemId, checked, true);
        return false;
    }

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
        state.putParcelable(KEY_CHECKED_IDS, new LongArrayList(mCheckedIds));
        outState.putBundle(KEY_MULTIPLE_CHOICE_MODE, state);
    }

}
