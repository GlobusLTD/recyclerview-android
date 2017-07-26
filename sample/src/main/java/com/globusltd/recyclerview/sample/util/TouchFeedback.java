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
package com.globusltd.recyclerview.sample.util;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.LayerDrawable;
import android.os.Build;
import android.support.annotation.MainThread;
import android.support.annotation.NonNull;
import android.support.v4.content.ContextCompat;
import android.util.TypedValue;
import android.view.View;

import com.globusltd.recyclerview.sample.R;

import java.util.ArrayList;
import java.util.List;

@MainThread
public class TouchFeedback {

    /**
     * Applies to view default background drawable which contains focus and pressed states.
     *
     * @param view View whose background drawable should be changed.
     */
    public static void selectableItemBackground(@NonNull final View view) {
        final Resources.Theme theme = view.getContext().getTheme();
        final TypedValue outValue = new TypedValue();
        if (theme.resolveAttribute(R.attr.selectableItemBackground, outValue, true)) {
            view.setBackgroundResource(outValue.resourceId);
        }
    }

    /**
     * Applies to view default background drawable which contains activated,
     * focus and pressed states.
     *
     * @param view View whose background drawable should be changed.
     */
    @SuppressWarnings("deprecation")
    public static void selectableActivatedItemBackground(@NonNull final View view) {
        final Context context = view.getContext();
        final Resources.Theme theme = context.getTheme();
        final TypedValue outValue = new TypedValue();

        final List<Drawable> layers = new ArrayList<>();

        if (theme.resolveAttribute(android.R.attr.activatedBackgroundIndicator, outValue, true)) {
            final Drawable activatedBackgroundIndicator = ContextCompat
                    .getDrawable(context, outValue.resourceId);
            layers.add(activatedBackgroundIndicator);
        }

        if (theme.resolveAttribute(R.attr.selectableItemBackground, outValue, true)) {
            final Drawable selectableItemBackground = ContextCompat
                    .getDrawable(context, outValue.resourceId);
            layers.add(selectableItemBackground);
        }

        final Drawable[] array = new Drawable[layers.size()];
        final LayerDrawable background = new LayerDrawable(layers.toArray(array));
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
            view.setBackground(background);
        } else {
            view.setBackgroundDrawable(background);
        }
    }

    private TouchFeedback() {
    }

}
