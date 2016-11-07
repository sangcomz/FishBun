package com.sangcomz.fishbun.adapter;

import android.app.Activity;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.sangcomz.fishbun.R;
import com.sangcomz.fishbun.bean.Album;
import com.sangcomz.fishbun.define.Define;
import com.sangcomz.fishbun.ui.picker.PickerActivity;
import com.squareup.picasso.Picasso;

import java.io.File;
import java.util.ArrayList;
import java.util.List;


public class AlbumListAdapter
        extends RecyclerView.Adapter<AlbumListAdapter.ViewHolder> {

    private List<Album> albumList;
    private List<String> thumbList = new ArrayList<>();
    private ArrayList<String> pickedImagePath;

    public static class ViewHolder extends RecyclerView.ViewHolder {

        private ImageView imgAlbum;
        private TextView txtAlbum;
        private TextView txtAlbumCount;
        private RelativeLayout areaAlbum;


        public ViewHolder(View view) {
            super(view);
            imgAlbum = (ImageView) view.findViewById(R.id.img_album);
            imgAlbum.setLayoutParams(new RelativeLayout.LayoutParams(Define.ALBUM_THUMBNAIL_SIZE, Define.ALBUM_THUMBNAIL_SIZE));

            txtAlbum = (TextView) view.findViewById(R.id.txt_album);
            txtAlbumCount = (TextView) view.findViewById(R.id.txt_album_count);
            areaAlbum = (RelativeLayout) view.findViewById(R.id.area_album);
        }
    }

    public AlbumListAdapter(List<Album> albumList, ArrayList<String> pickedImagePath) {
        this.albumList = albumList;
        this.pickedImagePath = pickedImagePath;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.album_item, parent, false);
        return new ViewHolder(view);
    }

    public void setThumbList(List<String> thumbList) {
        this.thumbList = thumbList;
        notifyDataSetChanged();
    }


    @Override
    public void onBindViewHolder(final ViewHolder holder, final int position) {
        if (thumbList != null && thumbList.size() > position) {
            Picasso
                    .with(holder.imgAlbum.getContext())
                    .load(new File(thumbList.get(position)))
                    .fit()
                    .centerCrop()
                    .into(holder.imgAlbum);
        }
        holder.areaAlbum.setTag(albumList.get(position));
        Album a = (Album) holder.areaAlbum.getTag();
        holder.txtAlbum.setText(albumList.get(position).bucketName);
        holder.txtAlbumCount.setText(String.valueOf(a.counter));


        holder.areaAlbum.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Album a = (Album) v.getTag();
                Intent i = new Intent(holder.areaAlbum.getContext(), PickerActivity.class);
                i.putExtra("album", a);
                i.putExtra("album_title", albumList.get(position).bucketName);
                i.putExtra("position", position);
                i.putStringArrayListExtra(Define.INTENT_PATH, pickedImagePath);
                ((Activity) holder.areaAlbum.getContext()).startActivityForResult(i, Define.ENTER_ALBUM_REQUEST_CODE);
            }
        });
    }

    @Override
    public int getItemCount() {
        return albumList.size();
    }

    public ArrayList<String> getPickedImagePath() {
        return pickedImagePath;
    }

    public void setPickedImagePath(ArrayList<String> pickedImagePath) {
        this.pickedImagePath = pickedImagePath;
    }

    public List<Album> getAlbumList() {
        return albumList;
    }

    public List<String> getThumbList() {
        return thumbList;
    }
}


