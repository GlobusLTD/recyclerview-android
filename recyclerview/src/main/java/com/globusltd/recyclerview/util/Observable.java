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
package com.globusltd.recyclerview.util;

import android.support.annotation.NonNull;

import java.util.ArrayList;
import java.util.List;

/**
 * Provides methods for registering or unregistering arbitrary observers in an {@link ArrayList}.
 * <p>
 * This abstract class is intended to be subclassed and specialized to maintain
 * a registry of observers of specific types and dispatch notifications to them.
 *
 * @param <T> The observer type.
 */
public abstract class Observable<T> {
    
    /**
     * The list of observers. An observer can be in the list at most
     * once and will never be null.
     */
    @NonNull
    protected final List<T> mObservers;
    
    public Observable() {
        mObservers = new ArrayList<>();
    }
    
    /**
     * Checks if this observable has any registered observers.
     *
     * @return true if this observable has any registered observers, false otherwise.
     */
    public boolean hasObservers() {
        return !mObservers.isEmpty();
    }
    
    /**
     * Adds an observer to the list.
     *
     * @param observer the observer to register
     */
    public void registerObserver(@NonNull final T observer) {
        synchronized (mObservers) {
            if (!mObservers.contains(observer)) {
                mObservers.add(observer);
            }
        }
    }
    
    /**
     * Removes a previously registered observer.
     *
     * @param observer the observer to unregister
     */
    public void unregisterObserver(@NonNull final T observer) {
        synchronized (mObservers) {
            int index = mObservers.indexOf(observer);
            if (index > -1) {
                mObservers.remove(index);
            }
        }
    }
    
    /**
     * Remove all registered observers.
     */
    public void unregisterAll() {
        synchronized (mObservers) {
            mObservers.clear();
        }
    }
    
}
