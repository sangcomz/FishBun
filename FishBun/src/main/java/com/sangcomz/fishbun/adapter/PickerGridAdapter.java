package com.sangcomz.fishbun.adapter;

import android.os.Handler;
import android.support.design.widget.Snackbar;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import com.bumptech.glide.Glide;
import com.sangcomz.fishbun.R;
import com.sangcomz.fishbun.bean.ImageBean;
import com.sangcomz.fishbun.bean.PickedImageBean;
import com.sangcomz.fishbun.define.Define;
import com.sangcomz.fishbun.ui.album.AlbumActivity;
import com.sangcomz.fishbun.ui.picker.PickerController;
import com.sangcomz.fishbun.util.SquareTextView;

import java.util.ArrayList;
import java.util.Collections;


public class PickerGridAdapter
        extends RecyclerView.Adapter<PickerGridAdapter.ViewHolder> {
    private static final int TYPE_HEADER = Integer.MIN_VALUE;

    private ArrayList<PickedImageBean> pickedImageBeans = new ArrayList<>();
    private ImageBean[] imageBeans;
    private PickerController pickerController;
    private boolean isHeader = Define.IS_CAMERA;

    String saveDir;

    public class ViewHolderImage extends ViewHolder {


        ImageView imgPhoto;
        SquareTextView txtPickCount;

        public ViewHolderImage(View view) {
            super(view);
            imgPhoto = (ImageView) view.findViewById(R.id.img_thum);
            txtPickCount = (SquareTextView) view.findViewById(R.id.txt_pick_count);
        }
    }

    public class ViewHolderHeader extends ViewHolder {


        RelativeLayout header;

        public ViewHolderHeader(View view) {
            super(view);
            header = (RelativeLayout) itemView.findViewById(R.id.area_header);
        }
    }

    public PickerGridAdapter(ImageBean[] imageBeans,
                             ArrayList<PickedImageBean> pickedImageBeans, PickerController pickerController,
                             String saveDir) {
        this.imageBeans = imageBeans;
        this.pickerController = pickerController;
        this.pickedImageBeans = pickedImageBeans;
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
                .inflate(R.layout.thum_item, parent, false);
        return new ViewHolderImage(view);
    }


    @Override
    public void onBindViewHolder(final ViewHolder holder, final int position) {

        if (holder instanceof ViewHolderHeader) {
            final ViewHolderHeader vh = (ViewHolderHeader) holder;
            vh.header.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    pickerController.takePicture(saveDir);
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

            final ImageBean imageBean = imageBeans[imagePos];
            final String imgPath = imageBean.getImgPath();

            if (!imageBean.isInit()) {
                imageBean.setIsInit(true);
                for (int i = 0; i < pickedImageBeans.size(); i++) {
                    if (imgPath.equals(pickedImageBeans.get(i).getImgPath())) {
                        imageBean.setImgOrder(i + 1);
                        pickedImageBeans.get(i).setImgPosition(imagePos);
                        break;
                    }
                }
            }


            if (imageBean.getImgOrder() != -1) {
                vh.txtPickCount.setVisibility(View.VISIBLE);
                if (Define.ALBUM_PICKER_COUNT == 1)
                    vh.txtPickCount.setText("");
                else
                    vh.txtPickCount.setText(String.valueOf(imageBean.getImgOrder()));
            } else
                vh.txtPickCount.setVisibility(View.GONE);


            if (imgPath != null && !imgPath.equals("")) {
                Glide
                        .with(vh.imgPhoto.getContext())
                        .load(imgPath)
//                        .thumbnail(0.9f)
//                        .placeholder(R.drawable.loading_img)
                        .override(Define.PHOTO_PICKER_SIZE, Define.PHOTO_PICKER_SIZE)
                        .crossFade()
                        .centerCrop()
                        .into(vh.imgPhoto);
            }


            vh.imgPhoto.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (vh.txtPickCount.getVisibility() == View.GONE &&
                            Define.ALBUM_PICKER_COUNT > pickedImageBeans.size()) {
                        vh.txtPickCount.setVisibility(View.VISIBLE);
                        pickedImageBeans.add(new PickedImageBean(pickedImageBeans.size() + 1, imgPath, imagePos));

                        if (Define.ALBUM_PICKER_COUNT == 1)
                            vh.txtPickCount.setText("");
                        else
                            vh.txtPickCount.setText(String.valueOf(pickedImageBeans.size()));

                        imageBean.setImgOrder(pickedImageBeans.size());
                        pickerController.setActionbarTitle(pickedImageBeans.size());
                    } else if (vh.txtPickCount.getVisibility() == View.VISIBLE) {
                        pickerController.setRecyclerViewClickable(false);
                        pickedImageBeans.remove(imageBean.getImgOrder() - 1);
                        if (Define.ALBUM_PICKER_COUNT != 1)
                            setOrder(Integer.valueOf(vh.txtPickCount.getText().toString()) - 1);
                        else
                            setOrder(0);
                        imageBean.setImgOrder(-1);
                        vh.txtPickCount.setVisibility(View.GONE);
                        pickerController.setActionbarTitle(pickedImageBeans.size());
                    }else {
//                        Snackbar.make(v.getContext(), v.getContext().getString(R.string.msg_no_slected), Snackbar.LENGTH_SHORT).show();
                        Snackbar.make(v, Define.MESSAGE_LIMIT_REACHED, Snackbar.LENGTH_SHORT).show();
                    }
                }
            });
        }

    }

    @Override
    public int getItemCount() {
        if (isHeader)
            return imageBeans.length + 1;

        return imageBeans.length;
    }

    @Override
    public int getItemViewType(int position) {
        if (position == 0 && isHeader) {
            return TYPE_HEADER;
        }
        return super.getItemViewType(position);
    }


    private void setOrder(int removePosition) {
        for (int i = removePosition; i < pickedImageBeans.size(); i++) {
            if (pickedImageBeans.get(i).getImgPosition() != -1) {
                imageBeans[pickedImageBeans.get(i).getImgPosition()]
                        .setImgOrder(i + 1);
                if (isHeader)
                    notifyItemChanged(pickedImageBeans.get(i).getImgPosition() + 1); //if use header +1
                else
                    notifyItemChanged(pickedImageBeans.get(i).getImgPosition()); //if don't use header
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

    public void addImage(String path) {
        ArrayList<ImageBean> al = new ArrayList<ImageBean>();
        Collections.addAll(al, imageBeans);
        al.add(0, new ImageBean(-1, path));

        imageBeans = al.toArray(new ImageBean[al.size()]);

        for (int i = 0; i < pickedImageBeans.size(); i++)
            pickedImageBeans.get(i).setImgPosition(pickedImageBeans.get(i).getImgPosition() + 1);

        notifyDataSetChanged();

        if(AlbumActivity.changeAlbumPublishSubject.hasObservers())
            AlbumActivity.changeAlbumPublishSubject.onNext("PATH|" + path);
    }

}