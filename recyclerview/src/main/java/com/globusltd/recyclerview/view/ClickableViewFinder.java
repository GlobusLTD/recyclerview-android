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

import com.globusltd.recyclerview.ClickableAdapter;

/**
 * Helper class for searching view that actually has tapped by the user.
 */
@MainThread
class ClickableViewFinder {

    @NonNull
    private final RecyclerView mHostView;

    ClickableViewFinder(@NonNull final RecyclerView hostView) {
        mHostView = hostView;
    }

    /**
     * Searches for view that actually has tapped by the user.
     *
     * @param x The x coordinate of the touch that caused the search.
     * @param y The y coordinate of the touch that caused the search.
     * @return {@link Target} instance that contains tapped view and containing view holder,
     * null if view has not been found.
     */
    @Nullable
    Target findTarget(final float x, final float y) {
        final View itemView = mHostView.findChildViewUnder(x, y);
        if (itemView != null) {
            final RecyclerView.ViewHolder viewHolder = mHostView.getChildViewHolder(itemView);
            final int position = viewHolder.getAdapterPosition();
            final int viewType = viewHolder.getItemViewType();
            if (position > RecyclerView.NO_POSITION && viewType > RecyclerView.INVALID_TYPE) {
                final ClickableAdapter<?> adapter = (ClickableAdapter) mHostView.getAdapter();
                if (!adapter.isEnabled(position)) {
                    // Quick return when element is not enabled
                    return null;
                }

                final ClickableViews clickableViews = adapter.getClickableViews(position, viewType);
                final int defaultViewId = clickableViews.getDefaultViewId();
                final int[] clickableViewIds = clickableViews.getClickableViewIds();
                if (defaultViewId == ClickableViews.NO_ID && clickableViewIds.length == 0) {
                    // Quick return when element is enabled but not clickable
                    return null;

                } else if (defaultViewId == ClickableViews.ITEM_VIEW_ID && clickableViewIds.length == 0) {
                    // Quick return when element is enabled but only the whole item view is clickable
                    return new Target(viewHolder, itemView);

                } else {
                    final View pointerOnView = findViewAt(itemView, x, y);
                    final View view = itemView;
                    // final View view = findClickableView(itemView, itemView, clickableViews, x, y);
                    return (view != null ? new Target(viewHolder, view) : null);
                }
            }
        }
        return null;
    }

    @Nullable
    private View findViewAt(@NonNull final View view, final float x, final float y) {
        return view; // TODO:
        /*if (view instanceof ViewGroup) {
            final float transformedX = x - view.getLeft() - ViewCompat.getTranslationX(view);
            final float transformedY = y - view.getTop() - ViewCompat.getTranslationY(view);

            final ViewGroup viewGroup = (ViewGroup) view;
            final int childCount = viewGroup.getChildCount();
            for (int i = childCount - 1; i >= 0; i--) {
                final View child = viewGroup.getChildAt(i);
                if (child.isShown() && )
            }

        } else {
            return view;
        }*/
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
                        view = (View) view.getParent();
                    } while (view != mViewHolder.itemView.getParent());

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
