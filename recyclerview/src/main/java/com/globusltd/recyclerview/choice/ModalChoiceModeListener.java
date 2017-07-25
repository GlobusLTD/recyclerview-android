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

import android.support.annotation.NonNull;
import android.support.v7.view.ActionMode;

/**
 * A ModalChoiceModeListener receives events for this choice mode.
 * It acts as the {@link ActionMode.Callback} for the selection mode and also receives
 * {@link #onItemCheckedStateChanged(ActionMode, long, boolean, boolean)} events when the user
 * selects and deselects list items.
 */
public interface ModalChoiceModeListener extends ActionMode.Callback {
    
    /**
     * Called when an item is checked or unchecked during selection mode.
     *
     * @param mode    The {@link ActionMode} providing the selection mode.
     * @param itemId  The item id that was checked or unchecked.
     * @param checked <code>true</code> if the item is now checked, <code>false</code>
     *                if the item is now unchecked.
     * @param fromUser true if the checked state change was initiated by the user.
     */
    void onItemCheckedStateChanged(@NonNull final ActionMode mode,
                                   final long itemId, boolean checked,
                                   final boolean fromUser);

}
