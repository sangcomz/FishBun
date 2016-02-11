package com.sangcomz.fishbun.util;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.TextView;

/**
 * Created by sangc on 2015-12-26.
 */
public class SquareTextView extends TextView {
    public SquareTextView(Context context) {
        super(context);
    }

    public SquareTextView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public SquareTextView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

//    public SquareTextView(Context context) {
//        super(context);
//    }
//
//    public SquareTextView(Context context, AttributeSet attrs) {
//        super(context, attrs);
//    }
//
//    public SquareTextView(Context context, AttributeSet attrs, int defStyleAttr) {
//        super(context, attrs, defStyleAttr);
//    }
//
    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        int width = getMeasuredWidth();
        setMeasuredDimension(width, width);
    }
}
