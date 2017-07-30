# RecyclerView
[![Maven Central](https://maven-badges.herokuapp.com/maven-central/com.globus-ltd/recyclerview/badge.svg)](https://maven-badges.herokuapp.com/maven-central/com.globus-ltd/recyclerview)

Simple and powerful library that brings ListView features like item clicks and choice modes to RecyclerView.

# Download
Gradle:
```groovy
compile 'com.globus-ltd:recyclerview:{latest_release}'
```
Maven:
```xml
<dependency>
  <groupId>com.globus-ltd</groupId>
  <artifactId>recyclerview</artifactId>
  <version>{latest_release}</version>
</dependency>
```

# Features
* Datasource - observable abstraction layer over your data.
* Diffs between previous and new datasets to animate item changes.
* Item click and item long click listeners.
* Choice modes (single, single modal, multiple and multiple modal).
* ViewHolder tracker that allows you apply custom behaviors to ViewHolder (library provides enable/disable item view behavior and lifecycle behavior).

# Planned features
* Filterable / Filter
* Swipes
* Drag and drop
* ViewHolder bindings

# How to use
### 1. Choose Datasource that fits your needs
<tt>Datasource</tt> is an observable abstraction layer over data storage. 

Usually Android developers put a reference to an <tt>ArrayList</tt> inside <tt>RecyclerView.Adapter</tt> and it looks good until your dataset doesn't change. But every time you need to modify dataset adapter grows because of adding data-manage methods like <tt>add</tt>, <tt>set</tt>, <tt>remove</tt>, etc. This sutuation leads to a violation [Single responsibility principle](https://en.wikipedia.org/wiki/Single_responsibility_principle).  

This library provides a way to separate data management from binding data to view and observe data changes.

There are three implementations of [<tt>Datasource</tt>](./recyclerview/src/main/java/com/globusltd/recyclerview/datasource/Datasource.java) interface:
* [<tt>ListDatasource</tt>](./recyclerview/src/main/java/com/globusltd/recyclerview/datasource/ListDatasource.java) stores your data models in <tt>ArrayList</tt> and has a lot of methods to manage stored models. It's better to store <tt>Datastore</tt> instance in the lifecycle-aware component, like <tt>ViewModel</tt>, but it's up to you how to restore data models across orientation changes.

```java
class MyItemsViewModel extends ViewModel {
    
    private ListDatasource<MyItem> mDatasource;
    
    public MyItemsViewModel() {
        mDatasource = new ListDatasource<>();
    }
    
    public Datasource<MyItem> getDatasource() {
        return mDatasource;
    }
    
    public void doSomething() {
        // Do whatever you need: calculate any value, do any of the CRUD
        // operations async, etc.
        ...
        // Update datasource when data manipulations is completed,
        // for example remove all items and adds the new ones.
        mDatasource.clear();
        mDatasource.addAll(myItems);
    }
    
}
```

* [<tt>CursorDatasource</tt>](./recyclerview/src/main/java/com/globusltd/recyclerview/datasource/CursorDatasource.java) holds <tt>Cursor</tt> instance inside. <tt>CursorDatasource</tt> is an immutable object so you should create a new instance when you got a new <tt>Cursor</tt> object.

```java
class MyActivity extends AppCompatActivity implements LoaderManager.LoaderCallbacks<Cursor> {
    
    
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        ...
        getLoaderManager().init(R.id.cursor_loader, Bundle.EMPTY, this);
    }
    
    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {
        ...
        return new CursorLoader(this, contenUri, projection,
                                selection, selectionArgs, sortOrder);
    }
    
    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor cursor) {
        Datasource<? extends Cursor> datasource = new CursorDatasource(cursor);
        ...
    }
    
}
```

* [<tt>Datasources.EmptyDatasource</tt>](./recyclerview/src/main/java/com/globusltd/recyclerview/datasource/Datasources.java) is designed to be a [Null object](https://en.wikipedia.org/wiki/Null_Object_pattern) when you need empty <tt>Datasource</tt> instance. Call <tt>Datasources#empty()</tt> to obtain an empty and immutable <tt>Datasource</tt> instance in a memory-efficient way. 

### 2. Implement your adapter
Extend [<tt>Adapter</tt>](./recyclerview/src/main/java/com/globusltd/recyclerview/Adapter.java), provide suitable constructor and [<tt>DiffCallbackFactory</tt>](./recyclerview/src/main/java/com/globusltd/recyclerview/diff/DiffCallbackFactory.java) (optional). 

```java
class MyItemsAdapter extends Adapter<MyItem, MyItemViewHolder> {
    
    public MyItemsAdapter() {
        this(Datasources.<MyItem>empty());
    }
    
    public MyItemsAdapter(Datasource<MyItem> datasource) {
        super(datasource, new MyItemDiffCallbackFactory());
    }
    
    // Override onCreateViewHolder(LayoutInflater, ViewGroup, int)
    ...
    
    // Override onBindViewHolder(MyItemViewHolder, MyItem, int)
    ...
    
}

class MyItemDiffCallbackFactory implements DiffCallbackFactory<MyItem> {
                                
    @Override
    public DiffCallback createDiffCallback(Datasource<? extends MyItem> oldDatasource,
                                           Datasource<? extends MyItem> newDatasource) {
        return new SimpleDatasourcesDiffCallback<MyItem>(oldDatasource, newDatasource) {
                                
            @Override
            public boolean areItemsTheSame(MyItem oldItem, MyItem newItem) {
                return oldItem.getId() == newItem.getId();
            }
            
            @Override
            public boolean areContentsTheSame(MyItem oldItem, MyItem newItem) {
                return oldItem.getNumber() == newItem.getNumber() &&
                        TextUtils.equals(oldItem.getString(), newItem.getString());
            }
        
        };
   }
   
}
```

Notice that you should provide non-null <tt>DiffCallbackFactory</tt> to handle data changes between previous and new <tt>Datasource</tt>s. In this case when you replace <tt>Datasource</tt> inside adapter by calling <tt>Adapter#swap(Datasource)</tt> all diffs will be properly animated by <tt>RecyclerView.ItemAnimator</tt>. Also you can provide <code>null</code> instead of <tt>DiffCallbackFactory</tt> instance and generic animation will be applied when <tt>Datasource</tt> is changed.

The sample below shows how to replace existing datasource in the adapter by the new one.

```java
class MyActivity extends AppCompatActivity implements LoaderManager.LoaderCallbacks<List<MyItem>> {
        
    @Override
    public void onLoadFinished(List<MyItem> items) {
        Datasource<? extends MyItem> newDatasource = new ListDatasource(items);
        
        Datasource<? extends MyItem> oldDatasource = mAdapter.swap(datasource);
        // Do what ever you want with the old datasource. 
        // For example, release resources
    }
    
}
```

### 3. Attach item click and item long click listeners (optional)
Define which views can be clicked by implementing [<tt>ItemClickHelper.Callback</tt>](./recyclerview/src/main/java/com/globusltd/recyclerview/view/ItemClickHelper.java). In many cases it's better to make your adapter implement <tt>ItemClickHelper.Callback</tt>.

```java
class MyItemsAdapter extends Adapter<MyItem, MyItemViewHolder>
            implements ItemClickHelper.Callback<MyItem> {

    ...
    
    @Override
    public MyItem get(int position) {
        return getDatasource().get(position);
    }
    
    @Override
    public ClickableViews getClickableViews(int position, int viewType) {
        return MyItemViewHolder.CLICKABLE_VIEWS;
    }
    
    ...
    
}

class MyItemViewHolder extends RecyclerView.ViewHolder {

    static final ClickableViews CLICKABLE_VIEWS = new ClickableViews(
            ClickableViews.ITEM_VIEW_ID, // Default clickable view
            R.id.action_delete); // Array of other clickable views
            
    ...
    
}
```

[<tt>ClickableViews</tt>](./recyclerview/src/main/java/com/globusltd/recyclerview/view/ClickableViews.java) is a simple class that keeps identifiers of the views that can be clicked. To make only root item view clickable you can use predefined constant <tt>ClickableViews#ITEM_VIEW</tt>. To avoid clicks on all views including item view provide <tt>ClickableViews#NONE</tt>. 
  
The sample above shows how to make clickable root item view and view with id=R.id.action_delete. Please don't forget set selector/ripple background/foreground to the clickable views in xml or programmatically or touch feedback will not be shown when view is pressed. 

After that create a new [<tt>ItemClickHelper</tt>](./recyclerview/src/main/java/com/globusltd/recyclerview/view/ItemClickHelper.java) instance in activity or fragment, provide <tt>ItemClickHelper.Callback</tt> as constructor argument, define click listeners and attach <tt>ItemClickHelper</tt> instance to <tt>RecyclerView</tt>.

```java
class MyActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        ...
        
        mRecyclerView = (RecyclerView) findViewById(android.R.id.list);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        mRecyclerView.setItemAnimator(new DefaultItemAnimator());
        
        // Adapter implements ItemClickHelper.Callback
        mAdapter = new MyItemsAdapter();
        mRecyclerView.setAdapter(mAdapter);

        // Add click support to RecyclerView
        mItemClickHelper = new ItemClickHelper<>(mAdapter);
        mItemClickHelper.setOnItemClickListener(this::onItemClick);
        mItemClickHelper.setOnItemLongClickListener(this::onItemLongClick);
        mItemClickHelper.setRecyclerView(mRecyclerView);        
        
        ...
    }
    
    boolean onItemClick(View view, MyItem myItem, int position) {
        switch (view.getId()) {
            case R.id.action_delete:
                // Do something when particular view is clicked
                return true;

            default:
                // Do something when the whole item view is clicked
                return true;
        }
    }

    boolean onItemLongClick(View view, MyItem myItem, int position) {
        // Do something
        return true;
    }
    
    @Override
    protected void onDestroy() {
        // Detach ItemClickHelper from RecyclerView to avoid memory leaks
        mItemClickHelper.setRecyclerView(null);
        
        // Reset adapter to destroy and release all created view holders
        mRecyclerView.setAdapter(null);
        
        super.onDestroy();
    }
    
}
```

### 4. Attach choice mode (optional)
Attach choice mode to the <tt>RecyclerView</tt> when you need item selection. 

There are 5 default choice modes:
* [<tt>NoneChoiceMode</tt>](./recyclerview/src/main/java/com/globusltd/recyclerview/choice/NoneChoiceMode.java) - default choice mode that does nothing.
* [<tt>SingleChoiceMode</tt>](./recyclerview/src/main/java/com/globusltd/recyclerview/choice/SingleChoiceMode.java) allows up to one choice.
* [<tt>MultipleChoiceMode</tt>](./recyclerview/src/main/java/com/globusltd/recyclerview/choice/MultipleChoiceMode.java) allows any number of items to be chosen.
* [<tt>SingleModalChoiceMode</tt>](./recyclerview/src/main/java/com/globusltd/recyclerview/choice/SingleModalChoiceMode.java) allows up to one choice in a modal selection mode.
* [<tt>MultipleModalChoiceMode</tt>](./recyclerview/src/main/java/com/globusltd/recyclerview/choice/MultipleModalChoiceMode.java) allows any number of items to be chosen in a modal selection mode.

All choice modes except <tt>NoneChoiceMode</tt> require adapter has stable ids otherwise exception will be thrown.

[<tt>ChoiceModeHelper</tt>](./recyclerview/src/main/java/com/globusltd/recyclerview/view/ChoiceModeHelper.java) extends <tt>ItemClickHelper</tt> and intercepts user clicks to dispatch them to chosen choice mode. It means that you've never got item long click event if you choose any of the choice modes except <tt>NoneChoiceMode</tt>. You've also never got item click event if you chose <tt>SingleChoiceMode</tt> or <tt>MultipleChoiceMode</tt> because they are always in activated state. Modal choice modes are activated by long click on item view.

Choice modes can save and restore state across orientation changes.

```java
class MyActivity extends AppCompatActivity {
        
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        ...
        
        mRecyclerView = (RecyclerView) findViewById(android.R.id.list);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        mRecyclerView.setItemAnimator(new DefaultItemAnimator());
        
        mAdapter = new MyItemsAdapter();
        mRecyclerView.setAdapter(mAdapter);

        mChoiceMode = new SingleChoiceMode(savedInstanceState);
        mChoiceMode.setChoiceModeListener(this::onItemCheckedChanged);
        
        mChoiceModeHelper = new ChoiceModeHelper<>(adapter, mChoiceMode);
        mChoiceModeHelper.setRecyclerView(mRecyclerView);
    }
    
    void onItemCheckedChanged(long itemId, boolean checked, boolean fromUser) {
        // Do something when item's checked state is changed.
    }
    
    @Override
    protected void onSaveInstanceState(Bundle outState) {
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
```

Modal choice modes require [<tt>ModalChoiceModeListener</tt>](./recyclerview/src/main/java/com/globusltd/recyclerview/choice/ModalChoiceModeListener.java) to be implemented.

```java
class MyActivity extends AppCompatActivity {
        
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        ...

        mRecyclerView = (RecyclerView) findViewById(android.R.id.list);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        mRecyclerView.setItemAnimator(new DefaultItemAnimator());
        
        mAdapter = new MyItemsAdapter();
        mRecyclerView.setAdapter(mAdapter);
        
        final ActionModeCompat actionMode = ActionModeCompat.from(this);
        mChoiceMode = new MultipleModalChoiceMode(actionMode, 
                mModalChoiceModeListener, savedInstanceState);
        mChoiceMode.setChoiceModeListener(this::onItemCheckedChanged);
        
        mChoiceModeHelper = new ChoiceModeHelper<>(adapter, mChoiceMode);
        mChoiceModeHelper.setOnItemClickListener(this::onItemClick);
        mChoiceModeHelper.setRecyclerView(mRecyclerView);
    }
    
    boolean onItemClick(View view, MyItem myItem, int position) {
        ...
    }
    
    private final ModalChoiceModeListener mModalChoiceModeListener =
            new ModalChoiceModeListener() {

        @Override
        public boolean onCreateActionMode(ActionMode mode, Menu menu) {
            final MenuInflater inflater = mode.getMenuInflater();
            inflater.inflate(R.menu.action_mode_menu, menu);
            return true;
        }

        @Override
        public boolean onPrepareActionMode(ActionMode mode, Menu menu) {
            int checkedCount = mChoiceMode.getCheckedItemCount();

            // Show / hide menu items or menu groups based on number of checked items
            MenuItem item = menu.findItem(R.id.action_delete);
            item.setVisible(checkedCount > 0);

            // Update action mode title
            if (checkedCount > 0) {
                final Resources resources = getResources();
                mode.setTitle(resources.getQuantityString(
                        R.plurals.action_mode_x_items_selected,
                        checkedCount, checkedCount));
            } else {
                mode.setTitle(R.string.action_mode_no_items_selected);
            }

            return true;
        }

        @Override
        public boolean onActionItemClicked(ActionMode mode, MenuItem item) {
            switch (item.getItemId()) {
                case R.id.action_delete:
                    LongArrayList checkedItems = mChoiceMode.getCheckedItems();
                    ...
                    mChoiceMode.finish();
                    return true;

                default:
                    return false;
            }
        }

        @Override
        public void onDestroyActionMode(ActionMode mode) {
            ...
        }

        @Override
        public void onItemCheckedStateChanged(ActionMode mode, long itemId, 
                                              boolean checked, boolean fromUser) {
            ...
        }

    };    
    
    @Override
    protected void onSaveInstanceState(Bundle outState) {
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
```

Make sure that view holder's item view is checkable (for example, <tt>CheckedTextView</tt>) or has selector background/foreground with defined <tt>android.R.attr.state_activated</tt> attribute value. This allows choice mode provide correct touch and state feedbacks to the user.    

You can also implement [<tt>CheckableViewHolder</tt>](./recyclerview/src/main/java/com/globusltd/recyclerview/choice/CheckableViewHolder.java) on your view holder to observe choice mode and item checked state changes. It can be useful to start smooth animations when checked state is changed.
```java
class MyItemViewHolder extends RecyclerView.ViewHolder implements CheckableViewHolder {
    
    ...
    
    @Override
    public void setInChoiceMode(boolean isInChoiceMode) {
        // Choice mode is activated or deactivated
        // Animate views appearance 
    }
    
    @Override
    public void setChecked(boolean checked, boolean fromUser) {
        // Item checked state is changed
        // Animate checked state
    }
    
}
```

### 5. Apply view holder behaviors (optional)
[<tt>ViewHolderTracker</tt>](./recyclerview/src/main/java/com/globusltd/recyclerview/ViewHolderTracker.java) allows you observe view holder's lifecycle events like attach, detach and position change.
  
There are 3 default view holder observers:
* [<tt>SimpleEnableBehavior</tt>](./recyclerview/src/main/java/com/globusltd/recyclerview/view/SimpleEnableBehavior.java) to enable or disable view holder's item view.
* [<tt>RecursiveEnableBehavior</tt>](./recyclerview/src/main/java/com/globusltd/recyclerview/RecursiveEnableBehavior.java) to enable or disable all of the views inside view holder's item view including item view.
* [<tt>LifecycleBehavior</tt>](./recyclerview/src/main/java/com/globusltd/recyclerview/lifecycle/) is an experimental view holder observer that provides acivity/fragment lifecycle callbacks to the view holder.

```java
class MyItemsAdapter extends ... implements ..., SimpleEnableBehavior.Callback {

    ...
    
    @Override
    public boolean isEnabled(int position) {
        MyItem myItem = getDatasource().get(position);
        return myItem.getId() > 0; // Any condition base on position and/or item data
    }
    
    @Override
    public ClickableViews getClickableViews(int position, int viewType) {
        if (isEnabled(position)) {
            return MyItemViewHolder.CLICKABLE_VIEWS;
        } else {
            return ClickableViews.NONE; // Make disabled view not clickable
        }
    }
    
    ...
    
}

class MyActivity extends AppCompatActivity {
        
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        ...

        mRecyclerView = (RecyclerView) findViewById(android.R.id.list);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        mRecyclerView.setItemAnimator(new DefaultItemAnimator());
        
        // Adapter implements SimpleEnableBehavior.Callback
        mAdapter = new MyItemsAdapter();
        mRecyclerView.setAdapter(mAdapter);
        
        ...
        
        // Add enable view holder behavior
        mViewHolderTracker = new ViewHolderTracker();
        mViewHolderTracker.registerViewHolderObserver(new RecursiveEnableBehavior(adapter));
        mViewHolderTracker.setRecyclerView(mRecyclerView);
    }
    
    @Override
    protected void onDestroy() {
        ...
        mViewHolderTracker.setRecyclerView(null);
        mRecyclerView.setAdapter(null);
        super.onDestroy();
    }
    
}
```

# License
    Copyright 2017 Globus Ltd.

    Licensed under the Apache License, Version 2.0 (the "License");
    you may not use this file except in compliance with the License.
    You may obtain a copy of the License at

       http://www.apache.org/licenses/LICENSE-2.0

    Unless required by applicable law or agreed to in writing, software
    distributed under the License is distributed on an "AS IS" BASIS,
    WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
    See the License for the specific language governing permissions and
    limitations under the License.
