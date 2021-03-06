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

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;

import com.globusltd.recyclerview.sample.R;

public class ChoiceModeExamplesActivity extends AppCompatActivity {

    @Override
    protected void onCreate(@Nullable final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_choice_mode_examples);

        findViewById(R.id.single_choice_mode_example)
                .setOnClickListener(v -> startActivity(new Intent(this, SingleChoiceModeExampleActivity.class)));
    
        findViewById(R.id.single_modal_choice_mode_example)
                .setOnClickListener(v -> startActivity(new Intent(this, SingleModalChoiceModeExampleActivity.class)));

        findViewById(R.id.multiple_choice_mode_example)
                .setOnClickListener(v -> startActivity(new Intent(this, MultipleChoiceModeExampleActivity.class)));

        findViewById(R.id.multiple_modal_choice_mode_example)
                .setOnClickListener(v -> startActivity(new Intent(this, MultipleModalChoiceModeExampleActivity.class)));
    }

}