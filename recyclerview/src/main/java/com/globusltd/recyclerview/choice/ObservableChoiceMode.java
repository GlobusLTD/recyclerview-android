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

import android.support.annotation.MainThread;
import android.support.annotation.NonNull;
import android.support.annotation.RestrictTo;
import android.support.v7.widget.RecyclerView;

/**
 * Base class for any choice mode.
 * Class is designed for handle notification events to the visible view holders.
 */
@MainThread
@RestrictTo(RestrictTo.Scope.LIBRARY)
public abstract class ObservableChoiceMode implements ChoiceMode {
    
    @NonNull
    private final ChoiceModeObservable mChoiceModeObservable;
    
    public ObservableChoiceMode() {
        mChoiceModeObservable = new ChoiceModeObservable();
    }
    
    /**
     * Returns true when choice mode is attached to adapter and RecyclerView.
     * @return true when choice mode is attached to adapter and RecyclerView, false otherwise.
     */
    public final boolean isAttached() {
        return mChoiceModeObservable.hasObservers();
    }
    
    /**
     * {@inheritDoc}
     */
    @Override
    public void registerChoiceModeObserver(@NonNull final ChoiceModeObserver observer) {
        mChoiceModeObservable.registerObserver(observer);
    }
    
    /**
     * {@inheritDoc}
     */
    @Override
    public void unregisterChoiceModeObserver(@NonNull final ChoiceModeObserver observer) {
        mChoiceModeObservable.unregisterObserver(observer);
    }
    
    /**
     * Notifies the registered observers that the selection of the item in the choice mode
     * have been changed. Note that method can be called multiple times.
     *
     * @param itemId The item's id or {@link RecyclerView#NO_ID}.
     * @param fromUser True if the checked state change was initiated by the user.
     */
    public void notifyItemCheckedChanged(final long itemId, final boolean fromUser) {
        mChoiceModeObservable.notifyItemCheckedChanged(itemId, fromUser);
    }
    
}