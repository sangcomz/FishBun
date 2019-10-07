package com.sangcomz.fishbundemo;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.RelativeLayout;

public class WithFragmentActivity extends AppCompatActivity {

    RelativeLayout areaContainer;
    SubFragment subFragment;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_withfragment);

        areaContainer = (RelativeLayout)findViewById(R.id.area_container);
        subFragment = new SubFragment();

        getSupportFragmentManager().beginTransaction().add(areaContainer.getId(), subFragment).commit();

    }

    /**
     * Send onActivityResult method to SubFragment
     */
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        subFragment.onActivityResult(requestCode, resultCode, data);
    }
}
