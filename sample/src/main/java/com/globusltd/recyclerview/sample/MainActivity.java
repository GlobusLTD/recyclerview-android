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

import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.widget.Toast;

import com.globusltd.recyclerview.Adapter;
import com.globusltd.recyclerview.datasource.ListDatasource;

public class MainActivity extends AppCompatActivity {
    
    private RecyclerView mRecyclerView;
    
    private Handler mHandler = new Handler(Looper.getMainLooper());
    
    @Override
    protected void onCreate(@Nullable final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        
        final ListDatasource<String> datasource = new ListDatasource<>();
        datasource.add("Test3 String");
        datasource.add("Test5 String");
        datasource.add("Test1 String");
        datasource.add("Test4 String");
        datasource.add("Test2 String");
        datasource.add("Test6 String");
        
        final Adapter<CharSequence, ?> adapter = new SampleAdapter(datasource);
        adapter.setOnItemClickListener((view, item, position) ->
                Toast.makeText(this, "Clicked: " + item, Toast.LENGTH_SHORT).show());
        adapter.setOnLongItemClickListener((view, item, position) -> {
            Toast.makeText(this, "Long clicked: " + item, Toast.LENGTH_SHORT).show();
            return true;
        });
        
        mRecyclerView = (RecyclerView) findViewById(android.R.id.list);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        mRecyclerView.setItemAnimator(new DefaultItemAnimator());
        mRecyclerView.setAdapter(adapter);
        
        /*mHandler.postDelayed(() -> datasource.add("Test1 String"), 2000L);
        mHandler.postDelayed(() -> datasource.add("Test2 String"), 2100L);
        mHandler.postDelayed(() -> datasource.add("Test3 String"), 2200L);
        
        final ListDatasource<String> changedDatasource = new ListDatasource<>();
        changedDatasource.add("Test3 String");
        changedDatasource.add("Test5 String");
        changedDatasource.add("Test1 String");
        changedDatasource.add("Test4 String");
        changedDatasource.add("Test2 String");
        changedDatasource.add("Test6 String");
        mHandler.postDelayed(() -> adapter.swap(changedDatasource), 5000L);
        
        mHandler.postDelayed(() -> changedDatasource.removeRange(1, 3), 8000L);
        
        mHandler.postDelayed(() -> adapter.swap(Datasources.empty()), 11000L);*/
    }
    
    @Override
    protected void onDestroy() {
        mHandler.removeCallbacksAndMessages(null);
        mRecyclerView.setAdapter(null);
        super.onDestroy();
    }
    
}