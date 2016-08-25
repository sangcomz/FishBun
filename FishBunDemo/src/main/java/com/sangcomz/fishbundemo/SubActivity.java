package com.sangcomz.fishbundemo;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.RelativeLayout;

public class SubActivity extends AppCompatActivity {

    RelativeLayout areaContainer;
    SubFragment subFragment;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sub);

        areaContainer = (RelativeLayout)findViewById(R.id.area_container);
        subFragment = new SubFragment();

        getSupportFragmentManager().beginTransaction().add(areaContainer.getId(), subFragment).commit();

    }
}
