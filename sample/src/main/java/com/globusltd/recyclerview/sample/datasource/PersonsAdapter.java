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
package com.globusltd.recyclerview.sample.datasource;

import android.content.res.Resources;
import android.support.annotation.IntRange;
import android.support.annotation.NonNull;
import android.text.TextUtils;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.globusltd.recyclerview.Adapter;
import com.globusltd.recyclerview.datasource.Datasource;
import com.globusltd.recyclerview.diff.DiffCallback;
import com.globusltd.recyclerview.diff.DiffCallbackFactory;
import com.globusltd.recyclerview.diff.SimpleDatasourcesDiffCallback;
import com.globusltd.recyclerview.sample.R;
import com.globusltd.recyclerview.sample.SingleLineViewHolder;
import com.globusltd.recyclerview.view.ClickableViews;

class PersonsAdapter extends Adapter<Person, SingleLineViewHolder> {
    
    PersonsAdapter(@NonNull final Datasource<Person> datasource) {
        super(datasource, new PersonDiffCallbackFactory());
    }
    
    @NonNull
    @Override
    public ClickableViews getClickableViews(@IntRange(from = 0) final int position,
                                            final int viewType) {
        return ClickableViews.ITEM_VIEW;
    }
    
    @NonNull
    @Override
    public SingleLineViewHolder onCreateViewHolder(@NonNull final LayoutInflater inflater,
                                                   @NonNull final ViewGroup parent,
                                                   final int viewType) {
        final View itemView = inflater.inflate(android.R.layout.simple_list_item_1, parent, false);
        final SingleLineViewHolder viewHolder = new SingleLineViewHolder(itemView);
        
        // Typically you should specify ?attr/selectableItemBackground as background in layout file,
        // but for platform views you can specify it programmatically
        final Resources.Theme theme = parent.getContext().getTheme();
        final TypedValue outValue = new TypedValue();
        if (theme.resolveAttribute(R.attr.selectableItemBackground, outValue, true)) {
            viewHolder.itemView.setBackgroundResource(outValue.resourceId);
        }
        
        return viewHolder;
    }
    
    @Override
    public void onBindViewHolder(@NonNull final SingleLineViewHolder holder,
                                 @NonNull final Person person, final int position) {
        holder.setText1(person.getFullName());
    }
    
    private static class PersonDiffCallbackFactory implements DiffCallbackFactory<Person> {
        
        @NonNull
        @Override
        public DiffCallback createDiffCallback(@NonNull final Datasource<? extends Person> oldDatasource,
                                               @NonNull final Datasource<? extends Person> newDatasource) {
            return new SimpleDatasourcesDiffCallback<Person>(oldDatasource, newDatasource) {
                
                @Override
                public boolean areItemsTheSame(@NonNull final Person oldItem, @NonNull final Person newItem) {
                    return oldItem.getId() == newItem.getId();
                }
                
                @Override
                public boolean areContentsTheSame(@NonNull final Person oldItem, @NonNull final Person newItem) {
                    return TextUtils.equals(oldItem.getFirstName(), newItem.getLastName()) &&
                            TextUtils.equals(oldItem.getLastName(), newItem.getLastName());
                }
                
            };
        }
        
    }
    
}
