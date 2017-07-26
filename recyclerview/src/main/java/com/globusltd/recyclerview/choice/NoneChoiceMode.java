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
import android.support.annotation.NonNull;

/**
 * {@link ChoiceMode} that does not have any choice behavior.
 * It is a default adapter's choice mode.
 */
public final class NoneChoiceMode implements ChoiceMode {
    
    public NoneChoiceMode() {
        super();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean requiresStableIds() {
        return false;
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
        return false;
    }
    
    /**
     * {@inheritDoc}
     */
    @IntRange(from = 0, to = 0)
    @Override
    public int getCheckedItemCount() {
        return 0;
    }
    
    /**
     * {@inheritDoc}
     */
    @Override
    public boolean isItemChecked(final long itemId) {
        return false;
    }
    
    /**
     * {@inheritDoc}
     */
    @Override
    public void setItemChecked(final long itemId, final boolean checked) {
    }
    
    /**
     * {@inheritDoc}
     */
    @Override
    public void clearChoices() {
    }
    
    /**
     * {@inheritDoc}
     */
    @Override
    public boolean onClick(final long itemId) {
        return false;
    }
    
    /**
     * {@inheritDoc}
     */
    @Override
    public boolean onLongClick(final long itemId) {
        return false;
    }
    
    /**
     * {@inheritDoc}
     */
    @Override
    public void registerChoiceModeObserver(@NonNull final ChoiceModeObserver observer) {
    }
    
    /**
     * {@inheritDoc}
     */
    @Override
    public void unregisterChoiceModeObserver(@NonNull final ChoiceModeObserver observer) {
    }
    
}
