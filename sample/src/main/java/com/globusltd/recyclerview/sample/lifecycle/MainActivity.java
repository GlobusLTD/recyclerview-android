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

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.IntRange;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Toast;

import com.globusltd.recyclerview.sample.R;
import com.globusltd.recyclerview.lifecycle.LifecycleComposite;
import com.globusltd.recyclerview.view.OnItemClickListener;
import com.globusltd.recyclerview.view.OnItemLongClickListener;

public class MainActivity extends AppCompatActivity implements OnItemClickListener<CharSequence>,
        OnItemLongClickListener<CharSequence> {

    private RecyclerView mRecyclerView;

    private LifecycleComposite mLifecycleComposite;

    @Override
    protected void onCreate(@Nullable final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mLifecycleComposite = new LifecycleComposite();

        /*final ListDatasource<String> datasource = new ListDatasource<>();
        for (int i = 1; i < 101; i++) {
            datasource.add(String.format(Locale.getDefault(), "Test%1$s String", i));
        }

        final SampleAdapter adapter = new SampleAdapter(datasource);
        adapter.setOnItemClickListener(this);
        adapter.setOnItemLongClickListener(this);
        adapter.registerViewHolderObserver(new EnableBehavior(adapter));
        adapter.registerViewHolderObserver(new LifecycleBehavior(mLifecycleComposite));

        mRecyclerView = (RecyclerView) findViewById(android.R.id.list);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        mRecyclerView.setItemAnimator(new DefaultItemAnimator());
        mRecyclerView.setAdapter(adapter);*/

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
    public boolean onItemClick(@NonNull final View view, @NonNull final CharSequence item,
                               @IntRange(from = 0) final int position) {
        Toast.makeText(this, "Clicked: " + item, Toast.LENGTH_SHORT).show();
        return true;
    }

    @Override
    public boolean onItemLongClick(@NonNull final View view, @NonNull final CharSequence item,
                                   @IntRange(from = 0) final int position) {
        Toast.makeText(this, "Long clicked: " + item, Toast.LENGTH_SHORT).show();
        return true;
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
        mRecyclerView.setAdapter(null);
        super.onDestroy();
    }

}