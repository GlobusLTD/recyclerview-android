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
import android.support.annotation.MainThread;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.RecyclerView;

import com.globusltd.recyclerview.choice.ChoiceMode;
import com.globusltd.recyclerview.choice.ChoiceModeOwner;
import com.globusltd.recyclerview.choice.NoneChoiceMode;
import com.globusltd.recyclerview.datasource.Datasource;
import com.globusltd.recyclerview.datasource.Datasources;
import com.globusltd.recyclerview.diff.DiffCallbackFactory;
import com.globusltd.recyclerview.view.ClickableViews;
import com.globusltd.recyclerview.view.ItemClickHelper;
import com.globusltd.recyclerview.view.OnItemClickListener;
import com.globusltd.recyclerview.view.OnItemLongClickListener;

/**
 * Base {@link RecyclerView.Adapter} that handles clicks and choice mode.
 * <p>
 * Notice that you should always detach adapter from RecyclerView when view is destroyed
 * to avoid possible memory leaks. Detaching the adapter from RecyclerView guarantees that
 * all ViewHolders will be recycled. Please see a code snippet below:
 * <pre>
 *     @Override
 *     public void onDestroy() {
 *         super.onDestroy();
 *
 *         // Unbind all active view holders
 *         mRecyclerView.setAdapter(null);
 *     }
 * </pre>
 */
@MainThread
public abstract class Adapter<E, VH extends RecyclerView.ViewHolder>
        extends DatasourceAdapter<E, VH> implements ClickableAdapter<E> {
    
    private static final ChoiceMode DEFAULT_CHOICE_MODE = new NoneChoiceMode();

    @NonNull
    private final ChoiceModeOwner<E, VH> mChoiceModeOwner;

    public Adapter() {
        this(Datasources.<E>empty());
    }

    public Adapter(@NonNull final Datasource<? extends E> datasource) {
        this(datasource, null, DEFAULT_CHOICE_MODE);
    }
    
    public Adapter(@Nullable final DiffCallbackFactory<E> diffCallbackFactory) {
        this(Datasources.<E>empty(), diffCallbackFactory);
    }
    
    public Adapter(@NonNull final Datasource<? extends E> datasource,
                   @Nullable final DiffCallbackFactory<E> diffCallbackFactory) {
        this(datasource, diffCallbackFactory, DEFAULT_CHOICE_MODE);
    }

    public Adapter(@NonNull final Datasource<? extends E> datasource,
                   @Nullable final DiffCallbackFactory<E> diffCallbackFactory,
                   @NonNull final ChoiceMode choiceMode) {
        super(datasource, diffCallbackFactory);
    
        mChoiceModeOwner = new ChoiceModeOwner<>(choiceMode);
        registerRecyclerViewBehavior(mChoiceModeOwner);
        registerViewHolderBehavior(mChoiceModeOwner);
        
        final ItemClickHelper<E, VH> itemClickHelper = new ItemClickHelper<>(this);
        itemClickHelper.setOnItemClickListener(mChoiceModeOwner);
        itemClickHelper.setOnItemLongClickListener(mChoiceModeOwner);
        registerViewHolderBehavior(itemClickHelper);
    }

    /**
     * Register a callback to be invoked when view is clicked.
     *
     * @param onItemClickListener The callback that will run.
     */
    public void setOnItemClickListener(@Nullable final OnItemClickListener<E> onItemClickListener) {
        mChoiceModeOwner.setOnItemClickListener(onItemClickListener);
    }

    /**
     * Register a callback to be invoked when view is long clicked.
     *
     * @param onItemLongClickListener The callback that will run.
     */
    public void setOnItemLongClickListener(@Nullable final OnItemLongClickListener<E> onItemLongClickListener) {
        mChoiceModeOwner.setOnItemLongClickListener(onItemLongClickListener);
    }
    
    /**
     * Sets a {@link ChoiceMode} implementation to handle selected items.
     *
     * @param choiceMode {@link ChoiceMode} implementation.
     */
    public void setChoiceMode(@NonNull final ChoiceMode choiceMode) {
        mChoiceModeOwner.setChoiceMode(choiceMode);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean isEnabled(@IntRange(from = 0) final int position) {
        return true;
    }

    /**
     * {@inheritDoc}
     */
    @NonNull
    @Override
    public ClickableViews getClickableViews(@IntRange(from = 0) final int position, final int viewType) {
        return ClickableViews.NONE;
    }

}