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
package com.globusltd.recyclerview.sample;

import android.support.annotation.NonNull;
import android.support.annotation.StringRes;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

public class SingleLineViewHolder extends RecyclerView.ViewHolder {
    
    @NonNull
    public static SingleLineViewHolder inflate(@NonNull final LayoutInflater inflater,
                                               @NonNull final ViewGroup parent) {
        final View itemView = inflater.inflate(android.R.layout.simple_list_item_1, parent, false);
        return new SingleLineViewHolder(itemView);
    }
    
    @NonNull
    private final TextView mTextView1;
    
    public SingleLineViewHolder(@NonNull final View itemView) {
        super(itemView);
        mTextView1 = (TextView) itemView.findViewById(android.R.id.text1);
    }
    
    public void setText1(@NonNull final CharSequence text) {
        mTextView1.setText(text);
    }

    public void setText1(@StringRes final int resId) {
        mTextView1.setText(resId);
    }

}
