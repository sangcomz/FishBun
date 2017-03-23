package com.sangcomz.fishbun.adapter;

import android.app.Activity;
import android.net.Uri;
import android.support.design.widget.Snackbar;
import android.support.v4.view.ViewCompat;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import com.sangcomz.fishbun.R;
import com.sangcomz.fishbun.define.Define;
import com.sangcomz.fishbun.ui.picker.PickerController;
import com.sangcomz.fishbun.util.SquareTextView;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.Collections;


public class PickerGridAdapter
        extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private static final int TYPE_HEADER = Integer.MIN_VALUE;

    private ArrayList<Uri> pickedImages = new ArrayList<>();
    private Uri[] images;
    private PickerController pickerController;
    private boolean isHeader = Define.IS_CAMERA;
    private OnPhotoActionListener actionListener;

    String saveDir;

    public class ViewHolderImage extends RecyclerView.ViewHolder {


        View item;
        ImageView imgThumbImage;
        SquareTextView txtThumbCount;

        public ViewHolderImage(View view) {
            super(view);
            item = view;
            imgThumbImage = (ImageView) view.findViewById(R.id.img_thumb_image);
            txtThumbCount = (SquareTextView) view.findViewById(R.id.txt_thumb_count);
        }
    }

    public class ViewHolderHeader extends RecyclerView.ViewHolder {


        RelativeLayout header;

        public ViewHolderHeader(View view) {
            super(view);
            header = (RelativeLayout) itemView.findViewById(R.id.rel_header_area);
        }
    }

    public PickerGridAdapter(Uri[] images,
                             ArrayList<Uri> pickedImages,
                             PickerController pickerController,
                             String saveDir) {
        this.images = images;
        this.pickerController = pickerController;
        this.pickedImages = pickedImages;
        this.saveDir = saveDir;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view;


        if (viewType == TYPE_HEADER) {
            view = LayoutInflater.from(parent.getContext()).inflate(R.layout.header_item, parent, false);
            return new ViewHolderHeader(view);
        }

        view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.thumb_item, parent, false);
        return new ViewHolderImage(view);
    }

    @Override
    public void onBindViewHolder(final RecyclerView.ViewHolder holder, final int position) {
        if (holder instanceof ViewHolderHeader) {
            final ViewHolderHeader vh = (ViewHolderHeader) holder;
            vh.header.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    pickerController.takePicture((Activity) vh.header.getContext(), saveDir);
                }
            });
        }

        if (holder instanceof ViewHolderImage) {
            final int imagePos;
            if (isHeader) imagePos = position - 1;
            else imagePos = position;

            final ViewHolderImage vh = (ViewHolderImage) holder;
            final Uri image = images[imagePos];
            vh.item.setTag(image);

            initState(pickedImages.indexOf(image), vh);
            if (image != null)
                Picasso
                        .with(vh.imgThumbImage.getContext())
                        .load(image)
                        .fit()
                        .centerCrop()
                        .into(vh.imgThumbImage);


            vh.item.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    onCheckStateChange(v, image);
                }
            });
        }

    }

    private void initState(int selectedIndex, ViewHolderImage vh) {
        if (selectedIndex != -1) {
            vh.txtThumbCount.setVisibility(View.VISIBLE);
            vh.txtThumbCount.setText(String.valueOf(selectedIndex + 1));
            animScale(vh.imgThumbImage, true, false);
        } else {
            vh.txtThumbCount.setVisibility(View.GONE);
            animScale(vh.imgThumbImage, false, false);
        }
    }

    private void onCheckStateChange(View v, Uri image) {
        boolean isContained = pickedImages.contains(image);
        if (Define.ALBUM_PICKER_COUNT == pickedImages.size()
                && !isContained) {
            Snackbar.make(v, Define.MESSAGE_LIMIT_REACHED, Snackbar.LENGTH_SHORT).show();
            return;
        }
        SquareTextView txtThumbCount = (SquareTextView) v.findViewById(R.id.txt_thumb_count);
        ImageView imgThumbImage = (ImageView) v.findViewById(R.id.img_thumb_image);
        if (isContained) {
            pickedImages.remove(image);
            txtThumbCount.setVisibility(View.GONE);
            animScale(imgThumbImage, false, true);
        } else {
            animScale(imgThumbImage, true, true);
            txtThumbCount.setVisibility(View.VISIBLE);
            pickedImages.add(image);
            if (Define.IS_AUTOMATIC_CLOSE
                    && Define.ALBUM_PICKER_COUNT == pickedImages.size()) {
                pickerController.finishActivity(pickedImages);
            }

            if (Define.ALBUM_PICKER_COUNT == 1)
                txtThumbCount.setText("");
            else
                txtThumbCount.setText(String.valueOf(pickedImages.size()));
        }
        pickerController.setToolbarTitle(pickedImages.size());
    }

    private void animScale(View view,
                           boolean isSelected,
                           boolean isAnimation) {
        int duration = 200;
        if (!isAnimation) duration = 0;
        if (isSelected)
            ViewCompat.animate(view)
                    .setDuration(duration)
                    .scaleX(0.7f)
                    .scaleY(0.7f)
                    .start();
        else
            ViewCompat.animate(view)
                    .setDuration(duration)
                    .scaleX(1f)
                    .scaleY(1f)
                    .withEndAction(new Runnable() {
                        @Override
                        public void run() {
                            actionListener.onDeselect();
                        }
                    })
                    .start();

    }

    @Override
    public int getItemCount() {
        if (isHeader)
            return images.length + 1;

        return images.length;
    }

    @Override
    public int getItemViewType(int position) {
        if (position == 0 && isHeader) {
            return TYPE_HEADER;
        }
        return super.getItemViewType(position);
    }

    public Uri[] getImages() {
        return images;
    }


    public void addImage(Uri path) {
        ArrayList<Uri> al = new ArrayList<>();
        Collections.addAll(al, images);
        al.add(0, path);
        images = al.toArray(new Uri[al.size()]);

        notifyDataSetChanged();

        pickerController.setAddImagePath(path);
    }

    public void setActionListener(OnPhotoActionListener actionListener) {
        this.actionListener = actionListener;
    }

    public interface OnPhotoActionListener {
        void onDeselect();
    }

    public int getPickedImageIndexOf(Uri uri) {
        return pickedImages.indexOf(uri);
    }
}
