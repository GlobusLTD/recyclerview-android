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
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.view.ActionMode;
import android.view.Menu;
import android.view.MenuItem;

import com.globusltd.collections.LongArrayList;

/**
 * {@link ChoiceMode} that allows multiple choices in a modal selection mode.
 */
public class MultipleModalChoiceMode extends ObservableChoiceMode {

    private static final String KEY_MULTIPLE_CHOICE_MODE = "multiple_choice_mode";
    private static final String KEY_CHECKED_IDS = "checked_ids";

    @NonNull
    private final ActionModeCompat mActionModeCompat;

    @NonNull
    private final ActionModeCallbacks mActionModeCallbacks;

    private boolean mStartOnSingleTapEnabled = false;
    private boolean mFinishActionModeOnClearEnabled = true;

    /**
     * Controls choice mode modal. Null when inactive.
     */
    @Nullable
    private ActionMode mActionMode;

    /**
     * Running state of which IDs are currently checked.
     * If there is a value for a given key, the checked state for that ID is true.
     */
    @NonNull
    private final LongArrayList mCheckedIds;

    public MultipleModalChoiceMode(@NonNull final ActionModeCompat actionModeCompat,
                                   @NonNull final ModalChoiceModeListener listener) {
        this(actionModeCompat, listener, null);
    }

    public MultipleModalChoiceMode(@NonNull final ActionModeCompat actionModeCompat,
                                   @NonNull final ModalChoiceModeListener listener,
                                   @Nullable final Bundle savedInstanceState) {
        super();
        mActionModeCompat = actionModeCompat;
        mActionModeCallbacks = new ActionModeCallbacks(listener);
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
     * Allows to start modal choice on single tap.
     * By default long tap is required.
     */
    public void setStartOnSingleTapEnabled(final boolean enabled) {
        mStartOnSingleTapEnabled = enabled;
    }

    /**
     * Allows to stay action mode be opened when no items are checked.
     * By default is true.
     */
    public void setFinishActionModeOnClearEnabled(final boolean enabled) {
        mFinishActionModeOnClearEnabled = enabled;
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
        return true;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean isActivated() {
        return (mActionMode != null);
    }

    /**
     * {@inheritDoc}
     */
    @IntRange(from = 0, to = 1)
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

    /**
     * {@inheritDoc}
     */
    @Override
    public void setItemChecked(final long itemId, final boolean checked) {
        setItemCheckedInternal(itemId, checked, false);
    }

    private void setItemCheckedInternal(final long itemId, final boolean checked,
                                        final boolean fromUser) {
        if (checked) {
            startActionMode(fromUser);
        }

        final int indexOf = mCheckedIds.indexOf(itemId);
        if (indexOf > -1) {
            mCheckedIds.removeAt(indexOf);
        }
        if (checked) {
            mCheckedIds.add(itemId);
        }

        if (mActionMode != null) {
            mActionModeCallbacks.onItemCheckedStateChanged(mActionMode, itemId, checked, fromUser);
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
        if (mActionMode != null) {
            // TODO: if (mFinishActionModeOnClearEnabled) {
                //mActionMode.finish();
            //} else {
                mActionMode.invalidate();
            //}
        }
    }

    @Override
    public boolean onClick(final long itemId) {
        if (mActionMode == null && mStartOnSingleTapEnabled) {
            setItemCheckedInternal(itemId, true, true);
            return true;

        } else if (mActionMode != null) {
            final boolean checked = !isItemChecked(itemId);
            setItemCheckedInternal(itemId, checked, true);
            return true;
        }

        return false;
    }

    @Override
    public boolean onLongClick(final long itemId) {
        setItemCheckedInternal(itemId, true, true);
        return true;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void registerChoiceModeObserver(@NonNull final ChoiceModeObserver observer) {
        super.registerChoiceModeObserver(observer);

        // Restore action mode when choice mode is registered
        if (!mCheckedIds.isEmpty()) {
            startActionMode(false);
        }
    }

    private void startActionMode(final boolean fromUser) {
        if (mActionMode == null) {
            mActionMode = mActionModeCompat.startActionMode(mActionModeCallbacks);
            notifyAllItemsCheckedChanged(fromUser);
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void unregisterChoiceModeObserver(@NonNull final ChoiceModeObserver observer) {
        super.unregisterChoiceModeObserver(observer);

        // It's safe to call finishActionMode here because normally
        // {@link onSaveInstanceState(Bundle)} will be called before
        // detach and per-instance state will be saved.
        finishActionMode();
    }

    /**
     * Finish and close this action mode. The action mode's {@link ModalChoiceModeListener} will
     * have its {@link ModalChoiceModeListener#onDestroyActionMode(ActionMode)} method called.
     */
    public void finish() {
        clearChoices();
        finishActionMode();
    }

    private void finishActionMode() {
        if (mActionMode != null) {
            mActionMode.finish();
            mActionMode = null;
        }
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

    private class ActionModeCallbacks implements ModalChoiceModeListener {

        private boolean mFinishFromUser;

        @NonNull
        private ModalChoiceModeListener mModalChoiceModeListener;

        private ActionModeCallbacks(@NonNull final ModalChoiceModeListener listener) {
            mModalChoiceModeListener = listener;
        }

        @Override
        public boolean onCreateActionMode(final ActionMode mode, final Menu menu) {
            return mModalChoiceModeListener.onCreateActionMode(mode, menu);
        }

        @Override
        public boolean onPrepareActionMode(final ActionMode mode, final Menu menu) {
            return mModalChoiceModeListener.onPrepareActionMode(mode, menu);
        }

        @Override
        public boolean onActionItemClicked(final ActionMode mode, final MenuItem item) {
            return mModalChoiceModeListener.onActionItemClicked(mode, item);
        }

        @Override
        public void onDestroyActionMode(final ActionMode mode) {
            // Ending selection mode means deselecting everything
            clearChoices();

            mModalChoiceModeListener.onDestroyActionMode(mode);
            mActionMode = null;
            notifyAllItemsCheckedChanged(mFinishFromUser);
            mFinishFromUser = false;
        }

        @Override
        public void onItemCheckedStateChanged(@NonNull final ActionMode mode, final long itemId,
                                              final boolean checked, final boolean fromUser) {
            mode.invalidate();
            mModalChoiceModeListener.onItemCheckedStateChanged(mode, itemId, checked, fromUser);

            // If there are no items selected we no longer need the selection mode.
            if (mFinishActionModeOnClearEnabled && getCheckedItemCount() == 0) {
                mFinishFromUser = true;
                mode.finish();
            }
        }

    }

}
