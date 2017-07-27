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

public class SampleAdapter /*extends Adapter1<CharSequence, SampleAdapter.SimpleCheckedViewHolder>*/ {
    
    /*private static final int VIEW_TYPE_SINGLE_LINE = 0;
    private static final int VIEW_TYPE_TWO_LINES = 1;
    
    public SampleAdapter() {
        super(new CharSequenceDiffCallbackFactory());
    }
    
    public SampleAdapter(@NonNull final Datasource<? extends CharSequence> datasource) {
        super(datasource, new CharSequenceDiffCallbackFactory());
    }
    
    @Override
    public int getItemViewType(final int position) {
        return position == 0 ? VIEW_TYPE_TWO_LINES : VIEW_TYPE_SINGLE_LINE;
    }
    
    @Override
    public boolean isEnabled(final int position) {
        return position % 2 == 0;
    }
    
    @NonNull
    @Override
    public ClickableViews getClickableViews(final int position, final int viewType) {
        return ClickableViews.ITEM_VIEW;
    }
    
    @NonNull
    @Override
    public SimpleCheckedViewHolder onCreateViewHolder(@NonNull final LayoutInflater inflater,
                                                   @NonNull final ViewGroup parent,
                                                   final int viewType) {
        final SimpleCheckedViewHolder viewHolder;
        switch (viewType) {
            case VIEW_TYPE_SINGLE_LINE:
                final View singleLineView = inflater.inflate(android.R.layout.simple_list_item_1, parent, false);
                viewHolder = new SimpleCheckedViewHolder(singleLineView);
                break;
            
            case VIEW_TYPE_TWO_LINES:
                final View twoLinesView = inflater.inflate(android.R.layout.simple_list_item_2, parent, false);
                viewHolder = new TwoLinesViewHolder(twoLinesView);
                break;
            
            default:
                throw new IllegalArgumentException("Unknown view type = " + viewType);
        }
        
        // Typically you should specify ?attr/selectableItemBackground as background in layout file,
        // but for platform views you can specify it programmatically
        TouchFeedback.selectableItemBackground(viewHolder.itemView);
        
        return viewHolder;
    }
    
    @Override
    public void onBindViewHolder(@NonNull final SimpleCheckedViewHolder holder,
                                 @NonNull final CharSequence item, final int position) {
        holder.setText1(item);
    }
    
    static class SimpleCheckedViewHolder extends RecyclerView.ViewHolder {
        
        @NonNull
        private final TextView mTextView1;
        
        SimpleCheckedViewHolder(@NonNull final View itemView) {
            super(itemView);
            mTextView1 = (TextView) itemView.findViewById(android.R.id.text1);
        }
        
        void setText1(@Nullable final CharSequence item) {
            mTextView1.setText(item);
        }
        
    }
    
    private static class TwoLinesViewHolder extends SimpleCheckedViewHolder implements LifecycleCallbacks {
        
        @NonNull
        private final TextView mTextView2;
        
        TwoLinesViewHolder(@NonNull final View itemView) {
            super(itemView);
            mTextView2 = (TextView) itemView.findViewById(android.R.id.text2);
        }
        
        @Override
        public void onStart() {
            mTextView2.setText("Started");
        }
        
        @Override
        public void onResume() {
            mTextView2.setText("Resumed");
        }
        
        @Override
        public void onPause() {
            mTextView2.setText("Paused");
        }
        
        @Override
        public void onStop() {
            mTextView2.setText("Stopped");
        }
        
    }
    
    private static class CharSequenceDiffCallbackFactory implements DiffCallbackFactory<CharSequence> {
        
        @NonNull
        @Override
        public DiffCallback createDiffCallback(@NonNull final Datasource<? extends CharSequence> oldDatasource,
                                               @NonNull final Datasource<? extends CharSequence> newDatasource) {
            return new SimpleDatasourcesDiffCallback<CharSequence>(oldDatasource, newDatasource) {
                
                @Override
                public boolean areItemsTheSame(@NonNull final CharSequence oldItem,
                                               @NonNull final CharSequence newItem) {
                    return TextUtils.equals(oldItem, newItem);
                }
                
                @Override
                public boolean areContentsTheSame(@NonNull final CharSequence oldItem,
                                                  @NonNull final CharSequence newItem) {
                    return true;
                }
                
            };
        }
        
    }*/
    
}