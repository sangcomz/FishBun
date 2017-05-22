package com.sangcomz.fishbundemo;


import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;

import com.sangcomz.fishbun.FishBun;
import com.sangcomz.fishbun.define.Define;

import java.util.ArrayList;


/**
 * A simple {@link Fragment} subclass.
 */
public class SubFragment extends Fragment {

    ArrayList<Uri> path = new ArrayList<>();
    ImageView imgMain;
    Button btnAddImages;
    RecyclerView recyclerView;
    LinearLayoutManager linearLayoutManager;
    ImageAdapter imageAdapter;
    ImageController withActivityController;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_sub, container, false);
        // Inflate the layout for this fragment
        imgMain = (ImageView) rootView.findViewById(R.id.img_main);
        recyclerView = (RecyclerView) rootView.findViewById(R.id.recyclerview);
        btnAddImages = (Button) rootView.findViewById(R.id.btn_add_images);
        linearLayoutManager = new LinearLayoutManager(getActivity(), LinearLayoutManager.HORIZONTAL, false);
        withActivityController = new ImageController(getActivity(), imgMain);
        imageAdapter = new ImageAdapter(getActivity(), withActivityController, path);
        recyclerView.setLayoutManager(linearLayoutManager);
        recyclerView.setAdapter(imageAdapter);


        btnAddImages.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FishBun.with(SubFragment.this)
                        .MultiPageMode()
                        .setPickerCount(10)
                        .setActionBarColor(Color.parseColor("#3F51B5"), Color.parseColor("#303F9F"))
                        .setArrayPaths(path)
                        .setCamera(true)
                        .startAlbum();
            }
        });

        return rootView;
    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode) {
            case Define.ALBUM_REQUEST_CODE:
                if (resultCode == getActivity().RESULT_OK) {
                    path = data.getParcelableArrayListExtra(Define.INTENT_PATH);
                    imageAdapter.changePath(path);
                    break;
                }
        }
    }
}
