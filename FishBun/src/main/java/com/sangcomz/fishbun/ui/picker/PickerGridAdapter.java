package com.sangcomz.fishbun.ui.picker;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import androidx.core.content.ContextCompat;
import androidx.core.view.ViewCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.snackbar.Snackbar;
import com.sangcomz.fishbun.Fishton;
import com.sangcomz.fishbun.R;
import com.sangcomz.fishbun.ui.detail.DetailActivity;
import com.sangcomz.fishbun.util.RadioWithTextButton;

import java.util.ArrayList;


public class PickerGridAdapter
        extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private static final int TYPE_HEADER = Integer.MIN_VALUE;

    private Fishton fishton;
    private PickerActivity pickerActivity;
    private OnPhotoActionListener actionListener;


    private String saveDir;


    public PickerGridAdapter(PickerActivity activity,
                             String saveDir) {
        this.pickerActivity = activity;
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
                    if (pickerActivity.checkCameraPermission()) {
                        pickerActivity.takePicture(saveDir);
                    }
                }
            });
        }

        if (holder instanceof ViewHolderImage) {
            final int imagePos;
            if (fishton.isCamera()) imagePos = position - 1;
            else imagePos = position;

            final ViewHolderImage vh = (ViewHolderImage) holder;
            final Uri image = fishton.getPickerImages().get(imagePos);
            final Context context = vh.item.getContext();
            vh.item.setTag(image);
            vh.btnThumbCount.unselect();
            vh.btnThumbCount.setCircleColor(fishton.getColorActionBar());
            vh.btnThumbCount.setTextColor(fishton.getColorActionBarTitle());
            vh.btnThumbCount.setStrokeColor(fishton.getColorSelectCircleStroke());

            initState(fishton.getSelectedImages().indexOf(image), vh);
            if (image != null
                    && vh.imgThumbImage != null)
                Fishton.getInstance().getImageAdapter()
                        .loadImage(vh.imgThumbImage, image);


            vh.btnThumbCount.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    onCheckStateChange(vh.item, image);
                }
            });

            vh.imgThumbImage.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (fishton.isUseDetailView()) {
                        if (context instanceof PickerActivity) {
                            PickerActivity activity = (PickerActivity) context;
                            Intent i = new Intent(activity, DetailActivity.class);
                            i.putExtra("position", imagePos);
                            activity.startActivityForResult(i, 130);
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
        ArrayList<Uri> pickedImages = fishton.getSelectedImages();
        boolean isContained = pickedImages.contains(image);
        if (fishton.getMaxCount() == pickedImages.size()
                && !isContained) {
            Snackbar.make(v, fishton.getMessageLimitReached(), Snackbar.LENGTH_SHORT).show();
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
            if (fishton.isAutomaticClose()
                    && fishton.getMaxCount() == pickedImages.size()) {
                pickerActivity.finishActivity();
            }
            updateRadioButton(btnThumbCount, String.valueOf(pickedImages.size()));
        }
        pickerActivity.setToolbarTitle(pickedImages.size());
    }

    public void updateRadioButton(RadioWithTextButton v, String text) {
        if (fishton.getMaxCount() == 1)
            v.setDrawable(ContextCompat.getDrawable(v.getContext(), R.drawable.ic_done_white_24dp));
        else
            v.setText(text);
    }

    public void updateRadioButton(ImageView imageView, RadioWithTextButton v, String text, boolean isSelected) {
        if (isSelected) {
            animScale(imageView, isSelected, false);
            if (fishton.getMaxCount() == 1)
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
        int count = fishton.getPickerImages().size();

        if (fishton.isCamera()) return count + 1;

        return count;
    }

    @Override
    public int getItemViewType(int position) {
        if (position == 0 && fishton.isCamera()) {
            return TYPE_HEADER;
        }
        return super.getItemViewType(position);
    }


    public void addImage(Uri path) {
        ArrayList<Uri> al = new ArrayList<>(fishton.getPickerImages());
        al.add(0, path);
        fishton.setPickerImages(al);

        notifyDataSetChanged();

        pickerActivity.setAddImagePath(path);
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
