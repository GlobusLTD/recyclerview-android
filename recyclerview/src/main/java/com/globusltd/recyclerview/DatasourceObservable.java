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
package com.globusltd.recyclerview;

import android.database.Observable;
import android.support.annotation.IntRange;
import android.support.annotation.Nullable;

public class DatasourceObservable extends Observable<DatasourceObserver> {

    public DatasourceObservable() {
        super();
    }

    public void notifyChanged() {
        final int size = mObservers.size();
        for (int i = size - 1; i >= 0; i--) {
            mObservers.get(i).onChanged();
        }
    }

    public void notifyItemRangeChanged(@IntRange(from = 0) final int positionStart,
                                       @IntRange(from = 0) final int itemCount,
                                       @Nullable final Object payload) {
        final int size = mObservers.size();
        for (int i = size - 1; i >= 0; i--) {
            mObservers.get(i).onItemRangeChanged(positionStart, itemCount, payload);
        }
    }

    public void notifyItemRangeInserted(@IntRange(from = 0) final int positionStart,
                                        @IntRange(from = 0) final int itemCount) {
        final int size = mObservers.size();
        for (int i = size - 1; i >= 0; i--) {
            mObservers.get(i).onItemRangeInserted(positionStart, itemCount);
        }
    }

    public void notifyItemRangeRemoved(@IntRange(from = 0) final int positionStart,
                                       @IntRange(from = 0) final int itemCount) {
        final int size = mObservers.size();
        for (int i = size - 1; i >= 0; i--) {
            mObservers.get(i).onItemRangeRemoved(positionStart, itemCount);
        }
    }

    public void notifyItemMoved(@IntRange(from = 0) final int fromPosition,
                                @IntRange(from = 0) final int toPosition) {
        final int size = mObservers.size();
        for (int i = size - 1; i >= 0; i--) {
            mObservers.get(i).onItemRangeMoved(fromPosition, toPosition, 1);
        }
    }

}
