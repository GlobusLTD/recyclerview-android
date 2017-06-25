package com.globusltd.recyclerview;

import android.support.annotation.IntRange;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.annotation.RestrictTo;
import android.support.v7.widget.RecyclerView;

/**
 * Observer class for watching changes to a {@link Datasource} and
 * dispatching them to the {@link RecyclerView.Adapter}.
 */
@RestrictTo(RestrictTo.Scope.LIBRARY)
class AdapterDatasourceObserver extends DatasourceObserver {

    @NonNull
    private final RecyclerView.Adapter<?> mAdapter;

    AdapterDatasourceObserver(@NonNull final RecyclerView.Adapter<?> adapter) {
        super();
        mAdapter = adapter;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void onChanged() {
        mAdapter.notifyDataSetChanged();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void onItemRangeChanged(@IntRange(from = 0) final int positionStart,
                                   @IntRange(from = 0) final int itemCount,
                                   @Nullable final Object payload) {
        mAdapter.notifyItemRangeChanged(positionStart, itemCount, payload);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void onItemRangeInserted(@IntRange(from = 0) final int positionStart,
                                    @IntRange(from = 0) final int itemCount) {
        mAdapter.notifyItemRangeInserted(positionStart, itemCount);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void onItemRangeRemoved(@IntRange(from = 0) final int positionStart,
                                   @IntRange(from = 0) final int itemCount) {
        mAdapter.notifyItemRangeRemoved(positionStart, itemCount);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void onItemMoved(@IntRange(from = 0) final int fromPosition,
                            @IntRange(from = 0) final int toPosition) {
        mAdapter.notifyItemMoved(fromPosition, toPosition);
    }

}
