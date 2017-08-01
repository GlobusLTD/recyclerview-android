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
package com.globusltd.recyclerview.sample.lifecycle;

import android.arch.lifecycle.ViewModelProviders;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import com.globusltd.recyclerview.ViewHolderTracker;
import com.globusltd.recyclerview.datasource.Datasource;
import com.globusltd.recyclerview.lifecycle.LifecycleBehavior;
import com.globusltd.recyclerview.sample.R;
import com.globusltd.recyclerview.lifecycle.LifecycleComposite;
import com.globusltd.recyclerview.sample.data.Person;

public class LifecycleExampleActivity extends AppCompatActivity {

    private RecyclerView mRecyclerView;
    private LifecycleComposite mLifecycleComposite;
    private ViewHolderTracker mViewHolderTracker;

    @Override
    protected void onCreate(@Nullable final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lifecycle_example);

        final LifecycleExampleViewModel viewModel = ViewModelProviders.of(this)
                .get(LifecycleExampleViewModel.class);
        final Datasource<Person> datasource = viewModel.getDatasource();
        final LifecyclePersonAdapter adapter = new LifecyclePersonAdapter(datasource);

        final DefaultItemAnimator itemAnimator = new DefaultItemAnimator();
        itemAnimator.setSupportsChangeAnimations(false);

        mRecyclerView = (RecyclerView) findViewById(android.R.id.list);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        mRecyclerView.setItemAnimator(itemAnimator);
        mRecyclerView.setAdapter(adapter);

        mLifecycleComposite = new LifecycleComposite();

        mViewHolderTracker = new ViewHolderTracker();
        mViewHolderTracker.registerViewHolderObserver(new LifecycleBehavior(mLifecycleComposite));
        mViewHolderTracker.setRecyclerView(mRecyclerView);

        findViewById(R.id.pause).setOnClickListener(v -> pauseActivity());
    }

    private void pauseActivity() {
        final Intent intent = new Intent(Intent.ACTION_MAIN);
        intent.addCategory(Intent.CATEGORY_LAUNCHER);
        startActivity(Intent.createChooser(intent, getString(R.string.app_name)));
    }

    @Override
    protected void onStart() {
        super.onStart();
        mLifecycleComposite.onStart();
    }

    @Override
    protected void onResume() {
        super.onResume();
        mLifecycleComposite.onResume();
    }

    @Override
    protected void onPause() {
        mLifecycleComposite.onPause();
        super.onPause();
    }

    @Override
    protected void onStop() {
        mLifecycleComposite.onStop();
        super.onStop();
    }

    @Override
    protected void onDestroy() {
        mViewHolderTracker.setRecyclerView(null);
        mRecyclerView.setAdapter(null);
        super.onDestroy();
    }

}