package com.sangcomz.fishbundemo;

import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.ImageView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.sangcomz.fishbun.FishBun;
import com.sangcomz.fishbun.adapter.image.impl.GlideAdapter;
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
        imgMain = findViewById(R.id.img_main);
        recyclerView = findViewById(R.id.recyclerview);
        linearLayoutManager = new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false);
        mainController = new ImageController(imgMain);
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
                            .setImageAdapter(new GlideAdapter())
                            .setIsUseAllDoneButton(true)
                            .setMenuDoneText("완료")
                            .setMenuAllDoneText("전부 완료")
                            .startAlbum();
                    break;
                }
                //dark
                case 1: {
                    FishBun.with(WithActivityActivity.this)
                            .setImageAdapter(new PicassoAdapter())
                            .setMaxCount(5)
                            .setMinCount(3)
                            .setPickerSpanCount(5)
                            .setActionBarColor(Color.parseColor("#795548"), Color.parseColor("#5D4037"), false)
                            .setActionBarTitleColor(Color.parseColor("#ffffff"))
                            .setSelectedImages(path)
                            .setAlbumSpanCount(2, 3)
                            .setButtonInAlbumActivity(false)
                            .setCamera(true)
                            .exceptGif(true)
                            .setReachLimitAutomaticClose(true)
                            .setHomeAsUpIndicatorDrawable(ContextCompat.getDrawable(this, R.drawable.ic_custom_back_white))
                            .setDoneButtonDrawable(ContextCompat.getDrawable(this, R.drawable.ic_custom_ok))
                            .setAllViewTitle("All")
                            .setActionBarTitle("FishBun Dark")
                            .textOnNothingSelected("Please select three or more!")
                            .startAlbum();
                    break;
                }
                //Light
                case 2: {
                    FishBun.with(WithActivityActivity.this)
                            .setImageAdapter(new PicassoAdapter())
                            .setPickerCount(50)
                            .setPickerSpanCount(4)
                            .setActionBarColor(Color.parseColor("#ffffff"), Color.parseColor("#ffffff"), true)
                            .setActionBarTitleColor(Color.parseColor("#000000"))
                            .setSelectedImages(path)
                            .setAlbumSpanCount(1, 2)
                            .setButtonInAlbumActivity(true)
                            .setCamera(false)
                            .exceptGif(true)
                            .setReachLimitAutomaticClose(false)
                            .setHomeAsUpIndicatorDrawable(ContextCompat.getDrawable(this, R.drawable.ic_arrow_back_black_24dp))
//                            .setDoneButtonDrawable(ContextCompat.getDrawable(this, R.drawable.ic_check_black_24dp))
//                            .setAllDoneButtonDrawable(ContextCompat.getDrawable(this, R.drawable.ic_done_all_black_24dp))

                            .setIsUseAllDoneButton(true)
                            .setMenuDoneText("완료")
                            .setMenuAllDoneText("전부 완료")

                            .setIsUseAllDoneButton(true)
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