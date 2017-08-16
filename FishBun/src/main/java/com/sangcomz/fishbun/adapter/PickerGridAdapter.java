package com.sangcomz.fishbun.adapter;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.ViewCompat;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import com.sangcomz.fishbun.R;
import com.sangcomz.fishbun.define.Define;
import com.sangcomz.fishbun.ui.detail.DetailActivity;
import com.sangcomz.fishbun.ui.picker.PickerActivity;
import com.sangcomz.fishbun.ui.picker.PickerController;
import com.sangcomz.fishbun.util.RadioWithTextButton;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.Collections;


public class PickerGridAdapter
        extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private static final int TYPE_HEADER = Integer.MIN_VALUE;

    private Uri[] images;
    private PickerController pickerController;
    private boolean isHeader;
    private OnPhotoActionListener actionListener;
    private int circleColor;
    private int circleTextColor;
    private int maxCount;
    private String messageLimitReached;
    private boolean isAutomaticClose;
    private boolean isUseDetailView;

    private String saveDir;


    public PickerGridAdapter(Uri[] images,
                             PickerController pickerController,
                             String saveDir,
                             Boolean isCamera,
                             int circleColor,
                             int circleTextColor,
                             int maxCount,
                             String messageLimitReached,
                             boolean isAutomaticClose,
                             boolean isUseDetailView) {
        this.images = images;
        this.pickerController = pickerController;
        this.saveDir = saveDir;
        this.isHeader = isCamera;
        this.circleColor = circleColor;
        this.circleTextColor = circleTextColor;
        this.maxCount = maxCount;
        this.messageLimitReached = messageLimitReached;
        this.isAutomaticClose = isAutomaticClose;
        this.isUseDetailView = isUseDetailView;
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
            ArrayList<Uri> pickedImages = pickerController.getPickedImages();
            final int imagePos;
            if (isHeader) imagePos = position - 1;
            else imagePos = position;

            final ViewHolderImage vh = (ViewHolderImage) holder;
            final Uri image = images[imagePos];
            final Context context = vh.item.getContext();
            vh.item.setTag(image);
            vh.btnThumbCount.unselect();
            vh.btnThumbCount.setCircleColor(circleColor);
            vh.btnThumbCount.setTextColor(circleTextColor);

            initState(pickedImages.indexOf(image), vh);
            if (image != null)
                Picasso
                        .with(vh.imgThumbImage.getContext())
                        .load(image)
                        .fit()
                        .centerCrop()
                        .into(vh.imgThumbImage);


            vh.btnThumbCount.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    onCheckStateChange(vh.item, image);
                }
            });

            vh.imgThumbImage.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (isUseDetailView) {
                        if (context instanceof PickerActivity) {
                            PickerActivity activity = (PickerActivity) context;
                            Intent i = new Intent(activity, DetailActivity.class);
                            Bundle bundle = new Bundle();
                            bundle.putParcelableArray(Define.BUNDLE_NAME.IMAGES.name(), images);
                            i.putExtras(activity.getIntent().getExtras());
                            i.putExtra(Define.BUNDLE_NAME.BUNDLE.name(), bundle);
                            i.putParcelableArrayListExtra(Define.BUNDLE_NAME.PICKED_IMAGES.name(), pickerController.getPickedImages());
                            i.putExtra(Define.BUNDLE_NAME.POSITION.name(), imagePos);
                            activity.startActivityForResult(i, new Define().ENTER_DETAIL_REQUEST_CODE);
                        }
                    } else
                        onCheckStateChange(vh.item, image);

                }
            });
        }
    }

    private void initState(int selectedIndex, ViewHolderImage vh) {
        if (selectedIndex != -1) {
            animScale(vh.imgThumbImage, true, false);
            updateRadioButton(vh.btnThumbCount, String.valueOf(selectedIndex + 1));
        } else {
            animScale(vh.imgThumbImage, false, false);
        }
    }

    private void onCheckStateChange(View v, Uri image) {
        ArrayList<Uri> pickedImages = pickerController.getPickedImages();
        boolean isContained = pickedImages.contains(image);
        if (maxCount == pickedImages.size()
                && !isContained) {
            Snackbar.make(v, messageLimitReached, Snackbar.LENGTH_SHORT).show();
            return;
        }
        ImageView imgThumbImage = (ImageView) v.findViewById(R.id.img_thumb_image);
        RadioWithTextButton btnThumbCount = (RadioWithTextButton) v.findViewById(R.id.btn_thumb_count);
        if (isContained) {
            pickedImages.remove(image);
            btnThumbCount.unselect();
            animScale(imgThumbImage, false, true);
        } else {
            animScale(imgThumbImage, true, true);
            pickedImages.add(image);
            if (isAutomaticClose
                    && maxCount == pickedImages.size()) {
                pickerController.finishActivity();
            }
            updateRadioButton(btnThumbCount, String.valueOf(pickedImages.size()));
        }
        pickerController.setPickedImages(pickedImages);
        pickerController.setToolbarTitle(pickedImages.size());
    }

    public void updateRadioButton(RadioWithTextButton v, String text) {
        if (maxCount == 1)
            v.setDrawable(ContextCompat.getDrawable(v.getContext(), R.drawable.ic_done_white_24dp));
        else
            v.setText(text);
    }

    public void updateRadioButton(ImageView imageView, RadioWithTextButton v, String text, boolean isSelected) {
        if (isSelected) {
            animScale(imageView, isSelected, false);
            if (maxCount == 1)
                v.setDrawable(ContextCompat.getDrawable(v.getContext(), R.drawable.ic_done_white_24dp));
            else
                v.setText(text);
        } else {
            v.unselect();
        }

    }


    private void animScale(View view,
                           final boolean isSelected,
                           final boolean isAnimation) {
        int duration = 200;
        if (!isAnimation) duration = 0;
        float toScale;
        if (isSelected)
            toScale = .8f;
        else
            toScale = 1.0f;

        ViewCompat.animate(view)
                .setDuration(duration)
                .withStartAction(new Runnable() {
                    @Override
                    public void run() {

                    }
                })
                .scaleX(toScale)
                .scaleY(toScale)
                .withEndAction(new Runnable() {
                    @Override
                    public void run() {
                        if (isAnimation && !isSelected) actionListener.onDeselect();
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

    public class ViewHolderImage extends RecyclerView.ViewHolder {


        View item;
        ImageView imgThumbImage;
        RadioWithTextButton btnThumbCount;

        public ViewHolderImage(View view) {
            super(view);
            item = view;
            imgThumbImage = (ImageView) view.findViewById(R.id.img_thumb_image);
            btnThumbCount = (RadioWithTextButton) view.findViewById(R.id.btn_thumb_count);
        }
    }

    public class ViewHolderHeader extends RecyclerView.ViewHolder {


        RelativeLayout header;

        public ViewHolderHeader(View view) {
            super(view);
            header = (RelativeLayout) itemView.findViewById(R.id.rel_header_area);
        }
    }
}
