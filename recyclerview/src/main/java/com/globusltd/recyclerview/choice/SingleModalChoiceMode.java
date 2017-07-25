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
import android.support.v7.widget.RecyclerView;
import android.view.Menu;
import android.view.MenuItem;

public class SingleModalChoiceMode extends ObservableChoiceMode {
    
    private static final String KEY_SINGLE_MODAL_CHOICE_MODE = "single_modal_choice_mode";
    private static final String KEY_CHECKED_ID = "checked_id";
    
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
     * Running state of which ID are currently checked.
     */
    private long mCheckedId = RecyclerView.NO_ID;
    
    public SingleModalChoiceMode(@NonNull final ActionModeCompat actionModeCompat,
                                 @NonNull final ModalChoiceModeListener listener) {
        this(actionModeCompat, listener, null);
    }
    
    public SingleModalChoiceMode(@NonNull final ActionModeCompat actionModeCompat,
                                 @NonNull final ModalChoiceModeListener listener,
                                 @Nullable final Bundle savedInstanceState) {
        super();
        mActionModeCompat = actionModeCompat;
        mActionModeCallbacks = new ActionModeCallbacks(listener);
        
        final Bundle state = (savedInstanceState != null ?
                savedInstanceState.getBundle(KEY_SINGLE_MODAL_CHOICE_MODE) : null);
        if (state != null) {
            mCheckedId = state.getLong(KEY_CHECKED_ID, RecyclerView.NO_ID);
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
    public boolean isActivated() {
        return (mActionMode != null);
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
        if (checked) {
            startActionMode(fromUser);
        }
        
        final long checkedId = mCheckedId;
        mCheckedId = (checked ? itemId : RecyclerView.NO_ID);
        
        if (mActionMode != null) {
            mActionModeCallbacks.onItemCheckedStateChanged(mActionMode, itemId, checked, fromUser);
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
        if (mActionMode != null) {
            mActionMode.invalidate();
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
        if (mCheckedId != RecyclerView.NO_ID) {
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
    public void finishActionMode() {
        clearChoices();
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
        state.putLong(KEY_CHECKED_ID, mCheckedId);
        outState.putBundle(KEY_SINGLE_MODAL_CHOICE_MODE, state);
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
