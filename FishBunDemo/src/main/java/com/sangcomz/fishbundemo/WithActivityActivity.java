package com.sangcomz.fishbundemo;

import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.ImageView;

import com.sangcomz.fishbun.FishBun;
import com.sangcomz.fishbun.define.Define;

import java.util.ArrayList;

public class WithActivityActivity extends AppCompatActivity {

    ArrayList<Uri> path = new ArrayList<>();
    ImageView imgMain;
    RecyclerView recyclerView;
    LinearLayoutManager linearLayoutManager;
    ImageAdapter imageAdapter;
    ImageController mainController;
    int mode;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_withactivity);
        mode = getIntent().getIntExtra("mode", -1);
        imgMain = (ImageView) findViewById(R.id.img_main);
        recyclerView = (RecyclerView) findViewById(R.id.recyclerview);
        linearLayoutManager = new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false);
        mainController = new ImageController(this, imgMain);
        imageAdapter = new ImageAdapter(this, mainController, path);
        recyclerView.setLayoutManager(linearLayoutManager);
        recyclerView.setAdapter(imageAdapter);
    }

    protected void onActivityResult(int requestCode, int resultCode,
                                    Intent imageData) {
        super.onActivityResult(requestCode, resultCode, imageData);

        switch (requestCode) {
            case Define.ALBUM_REQUEST_CODE:
                if (resultCode == RESULT_OK) {
                    path = imageData.getParcelableArrayListExtra(Define.INTENT_PATH);
                    imageAdapter.changePath(path);
                    break;
                }
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_main, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.action_plus) {
            switch (mode) {
                //basic
                case 0: {
                    FishBun.with(WithActivityActivity.this)
                            .MultiPageMode()
                            .setIsUseDetailView(false)
                            .startAlbum();
                    break;
                }
                //dark
                case 1: {
                    FishBun.with(WithActivityActivity.this)
                            .MultiPageMode()
                            .setMaxCount(5)
                            .setMinCount(3)
                            .setPickerSpanCount(5)
                            .setActionBarColor(Color.parseColor("#795548"), Color.parseColor("#5D4037"), false)
                            .setActionBarTitleColor(Color.parseColor("#ffffff"))
                            .setArrayPaths(path)
                            .setAlbumSpanCount(2, 3)
                            .setButtonInAlbumActivity(false)
                            .setCamera(true)
                            .exceptGif(true)
                            .setReachLimitAutomaticClose(true)
                            .setHomeAsUpIndicatorDrawable(ContextCompat.getDrawable(this, R.drawable.ic_custom_back_white))
                            .setOkButtonDrawable(ContextCompat.getDrawable(this, R.drawable.ic_custom_ok))
                            .setAllViewTitle("All")
                            .setActionBarTitle("FishBun Dark")
                            .textOnNothingSelected("Please select three or more!")
                            .startAlbum();
                    break;
                }
                //Light
                case 2: {
                    FishBun.with(WithActivityActivity.this)
                            .MultiPageMode()
                            .setPickerCount(50)
                            .setPickerSpanCount(4)
                            .setActionBarColor(Color.parseColor("#ffffff"), Color.parseColor("#ffffff"), true)
                            .setActionBarTitleColor(Color.parseColor("#000000"))
                            .setArrayPaths(path)
                            .setAlbumSpanCount(1, 2)
                            .setButtonInAlbumActivity(true)
                            .setCamera(false)
                            .exceptGif(true)
                            .setReachLimitAutomaticClose(false)
                            .setHomeAsUpIndicatorDrawable(ContextCompat.getDrawable(this, R.drawable.ic_arrow_back_black_24dp))
                            .setOkButtonDrawable(ContextCompat.getDrawable(this, R.drawable.ic_check_black_24dp))
                            .setAllViewTitle("All of your photos")
                            .setActionBarTitle("FishBun Light")
                            .textOnImagesSelectionLimitReached("You can't select any more.")
                            .textOnNothingSelected("I need a photo!")
                            .startAlbum();
                    break;
                }
                default: {
                    finish();
                }
            }

            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
