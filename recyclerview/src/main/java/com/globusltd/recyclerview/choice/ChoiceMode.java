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

/**
 * Base class for any choice mode that can be used with {@link com.globusltd.recyclerview.Adapter}.
 */

public interface ChoiceMode {
    
    /**
     * Returns true if choice mode is active.
     */
    boolean isActivated();
    
    /**
     * Returns the number of items currently selected.
     * <p/>
     * <p>To determine the specific items that are currently selected, use one of
     * the <code>getChecked*</code> methods.
     *
     * @return The number of items currently selected
     */
    @IntRange(from = 0)
    int getCheckedItemCount();
    
    /**
     * Returns the checked state of the specified position.
     *
     * @param itemId The item whose checked state to return.
     * @return The item's checked state or <code>false</code>.
     */
    boolean isItemChecked(final long itemId);
    
    /**
     * Sets the checked state of the specified position.
     *
     * @param itemId  The item id whose checked state is to be checked.
     * @param checked The new checked state for the item.
     */
    void setItemChecked(final long itemId, final boolean checked);
    
    /**
     * Clears any checked items.
     */
    void clearChoices();
    
    /**
     * Dispatches click on a specific item.
     *
     * @param itemId The item id which has been clicked.
     * @return true if click is handled by choice mode.
     */
    boolean onClick(final long itemId);
    
    /**
     * Dispatches long click on a specific item.
     *
     * @param itemId The item id which has been clicked.
     * @return true if click is handled by choice mode.
     */
    boolean onLongClick(final long itemId);
    
}