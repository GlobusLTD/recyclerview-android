package android.support.v7.widget;

import android.support.annotation.NonNull;
import android.util.Log;
import android.view.View;

public class ObservableViewHolder extends RecyclerView.ViewHolder {
    
    public ObservableViewHolder(@NonNull final View itemView) {
        super(itemView);
    }
    
    @Override
    void offsetPosition(final int offset, final boolean applyToPreLayout) {
        super.offsetPosition(offset, applyToPreLayout);
        if (isBound()) {
            Log.i("11111", "Position changed from " + mOldPosition + " to " + mPosition);
        }
    }
    
}