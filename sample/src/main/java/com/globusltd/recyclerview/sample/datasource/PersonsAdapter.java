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

import android.support.annotation.IntRange;
import android.support.annotation.NonNull;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.globusltd.recyclerview.Adapter;
import com.globusltd.recyclerview.datasource.Datasource;
import com.globusltd.recyclerview.diff.DiffCallback;
import com.globusltd.recyclerview.diff.DiffCallbackFactory;
import com.globusltd.recyclerview.diff.SimpleDatasourcesDiffCallback;
import com.globusltd.recyclerview.sample.R;
import com.globusltd.recyclerview.sample.TwoLinesViewHolder;
import com.globusltd.recyclerview.view.ClickableViews;
import com.globusltd.recyclerview.view.ItemClickHelper;

class PersonsAdapter extends Adapter<Person, PersonsAdapter.TwoLinesAndButtonViewHolder>
        implements ItemClickHelper.Callback<Person> {

    PersonsAdapter(@NonNull final Datasource<Person> datasource) {
        super(datasource, new PersonDiffCallbackFactory());
        setHasStableIds(true);
    }

    @Override
    public long getItemId(final int position) {
        final Person person = getDatasource().get(position);
        return person.getId();
    }

    @NonNull
    @Override
    public TwoLinesAndButtonViewHolder onCreateViewHolder(@NonNull final LayoutInflater inflater,
                                                          @NonNull final ViewGroup parent,
                                                          final int viewType) {
        return TwoLinesAndButtonViewHolder.inflate(inflater, parent);
    }

    @Override
    public void onBindViewHolder(@NonNull final TwoLinesAndButtonViewHolder holder,
                                 @NonNull final Person person, final int position) {
        holder.setText1(person.getLastName());
        holder.setText2(person.getFirstName());
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
        return TwoLinesAndButtonViewHolder.CLICKABLE_VIEWS;
    }

    /* End of ItemClickHelper.Callback */

    static class TwoLinesAndButtonViewHolder extends TwoLinesViewHolder {

        static final ClickableViews CLICKABLE_VIEWS = new ClickableViews(ClickableViews.ITEM_VIEW_ID,
                R.id.action_delete);

        @NonNull
        public static TwoLinesAndButtonViewHolder inflate(@NonNull final LayoutInflater inflater,
                                                          @NonNull final ViewGroup parent) {
            final View itemView = inflater.inflate(R.layout.simple_list_item_2_and_button, parent, false);
            return new TwoLinesAndButtonViewHolder(itemView);
        }

        TwoLinesAndButtonViewHolder(@NonNull final View itemView) {
            super(itemView);
        }

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
                    return TextUtils.equals(oldItem.getFirstName(), newItem.getFirstName()) &&
                            TextUtils.equals(oldItem.getLastName(), newItem.getLastName());
                }

            };
        }

    }

}
