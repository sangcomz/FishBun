package com.sangcomz.fishbun.adapter;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.sangcomz.fishbun.R;
import com.sangcomz.fishbun.bean.Album;
import com.sangcomz.fishbun.define.Define;
import com.sangcomz.fishbun.ui.picker.PickerActivity;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;


public class AlbumListAdapter
        extends RecyclerView.Adapter<AlbumListAdapter.ViewHolder> {

    private List<Album> albumList;
    private ArrayList<Uri> pickedImagePath = new ArrayList<>();
    private int albumSize;
    private Bundle bundle;


    public AlbumListAdapter(ArrayList<Uri> pickedImagePath,
                            int albumSize,
                            Bundle bundle) {
        this.pickedImagePath = pickedImagePath;
        this.albumSize = albumSize;
        this.bundle = bundle;
    }

    public void setAlbumList(List<Album> albumList) {
        this.albumList = albumList;

    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.album_item, parent, false);
        return new ViewHolder(view, albumSize);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, final int position) {
        holder.imgAlbumThumb.setImageDrawable(null);
        Picasso
                .with(holder.imgAlbumThumb.getContext())
                .load(Uri.parse(albumList.get(position).thumbnailPath))
                .fit()
                .centerCrop()
                .into(holder.imgAlbumThumb);
        holder.view.setTag(albumList.get(position));
        Album a = (Album) holder.view.getTag();
        holder.txtAlbumName.setText(albumList.get(position).bucketName);
        holder.txtAlbumCount.setText(String.valueOf(a.counter));


        holder.view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Album a = (Album) v.getTag();
                Context context = holder.view.getContext();
                Intent i = new Intent(context, PickerActivity.class);
                i.putExtra(Define.BUNDLE_NAME.ALBUM.name(), a);
                i.putExtra(Define.BUNDLE_NAME.POSITION.name(), position);
                i.putParcelableArrayListExtra(Define.INTENT_PATH, pickedImagePath);
                i.putExtras(bundle);
                ((Activity) context).startActivityForResult(i, new Define().ENTER_ALBUM_REQUEST_CODE);
            }
        });
    }

    @Override
    public int getItemCount() {
        return albumList.size();
    }

    public ArrayList<Uri> getPickedImagePath() {
        return pickedImagePath;
    }

    public void setPickedImagePath(ArrayList<Uri> pickedImagePath) {
        this.pickedImagePath = pickedImagePath;
    }

    public List<Album> getAlbumList() {
        return albumList;
    }


    public static class ViewHolder extends RecyclerView.ViewHolder {
        private View view;
        private ImageView imgAlbumThumb;
        private TextView txtAlbumName;
        private TextView txtAlbumCount;

        public ViewHolder(View view, int albumSize) {
            super(view);
            this.view = view;
            imgAlbumThumb = (ImageView) view.findViewById(R.id.img_album_thumb);
            imgAlbumThumb.setLayoutParams(new LinearLayout.LayoutParams(albumSize, albumSize));

            txtAlbumName = (TextView) view.findViewById(R.id.txt_album_name);
            txtAlbumCount = (TextView) view.findViewById(R.id.txt_album_count);
        }
    }
}


