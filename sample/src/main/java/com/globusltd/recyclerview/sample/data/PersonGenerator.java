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

import android.support.annotation.NonNull;

import java.util.Random;

public class PersonGenerator {
    
    private static final String[] FIRST_NAMES = {
            "Alfred", "Anna", "Brad", "Cristina",
            "Daron", "Jaylen", "Jonathon", "Katlynn",
            "Rafael", "Terra", "Zackary"
    };
    
    private static final String[] LAST_NAMES = {
            "Burks", "Charles", "Cleveland",
            "Donaldson", "Hernandez", "Garrett",
            "Lowery", "Mason", "Obrien", "Sharp",
            "Villarreal"
    };
    
    private int mCounter;
    
    private Random mRandom;

    public PersonGenerator() {
        mRandom = new Random();
    }
    
    @NonNull
    public Person generate() {
        final int firstNamePosition = Math.abs(mRandom.nextInt()) % FIRST_NAMES.length;
        final String firstName = FIRST_NAMES[firstNamePosition];
        final int lastNamePosition = Math.abs(mRandom.nextInt()) % LAST_NAMES.length;
        final String lastName = LAST_NAMES[lastNamePosition];
        return new Person(++mCounter, firstName, lastName);
    }
    
}