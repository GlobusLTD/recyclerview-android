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

import android.annotation.TargetApi;
import android.content.Context;
import android.os.Build;
import android.support.annotation.MainThread;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.RecyclerView;
import android.util.SparseBooleanArray;
import android.view.View;
import android.widget.Checkable;

import com.globusltd.recyclerview.ViewHolderObserver;
import com.globusltd.recyclerview.ViewHolderTracker;
import com.globusltd.recyclerview.choice.CheckableViewHolder;
import com.globusltd.recyclerview.choice.ChoiceMode;
import com.globusltd.recyclerview.choice.ChoiceModeObserver;
import com.globusltd.recyclerview.choice.NoneChoiceMode;

/**
 * This is an utility class to add choice mode support to RecyclerView.
 */
@MainThread
public class ChoiceModeHelper<E> extends ItemClickHelper<E> {

    private static final ChoiceMode DEFAULT_CHOICE_MODE = new NoneChoiceMode();

    @NonNull
    private ChoiceMode mChoiceMode;

    @NonNull
    private final ChoiceModeObserver mChoiceModeObserver;

    @NonNull
    private final SparseBooleanArray mCheckableViewTypes;

    @NonNull
    private final SparseBooleanArray mCheckableViewHolderTypes;

    @NonNull
    private final ViewHolderTracker mViewHolderTracker;

    @Nullable
    private ViewHolderObserver mViewHolderObserver;

    public ChoiceModeHelper(@NonNull final Callback<E> callback) {
        this(callback, DEFAULT_CHOICE_MODE);
    }

    public ChoiceModeHelper(@NonNull final Callback<E> callback,
                            @NonNull final ChoiceMode choiceMode) {
        super(callback);
        mChoiceMode = choiceMode;
        mChoiceModeObserver = new ChoiceModeObserverImpl();
        mCheckableViewTypes = new SparseBooleanArray();
        mCheckableViewHolderTypes = new SparseBooleanArray();
        mViewHolderTracker = new ViewHolderTracker();
    }

    /**
     * Sets a {@link ChoiceMode} implementation to handle selected items.
     *
     * @param choiceMode {@link ChoiceMode} implementation.
     */
    public void setChoiceMode(@NonNull final ChoiceMode choiceMode) {
        mChoiceMode.unregisterChoiceModeObserver(mChoiceModeObserver);
        mChoiceMode = choiceMode;
        if (isAttached()) {
            mChoiceMode.registerChoiceModeObserver(mChoiceModeObserver);
            // TODO: invalidate visible view holders
        }
    }

    @Override
    public boolean setRecyclerView(@Nullable final RecyclerView recyclerView) {
        final boolean handled = super.setRecyclerView(recyclerView);
        if (handled) {
            mCheckableViewTypes.clear();
            mCheckableViewHolderTypes.clear();
            if (mViewHolderObserver != null) {
                mViewHolderTracker.unregisterViewHolderObserver(mViewHolderObserver);
                mViewHolderObserver = null;
            }
            if (recyclerView != null) {
                mViewHolderObserver = new ChoiceModeViewHolderObserver();
                mViewHolderTracker.registerViewHolderObserver(mViewHolderObserver);
            }
            mViewHolderTracker.setRecyclerView(recyclerView);
        }
        return handled;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void onAttachedToRecyclerView(@NonNull final RecyclerView recyclerView) {
        super.onAttachedToRecyclerView(recyclerView);
        mChoiceMode.registerChoiceModeObserver(mChoiceModeObserver);
    }

    @Override
    public void onDetachedFromRecyclerView(@NonNull final RecyclerView recyclerView) {
        mChoiceMode.unregisterChoiceModeObserver(mChoiceModeObserver);
        super.onDetachedFromRecyclerView(recyclerView);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected boolean performClick(@NonNull final RecyclerView.ViewHolder viewHolder,
                                   @NonNull final View view) {
        final boolean isItemViewClicked = (viewHolder.itemView == view);
        return (isItemViewClicked && mChoiceMode.onClick(viewHolder.getItemId())) ||
                super.performClick(viewHolder, view);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected boolean performLongPress(@NonNull final RecyclerView.ViewHolder viewHolder,
                                       @NonNull final View view) {
        final boolean isItemViewClicked = (viewHolder.itemView == view);
        return (isItemViewClicked && mChoiceMode.onLongClick(viewHolder.getItemId())) ||
                super.performLongPress(viewHolder, view);
    }

    private class ChoiceModeViewHolderObserver implements ViewHolderObserver {

        @Override
        public void onViewHolderAttached(@NonNull final RecyclerView.ViewHolder viewHolder) {
            onViewHolderCheckedChanged(viewHolder, false);
        }

        @Override
        public void onViewHolderPositionChanged(@NonNull final RecyclerView.ViewHolder viewHolder) {
            onViewHolderCheckedChanged(viewHolder, false);
        }

        @Override
        public void onViewHolderDetached(@NonNull final RecyclerView.ViewHolder viewHolder) {
            setViewHolderChecked(viewHolder, false);
            updateCheckableViewHolder(viewHolder, false, false, false);
        }

    }

    private class ChoiceModeObserverImpl extends ChoiceModeObserver {

        @Override
        public void onItemCheckedChanged(final long itemId, final boolean fromUser) {
            final RecyclerView recyclerView = getRecyclerView();
            final RecyclerView.ViewHolder viewHolder = (recyclerView != null ?
                    recyclerView.findViewHolderForItemId(itemId) : null);
            if (viewHolder != null) {
                onViewHolderCheckedChanged(viewHolder, fromUser);
            }
        }

    }

    private void onViewHolderCheckedChanged(@NonNull final RecyclerView.ViewHolder viewHolder,
                                            final boolean fromUser) {
        final boolean isActivated = mChoiceMode.isActivated();
        final long itemId = viewHolder.getItemId();
        final boolean isChecked = mChoiceMode.isItemChecked(itemId);
        setViewHolderChecked(viewHolder, isChecked);
        updateCheckableViewHolder(viewHolder, isActivated, isChecked, fromUser);
    }

    @TargetApi(Build.VERSION_CODES.HONEYCOMB)
    private void setViewHolderChecked(@NonNull final RecyclerView.ViewHolder viewHolder,
                                      final boolean isChecked) {
        final View itemView = viewHolder.itemView;
        if (isCheckableView(viewHolder)) {
            ((Checkable) itemView).setChecked(isChecked);
        } else if (shouldUseActivated(viewHolder)) {
            itemView.setActivated(isChecked);
        }
    }

    private boolean isCheckableView(@NonNull final RecyclerView.ViewHolder viewHolder) {
        final int viewType = viewHolder.getItemViewType();
        if (mCheckableViewTypes.indexOfKey(viewType) >= 0) {
            return mCheckableViewTypes.get(viewType);

        } else {
            final boolean isCheckable = Checkable.class.isInstance(viewHolder.itemView);
            mCheckableViewTypes.put(viewType, isCheckable);
            return isCheckable;
        }
    }

    private boolean shouldUseActivated(@NonNull final RecyclerView.ViewHolder viewHolder) {
        final Context context = viewHolder.itemView.getContext();
        final int targetSdkVersion = context.getApplicationInfo().targetSdkVersion;
        return (targetSdkVersion >= Build.VERSION_CODES.HONEYCOMB);
    }

    private void updateCheckableViewHolder(@NonNull final RecyclerView.ViewHolder viewHolder,
                                           final boolean isActivated, final boolean isChecked,
                                           final boolean fromUser) {
        if (isCheckableViewHolder(viewHolder)) {
            final CheckableViewHolder checkableViewHolder = (CheckableViewHolder) viewHolder;
            checkableViewHolder.setInChoiceMode(isActivated);
            checkableViewHolder.setChecked(isChecked, fromUser);
        }
    }

    private boolean isCheckableViewHolder(@NonNull final RecyclerView.ViewHolder viewHolder) {
        final int viewType = viewHolder.getItemViewType();
        if (mCheckableViewHolderTypes.indexOfKey(viewType) >= 0) {
            return mCheckableViewHolderTypes.get(viewType);

        } else {
            final boolean isCheckable = CheckableViewHolder.class.isInstance(viewHolder);
            mCheckableViewHolderTypes.put(viewType, isCheckable);
            return isCheckable;
        }
    }

}