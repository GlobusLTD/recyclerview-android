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
package com.globusltd.recyclerview.sample.datasource;

import android.arch.lifecycle.ViewModel;
import android.support.annotation.IntRange;
import android.support.annotation.NonNull;

import com.globusltd.recyclerview.datasource.Datasource;
import com.globusltd.recyclerview.datasource.ListDatasource;

import java.util.Arrays;

public class SimpleListDatasourceExampleViewModel extends ViewModel {
    
    @NonNull
    private final ListDatasource<Person> mDatasource;
    
    @NonNull
    private final PersonGenerator mPersonGenerator;
    
    private int mCounter = 0;
    
    public SimpleListDatasourceExampleViewModel() {
        mDatasource = new ListDatasource<>();
        mPersonGenerator = new PersonGenerator();
    }
    
    @NonNull
    Datasource<Person> getDatasource() {
        return mDatasource;
    }
    
    void addSingleItem() {
        mDatasource.add(generateItem());
    }
    
    void addMultipleItems() {
        mDatasource.addAll(Arrays.asList(generateItem(), generateItem(), generateItem()));
    }
    
    @NonNull
    private Person generateItem() {
        return mPersonGenerator.generate();
    }
    
    void removeItemAtPosition(@IntRange(from = 0) final int position) {
        mDatasource.remove(position);
    }
    
}
