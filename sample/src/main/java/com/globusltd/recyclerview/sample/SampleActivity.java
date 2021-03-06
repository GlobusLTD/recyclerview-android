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

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;

import com.globusltd.recyclerview.sample.choicemode.ChoiceModeExamplesActivity;
import com.globusltd.recyclerview.sample.datasource.DatasourceExamplesActivity;
import com.globusltd.recyclerview.sample.lifecycle.LifecycleExampleActivity;

public class SampleActivity extends AppCompatActivity {

    @Override
    protected void onCreate(@Nullable final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sample);

        findViewById(R.id.datasource_examples)
                .setOnClickListener(v -> startActivity(new Intent(this, DatasourceExamplesActivity.class)));

        findViewById(R.id.choice_mode_examples)
                .setOnClickListener(v -> startActivity(new Intent(this, ChoiceModeExamplesActivity.class)));

        findViewById(R.id.lifecycle_example)
                .setOnClickListener(v -> startActivity(new Intent(this, LifecycleExampleActivity.class)));
    }

}
