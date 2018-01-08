package com.sangcomz.fishbun;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.sangcomz.fishbun.define.Define;
import com.sangcomz.fishbun.util.UiUtil;

/**
 * Created by sangcomz on 04/06/2017.
 */

public class BaseFragment extends Fragment {

    protected Define define = new Define();
    protected UiUtil uiUtil = new UiUtil();

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return super.onCreateView(inflater, container, savedInstanceState);
    }
}
