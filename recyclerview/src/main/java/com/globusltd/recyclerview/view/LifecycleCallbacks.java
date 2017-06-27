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

import android.support.annotation.MainThread;

@MainThread
public interface LifecycleCallbacks {

    /**
     * This method will be called when the activity or fragment is
     * being started.
     *
     * @see android.app.Activity#onStart
     * @see android.app.Fragment#onStart
     * @see android.support.v4.app.Fragment#onStart
     */
    void onStart();

    /**
     * This method will be called when the activity or fragment is
     * being resumed.
     *
     * @see android.app.Activity#onResume
     * @see android.app.Fragment#onResume
     * @see android.support.v4.app.Fragment#onResume
     */
    void onResume();

    /**
     * This method should be called when the activity or fragment is
     * being paused.
     *
     * @see android.app.Activity#onPause
     * @see android.app.Fragment#onPause
     * @see android.support.v4.app.Fragment#onPause
     */
    void onPause();

    /**
     * This method will be called when the activity or fragment is
     * being stopped.
     *
     * @see android.app.Activity#onStop
     * @see android.app.Fragment#onStop
     * @see android.support.v4.app.Fragment#onStop
     */
    void onStop();

}