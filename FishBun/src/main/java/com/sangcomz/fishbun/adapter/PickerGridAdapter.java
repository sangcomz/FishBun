package com.sangcomz.fishbun.adapter;

import android.app.Activity;
import android.net.Uri;
import android.os.Handler;
import android.support.design.widget.Snackbar;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import com.sangcomz.fishbun.R;
import com.sangcomz.fishbun.bean.Image;
import com.sangcomz.fishbun.bean.PickedImage;
import com.sangcomz.fishbun.define.Define;
import com.sangcomz.fishbun.ui.picker.PickerController;
import com.sangcomz.fishbun.util.SquareTextView;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.Collections;


public class PickerGridAdapter
        extends RecyclerView.Adapter<PickerGridAdapter.ViewHolder> {
    private static final int TYPE_HEADER = Integer.MIN_VALUE;

    private ArrayList<PickedImage> pickedImages = new ArrayList<>();
    private Image[] images;
    private PickerController pickerController;
    private boolean isHeader = Define.IS_CAMERA;

    String saveDir;

    public class ViewHolderImage extends ViewHolder {


        ImageView imgThumbImage;
        SquareTextView txtThumbCount;

        public ViewHolderImage(View view) {
            super(view);
            imgThumbImage = (ImageView) view.findViewById(R.id.img_thumb_image);
            txtThumbCount = (SquareTextView) view.findViewById(R.id.txt_thumb_count);
        }
    }

    public class ViewHolderHeader extends ViewHolder {


        RelativeLayout header;

        public ViewHolderHeader(View view) {
            super(view);
            header = (RelativeLayout) itemView.findViewById(R.id.rel_header_area);
        }
    }

    public PickerGridAdapter(Image[] images,
                             ArrayList<PickedImage> pickedImages, PickerController pickerController,
                             String saveDir) {
        this.images = images;
        this.pickerController = pickerController;
        this.pickedImages = pickedImages;
        this.saveDir = saveDir;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
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
    public void onBindViewHolder(final ViewHolder holder, final int position) {
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

            if (isHeader)
                imagePos = position - 1;
            else
                imagePos = position;

            final ViewHolderImage vh = (ViewHolderImage) holder;

            final Image image = images[imagePos];
            final Uri imgUri = image.getImgPath();

            if (!image.isInit()) {
                image.setIsInit(true);
                for (int i = 0; i < pickedImages.size(); i++) {
                    if (imgUri.equals(pickedImages.get(i).getImgPath())) {
                        image.setImgOrder(i + 1);
                        pickedImages.get(i).setImgPosition(imagePos);
                        break;
                    }
                }
            }


            if (image.getImgOrder() != -1) {
                vh.txtThumbCount.setVisibility(View.VISIBLE);
                if (Define.ALBUM_PICKER_COUNT == 1)
                    vh.txtThumbCount.setText("");
                else
                    vh.txtThumbCount.setText(String.valueOf(image.getImgOrder()));
            } else
                vh.txtThumbCount.setVisibility(View.GONE);

            if (imgUri != null && !imgUri.equals("")) {
                Picasso
                        .with(vh.imgThumbImage.getContext())
                        .load(imgUri)
                        .fit()
                        .centerCrop()
                        .into(vh.imgThumbImage);
            }


            vh.imgThumbImage.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (vh.txtThumbCount.getVisibility() == View.GONE &&
                            Define.ALBUM_PICKER_COUNT > pickedImages.size()) {

                        vh.txtThumbCount.setVisibility(View.VISIBLE);
                        pickedImages.add(new PickedImage(pickedImages.size() + 1, imgUri, imagePos));

                        pickerController.setToolbarTitle(pickedImages.size());
                        if (Define.IS_AUTOMATIC_CLOSE
                                && Define.ALBUM_PICKER_COUNT == pickedImages.size()) {
                            pickerController.finishActivity(pickedImages);
                        }

                        if (Define.ALBUM_PICKER_COUNT == 1)
                            vh.txtThumbCount.setText("");
                        else
                            vh.txtThumbCount.setText(String.valueOf(pickedImages.size()));

                        image.setImgOrder(pickedImages.size());

                    } else if (vh.txtThumbCount.getVisibility() == View.VISIBLE) {
                        pickerController.setRecyclerViewClickable(false);
                        pickedImages.remove(image.getImgOrder() - 1);
                        if (Define.ALBUM_PICKER_COUNT != 1)
                            setOrder(Integer.valueOf(vh.txtThumbCount.getText().toString()) - 1);
                        else
                            setOrder(0);
                        image.setImgOrder(-1);
                        vh.txtThumbCount.setVisibility(View.GONE);
                        pickerController.setToolbarTitle(pickedImages.size());
                    } else {
                        Snackbar.make(v, Define.MESSAGE_LIMIT_REACHED, Snackbar.LENGTH_SHORT).show();
                    }
                }
            });
        }

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


    private void setOrder(int removePosition) {
        for (int i = removePosition; i < pickedImages.size(); i++) {
            if (pickedImages.get(i).getImgPosition() != -1) {
                images[pickedImages.get(i).getImgPosition()]
                        .setImgOrder(i + 1);
                if (isHeader)
                    notifyDataSetChanged();
                else
                    notifyDataSetChanged();
            }
        }
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                pickerController.setRecyclerViewClickable(true);
            }
        }, 500);

    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        // Define elements of a row here
        public ViewHolder(View itemView) {
            super(itemView);
            // Find view by ID and initialize here
        }

        public void bindView(int position) {
            // bindView() method to implement actions
        }
    }


    public Image[] getImages() {
        return images;
    }


    public void addImage(Uri path) {
        ArrayList<Image> al = new ArrayList<>();
        Collections.addAll(al, images);
        al.add(0, new Image(-1, path));

        images = al.toArray(new Image[al.size()]);

        for (int i = 0; i < pickedImages.size(); i++)
            pickedImages.get(i).setImgPosition(pickedImages.get(i).getImgPosition() + 1);

        notifyDataSetChanged();

        pickerController.setAddImagePath(path);
    }

}
