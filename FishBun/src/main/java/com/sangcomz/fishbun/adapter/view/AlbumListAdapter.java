package com.sangcomz.fishbun.adapter.view;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.sangcomz.fishbun.Fishton;
import com.sangcomz.fishbun.R;
import com.sangcomz.fishbun.bean.Album;
import com.sangcomz.fishbun.define.Define;
import com.sangcomz.fishbun.ui.picker.PickerActivity;

import java.util.List;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;


public class AlbumListAdapter
        extends RecyclerView.Adapter<AlbumListAdapter.ViewHolder> {

    private Fishton fishton;
    private List<Album> albumList;


    public AlbumListAdapter() {
        fishton = Fishton.getInstance();
    }

    public void setAlbumList(List<Album> albumList) {
        this.albumList = albumList;

    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.album_item, parent, false);
        return new ViewHolder(view, fishton.albumThumbnailSize);
    }

    @Override
    public void onBindViewHolder(@NonNull final ViewHolder holder, final int position) {
        holder.imgAlbumThumb.setImageDrawable(null);

        Uri loadUrl = Uri.parse(albumList.get(position).thumbnailPath);
        int orientation = albumList.get(position).orientation;

        if (holder.imgAlbumThumb != null && loadUrl != null)
            Fishton.getInstance().imageAdapter
                    .loadImage(holder.imgAlbumThumb, loadUrl, orientation);

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
                ((Activity) context).startActivityForResult(i, new Define().ENTER_ALBUM_REQUEST_CODE);
            }
        });
    }

    @Override
    public int getItemCount() {
        return albumList.size();
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
            imgAlbumThumb = view.findViewById(R.id.img_album_thumb);
            imgAlbumThumb.setLayoutParams(new LinearLayout.LayoutParams(albumSize, albumSize));

            txtAlbumName = view.findViewById(R.id.txt_album_name);
            txtAlbumCount = view.findViewById(R.id.txt_album_count);
        }
    }
}


