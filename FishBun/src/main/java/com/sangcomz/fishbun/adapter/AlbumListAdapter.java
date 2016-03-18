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

import com.bumptech.glide.Glide;
import com.sangcomz.fishbun.R;
import com.sangcomz.fishbun.bean.Album;
import com.sangcomz.fishbun.define.Define;
import com.sangcomz.fishbun.ui.album.AlbumActivity;
import com.sangcomz.fishbun.ui.picker.PickerActivity;

import java.util.ArrayList;
import java.util.List;


public class AlbumListAdapter
        extends RecyclerView.Adapter<AlbumListAdapter.ViewHolder> {

    private List<Album> albumlist;
    private List<String> thumbList = new ArrayList<String>();
    private String thumPath;
    private ArrayList<String> path;

    public static class ViewHolder extends RecyclerView.ViewHolder {

        private ImageView imgAlbum;
        private TextView txtAlbum;
        private TextView txtAlbumCount;
        private RelativeLayout areaAlbum;


        public ViewHolder(View view) {
            super(view);
            imgAlbum = (ImageView) view.findViewById(R.id.img_album);
            imgAlbum.setLayoutParams(new RelativeLayout.LayoutParams(Define.ALBUM_THUMNAIL_SIZE, Define.ALBUM_THUMNAIL_SIZE));

            txtAlbum = (TextView) view.findViewById(R.id.txt_album);
            txtAlbumCount = (TextView) view.findViewById(R.id.txt_album_count);
            areaAlbum = (RelativeLayout) view.findViewById(R.id.area_album);
        }
    }

    public AlbumListAdapter(List<Album> albumlist, ArrayList<String> path) {
        this.albumlist = albumlist;
        this.path = path;
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

        if (thumbList != null && thumbList.size() > position)
            thumPath = thumbList.get(position);


        if (thumbList != null) {
            if (thumbList.size() > position) {
                Glide
                        .with(holder.imgAlbum.getContext())
                        .load(thumPath)
                        .asBitmap()
                        .override(Define.ALBUM_THUMNAIL_SIZE, Define.ALBUM_THUMNAIL_SIZE)
                        .placeholder(R.mipmap.loading_img)
                        .into(holder.imgAlbum);
            } else {
                Glide.with(holder.imgAlbum.getContext()).load(R.mipmap.loading_img).into(holder.imgAlbum);
            }
        }
        holder.areaAlbum.setTag(albumlist.get(position));
        Album a = (Album) holder.areaAlbum.getTag();
        holder.txtAlbum.setText(albumlist.get(position).bucketname);
        holder.txtAlbumCount.setText(String.valueOf(a.counter));


        holder.areaAlbum.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Album a = (Album) v.getTag();
                Intent i = new Intent(holder.areaAlbum.getContext(), PickerActivity.class);
                i.putExtra("album", a);
                i.putExtra("album_title", albumlist.get(position).bucketname);
                i.putStringArrayListExtra(Define.INTENT_PATH, path);
                if (AlbumActivity.changeAlbumPublishSubject.hasObservers())
                    AlbumActivity.changeAlbumPublishSubject.onNext("POSITION|" + String.valueOf(position));

                ((Activity) holder.areaAlbum.getContext()).startActivityForResult(i, Define.ENTER_ALBUM_REQUEST_CODE);
            }
        });
    }

    @Override
    public int getItemCount() {
        return albumlist.size();
    }

    public void setPath(ArrayList<String> path) {
        this.path = path;
    }

    public ArrayList<String> getPath() {
        return path;
    }
}


