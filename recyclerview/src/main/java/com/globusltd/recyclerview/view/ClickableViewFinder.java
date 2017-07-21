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
package com.globusltd.recyclerview.view;

import android.os.Build;
import android.support.annotation.MainThread;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.view.ViewCompat;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewParent;

/**
 * Helper class for searching view that actually has tapped by the user.
 */
@MainThread
class ClickableViewFinder {

    @NonNull
    private final RecyclerView mHostView;

    @NonNull
    private final ItemClickHelper.Callback<?> mCallback;

    ClickableViewFinder(@NonNull final RecyclerView hostView,
                        @NonNull final ItemClickHelper.Callback<?> callback) {
        mHostView = hostView;
        mCallback = callback;
    }

    /**
     * Searches for a clickable view that has tapped by the user.
     *
     * @param x The x coordinate of the touch that caused the search.
     * @param y The y coordinate of the touch that caused the search.
     * @return {@link Target} instance that contains tapped view and containing view holder,
     * null if view has not been found.
     */
    @Nullable
    Target findTarget(final float x, final float y) {
        final View itemView = mHostView.findChildViewUnder(x, y);
        if (itemView == null) {
            return null;
        }

        final RecyclerView.ViewHolder viewHolder = mHostView.getChildViewHolder(itemView);
        final int position = viewHolder.getAdapterPosition();
        final int viewType = viewHolder.getItemViewType();
        if (position <= RecyclerView.NO_POSITION || viewType <= RecyclerView.INVALID_TYPE) {
            return null;
        }

        final ClickableViews clickableViews = mCallback.getClickableViews(position, viewType);
        return findTargetInViewHolder(viewHolder, clickableViews, x, y);
    }

    @Nullable
    private Target findTargetInViewHolder(@NonNull final RecyclerView.ViewHolder viewHolder,
                                          @NonNull final ClickableViews clickableViews,
                                          final float x, final float y) {
        final View itemView = viewHolder.itemView;

        final int defaultViewId = clickableViews.getDefaultViewId();
        final int[] clickableViewIds = clickableViews.getClickableViewIds();
        if (defaultViewId == ClickableViews.NO_ID && clickableViewIds.length == 0) {
            // Quick return when element is not clickable
            return null;

        } else if (defaultViewId == ClickableViews.ITEM_VIEW_ID && clickableViewIds.length == 0) {
            // Quick return when only the whole item view is clickable
            return new Target(viewHolder, itemView);

        } else {
            final View touchedView = findViewAt(itemView, x, y);
            final View clickableView = (touchedView != null ?
                    findClickableViewUpward(touchedView, itemView, clickableViews) : null);
            return (clickableView != null ? new Target(viewHolder, clickableView) : null);
        }
    }

    @Nullable
    private View findViewAt(@NonNull final View view, final float x, final float y) {
        if (!view.isShown() || !isViewAt(view, x, y)) {
            return null;
        }

        if (view instanceof ViewGroup) {
            final float transformedX = x - view.getLeft() - ViewCompat.getTranslationX(view);
            final float transformedY = y - view.getTop() - ViewCompat.getTranslationY(view);

            final ViewGroup viewGroup = (ViewGroup) view;
            final int childCount = viewGroup.getChildCount();
            for (int i = childCount - 1; i >= 0; i--) {
                final View child = viewGroup.getChildAt(i);
                final View foundView = findViewAt(child, transformedX, transformedY);
                if (foundView != null) {
                    return foundView;
                }
            }
        }

        return view;
    }

    private boolean isViewAt(@NonNull final View view, final float x, final float y) {
        final float viewX = view.getLeft() - ViewCompat.getTranslationX(view);
        final float viewY = view.getTop() - ViewCompat.getTranslationY(view);
        return ((x > viewX && x < (viewX + view.getWidth())) &&
                (y > viewY && y < (viewY + view.getHeight())));
    }

    @Nullable
    private View findClickableViewUpward(@NonNull final View view, @NonNull final View itemView,
                                         @NonNull final ClickableViews clickableViews) {
        View candidate = view;
        do {
            final boolean isDefaultView = (candidate == itemView &&
                    clickableViews.getDefaultViewId() == ClickableViews.ITEM_VIEW_ID);
            if (isDefaultView || isViewClickable(candidate, clickableViews)) {
                return candidate;
            }
            final ViewParent parent = view.getParent();
            candidate = (parent instanceof View ? (View) parent : null);
        } while (candidate != null && candidate != itemView.getParent());

        return null;
    }

    private boolean isViewClickable(@NonNull final View view,
                                    @NonNull final ClickableViews clickableViews) {
        final int viewId = view.getId();
        if (clickableViews.getDefaultViewId() == viewId) {
            return true;
        }

        final int[] clickableViewIds = clickableViews.getClickableViewIds();
        for (final int id : clickableViewIds) {
            if (viewId == id) {
                return true;
            }
        }

        return false;
    }

    static class Target {

        @NonNull
        private final RecyclerView.ViewHolder mViewHolder;

        @NonNull
        private final View mView;

        private Target(@NonNull final RecyclerView.ViewHolder viewHolder,
                       @NonNull final View view) {
            mViewHolder = viewHolder;
            mView = view;
        }

        /**
         * Returns ViewHolder in which tap has occurred.
         *
         * @return ViewHolder in which tap has occurred.
         */
        @NonNull
        RecyclerView.ViewHolder getViewHolder() {
            return mViewHolder;
        }

        /**
         * Returns view that actually has been tapped by the user.
         *
         * @return view that actually has been tapped by the user.
         */
        @NonNull
        View getView() {
            return mView;
        }

        /**
         * Sets the pressed state for tapped view and provides a touch coordinate for
         * animation hinting.
         *
         * @param pressed Pass true to set the View's internal state to "pressed",
         *                or false to reverts the View's internal state from a
         *                previously set "pressed" state.
         * @param x       The x coordinate of the touch that caused the press.
         * @param y       The y coordinate of the touch that caused the press.
         */
        void setPressed(final boolean pressed, final float x, final float y) {
            if (pressed) {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                    float hotspotX = x;
                    float hotspotY = y;

                    View view = mView;
                    do {
                        hotspotX = hotspotX - view.getLeft() - ViewCompat.getTranslationX(view);
                        hotspotY = hotspotY - view.getTop() - ViewCompat.getTranslationY(view);
                        final ViewParent parent = view.getParent();
                        view = (parent instanceof View ? (View) parent : null);
                    } while (view != null && view != mViewHolder.itemView.getParent());

                    mView.drawableHotspotChanged(hotspotX, hotspotY);
                }
            }
            setPressed(pressed);
        }

        /**
         * Sets the pressed state for tapped view.
         *
         * @param pressed Pass true to set the View's internal state to "pressed", or false to reverts
         *                the View's internal state from a previously set "pressed" state.
         */
        void setPressed(final boolean pressed) {
            mView.setPressed(pressed);
        }

    }

}
