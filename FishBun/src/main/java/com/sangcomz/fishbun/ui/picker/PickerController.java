package com.sangcomz.fishbun.ui.picker;

import android.support.v7.app.ActionBar;
import android.support.v7.widget.RecyclerView;
import android.view.MotionEvent;

import com.sangcomz.fishbun.define.Define;

/**
 * Created by sangc on 2015-11-05.
 */
public class PickerController {
    private RecyclerView recyclerView;
    private RecyclerView.OnItemTouchListener OnItemTouchListener;
    ActionBar actionBar;
    String bucketTitle;

    PickerController(ActionBar actionBar, RecyclerView recyclerView, String bucketTitle) {
        this.recyclerView = recyclerView;
        this.actionBar = actionBar;
        this.bucketTitle = bucketTitle;

        OnItemTouchListener = new RecyclerView.OnItemTouchListener() {
            @Override
            public boolean onInterceptTouchEvent(RecyclerView rv, MotionEvent e) {
                return true;
            }

            @Override
            public void onTouchEvent(RecyclerView rv, MotionEvent e) {

            }

            @Override
            public void onRequestDisallowInterceptTouchEvent(boolean disallowIntercept) {

            }
        };
    }

    /**
     * @param isAble true == can clickable
     */
    public void setRecyclerViewClickable(final boolean isAble) {
        if (isAble)
            recyclerView.removeOnItemTouchListener(OnItemTouchListener);
        else {
            recyclerView.addOnItemTouchListener(OnItemTouchListener);
        }

    }

    public void setActionbarTitle(int total) {
        actionBar.setTitle(bucketTitle + "(" + String.valueOf(total) + "/" + Define.ALBUM_PICKER_COUNT + ")");
    }
}
