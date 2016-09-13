package com.sangcomz.fishbundemo;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;

public class ChoiceActivity extends AppCompatActivity {
    Button btnWithActivity;
    Button btnWithFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_choice);

        btnWithActivity = (Button)findViewById(R.id.btn_with_activity);
        btnWithFragment = (Button)findViewById(R.id.btn_with_Fragment);

        btnWithActivity.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(ChoiceActivity.this, MainActivity.class);
                startActivity(i);
            }
        });

        btnWithFragment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(ChoiceActivity.this, SubActivity.class);
                startActivity(i);
            }
        });
    }
}
