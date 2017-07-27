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
package com.globusltd.recyclerview.sample.choicemode;

import android.support.annotation.IntRange;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import com.globusltd.recyclerview.Adapter;
import com.globusltd.recyclerview.datasource.Datasource;
import com.globusltd.recyclerview.sample.data.Person;
import com.globusltd.recyclerview.sample.util.TouchFeedback;
import com.globusltd.recyclerview.view.ClickableViews;
import com.globusltd.recyclerview.view.ItemClickHelper;

class ActivatedPersonsAdapter extends Adapter<Person, ActivatedViewHolder>
        implements ItemClickHelper.Callback<Person> {
    
    ActivatedPersonsAdapter(@NonNull final Datasource<Person> datasource) {
        super(datasource);
        setHasStableIds(true);
    }
    
    @Override
    public long getItemId(final int position) {
        final Person person = getDatasource().get(position);
        return person.getId();
    }
    
    @NonNull
    @Override
    public ActivatedViewHolder onCreateViewHolder(@NonNull final LayoutInflater inflater,
                                                  @NonNull final ViewGroup parent,
                                                  final int viewType) {
        final ActivatedViewHolder viewHolder = ActivatedViewHolder.inflate(inflater, parent);
        TouchFeedback.selectableActivatedItemBackground(viewHolder.itemView);
        return viewHolder;
    }
    
    @Override
    public void onBindViewHolder(@NonNull final ActivatedViewHolder holder,
                                 @NonNull final Person item, final int position) {
        holder.setText1(item.getFullName());
    }

    /* ItemClickHelper.Callback */
    
    @NonNull
    @Override
    public Person get(@IntRange(from = 0) final int position) {
        return getDatasource().get(position);
    }
    
    @NonNull
    @Override
    public ClickableViews getClickableViews(@IntRange(from = 0) final int position,
                                            final int viewType) {
        return ClickableViews.ITEM_VIEW;
    }

    /* End of ItemClickHelper.Callback */
    
}
