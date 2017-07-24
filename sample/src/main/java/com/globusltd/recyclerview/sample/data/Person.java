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
package com.globusltd.recyclerview.sample.data;

import java.util.Locale;

public class Person {
    
    private long mId;
    
    private String mFirstName;
    
    private String mLastName;
    
    Person(final int id, final String firstName, final String lastName) {
        mId = id;
        mFirstName = firstName;
        mLastName = lastName;
    }
    
    public long getId() {
        return mId;
    }
    
    public String getFirstName() {
        return mFirstName;
    }
    
    public String getLastName() {
        return mLastName;
    }
    
    public String getFullName() {
        return String.format(Locale.getDefault(), "%s %s", mFirstName, mLastName);
    }
    
}
