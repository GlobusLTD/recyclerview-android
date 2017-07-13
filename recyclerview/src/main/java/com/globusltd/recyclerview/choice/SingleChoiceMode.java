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

import android.support.annotation.IntRange;
import android.support.annotation.Nullable;
import android.support.v7.widget.RecyclerView;

/**
 * {@link ChoiceMode} that allows up to one choice.
 */
public class SingleChoiceMode implements ChoiceMode {
    
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
        super();
    }
    
    /**
     * Set single choice mode callback.
     */
    public void setChoiceModeListener(@Nullable final SimpleChoiceModeListener listener) {
        mChoiceModeListener = listener;
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
        final long checkedId = mCheckedId;
        mCheckedId = (checked ? itemId : RecyclerView.NO_ID);
    
        if (mChoiceModeListener != null) {
            mChoiceModeListener.onItemCheckedStateChanged(itemId, checked);
        }
    
        // TODO: notifyItemCheckedChanged(checkedId);
        // TODO: notifyItemCheckedChanged(mCheckedId);
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
        // TODO: notifyItemCheckedChanged(itemId);
    }
    
    /**
     * {@inheritDoc}
     */
    @Override
    public boolean onClick(final long itemId) {
        if (!isItemChecked(itemId)) {
            setItemChecked(itemId, true);
        }
        return true;
    }
    
    /**
     * {@inheritDoc}
     */
    @Override
    public boolean onLongClick(final long itemId) {
        return true;
    }
    
}
