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

import android.arch.lifecycle.ViewModelProviders;
import android.os.Bundle;
import android.support.annotation.IntRange;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Toast;

import com.globusltd.recyclerview.view.ItemClickHelperV2;
import com.globusltd.recyclerview.datasource.Datasource;
import com.globusltd.recyclerview.sample.R;

public class SimpleListDatasourceExampleActivity extends AppCompatActivity {
    
    private SimpleListDatasourceExampleViewModel mViewModel;
    
    private RecyclerView mRecyclerView;

    @Override
    protected void onCreate(@Nullable final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_manual_list_example);
    
        mViewModel = ViewModelProviders.of(this).get(SimpleListDatasourceExampleViewModel.class);
        
        final Datasource<Person> datasource = mViewModel.getDatasource();
        final PersonsAdapter adapter = new PersonsAdapter(datasource);
        //adapter.setOnItemClickListener(this::onItemClick);
        //adapter.setOnItemLongClickListener(this::onItemLongClick);

        mRecyclerView = (RecyclerView) findViewById(android.R.id.list);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        mRecyclerView.setItemAnimator(new DefaultItemAnimator());
        mRecyclerView.setAdapter(adapter);
    
        final ItemClickHelperV2<Person> itemClickHelper = new ItemClickHelperV2<>(adapter);
        itemClickHelper.setOnItemClickListener(this::onItemClick);
        itemClickHelper.setOnItemLongClickListener(this::onItemLongClick);
        itemClickHelper.attachToRecyclerView(mRecyclerView);
        
        findViewById(R.id.action_add).setOnClickListener(v -> mViewModel.addSingleItem());
        findViewById(R.id.action_add_multiple).setOnClickListener(v -> mViewModel.addMultipleItems());
    }

    public boolean onItemClick(@NonNull final View view, @NonNull final Person person,
                               @IntRange(from = 0) final int position) {
        switch (view.getId()) {
            case R.id.action_delete:
                mViewModel.removeItemAtPosition(position);
                return true;

            default:
                Toast.makeText(this, "Clicked: " + person.getFullName(), Toast.LENGTH_SHORT).show();
                return true;
        }
    }

    public boolean onItemLongClick(@NonNull final View view, @NonNull final Person person,
                                   @IntRange(from = 0) final int position) {
        Toast.makeText(this, "Long clicked: " + person.getFullName(), Toast.LENGTH_SHORT).show();
        return true;
    }

    @Override
    protected void onDestroy() {
        mRecyclerView.setAdapter(null);
        super.onDestroy();
    }

}