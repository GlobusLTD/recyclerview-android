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
package com.globusltd.recyclerview.sample.lifecycle;

import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.globusltd.recyclerview.Adapter;
import com.globusltd.recyclerview.datasource.Datasource;
import com.globusltd.recyclerview.lifecycle.LifecycleCallbacks;
import com.globusltd.recyclerview.sample.R;
import com.globusltd.recyclerview.sample.SingleLineViewHolder;
import com.globusltd.recyclerview.sample.TwoLinesViewHolder;
import com.globusltd.recyclerview.sample.data.Person;

class LifecyclePersonAdapter extends Adapter<Person, SingleLineViewHolder> {

    private static final int VIEW_TYPE_SINGLE_LINE = 0;
    private static final int VIEW_TYPE_TWO_LINES = 1;

    LifecyclePersonAdapter(@NonNull final Datasource<? extends Person> datasource) {
        super(datasource);
    }

    @Override
    public int getItemViewType(final int position) {
        return position == 0 ? VIEW_TYPE_TWO_LINES : VIEW_TYPE_SINGLE_LINE;
    }

    @NonNull
    @Override
    public SingleLineViewHolder onCreateViewHolder(@NonNull final LayoutInflater inflater,
                                                   @NonNull final ViewGroup parent,
                                                   final int viewType) {
        switch (viewType) {
            case VIEW_TYPE_SINGLE_LINE:
                return SingleLineViewHolder.inflate(inflater, parent);

            case VIEW_TYPE_TWO_LINES:
                return LifecycleTwoLinesViewHolder.inflate(inflater, parent);

            default:
                throw new IllegalArgumentException("Unknown view type = " + viewType);
        }
    }

    @Override
    public void onBindViewHolder(@NonNull final SingleLineViewHolder holder,
                                 @NonNull final Person person, final int position) {
        holder.setText1(person.getFullName());
    }

    private static class LifecycleTwoLinesViewHolder extends TwoLinesViewHolder
            implements LifecycleCallbacks {

        @NonNull
        public static LifecycleTwoLinesViewHolder inflate(@NonNull final LayoutInflater inflater,
                                                          @NonNull final ViewGroup parent) {
            final View itemView = inflater.inflate(android.R.layout.simple_list_item_2, parent, false);
            return new LifecycleTwoLinesViewHolder(itemView);
        }

        LifecycleTwoLinesViewHolder(@NonNull final View itemView) {
            super(itemView);
        }

        @Override
        public void onStart() {
            setText2(R.string.lifecycle_started);
        }

        @Override
        public void onResume() {
            setText2(R.string.lifecycle_resumed);
        }

        @Override
        public void onPause() {
            setText2(R.string.lifecycle_paused);
        }

        @Override
        public void onStop() {
            setText2(R.string.lifecycle_stopped);
        }

    }

}
