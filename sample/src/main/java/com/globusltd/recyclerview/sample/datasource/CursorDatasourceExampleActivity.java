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

import android.Manifest;
import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.CallLog;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.ContextCompat;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;

import com.globusltd.recyclerview.datasource.CursorDatasource;
import com.globusltd.recyclerview.datasource.Datasource;
import com.globusltd.recyclerview.datasource.Datasources;
import com.globusltd.recyclerview.sample.R;

import java.io.Closeable;
import java.io.IOException;

public class CursorDatasourceExampleActivity extends AppCompatActivity
        implements LoaderManager.LoaderCallbacks<Cursor> {
    
    private static final String TAG = "CursorDatasourceExample";
    
    private static final int REQUEST_CODE_CALL_LOG_PERMISSION = 1;
    private static final int REQUEST_CODE_SETTINGS = 2;
    
    @SuppressWarnings("InlinedApi")
    private static final String CALL_LOG_PERMISSION = Manifest.permission.READ_CALL_LOG;
    
    @SuppressWarnings("InlinedApi")
    private static final String[] CALL_LOG_PERMISSIONS = new String[] { CALL_LOG_PERMISSION };
    
    private CoordinatorLayout mCoordinatorLayout;
    private RecyclerView mRecyclerView;
    private CallsAdapter mAdapter;
    
    @Override
    protected void onCreate(@Nullable final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cursor_datasource_example);
        
        mCoordinatorLayout = (CoordinatorLayout) findViewById(R.id.coordinator);
        
        mAdapter = new CallsAdapter();
        
        mRecyclerView = (RecyclerView) findViewById(android.R.id.list);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        mRecyclerView.setItemAnimator(new DefaultItemAnimator());
        mRecyclerView.addItemDecoration(new DividerItemDecoration(this, DividerItemDecoration.VERTICAL));
        mRecyclerView.setAdapter(mAdapter);
        
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (ContextCompat.checkSelfPermission(this, CALL_LOG_PERMISSION) == PackageManager.PERMISSION_GRANTED) {
                onCallLogPermissionGranted();
            } else {
                ActivityCompat.requestPermissions(this, CALL_LOG_PERMISSIONS,
                        REQUEST_CODE_CALL_LOG_PERMISSION);
            }
        } else {
            onCallLogPermissionGranted();
        }
    }
    
    @Override
    public void onRequestPermissionsResult(final int requestCode, @NonNull final String[] permissions,
                                           @NonNull final int[] grantResults) {
        if (requestCode == REQUEST_CODE_CALL_LOG_PERMISSION) {
            if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                onCallLogPermissionGranted();
            } else {
                Snackbar.make(mCoordinatorLayout, R.string.rationale_request_call_log_permisson, Snackbar.LENGTH_INDEFINITE)
                        .setAction(R.string.action_open_settings, v -> openSettings())
                        .show();
            }
            
        } else {
            super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        }
    }
    
    private void onCallLogPermissionGranted() {
        getSupportLoaderManager().initLoader(0, Bundle.EMPTY, this);
    }
    
    private void openSettings() {
        try {
            final String packageName = getPackageName();
            final Uri data = Uri.parse("package:" + packageName);
            
            final Intent intent = new Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS)
                    .setData(data);
            startActivityForResult(intent, REQUEST_CODE_SETTINGS);
        } catch (final ActivityNotFoundException anfe) {
            Log.e(TAG, "Settings#ACTION_APPLICATION_DETAILS_SETTINGS activity not found", anfe);
            
            final Intent intent = new Intent(Settings.ACTION_MANAGE_APPLICATIONS_SETTINGS);
            startActivityForResult(intent, REQUEST_CODE_SETTINGS);
        }
    }
    
    @SuppressWarnings("InlinedApi")
    @Override
    protected void onActivityResult(final int requestCode, final int resultCode, final Intent data) {
        if (requestCode == REQUEST_CODE_SETTINGS) {
            ActivityCompat.requestPermissions(this, CALL_LOG_PERMISSIONS, REQUEST_CODE_CALL_LOG_PERMISSION);
            
        } else {
            super.onActivityResult(requestCode, resultCode, data);
        }
    }
    
    @Override
    public Loader<Cursor> onCreateLoader(final int id, final Bundle args) {
        final Uri contentUri = CallLog.Calls.CONTENT_URI.buildUpon()
                .appendQueryParameter("limit", "100")
                .build();
        final String[] projection = {
                CallLog.Calls._ID,
                CallLog.Calls.CACHED_NAME,
                CallLog.Calls.NUMBER,
                CallLog.Calls.DATE,
                CallLog.Calls.DURATION,
                CallLog.Calls.TYPE
        };
        final String sortOrder = CallLog.Calls.DEFAULT_SORT_ORDER;
        return new CursorLoader(this, contentUri, projection, null, null, sortOrder);
    }
    
    @Override
    public void onLoadFinished(final Loader<Cursor> loader, final Cursor cursor) {
        final Datasource<? extends Cursor> datasource = new CursorDatasource(cursor);
        swapDatasource(datasource);
    }
    
    @Override
    public void onLoaderReset(final Loader<Cursor> loader) {
        swapDatasource(Datasources.empty());
    }
    
    private void swapDatasource(@NonNull final Datasource<? extends Cursor> datasource) {
        final Datasource<? extends Cursor> oldDatasource = mAdapter.swap(datasource);
        if (oldDatasource instanceof Closeable) {
            try {
                ((Closeable) oldDatasource).close();
            } catch (final IOException ioe) {
                Log.e(TAG, ioe.getMessage(), ioe);
            }
        }
    }
    
    @Override
    protected void onDestroy() {
        mRecyclerView.setAdapter(null);
        super.onDestroy();
    }
    
}
