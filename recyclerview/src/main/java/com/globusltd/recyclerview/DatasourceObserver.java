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

import android.support.annotation.IntRange;
import android.support.annotation.Nullable;

/**
 * Observer base class for watching changes to a {@link Datasource}.
 * See {@link Datasource#registerDatasourceObserver(DatasourceObserver)}.
 */
public abstract class DatasourceObserver {

    public void onChanged() {
        // Do nothing
    }

    public void onItemRangeChanged(@IntRange(from = 0) final int positionStart,
                                   @IntRange(from = 0) final int itemCount,
                                   @Nullable final Object payload) {
        // Do nothing
    }

    public void onItemRangeInserted(@IntRange(from = 0) final int positionStart,
                                    @IntRange(from = 0) final int itemCount) {
        // Do nothing
    }

    public void onItemRangeRemoved(@IntRange(from = 0) final int positionStart,
                                   @IntRange(from = 0) final int itemCount) {
        // Do nothing
    }

    public void onItemRangeMoved(@IntRange(from = 0) final int fromPosition,
                                 @IntRange(from = 0) final int toPosition,
                                 @IntRange(from = 0) final int itemCount) {
        // Do nothing
    }

}