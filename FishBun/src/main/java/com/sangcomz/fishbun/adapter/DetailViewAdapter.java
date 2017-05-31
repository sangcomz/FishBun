package com.sangcomz.fishbun.adapter;

import android.net.Uri;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.sangcomz.fishbun.R;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;


public class DetailViewAdapter
        extends RecyclerView.Adapter<DetailViewAdapter.ViewHolder> {

    private Uri[] images;
    private ArrayList<Uri> pickedImages;
    private int circleColor;
    private int circleTextColor;
    private int maxCount;
    private String messageLimitReached;
    private boolean isAutomaticClose;


    public DetailViewAdapter(Uri[] images,
                             ArrayList<Uri> pickedImages,
                             int circleColor,
                             int circleTextColor,
                             int maxCount,
                             String messageLimitReached,
                             boolean isAutomaticClose) {
        this.images = images;
        this.pickedImages = pickedImages;
        this.circleColor = circleColor;
        this.circleTextColor = circleTextColor;
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
                .fit()
                .centerInside()
                .into(holder.imgDetailImage);
//        holder.btnDetailCount.unselect();
//        holder.btnDetailCount.setCircleColor(circleColor);
//        holder.btnDetailCount.setTextColor(circleTextColor);
    }

    @Override
    public int getItemCount() {
        return images.length;
    }


    public static class ViewHolder extends RecyclerView.ViewHolder {
        private View view;
        private ImageView imgDetailImage;
//        private RadioWithTextButton btnDetailCount;

        public ViewHolder(View view) {
            super(view);
            this.view = view;
            imgDetailImage = (ImageView) view.findViewById(R.id.img_detail_image);
//            btnDetailCount = (RadioWithTextButton) view.findViewById(R.id.btn_detail_count);
        }
    }
}


