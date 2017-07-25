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
import android.content.Context;
import android.content.res.Resources;
import android.os.Bundle;
import android.support.annotation.IntRange;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.view.ActionMode;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import com.globusltd.recyclerview.choice.ActionModeCompat;
import com.globusltd.recyclerview.choice.ModalChoiceModeListener;
import com.globusltd.recyclerview.choice.SingleModalChoiceMode;
import com.globusltd.recyclerview.datasource.Datasource;
import com.globusltd.recyclerview.sample.R;
import com.globusltd.recyclerview.sample.data.Person;
import com.globusltd.recyclerview.view.ChoiceModeHelper;

public class SingleModalChoiceModeExampleActivity extends AppCompatActivity {
    
    private PersonsViewModel mViewModel;
    
    private RecyclerView mRecyclerView;
    private SingleModalChoiceMode mChoiceMode;
    private ChoiceModeHelper<Person> mChoiceModeHelper;
    
    @Override
    protected void onCreate(@Nullable final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_single_modal_choice_mode_example);
        
        mViewModel = ViewModelProviders.of(this).get(PersonsViewModel.class);
        final Datasource<Person> datasource = mViewModel.getDatasource();
        final ActivatedPersonsAdapter adapter = new ActivatedPersonsAdapter(datasource);
        
        mRecyclerView = (RecyclerView) findViewById(android.R.id.list);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        mRecyclerView.setItemAnimator(new DefaultItemAnimator());
        mRecyclerView.setAdapter(adapter);
        
        final ActionModeCompat actionMode = ActionModeCompat.from(this);
        mChoiceMode = new SingleModalChoiceMode(actionMode, mModalChoiceModeListener, savedInstanceState);
        // mChoiceMode.setStartOnSingleTapEnabled(true);
        // mChoiceMode.setFinishActionModeOnClearEnabled(false);
        
        mChoiceModeHelper = new ChoiceModeHelper<>(adapter, mChoiceMode);
        mChoiceModeHelper.setOnItemClickListener(this::onItemClick);
        mChoiceModeHelper.setRecyclerView(mRecyclerView);
        
        findViewById(R.id.action_clear_choices).setOnClickListener(v -> mChoiceMode.clearChoices());
        findViewById(R.id.action_get_checked_item).setOnClickListener(v -> showCheckedItem());
    }
    
    public boolean onItemClick(@NonNull final View view, @NonNull final Person person,
                               @IntRange(from = 0) final int position) {
        Toast.makeText(this, "Clicked: " + person.getFullName(), Toast.LENGTH_SHORT).show();
        return true;
    }
    
    private void showCheckedItem() {
        if (mChoiceMode.getCheckedItemCount() > 0) {
            final long checkedId = mChoiceMode.getCheckedItem();
            Toast.makeText(this, "Item with id=" + checkedId + " is checked", Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(this, "No item is checked", Toast.LENGTH_SHORT).show();
        }
    }
    
    private final ModalChoiceModeListener mModalChoiceModeListener = new ModalChoiceModeListener() {
        
        private final Context mContext = SingleModalChoiceModeExampleActivity.this;
        
        @Override
        public boolean onCreateActionMode(final ActionMode mode, final Menu menu) {
            final MenuInflater inflater = mode.getMenuInflater();
            inflater.inflate(R.menu.action_mode_menu, menu);
            return true;
        }
        
        @Override
        public boolean onPrepareActionMode(final ActionMode mode, final Menu menu) {
            final int checkedCount = mChoiceMode.getCheckedItemCount();
            
            // Show / hide menu items or menu groups based on number of checked items
            final MenuItem item = menu.findItem(R.id.action_delete);
            item.setVisible(checkedCount > 0);
            
            // Update action mode title
            if (checkedCount > 0) {
                final Resources resources = getResources();
                mode.setTitle(resources.getQuantityString(R.plurals.action_mode_x_items_selected,
                        checkedCount, checkedCount));
            } else {
                mode.setTitle(R.string.action_mode_no_items_selected);
            }
            
            return true;
        }
        
        @Override
        public boolean onActionItemClicked(final ActionMode mode, final MenuItem item) {
            final long checkedItemId = mChoiceMode.getCheckedItem();
            mViewModel.deleteById(checkedItemId);
            mChoiceMode.finishActionMode();
            return true;
        }
        
        @Override
        public void onDestroyActionMode(final ActionMode mode) {
        }
        
        @Override
        public void onItemCheckedStateChanged(@NonNull final ActionMode mode, final long itemId,
                                              final boolean checked, final boolean fromUser) {
            Toast.makeText(mContext, "Item with id=" + itemId + " has been " +
                            (checked ? "" : "un") + "checked" +
                            (fromUser ? " by the user" : " programmatically"),
                    Toast.LENGTH_SHORT).show();
        }
        
    };
    
    
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
