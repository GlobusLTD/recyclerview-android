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

import android.support.v7.widget.RecyclerView;

/**
 * Observer base class for watching changes to a {@link ChoiceMode}.
 * See {@link ChoiceMode#registerChoiceModeObserver(ChoiceModeObserver)} .
 */
public abstract class ChoiceModeObserver {
    
    /**
     * Called when the selection of the item in the choice mode have been changed.
     *
     * @param itemId The item's id or {@link RecyclerView#NO_ID}.
     * @param fromUser True if the checked state change was initiated by the user.
     */
    public void onItemCheckedChanged(final long itemId, final boolean fromUser) {
        // Do nothing
    }
    
}
