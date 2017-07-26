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
package com.globusltd.recyclerview.sample.choicemode;

import android.arch.lifecycle.ViewModel;
import android.support.annotation.NonNull;

import com.globusltd.recyclerview.datasource.Datasource;
import com.globusltd.recyclerview.datasource.ListDatasource;
import com.globusltd.recyclerview.sample.data.Person;
import com.globusltd.recyclerview.sample.data.PersonGenerator;

public class PersonsViewModel extends ViewModel {
    
    @NonNull
    private final PersonGenerator mPersonGenerator;
    
    @NonNull
    private final ListDatasource<Person> mDatasource;
    
    public PersonsViewModel() {
        mPersonGenerator = new PersonGenerator();
        mDatasource = new ListDatasource<>();
        for (int i = 0; i < 50; i++) {
            mDatasource.add(mPersonGenerator.generate());
        }
    }
    
    @NonNull
    Datasource<Person> getDatasource() {
        return mDatasource;
    }
    
    void deleteById(final long itemId) {
        final int size = mDatasource.size();
        for (int index = size - 1; index >= 0; index--) {
            final Person person = mDatasource.get(index);
            if (person.getId() == itemId) {
                mDatasource.remove(index);
                break;
            }
        }
    }
    
}