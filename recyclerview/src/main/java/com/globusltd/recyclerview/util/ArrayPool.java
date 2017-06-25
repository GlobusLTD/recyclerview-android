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

import java.util.ArrayDeque;
import java.util.Queue;

/**
 * Simple pool of objects that creates new pooled object automatically
 * when there are no any free object in the pool.
 *
 * @param <T> The pooled type.
 */
public class ArrayPool<T> implements Pool<T> {

    @NonNull
    private final Factory<T> mFactory;

    @NonNull
    private final Queue<T> mQueue;

    public ArrayPool(@NonNull final Factory<T> factory) {
        mFactory = factory;
        mQueue = new ArrayDeque<>();
    }

    /**
     * {@inheritDoc}
     */
    @NonNull
    @Override
    public T obtain() {
        final T instance = mQueue.poll();
        return (instance != null ? instance : mFactory.create());
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void recycle(@NonNull final T instance) {
        if (!mQueue.contains(instance)) {
            mQueue.offer(instance);
        }
    }

}
