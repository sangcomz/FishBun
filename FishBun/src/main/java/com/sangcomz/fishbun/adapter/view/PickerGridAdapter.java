package com.sangcomz.fishbun.adapter.view;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import com.google.android.material.snackbar.Snackbar;
import com.sangcomz.fishbun.Fishton;
import com.sangcomz.fishbun.R;
import com.sangcomz.fishbun.bean.PickerImage;
import com.sangcomz.fishbun.define.Define;
import com.sangcomz.fishbun.ui.detail.DetailActivity;
import com.sangcomz.fishbun.ui.picker.PickerActivity;
import com.sangcomz.fishbun.ui.picker.PickerController;
import com.sangcomz.fishbun.util.RadioWithTextButton;

import java.util.ArrayList;
import java.util.Collections;

import androidx.core.content.ContextCompat;
import androidx.core.view.ViewCompat;
import androidx.recyclerview.widget.RecyclerView;


public class PickerGridAdapter
        extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private static final int TYPE_HEADER = Integer.MIN_VALUE;

    private Fishton fishton;
    private PickerController pickerController;
    private OnPhotoActionListener actionListener;


    private String saveDir;


    public PickerGridAdapter(PickerController pickerController,
                             String saveDir) {
        this.pickerController = pickerController;
        this.saveDir = saveDir;
        this.fishton = Fishton.getInstance();
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
            if (fishton.isCamera) imagePos = position - 1;
            else imagePos = position;

            final ViewHolderImage vh = (ViewHolderImage) holder;
            final Uri image = fishton.pickerImages[imagePos].path;
            final int orientation = fishton.pickerImages[imagePos].orientation;
            final Context context = vh.item.getContext();
            vh.item.setTag(image);
            vh.btnThumbCount.unselect();
            vh.btnThumbCount.setCircleColor(fishton.colorActionBar);
            vh.btnThumbCount.setTextColor(fishton.colorActionBarTitle);
            vh.btnThumbCount.setStrokeColor(fishton.colorSelectCircleStroke);

            initState(fishton.selectedImages.indexOf(image), vh);
            if (image != null
                    && vh.imgThumbImage != null)
                Fishton.getInstance().imageAdapter
                        .loadImage(vh.imgThumbImage, image, orientation);


            vh.btnThumbCount.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    onCheckStateChange(vh.item, image);
                }
            });

            vh.imgThumbImage.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (fishton.isUseDetailView) {
                        if (context instanceof PickerActivity) {
                            PickerActivity activity = (PickerActivity) context;
                            Intent i = new Intent(activity, DetailActivity.class);
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
        ArrayList<Uri> pickedImages = fishton.selectedImages;
        boolean isContained = pickedImages.contains(image);
        if (fishton.maxCount == pickedImages.size()
                && !isContained) {
            Snackbar.make(v, fishton.messageLimitReached, Snackbar.LENGTH_SHORT).show();
            return;
        }
        ImageView imgThumbImage = v.findViewById(R.id.img_thumb_image);
        RadioWithTextButton btnThumbCount = v.findViewById(R.id.btn_thumb_count);
        if (isContained) {
            pickedImages.remove(image);
            btnThumbCount.unselect();
            animScale(imgThumbImage, false, true);
        } else {
            animScale(imgThumbImage, true, true);
            pickedImages.add(image);
            if (fishton.isAutomaticClose
                    && fishton.maxCount == pickedImages.size()) {
                pickerController.finishActivity();
            }
            updateRadioButton(btnThumbCount, String.valueOf(pickedImages.size()));
        }
        pickerController.setToolbarTitle(pickedImages.size());
    }

    public void updateRadioButton(RadioWithTextButton v, String text) {
        if (fishton.maxCount == 1)
            v.setDrawable(ContextCompat.getDrawable(v.getContext(), R.drawable.ic_done_white_24dp));
        else
            v.setText(text);
    }

    public void updateRadioButton(ImageView imageView, RadioWithTextButton v, String text, boolean isSelected) {
        if (isSelected) {
            animScale(imageView, isSelected, false);
            if (fishton.maxCount == 1)
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
        int count;
        if (fishton.pickerImages == null) count = 0;
        else count = fishton.pickerImages.length;

        if (fishton.isCamera)
            return count + 1;

        if (fishton.pickerImages == null) return 0;
        else return count;
    }

    @Override
    public int getItemViewType(int position) {
        if (position == 0 && fishton.isCamera) {
            return TYPE_HEADER;
        }
        return super.getItemViewType(position);
    }


    public void addImage(PickerImage pickerImage) {
        ArrayList<PickerImage> al = new ArrayList<>();
        Collections.addAll(al, fishton.pickerImages);
        al.add(0, pickerImage);
        fishton.pickerImages = al.toArray(new PickerImage[al.size()]);

        notifyDataSetChanged();

        pickerController.setAddPickerImage(pickerImage);
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
            imgThumbImage = view.findViewById(R.id.img_thumb_image);
            btnThumbCount = view.findViewById(R.id.btn_thumb_count);
        }
    }

    public class ViewHolderHeader extends RecyclerView.ViewHolder {


        RelativeLayout header;

        public ViewHolderHeader(View view) {
            super(view);
            header = itemView.findViewById(R.id.rel_header_area);
        }
    }
}
