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

import android.graphics.Rect;
import android.support.annotation.MainThread;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.globusltd.recyclerview.util.Observable;

/**
 * {@link ViewHolderTracker} provides view holder attach/detach events to
 * the registered {@link ViewHolderObserver} objects.
 */
@MainThread
public class ViewHolderTracker extends RecyclerViewOwner {

    @NonNull
    private final ViewHolderObservable mViewHolderObservable;

    @Nullable
    private OnChildAttachStateChangeListener mOnChildAttachStateChangeListener;

    @Nullable
    private RecyclerView.ItemDecoration mItemDecoration;

    public ViewHolderTracker() {
        mViewHolderObservable = new ViewHolderObservable();
    }

    /**
     * Add a new {@link ViewHolderObserver} to the {@link ViewHolderTracker},
     * which will be called at the same times as the attach/detach methods of
     * adapter are called.
     *
     * @param viewHolderObserver The interface to call.
     */
    public void registerViewHolderObserver(@NonNull final ViewHolderObserver viewHolderObserver) {
        mViewHolderObservable.registerObserver(viewHolderObserver);
    }

    /**
     * Remove a {@link ViewHolderObserver} object that was previously registered
     * with {@link #registerViewHolderObserver(ViewHolderObserver)}.
     */
    public void unregisterViewHolderObserver(@NonNull final ViewHolderObserver viewHolderObserver) {
        mViewHolderObservable.unregisterObserver(viewHolderObserver);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected void onAttachedToRecyclerView(@NonNull final RecyclerView recyclerView) {
        mOnChildAttachStateChangeListener = new OnChildAttachStateChangeListener(recyclerView);
        mOnChildAttachStateChangeListener.onAttachedToRecyclerView();
        recyclerView.addOnChildAttachStateChangeListener(mOnChildAttachStateChangeListener);

        mItemDecoration = new ItemDecoration();
        recyclerView.addItemDecoration(mItemDecoration);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected void onDetachedFromRecyclerView(@NonNull final RecyclerView recyclerView) {
        if (mOnChildAttachStateChangeListener != null) {
            recyclerView.removeOnChildAttachStateChangeListener(mOnChildAttachStateChangeListener);
            mOnChildAttachStateChangeListener.onDetachedFromRecyclerView();
            mOnChildAttachStateChangeListener = null;
        }
        if (mItemDecoration != null) {
            recyclerView.removeItemDecoration(mItemDecoration);
            mItemDecoration = null;
        }
    }

    private class OnChildAttachStateChangeListener implements RecyclerView.OnChildAttachStateChangeListener {

        @NonNull
        private final RecyclerView mHostView;

        private OnChildAttachStateChangeListener(@NonNull final RecyclerView recyclerView) {
            mHostView = recyclerView;
        }

        void onAttachedToRecyclerView() {
            final int childCount = mHostView.getChildCount();
            for (int index = 0; index < childCount; index++) {
                final View child = mHostView.getChildAt(index);
                onChildViewAttachedToWindow(child);
            }
        }

        @Override
        public void onChildViewAttachedToWindow(final View view) {
            final RecyclerView.ViewHolder viewHolder = mHostView.findContainingViewHolder(view);
            if (viewHolder != null) {
                mViewHolderObservable.notifyAttached(viewHolder);
            }
        }

        @Override
        public void onChildViewDetachedFromWindow(final View view) {
            final RecyclerView.ViewHolder viewHolder = mHostView.findContainingViewHolder(view);
            if (viewHolder != null) {
                mViewHolderObservable.notifyDetached(viewHolder);
            }
        }

        void onDetachedFromRecyclerView() {
            final int childCount = mHostView.getChildCount();
            for (int index = 0; index < childCount; index++) {
                final View child = mHostView.getChildAt(index);
                onChildViewAttachedToWindow(child);
            }
        }

    }

    private class ItemDecoration extends RecyclerView.ItemDecoration {

        @Override
        public void getItemOffsets(final Rect outRect, final View itemView,
                                   final RecyclerView parent, final RecyclerView.State state) {
            if (state.isMeasuring()) {
                return;
            }

            final RecyclerView.ViewHolder viewHolder = parent.findContainingViewHolder(itemView);
            if (viewHolder == null) {
                return;
            }

            final int position = viewHolder.getAdapterPosition();
            if (state.isPreLayout()) {
                final int oldPosition = viewHolder.getOldPosition();
                if (position > RecyclerView.NO_POSITION && position != oldPosition) {
                    mViewHolderObservable.notifyPositionChanged(viewHolder);
                }
            } else {
                mViewHolderObservable.notifyChanged(viewHolder);
            }
        }

    }

    private static class ViewHolderObservable extends Observable<ViewHolderObserver> {

        void notifyAttached(@NonNull final RecyclerView.ViewHolder viewHolder) {
            final int size = mObservers.size();
            for (int i = size - 1; i >= 0; i--) {
                final ViewHolderObserver behavior = mObservers.get(i);
                behavior.onAttached(viewHolder);
            }
        }

        void notifyPositionChanged(@NonNull final RecyclerView.ViewHolder viewHolder) {
            final int size = mObservers.size();
            for (int i = size - 1; i >= 0; i--) {
                final ViewHolderObserver behavior = mObservers.get(i);
                behavior.onPositionChanged(viewHolder);
            }
        }

        void notifyChanged(@NonNull final RecyclerView.ViewHolder viewHolder) {
            final int size = mObservers.size();
            for (int i = size - 1; i >= 0; i--) {
                final ViewHolderObserver behavior = mObservers.get(i);
                behavior.onChanged(viewHolder);
            }
        }

        void notifyDetached(@NonNull final RecyclerView.ViewHolder viewHolder) {
            final int size = mObservers.size();
            for (int i = size - 1; i >= 0; i--) {
                final ViewHolderObserver behavior = mObservers.get(i);
                behavior.onDetached(viewHolder);
            }
        }

    }

}