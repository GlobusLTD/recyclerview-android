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
package com.globusltd.recyclerview.choice;

import android.annotation.TargetApi;
import android.content.Context;
import android.os.Build;
import android.support.annotation.MainThread;
import android.support.annotation.NonNull;
import android.support.annotation.RestrictTo;
import android.support.v7.widget.RecyclerView;
import android.util.SparseBooleanArray;
import android.view.View;
import android.widget.Checkable;

import com.globusltd.recyclerview.ViewHolderObserver;

@MainThread
@RestrictTo(RestrictTo.Scope.LIBRARY)
public class ChoiceModeBehavior implements ViewHolderObserver {
    
    @NonNull
    private final SparseBooleanArray mCheckableViewTypes;
    
    public ChoiceModeBehavior() {
        mCheckableViewTypes = new SparseBooleanArray();
    }
    
    @Override
    public void onViewHolderAttached(@NonNull final RecyclerView.ViewHolder viewHolder) {
        onViewHolderPositionChanged(viewHolder);
    }
    
    @Override
    public void onViewHolderPositionChanged(@NonNull final RecyclerView.ViewHolder viewHolder) {
        setViewChecked(viewHolder, false); // TODO: isChecked from choice mode;
    }
    
    @Override
    public void onViewHolderDetached(@NonNull final RecyclerView.ViewHolder viewHolder) {
        setViewChecked(viewHolder, false);
    }
    
    @TargetApi(Build.VERSION_CODES.HONEYCOMB)
    private void setViewChecked(@NonNull final RecyclerView.ViewHolder viewHolder, final boolean isChecked) {
        final View itemView = viewHolder.itemView;
        if (isCheckableView(viewHolder)) {
            ((Checkable) itemView).setChecked(isChecked);
        } else if (shouldUseActivated(viewHolder)) {
            itemView.setActivated(isChecked);
        }
    }
    
    private boolean isCheckableView(@NonNull final RecyclerView.ViewHolder viewHolder) {
        final int viewType = viewHolder.getItemViewType();
        if (mCheckableViewTypes.indexOfKey(viewType) >= 0) {
            return mCheckableViewTypes.get(viewType);
            
        } else {
            final boolean isCheckable = Checkable.class.isInstance(viewHolder.itemView);
            mCheckableViewTypes.put(viewType, isCheckable);
            return isCheckable;
        }
    }
    
    private boolean shouldUseActivated(@NonNull final RecyclerView.ViewHolder viewHolder) {
        final Context context = viewHolder.itemView.getContext();
        final int targetSdkVersion = context.getApplicationInfo().targetSdkVersion;
        return (targetSdkVersion >= Build.VERSION_CODES.HONEYCOMB);
    }
    
}