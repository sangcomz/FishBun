package com.sangcomz.fishbun.adapter;

import android.net.Uri;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.sangcomz.fishbun.R;
import com.sangcomz.fishbun.ui.detail.DetailController;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;


public class DetailViewAdapter
        extends RecyclerView.Adapter<DetailViewAdapter.ViewHolder> {

    private Uri[] images;
    private ArrayList<Uri> pickedImages;
    private DetailController controller;
    private int maxCount;
    private String messageLimitReached;
    private boolean isAutomaticClose;


    public DetailViewAdapter(Uri[] images,
                             DetailController controller,
                             ArrayList<Uri> pickedImages,
                             int maxCount,
                             String messageLimitReached,
                             boolean isAutomaticClose) {
        this.images = images;
        this.controller = controller;
        this.pickedImages = pickedImages;
        this.maxCount = maxCount;
        this.messageLimitReached = messageLimitReached;
        this.isAutomaticClose = isAutomaticClose;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.detail_item, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, final int position) {
        holder.imgDetailImage.setImageDrawable(null);
        Picasso
                .with(holder.imgDetailImage.getContext())
                .load(images[position])
                .noPlaceholder()
                .fit()
                .centerInside()
                .into(holder.imgDetailImage);
    }

    @Override
    public int getItemCount() {
        return images.length;
    }


    public static class ViewHolder extends RecyclerView.ViewHolder {
        private View view;
        private ImageView imgDetailImage;

        public ViewHolder(View view) {
            super(view);
            this.view = view;
            imgDetailImage = (ImageView) view.findViewById(R.id.img_detail_image);
        }
    }
}


