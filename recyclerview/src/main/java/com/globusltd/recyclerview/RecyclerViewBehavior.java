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

import android.support.annotation.MainThread;
import android.support.v7.widget.RecyclerView;

@MainThread
public interface RecyclerViewBehavior {
    
    /**
     * Called by RecyclerView when it starts observing this behavior.
     * <p>
     * Keep in mind that same behavior may be observed by multiple RecyclerViews.
     *
     * @param recyclerView The RecyclerView instance which started observing this behavior.
     * @see #onDetachedFromRecyclerView(RecyclerView)
     */
    void onAttachedToRecyclerView(final RecyclerView recyclerView);
    
    /**
     * Called by RecyclerView when it stops observing this behavior.
     *
     * @param recyclerView The RecyclerView instance which stopped observing this behavior.
     * @see #onAttachedToRecyclerView(RecyclerView)
     */
    void onDetachedFromRecyclerView(final RecyclerView recyclerView);
    
}
