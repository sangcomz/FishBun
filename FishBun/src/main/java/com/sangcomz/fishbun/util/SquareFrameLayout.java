package com.sangcomz.fishbun.util;

import android.content.Context;
import android.util.AttributeSet;

/**
 * Created by sangc on 2015-12-26.
 */
public class SquareFrameLayout extends android.support.v7.widget.ContentFrameLayout {
    public SquareFrameLayout(Context context) {
        super(context);
    }

    public SquareFrameLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public SquareFrameLayout(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, widthMeasureSpec);
    }
}
