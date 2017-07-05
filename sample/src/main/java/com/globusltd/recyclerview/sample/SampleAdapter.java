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

import android.content.res.Resources;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.util.Log;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.globusltd.recyclerview.Adapter;
import com.globusltd.recyclerview.Datasource;
import com.globusltd.recyclerview.diff.DiffCallback;
import com.globusltd.recyclerview.diff.DiffCallbackFactory;
import com.globusltd.recyclerview.diff.SimpleDatasourcesDiffCallback;
import com.globusltd.recyclerview.view.ClickableViews;
import com.globusltd.recyclerview.view.LifecycleCallbacks;

class SampleAdapter extends Adapter<CharSequence, SampleAdapter.SampleViewHolder> {
    
    SampleAdapter(@NonNull final Datasource<? extends CharSequence> datasource) {
        super(datasource, new CharSequenceDiffCallbackFactory());
    }
    
    @Override
    public boolean isEnabled(final int position) {
        return position % 2 == 0;
    }
    
    @NonNull
    @Override
    public ClickableViews getClickableViews(final int position, final int viewType) {
        return ClickableViews.ITEM_VIEW;
    }
    
    @NonNull
    @Override
    public SampleViewHolder onCreateViewHolder(@NonNull final LayoutInflater inflater,
                                               @NonNull final ViewGroup parent,
                                               final int viewType) {
        final View itemView = inflater.inflate(android.R.layout.simple_list_item_1, parent, false);
        
        final Resources.Theme theme = parent.getContext().getTheme();
        final TypedValue outValue = new TypedValue();
        if (theme.resolveAttribute(R.attr.selectableItemBackground, outValue, true)) {
            itemView.setBackgroundResource(outValue.resourceId);
        }
        
        return new SampleViewHolder(itemView);
    }
    
    @Override
    public void onBindViewHolder(@NonNull final SampleViewHolder holder,
                                 @NonNull final CharSequence item, final int position) {
        holder.setText1(item);
    }
    
    static class SampleViewHolder extends RecyclerView.ViewHolder implements LifecycleCallbacks {
        
        private static final String TAG = "SampleViewHolder";
        
        @NonNull
        private final TextView mTextView1;
        
        SampleViewHolder(@NonNull final View itemView) {
            super(itemView);
            mTextView1 = (TextView) itemView.findViewById(android.R.id.text1);
        }
        
        void setText1(@NonNull final CharSequence item) {
            mTextView1.setText(item);
        }
        
        @Override
        public void onStart() {
            if (getAdapterPosition() == 0) {
                Log.d(TAG, "onStart()");
            }
        }
        
        @Override
        public void onResume() {
            if (getAdapterPosition() == 0) {
                Log.d(TAG, "onResume()");
            }
        }
        
        @Override
        public void onPause() {
            if (getAdapterPosition() == 0) {
                Log.d(TAG, "onPause()");
            }
        }
        
        @Override
        public void onStop() {
            if (getAdapterPosition() == 0) {
                Log.d(TAG, "onStop()");
            }
        }
        
    }
    
    private static class CharSequenceDiffCallbackFactory implements DiffCallbackFactory<CharSequence> {
        
        @NonNull
        @Override
        public DiffCallback createDiffCallback(@NonNull final Datasource<? extends CharSequence> oldDatasource,
                                               @NonNull final Datasource<? extends CharSequence> newDatasource) {
            return new SimpleDatasourcesDiffCallback<CharSequence>(oldDatasource, newDatasource) {
                
                @Override
                public boolean areItemsTheSame(@NonNull final CharSequence oldItem,
                                               @NonNull final CharSequence newItem) {
                    return TextUtils.equals(oldItem, newItem);
                }
                
                @Override
                public boolean areContentsTheSame(@NonNull final CharSequence oldItem,
                                                  @NonNull final CharSequence newItem) {
                    return true;
                }
                
            };
        }
        
    }
    
}
