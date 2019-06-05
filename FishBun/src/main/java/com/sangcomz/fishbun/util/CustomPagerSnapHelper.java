package com.sangcomz.fishbun.util;

import androidx.recyclerview.widget.PagerSnapHelper;
import androidx.recyclerview.widget.RecyclerView;

/**
 * Created by sangcomz on 11/06/2017.
 */

public class CustomPagerSnapHelper extends PagerSnapHelper {

    RecyclerView.LayoutManager layoutManager;
    OnPageChangeListener listener;

    public CustomPagerSnapHelper(RecyclerView.LayoutManager layoutManager) {
        this.layoutManager = layoutManager;
    }

    @Override
    public boolean onFling(int velocityX, int velocityY) {
        if (listener != null) {
            listener.onPageChanged(findTargetSnapPosition(layoutManager, velocityX, velocityY));
        }
        return super.onFling(velocityX, velocityY);
    }

    public void setOnPageChangeListener(OnPageChangeListener listener) {
        this.listener = listener;
    }

    public interface OnPageChangeListener {
        void onPageChanged(int position);
    }
}
