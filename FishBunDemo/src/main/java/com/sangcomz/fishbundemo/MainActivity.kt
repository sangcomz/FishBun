package com.sangcomz.fishbundemo;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity {
    Button btnWithActivityLight;
    Button btnWithActivityBase;
    Button btnWithActivityDark;
    Button btnWithFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        btnWithActivityBase = (Button) findViewById(R.id.btn_with_activity_basic);
        btnWithActivityDark = (Button) findViewById(R.id.btn_with_activity_dark);
        btnWithActivityLight = (Button) findViewById(R.id.btn_with_activity_light);
        btnWithFragment = (Button) findViewById(R.id.btn_with_Fragment);

        btnWithActivityBase.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(MainActivity.this, WithActivityActivity.class);
                i.putExtra("mode", 0);
                startActivity(i);
            }
        });

        btnWithActivityDark.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(MainActivity.this, WithActivityActivity.class);
                i.putExtra("mode", 1);
                startActivity(i);
            }
        });

        btnWithActivityLight.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(MainActivity.this, WithActivityActivity.class);
                i.putExtra("mode", 2);
                startActivity(i);
            }
        });

        btnWithFragment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(MainActivity.this, WithFragmentActivity.class);
                startActivity(i);
            }
        });
    }
}
