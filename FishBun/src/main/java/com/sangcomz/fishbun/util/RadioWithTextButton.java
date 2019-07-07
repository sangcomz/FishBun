package com.sangcomz.fishbun.util;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import androidx.annotation.Nullable;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.View;

import com.sangcomz.fishbun.R;

/**
 * Created by sangcomz on 01/05/2017.
 */

public class RadioWithTextButton extends View {

    private Paint mStrokePaint;
    private Paint mCirclePaint;
    private Paint mTextPaint;

    private String mText = null;
    private float mTextWidth;

    private Drawable mDrawable = null;
    private Rect mCenterRect = null;

    public RadioWithTextButton(Context context) {
        super(context);
        init();
    }

    public RadioWithTextButton(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public RadioWithTextButton(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    private void init() {
        mTextPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        mStrokePaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        mCirclePaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        mTextPaint.setFakeBoldText(true);
    }


    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        int w = getWidth();
        int h = getHeight();

        mStrokePaint.setStrokeWidth(w / 18);

        if (isChecked()) {
            canvas.drawCircle(w / 2, h / 2, w / 3, mCirclePaint);
            if (mText != null) {
                int PADDING_TEXT = 20;
                mTextWidth = (w / 3) * 2 - PADDING_TEXT;
                drawTextCentred(canvas, mTextPaint, mText, w / 2, h / 2);
            } else if (mDrawable != null) {
                mDrawable.setBounds(getCenterRect());
                mDrawable.draw(canvas);
            }
        } else {
            mStrokePaint.setStyle(Paint.Style.STROKE);
            canvas.drawCircle(w / 2, h / 2, w / 3, mStrokePaint);
        }

    }

    public void setCircleColor(int color) {
        if (mCirclePaint != null)
            mCirclePaint.setColor(color);
    }

    public void setTextColor(int color) {
        if (mTextPaint != null)
            mTextPaint.setColor(color);
    }

    public void setStrokeColor(int color) {
        if (mStrokePaint != null)
            mStrokePaint.setColor(color);
    }

    public void setText(String text) {
        mDrawable = null;
        mText = text;
        invalidate();
    }

    public void setDrawable(Drawable drawable) {
        mText = null;
        mDrawable = drawable;
        invalidate();
    }

    public boolean isChecked() {
        return mText != null || mDrawable != null;
    }

    public void unselect() {
        mText = null;
        mDrawable = null;
        invalidate();
    }

    private final Rect textBounds = new Rect(); //don't new this up in a draw method

    public void drawTextCentred(Canvas canvas, Paint paint, String text, float cx, float cy) {
        setTextSizeForWidth(paint, text, mTextWidth);
        paint.getTextBounds(text, 0, text.length(), textBounds);
        canvas.drawText(text, cx - textBounds.exactCenterX(), cy - textBounds.exactCenterY(), paint);
    }

    /**
     * Sets the text size for a Paint object so a given string of text will be a
     * given width.
     *
     * @param paint        the Paint to set the text size for
     * @param desiredWidth the desired width
     */
    private static void setTextSizeForWidth(Paint paint, String text, float desiredWidth) {

        // Pick a reasonably large value for the test. Larger values produce
        // more accurate results, but may cause problems with hardware
        // acceleration. But there are workarounds for that, too; refer to
        // http://stackoverflow.com/questions/6253528/font-size-too-large-to-fit-in-cache
        final float defaultTextSize = 44f;
        paint.setTextSize(defaultTextSize);
        Rect textBounds = new Rect();
        paint.getTextBounds(text, 0, text.length(), textBounds);
        // Calculate the desired size as a proportion of our testTextSize.
        if (textBounds.width() > desiredWidth) {
            float desiredTextSize = defaultTextSize * (desiredWidth / textBounds.width());

            // Set the paint for that size.
            paint.setTextSize(desiredTextSize);
        }

    }

    private int fetchAccentColor() {
        TypedValue typedValue = new TypedValue();
        TypedArray a = getContext().obtainStyledAttributes(typedValue.data, new int[]{R.attr.colorAccent});
        int color = a.getColor(0, 0);
        a.recycle();
        return color;
    }

    private Rect getCenterRect() {
        if (mCenterRect == null) {
            Rect r = new Rect(0, 0, getWidth(), getHeight());
            int width = getWidth() / 4;
            mCenterRect = new Rect((int) (r.exactCenterX() - width), (int) (r.exactCenterY() - width), getWidth() - width, getHeight() - width);
        }
        return mCenterRect;
    }
}
