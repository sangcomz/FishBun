package com.sangcomz.fishbun;

import android.os.Build;
import android.os.Bundle;
import android.view.Window;

import androidx.appcompat.app.AppCompatActivity;

import com.sangcomz.fishbun.define.Define;

public abstract class BaseActivity extends AppCompatActivity {
    protected Define define = new Define();
    protected Fishton fishton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            getWindow().requestFeature(Window.FEATURE_CONTENT_TRANSITIONS);
        }
        super.onCreate(savedInstanceState);
        fishton = Fishton.getInstance();
    }
}
