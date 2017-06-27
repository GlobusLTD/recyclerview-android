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

import android.support.annotation.IntDef;
import android.support.annotation.MainThread;
import android.support.annotation.NonNull;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.util.LinkedHashMap;
import java.util.Map;

/**
 * {@link LifecycleComposite} provides activity or fragment lifecycle events to
 * the registered {@link LifecycleCallbacks} objects.
 */
@MainThread
public class LifecycleComposite implements LifecycleCallbacks {

    private static final int NONE = 0;
    private static final int STARTED = 1;
    private static final int RESUMED = 1 << 1;

    @IntDef(value = { NONE, STARTED, RESUMED }, flag = true)
    @Retention(RetentionPolicy.SOURCE)
    private @interface LifecycleState {
    }

    @LifecycleState
    private int mLifecycleState = NONE;

    @NonNull
    private final Map<LifecycleCallbacks, Integer> mLifecycleCallbacks;

    public LifecycleComposite() {
        mLifecycleCallbacks = new LinkedHashMap<>();
    }

    /**
     * Add a new {@link LifecycleCallbacks} to the {@link LifecycleComposite},
     * which will be called at the same times as the lifecycle methods of
     * activities or fragments are called. Note that you
     * <em>must</em> be sure to use {@link #unregisterLifecycleCallbacks(LifecycleCallbacks)}
     * when appropriate in the future; this will not be removed for you.
     * <p>
     * The {@link LifecycleComposite} holds latest known lifecycle state to be sent
     * to any later registrations.
     *
     * @param lifecycleCallbacks The interface to call.
     */
    public void registerLifecycleCallbacks(@NonNull final LifecycleCallbacks lifecycleCallbacks) {
        if (!mLifecycleCallbacks.containsKey(lifecycleCallbacks)) {
            @LifecycleState int lifecycleState = NONE;
            if ((mLifecycleState & STARTED) == STARTED) {
                lifecycleCallbacks.onStart();
                lifecycleState |= STARTED;
            }
            if ((mLifecycleState & RESUMED) == RESUMED) {
                lifecycleCallbacks.onResume();
                lifecycleState |= RESUMED;
            }
            mLifecycleCallbacks.put(lifecycleCallbacks, lifecycleState);
        }
    }

    /**
     * Remove a {@link LifecycleCallbacks} object that was previously registered
     * with {@link #registerLifecycleCallbacks(LifecycleCallbacks)}.
     */
    public void unregisterLifecycleCallbacks(@NonNull final LifecycleCallbacks lifecycleCallbacks) {
        if (mLifecycleCallbacks.containsKey(lifecycleCallbacks)) {
            @LifecycleState final int lifecycleState = mLifecycleCallbacks.remove(lifecycleCallbacks);
            if ((lifecycleState & RESUMED) == RESUMED) {
                lifecycleCallbacks.onPause();
            }
            if ((lifecycleState & STARTED) == STARTED) {
                lifecycleCallbacks.onStop();
            }
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void onStart() {
        mLifecycleState |= STARTED;

        for (final Map.Entry<LifecycleCallbacks, Integer> entry : mLifecycleCallbacks.entrySet()) {
            @LifecycleState final int lifecycleState = entry.getValue();
            if ((lifecycleState & STARTED) != STARTED) {
                final LifecycleCallbacks lifecycleCallbacks = entry.getKey();
                lifecycleCallbacks.onStart();
                entry.setValue(lifecycleState | STARTED);
            }
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void onResume() {
        mLifecycleState |= RESUMED;

        for (final Map.Entry<LifecycleCallbacks, Integer> entry : mLifecycleCallbacks.entrySet()) {
            @LifecycleState final int lifecycleState = entry.getValue();
            if ((lifecycleState & RESUMED) != RESUMED) {
                final LifecycleCallbacks lifecycleCallbacks = entry.getKey();
                lifecycleCallbacks.onResume();
                entry.setValue(lifecycleState | RESUMED);
            }
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void onPause() {
        mLifecycleState &= ~RESUMED;

        for (final Map.Entry<LifecycleCallbacks, Integer> entry : mLifecycleCallbacks.entrySet()) {
            @LifecycleState final int lifecycleState = entry.getValue();
            if ((lifecycleState & RESUMED) == RESUMED) {
                final LifecycleCallbacks lifecycleCallbacks = entry.getKey();
                lifecycleCallbacks.onPause();
                entry.setValue(lifecycleState & ~RESUMED);
            }
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void onStop() {
        mLifecycleState &= ~STARTED;

        for (final Map.Entry<LifecycleCallbacks, Integer> entry : mLifecycleCallbacks.entrySet()) {
            @LifecycleState final int lifecycleState = entry.getValue();
            if ((lifecycleState & STARTED) == STARTED) {
                final LifecycleCallbacks lifecycleCallbacks = entry.getKey();
                lifecycleCallbacks.onStop();
                entry.setValue(lifecycleState & ~STARTED);
            }
        }
    }

}
