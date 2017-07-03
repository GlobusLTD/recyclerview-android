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

import android.support.annotation.CallSuper;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.globusltd.recyclerview.datasource.Datasources;
import com.globusltd.recyclerview.diff.DiffCallbackFactory;
import com.globusltd.recyclerview.view.ViewHolderBehaviorComposite;

import java.util.List;

/**
 * Base {@link RecyclerView.Adapter} that holds a reference to the {@link Datasource} object
 * and provides simple bindings.
 */
public abstract class Adapter<E, VH extends RecyclerView.ViewHolder> extends RecyclerView.Adapter<VH>
        implements DatasourceSwappable<E>, DatasourceAdapter<E> {
    
    @NonNull
    private final DatasourceProxy<E> mDatasource;
    
    @NonNull
    private final RecyclerViewBehavior mDatasourceAdapterBehavior;
    
    @NonNull
    private final ViewHolderBehaviorComposite<VH> mViewHolderBehaviorComposite;
    
    public Adapter() {
        this(Datasources.<E>empty());
    }
    
    public Adapter(@NonNull final Datasource<? extends E> datasource) {
        this(datasource, null);
    }
    
    public Adapter(@NonNull final Datasource<? extends E> datasource,
                   @Nullable final DiffCallbackFactory<E> diffCallbackFactory) {
        super();
        mDatasource = new DatasourceProxy<>(datasource, diffCallbackFactory);
        
        final DatasourceObserver datasourceObserver = new AdapterDatasourceObserver(this);
        mDatasourceAdapterBehavior = new DatasourceAdapterBehavior(mDatasource, datasourceObserver);
        
        mViewHolderBehaviorComposite = new ViewHolderBehaviorComposite<>();
    }
    
    /**
     * Returns an immutable {@link Datasource} instance.
     *
     * @return An immutable {@link Datasource} instance.
     * @see #swap(Datasource)
     */
    @NonNull
    @Override
    public Datasource<? extends E> getDatasource() {
        return mDatasource;
    }
    
    /**
     * {@inheritDoc}
     */
    @Nullable
    @Override
    public Datasource<? extends E> swap(@NonNull final Datasource<? extends E> datasource) {
        return mDatasource.swap(datasource);
    }
    
    /**
     * {@inheritDoc}
     */
    @Override
    public int getItemCount() {
        return mDatasource.size();
    }
    
    /**
     * Add a new {@link ViewHolderBehavior} to the {@link ViewHolderBehaviorComposite},
     * which will be called at the same times as the attach/detach methods of the
     * adapter are called.
     *
     * @param viewHolderBehavior The interface to call.
     */
    public void addViewHolderBehavior(@NonNull final ViewHolderBehavior<VH> viewHolderBehavior) {
        mViewHolderBehaviorComposite.addViewHolderBehavior(viewHolderBehavior);
    }
    
    /**
     * Remove a {@link ViewHolderBehavior} object that was previously registered
     * with {@link #addViewHolderBehavior(ViewHolderBehavior)}.
     */
    public void removeViewHolderBehavior(@NonNull final ViewHolderBehavior<VH> viewHolderBehavior) {
        mViewHolderBehaviorComposite.removeViewHolderBehavior(viewHolderBehavior);
    }
    
    @Override
    public final VH onCreateViewHolder(@NonNull final ViewGroup parent, final int viewType) {
        final LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        return onCreateViewHolder(inflater, parent, viewType);
    }
    
    /**
     * Called when RecyclerView needs a new {@link RecyclerView.ViewHolder} of the given type to represent
     * an item.
     * <p>
     * This new ViewHolder should be constructed with a new View that can represent the items
     * of the given type. You can either create a new View manually or inflate it from an XML
     * layout file.
     * <p>
     * The new ViewHolder will be used to display items of the adapter using
     * {@link #onBindViewHolder(RecyclerView.ViewHolder, int, List)}. Since it will be re-used to display
     * different items in the data set, it is a good idea to cache references to sub views of
     * the View to avoid unnecessary {@link View#findViewById(int)} calls.
     *
     * @param inflater {@link LayoutInflater} to inflate views from XML.
     * @param parent   The ViewGroup into which the new View will be added after it is bound to
     *                 an adapter position.
     * @param viewType The view type of the new View.
     * @return A new ViewHolder that holds a View of the given view type.
     * @see #getItemViewType(int)
     * @see #onBindViewHolder(RecyclerView.ViewHolder, int)
     */
    @NonNull
    public abstract VH onCreateViewHolder(@NonNull final LayoutInflater inflater,
                                          @NonNull final ViewGroup parent, final int viewType);
    
    @Override
    public final void onBindViewHolder(final VH holder, final int position) {
        final E item = mDatasource.get(position);
        onBindViewHolder(holder, item, position);
        mViewHolderBehaviorComposite.onAttachViewHolder(holder);
    }
    
    /**
     * Called by RecyclerView to display the data at the specified position. This method should
     * update the contents of the {@link RecyclerView.ViewHolder#itemView} to reflect the item at the given
     * position.
     * <p>
     * Note that unlike {@link android.widget.ListView}, RecyclerView will not call this method
     * again if the position of the item changes in the data set unless the item itself is
     * invalidated or the new position cannot be determined. For this reason, you should only
     * use the <code>position</code> parameter while acquiring the related data item inside
     * this method and should not keep a copy of it. If you need the position of an item later
     * on (e.g. in a click listener), use {@link RecyclerView.ViewHolder#getAdapterPosition()} which will
     * have the updated adapter position.
     *
     * @param holder   The ViewHolder which should be updated to represent the contents of the
     *                 item at the given position in the data set.
     * @param item     The item at the given position in the data set.
     * @param position The position of the item within the adapter's data set.
     */
    public abstract void onBindViewHolder(@NonNull final VH holder, @NonNull final E item,
                                          final int position);
    
    @Override
    public final void onBindViewHolder(final VH holder, final int position,
                                       final List<Object> payloads) {
        final E item = mDatasource.get(position);
        onBindViewHolder(holder, item, position, payloads);
        mViewHolderBehaviorComposite.onAttachViewHolder(holder);
    }
    
    /**
     * Called by RecyclerView to display the data at the specified position. This method
     * should update the contents of the {@link RecyclerView.ViewHolder#itemView} to reflect
     * the item at the given position.
     * <p>
     * Note that unlike {@link android.widget.ListView}, RecyclerView will not call this method
     * again if the position of the item changes in the data set unless the item itself is
     * invalidated or the new position cannot be determined. For this reason, you should only
     * use the <code>position</code> parameter while acquiring the related data item inside
     * this method and should not keep a copy of it. If you need the position of an item later
     * on (e.g. in a click listener), use {@link RecyclerView.ViewHolder#getAdapterPosition()}
     * which will have the updated adapter position.
     * <p>
     * Partial bind vs full bind:
     * <p>
     * The payloads parameter is a merge list from {@link #notifyItemChanged(int, Object)} or
     * {@link #notifyItemRangeChanged(int, int, Object)}.  If the payloads list is not empty,
     * the ViewHolder is currently bound to old data and Adapter may run an efficient partial
     * update using the payload info.  If the payload is empty,  Adapter must run a full bind.
     * Adapter should not assume that the payload passed in notify methods will be received by
     * onAttachViewHolder(). For example when the view is not attached to the screen, the
     * payload in notifyItemChange() will be simply dropped.
     *
     * @param holder   The ViewHolder which should be updated to represent the contents of the
     *                 item at the given position in the data set.
     * @param position The position of the item within the adapter's data set.
     * @param payloads A non-null list of merged payloads. Can be empty list if requires full
     *                 update.
     */
    public void onBindViewHolder(@NonNull final VH holder, @NonNull final E item,
                                 final int position, final List<Object> payloads) {
        onBindViewHolder(holder, item, position);
    }
    
    /**
     * {@inheritDoc}
     */
    @CallSuper
    @Override
    public void onViewRecycled(final VH holder) {
        mViewHolderBehaviorComposite.onDetachViewHolder(holder);
        super.onViewRecycled(holder);
    }
    
    /**
     * {@inheritDoc}
     */
    @CallSuper
    @Override
    public boolean onFailedToRecycleView(final VH holder) {
        mViewHolderBehaviorComposite.onDetachViewHolder(holder);
        return super.onFailedToRecycleView(holder);
    }
    
    /**
     * {@inheritDoc}
     */
    @CallSuper
    @Override
    public void onAttachedToRecyclerView(final RecyclerView recyclerView) {
        super.onAttachedToRecyclerView(recyclerView);
        mDatasourceAdapterBehavior.onAttachedToRecyclerView(recyclerView);
    }
    
    /**
     * {@inheritDoc}
     */
    @CallSuper
    @Override
    public void onDetachedFromRecyclerView(final RecyclerView recyclerView) {
        mDatasourceAdapterBehavior.onDetachedFromRecyclerView(recyclerView);
        super.onDetachedFromRecyclerView(recyclerView);
    }
    
}
