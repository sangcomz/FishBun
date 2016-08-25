package com.sangcomz.fishbundemo;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.bumptech.glide.Glide;

import java.util.ArrayList;

/**
 * Created by sangc on 2015-11-06.
 */
public class MainAdapter extends RecyclerView.Adapter<MainAdapter.ViewHolder> {
    Context context;
    ArrayList<String> imagePaths;
    MainController mainController;

    MainAdapter(Context context, MainController mainController, ArrayList<String> imagePaths) {
        this.context = context;
        this.mainController = mainController;
        this.imagePaths = imagePaths;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, final int position) {
        final String imagePath = imagePaths.get(position);
        Glide.with(context).load(imagePath).centerCrop().into(holder.imageView);
        holder.imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mainController.setImgMain(imagePath);
            }
        });
    }

    public void changePath(ArrayList<String> imagePaths) {
        this.imagePaths = imagePaths;
        mainController.setImgMain(imagePaths.get(0));
        notifyDataSetChanged();
    }

    @Override
    public int getItemCount() {
        return imagePaths.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        ImageView imageView;

        public ViewHolder(View itemView) {
            super(itemView);
            imageView = (ImageView) itemView.findViewById(R.id.img_item);
        }
    }
}