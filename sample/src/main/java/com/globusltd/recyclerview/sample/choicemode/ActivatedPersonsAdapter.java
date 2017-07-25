package com.globusltd.recyclerview.sample.choicemode;

import android.support.annotation.IntRange;
import android.support.annotation.NonNull;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import com.globusltd.recyclerview.Adapter;
import com.globusltd.recyclerview.datasource.Datasource;
import com.globusltd.recyclerview.diff.DiffCallback;
import com.globusltd.recyclerview.diff.DiffCallbackFactory;
import com.globusltd.recyclerview.diff.SimpleDatasourcesDiffCallback;
import com.globusltd.recyclerview.sample.data.Person;
import com.globusltd.recyclerview.view.ClickableViews;
import com.globusltd.recyclerview.view.ItemClickHelper;

class ActivatedPersonsAdapter extends Adapter<Person, ActivatedViewHolder>
        implements ItemClickHelper.Callback<Person> {
    
    ActivatedPersonsAdapter(@NonNull final Datasource<Person> datasource) {
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
    public ActivatedViewHolder onCreateViewHolder(@NonNull final LayoutInflater inflater,
                                                  @NonNull final ViewGroup parent,
                                                  final int viewType) {
        return ActivatedViewHolder.inflate(inflater, parent);
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
