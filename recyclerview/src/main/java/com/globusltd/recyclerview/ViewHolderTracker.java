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
import android.support.v4.util.SimpleArrayMap;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewTreeObserver;

import com.globusltd.recyclerview.util.ArrayPool;
import com.globusltd.recyclerview.util.Pool;

import java.util.ArrayList;
import java.util.List;

/**
 * {@link ViewHolderTracker} provides view holder attach/detach events to
 * the registered {@link ViewHolderObserver} objects.
 */
@MainThread
public class ViewHolderTracker extends RecyclerViewOwner {

    @NonNull
    private final List<ViewHolderObserver> mBehaviors;

    @Nullable
    private OnChildAttachStateChangeListener mOnChildAttachStateChangeListener;

    public ViewHolderTracker() {
        mBehaviors = new ArrayList<>();
    }

    /**
     * Add a new {@link ViewHolderObserver} to the {@link ViewHolderTracker},
     * which will be called at the same times as the attach/detach methods of
     * adapter are called.
     *
     * @param viewHolderObserver The interface to call.
     */
    public void registerViewHolderObserver(@NonNull final ViewHolderObserver viewHolderObserver) {
        if (!mBehaviors.contains(viewHolderObserver)) {
            mBehaviors.add(viewHolderObserver);
        }
    }

    /**
     * Remove a {@link ViewHolderObserver} object that was previously registered
     * with {@link #registerViewHolderObserver(ViewHolderObserver)}.
     */
    public void unregisterViewHolderObserver(@NonNull final ViewHolderObserver viewHolderObserver) {
        mBehaviors.remove(viewHolderObserver);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void onAttachedToRecyclerView(@NonNull final RecyclerView recyclerView) {
        mOnChildAttachStateChangeListener = new OnChildAttachStateChangeListener(recyclerView);
        mOnChildAttachStateChangeListener.onAttachedToRecyclerView();
        recyclerView.addOnChildAttachStateChangeListener(mOnChildAttachStateChangeListener);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void onDetachedFromRecyclerView(@NonNull final RecyclerView recyclerView) {
        if (mOnChildAttachStateChangeListener != null) {
            recyclerView.removeOnChildAttachStateChangeListener(mOnChildAttachStateChangeListener);
            mOnChildAttachStateChangeListener.onDetachedFromRecyclerView();
            mOnChildAttachStateChangeListener = null;
        }
    }

    private void notifyViewHolderAttached(@NonNull final RecyclerView.ViewHolder viewHolder) {
        final int size = mBehaviors.size();
        for (int i = size - 1; i >= 0; i--) {
            final ViewHolderObserver behavior = mBehaviors.get(i);
            behavior.onViewHolderAttached(viewHolder);
        }
    }

    private void notifyViewHolderPositionChanged(@NonNull final RecyclerView.ViewHolder viewHolder) {
        final int size = mBehaviors.size();
        for (int i = size - 1; i >= 0; i--) {
            final ViewHolderObserver behavior = mBehaviors.get(i);
            behavior.onViewHolderPositionChanged(viewHolder);
        }
    }

    private void notifyViewHolderDetached(@NonNull final RecyclerView.ViewHolder viewHolder) {
        final int size = mBehaviors.size();
        for (int i = size - 1; i >= 0; i--) {
            final ViewHolderObserver behavior = mBehaviors.get(i);
            behavior.onViewHolderDetached(viewHolder);
        }
    }

    private class OnChildAttachStateChangeListener implements RecyclerView.OnChildAttachStateChangeListener {

        @NonNull
        private final RecyclerView mHostView;

        @NonNull
        private final Pool<OnViewHolderPositionChangeListener> mPool;

        @NonNull
        private final SimpleArrayMap<View, OnViewHolderPositionChangeListener> mOnPositionChangeListeners;

        private OnChildAttachStateChangeListener(@NonNull final RecyclerView recyclerView) {
            mHostView = recyclerView;
            mPool = new ArrayPool<>(new OnViewHolderPositionChangeListenerFactory());
            mOnPositionChangeListeners = new SimpleArrayMap<>();
        }

        void onAttachedToRecyclerView() {
            final int childCount = mHostView.getChildCount();
            for (int index = 0; index < childCount - 1; index++) {
                final View child = mHostView.getChildAt(index);
                onChildViewAttachedToWindow(child);
            }
        }

        @Override
        public void onChildViewAttachedToWindow(final View view) {
            final RecyclerView.ViewHolder viewHolder = mHostView.findContainingViewHolder(view);
            if (viewHolder != null && !mOnPositionChangeListeners.containsKey(view)) {
                final OnViewHolderPositionChangeListener listener = mPool.obtain();
                listener.viewHolder = viewHolder;
                listener.position = viewHolder.getAdapterPosition();
                view.getViewTreeObserver().addOnPreDrawListener(listener);
                mOnPositionChangeListeners.put(view, listener);

                notifyViewHolderAttached(viewHolder);
            }
        }

        @Override
        public void onChildViewDetachedFromWindow(final View view) {
            final OnViewHolderPositionChangeListener listener = mOnPositionChangeListeners.remove(view);
            if (listener != null && listener.viewHolder != null) {
                view.getViewTreeObserver().removeOnPreDrawListener(listener);

                notifyViewHolderDetached(listener.viewHolder);

                listener.viewHolder = null;
                listener.position = RecyclerView.NO_POSITION;
                mPool.recycle(listener);
            }
        }

        void onDetachedFromRecyclerView() {
            final int size = mOnPositionChangeListeners.size();
            for (int index = size - 1; index >= 0; index--) {
                final View view = mOnPositionChangeListeners.keyAt(index);
                onChildViewDetachedFromWindow(view);
            }
        }

    }

    private class OnViewHolderPositionChangeListenerFactory implements
            Pool.Factory<OnViewHolderPositionChangeListener> {

        @NonNull
        @Override
        public OnViewHolderPositionChangeListener create() {
            return new OnViewHolderPositionChangeListener();
        }

    }

    private class OnViewHolderPositionChangeListener implements ViewTreeObserver.OnPreDrawListener {

        @Nullable
        RecyclerView.ViewHolder viewHolder;

        @IntRange(from = RecyclerView.NO_POSITION)
        int position;

        @Override
        public boolean onPreDraw() {
            final boolean isPositionChanged = (viewHolder != null && viewHolder.getAdapterPosition() != position);
            if (isPositionChanged) {
                position = viewHolder.getAdapterPosition();
                if (position != RecyclerView.NO_POSITION) {
                    notifyViewHolderPositionChanged(viewHolder);
                }
            }
            return true;
        }

    }

}