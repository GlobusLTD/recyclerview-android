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
package com.globusltd.recyclerview.sample;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.globusltd.recyclerview.Adapter;
import com.globusltd.recyclerview.Datasource;
import com.globusltd.recyclerview.diff.DiffCallback;
import com.globusltd.recyclerview.diff.DiffCallbackFactory;
import com.globusltd.recyclerview.diff.SimpleDatasourcesDiffCallback;

class SampleAdapter extends Adapter<String, SampleAdapter.SampleViewHolder> {

    SampleAdapter(@NonNull final Datasource<? extends String> datasource) {
        super(datasource, new StringDiffCallbackFactory());
    }

    @NonNull
    @Override
    public SampleViewHolder onCreateViewHolder(@NonNull final LayoutInflater inflater,
                                               @NonNull final ViewGroup parent,
                                               final int viewType) {
        final View itemView = inflater.inflate(android.R.layout.simple_list_item_1, parent, false);
        return new SampleViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull final SampleViewHolder holder,
                                 @NonNull final String item, final int position) {
        holder.setText1(item);
    }

    static class SampleViewHolder extends RecyclerView.ViewHolder {

        @NonNull
        private final TextView mTextView1;

        SampleViewHolder(@NonNull final View itemView) {
            super(itemView);
            mTextView1 = (TextView) itemView.findViewById(android.R.id.text1);
        }

        void setText1(@NonNull final String item) {
            mTextView1.setText(item);
        }

    }

    private static class StringDiffCallbackFactory implements DiffCallbackFactory<String> {

        @NonNull
        @Override
        public DiffCallback createDiffCallback(@NonNull final Datasource<? extends String> oldDatasource,
                                               @NonNull final Datasource<? extends String> newDatasource) {
            return new SimpleDatasourcesDiffCallback<String>(oldDatasource, newDatasource) {

                @Override
                public boolean areItemsTheSame(@NonNull final String oldItem, @NonNull final String newItem) {
                    return TextUtils.equals(oldItem, newItem);
                }

                @Override
                public boolean areContentsTheSame(@NonNull final String oldItem, @NonNull final String newItem) {
                    return true;
                }

            };
        }

    }

}
