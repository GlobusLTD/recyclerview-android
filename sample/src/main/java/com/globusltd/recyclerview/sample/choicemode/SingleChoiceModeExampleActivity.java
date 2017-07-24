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

import android.arch.lifecycle.ViewModelProviders;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.widget.Toast;

import com.globusltd.recyclerview.choice.SingleChoiceMode;
import com.globusltd.recyclerview.datasource.Datasource;
import com.globusltd.recyclerview.sample.R;
import com.globusltd.recyclerview.sample.data.Person;
import com.globusltd.recyclerview.view.ChoiceModeHelper;

public class SingleChoiceModeExampleActivity extends AppCompatActivity {

    private RecyclerView mRecyclerView;
    private SingleChoiceMode mChoiceMode;
    private ChoiceModeHelper<Person> mChoiceModeHelper;

    @Override
    protected void onCreate(@Nullable final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_single_choice_mode_example);

        final PersonsViewModel viewModel = ViewModelProviders.of(this).get(PersonsViewModel.class);
        final Datasource<Person> datasource = viewModel.getDatasource();
        final SimpleCheckablePersonsAdapter adapter = new SimpleCheckablePersonsAdapter(datasource, false);

        mRecyclerView = (RecyclerView) findViewById(android.R.id.list);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        mRecyclerView.setItemAnimator(new DefaultItemAnimator());
        mRecyclerView.setAdapter(adapter);

        mChoiceMode = new SingleChoiceMode(savedInstanceState);
        mChoiceMode.setChoiceModeListener(this::onItemCheckedChanged);
        mChoiceModeHelper = new ChoiceModeHelper<>(adapter, mChoiceMode);
        mChoiceModeHelper.setRecyclerView(mRecyclerView);

        findViewById(R.id.action_clear_choices).setOnClickListener(v -> mChoiceMode.clearChoices());
        findViewById(R.id.action_get_checked_item).setOnClickListener(v -> showCheckedItem());
    }

    private void onItemCheckedChanged(final long itemId, final boolean checked, final boolean fromUser) {
        if (checked) {
            Toast.makeText(this, "Item with id=" + itemId + " has been checked " +
                    (fromUser ? "by the user" : "programmatically"), Toast.LENGTH_SHORT).show();
        }
    }

    private void showCheckedItem() {
        if (mChoiceMode.getCheckedItemCount() > 0) {
            final long checkedId = mChoiceMode.getCheckedItem();
            Toast.makeText(this, "Item with id=" + checkedId + " is checked", Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(this, "No item is checked", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    protected void onSaveInstanceState(final Bundle outState) {
        super.onSaveInstanceState(outState);
        mChoiceMode.onSaveInstanceState(outState);
    }

    @Override
    protected void onDestroy() {
        mChoiceModeHelper.setRecyclerView(null);
        mRecyclerView.setAdapter(null);
        super.onDestroy();
    }

}