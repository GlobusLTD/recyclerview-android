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

/**
 * Defines an extension for view holders that make them able to handle choice mode.
 */
public interface ChoiceModeCheckable {
    
    void setInChoiceMode(final boolean isInChoiceMode);
    
    /**
     * Change the checked state of the view holder
     *
     * @param checked  The new checked state.
     * @param fromUser True if the checked state change was initiated by the user.
     */
    void setChecked(final boolean checked, final boolean fromUser);
    
}
