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

import android.database.Cursor;
import android.graphics.Color;
import android.graphics.Typeface;
import android.provider.CallLog;
import android.support.annotation.NonNull;
import android.text.SpannableStringBuilder;
import android.text.Spanned;
import android.text.TextUtils;
import android.text.format.DateUtils;
import android.text.style.ForegroundColorSpan;
import android.text.style.StyleSpan;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import com.globusltd.recyclerview.Adapter;
import com.globusltd.recyclerview.datasource.Datasource;
import com.globusltd.recyclerview.datasource.Datasources;
import com.globusltd.recyclerview.diff.DiffCallback;
import com.globusltd.recyclerview.diff.DiffCallbackFactory;
import com.globusltd.recyclerview.diff.SimpleDatasourcesDiffCallback;
import com.globusltd.recyclerview.sample.TwoLinesViewHolder;

class CallsAdapter extends Adapter<Cursor, TwoLinesViewHolder> {

    CallsAdapter() {
        super(Datasources.empty(), new CallsDiffCallbackFactory());
        setHasStableIds(true);
    }

    @Override
    public long getItemId(final int position) {
        final Cursor cursor = getDatasource().get(position);
        return cursor.getLong(cursor.getColumnIndexOrThrow(CallLog.Calls._ID));
    }

    @NonNull
    @Override
    public TwoLinesViewHolder onCreateViewHolder(@NonNull final LayoutInflater inflater,
                                                 @NonNull final ViewGroup parent, final int viewType) {
        return TwoLinesViewHolder.inflate(inflater, parent);
    }

    @Override
    public void onBindViewHolder(@NonNull final TwoLinesViewHolder holder,
                                 @NonNull final Cursor cursor, final int position) {
        final SpannableStringBuilder primary = new SpannableStringBuilder();
        final String cachedName = cursor.getString(cursor.getColumnIndexOrThrow(CallLog.Calls.CACHED_NAME));
        final String number = cursor.getString(cursor.getColumnIndexOrThrow(CallLog.Calls.NUMBER));
        SpannableStringBuilderCompat.append(primary, (cachedName != null ? cachedName : number),
                new StyleSpan(Typeface.BOLD), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        primary.append(" ");

        final long duration = cursor.getLong(cursor.getColumnIndexOrThrow(CallLog.Calls.DURATION));
        if (duration > 0) {
            final CharSequence formattedDuration = DateUtils.formatElapsedTime(duration);
            primary.append(formattedDuration);
        }

        holder.setText1(primary);

        final SpannableStringBuilder summary = new SpannableStringBuilder();
        final int type = cursor.getInt(cursor.getColumnIndexOrThrow(CallLog.Calls.TYPE));
        switch (type) {
            case CallLog.Calls.INCOMING_TYPE:
                SpannableStringBuilderCompat.append(summary, "←", new ForegroundColorSpan(Color.BLUE),
                        Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
                summary.append(" ");
                break;

            case CallLog.Calls.OUTGOING_TYPE:
                SpannableStringBuilderCompat.append(summary, "→", new ForegroundColorSpan(Color.GREEN),
                        Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
                summary.append(" ");
                break;

            case CallLog.Calls.MISSED_TYPE:
            case CallLog.Calls.REJECTED_TYPE:
                SpannableStringBuilderCompat.append(summary, "☇", new ForegroundColorSpan(Color.RED),
                        Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
                summary.append(" ");
                break;

            case CallLog.Calls.BLOCKED_TYPE:
                SpannableStringBuilderCompat.append(summary, "☓", new ForegroundColorSpan(Color.BLACK),
                        Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
                summary.append(" ");
                break;
        }

        final long date = cursor.getLong(cursor.getColumnIndexOrThrow(CallLog.Calls.DATE));
        final CharSequence relativeDate = DateUtils.getRelativeTimeSpanString(date,
                System.currentTimeMillis(), DateUtils.MINUTE_IN_MILLIS);
        summary.append(relativeDate);

        holder.setText2(summary);
    }

    private static class SpannableStringBuilderCompat {

        static void append(@NonNull final SpannableStringBuilder ssb, @NonNull final CharSequence text,
                           @NonNull final Object what, final int flags) {
            int start = ssb.length();
            ssb.append(text);
            ssb.setSpan(what, start, ssb.length(), flags);
        }

        private SpannableStringBuilderCompat() {
        }

    }

    private static class CallsDiffCallbackFactory implements DiffCallbackFactory<Cursor> {

        @NonNull
        @Override
        public DiffCallback createDiffCallback(@NonNull final Datasource<? extends Cursor> oldDatasource,
                                               @NonNull final Datasource<? extends Cursor> newDatasource) {
            return new SimpleDatasourcesDiffCallback<Cursor>(oldDatasource, newDatasource) {

                @Override
                public boolean areItemsTheSame(@NonNull final Cursor oldItem, @NonNull final Cursor newItem) {
                    final long oldId = oldItem.getLong(oldItem.getColumnIndexOrThrow(CallLog.Calls._ID));
                    final long newId = newItem.getLong(newItem.getColumnIndexOrThrow(CallLog.Calls._ID));
                    return oldId == newId;
                }

                @Override
                public boolean areContentsTheSame(@NonNull final Cursor oldItem, @NonNull final Cursor newItem) {
                    final String oldCachedName = oldItem.getString(oldItem.getColumnIndexOrThrow(CallLog.Calls.CACHED_NAME));
                    final String newCachedName = newItem.getString(newItem.getColumnIndexOrThrow(CallLog.Calls.CACHED_NAME));
                    final String oldNumber = oldItem.getString(oldItem.getColumnIndexOrThrow(CallLog.Calls.NUMBER));
                    final String newNumber = newItem.getString(newItem.getColumnIndexOrThrow(CallLog.Calls.NUMBER));
                    final long oldDate = oldItem.getLong(oldItem.getColumnIndexOrThrow(CallLog.Calls.DATE));
                    final long newDate = newItem.getLong(newItem.getColumnIndexOrThrow(CallLog.Calls.DATE));
                    final long oldDuration = oldItem.getLong(oldItem.getColumnIndexOrThrow(CallLog.Calls.DURATION));
                    final long newDuration = newItem.getLong(newItem.getColumnIndexOrThrow(CallLog.Calls.DURATION));
                    return TextUtils.equals(oldCachedName, newCachedName) &&
                            TextUtils.equals(oldNumber, newNumber) &&
                            oldDate == newDate &&
                            oldDuration == newDuration;
                }

            };
        }

    }

}
