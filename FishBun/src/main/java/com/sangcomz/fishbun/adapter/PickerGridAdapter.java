package com.sangcomz.fishbun.adapter;

import android.content.Context;
import android.os.Handler;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.sangcomz.fishbun.bean.ImageBean;
import com.sangcomz.fishbun.bean.PickedImageBean;
import com.sangcomz.fishbun.define.Define;
import com.sangcomz.fishbun.ui.picker.PickerController;

import java.util.ArrayList;

import kr.co.sangcomz.albummodule.R;

public class PickerGridAdapter
        extends RecyclerView.Adapter<PickerGridAdapter.ViewHolder> {


    private Context context;
    private ArrayList<PickedImageBean> pickedImageBeans = new ArrayList<>();
    private ImageBean[] imageBeans;
    private PickerController pickerController;

    int width;
    int height;
    RelativeLayout.LayoutParams params;

    public class ViewHolder extends RecyclerView.ViewHolder {

        ImageView imgPhoto;
        TextView txtPickCount;

        public ViewHolder(View view) {
            super(view);
            imgPhoto = (ImageView) view.findViewById(R.id.img_thum);
            txtPickCount = (TextView) view.findViewById(R.id.txt_pick_count);

            imgPhoto.setLayoutParams(params);
            txtPickCount.setLayoutParams(params);
        }
    }

    public PickerGridAdapter(Context context, ImageBean[] imageBeans, ArrayList<PickedImageBean> pickedImageBeans, PickerController pickerController) {
        this.context = context;
        this.imageBeans = imageBeans;
        this.pickerController = pickerController;
        this.pickedImageBeans = pickedImageBeans;
        setSize(context);
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.thum_item, parent, false);
        return new ViewHolder(view);
    }


    @Override
    public void onBindViewHolder(final ViewHolder holder, final int position) {

        final ImageBean imageBean = imageBeans[position];
        final String imgPath = imageBean.getImgPath();

        if (!imageBean.isInit()) {
            imageBean.setIsInit(true);
            for (int i = 0; i < pickedImageBeans.size(); i++) {
                if (imgPath.equals(pickedImageBeans.get(i).getImgPath())) {
                    imageBean.setImgOrder(i + 1);
                    pickedImageBeans.get(i).setImgPosition(position);
                    break;
                }
            }
        }


        if (imageBean.getImgOrder() != -1) {
            holder.txtPickCount.setVisibility(View.VISIBLE);
            holder.txtPickCount.setText(String.valueOf(imageBean.getImgOrder()));
        } else
            holder.txtPickCount.setVisibility(View.GONE);


        if (imgPath != null && !imgPath.equals("")) {
            Glide
                    .with(context)
                    .load(imgPath)
//                        .thumbnail(0.7f)
//                        .placeholder(R.drawable.loading_img)
                    .override(width, height)
                    .crossFade()
                    .centerCrop()
                    .into(holder.imgPhoto);
        }


        holder.imgPhoto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (holder.txtPickCount.getVisibility() == View.GONE &&
                        Define.ALBUM_PICKER_COUNT > pickedImageBeans.size()) {
                    holder.txtPickCount.setVisibility(View.VISIBLE);
                    pickedImageBeans.add(new PickedImageBean(pickedImageBeans.size() + 1, imgPath, position));
                    holder.txtPickCount.setText(String.valueOf(pickedImageBeans.size()));
                    imageBean.setImgOrder(pickedImageBeans.size());
                    pickerController.setActionbarTitle(pickedImageBeans.size());
                } else if (holder.txtPickCount.getVisibility() == View.VISIBLE) {
                    pickerController.setRecyclerViewClickable(false);
                    pickedImageBeans.remove(imageBean.getImgOrder() - 1);
                    setOrder(Integer.valueOf(holder.txtPickCount.getText().toString()) - 1);
                    imageBean.setImgOrder(-1);
                    holder.txtPickCount.setVisibility(View.GONE);
                    pickerController.setActionbarTitle(pickedImageBeans.size());
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return imageBeans.length;
    }


    private void setOrder(int removePosition) {
        for (int i = removePosition; i < pickedImageBeans.size(); i++) {
            if (pickedImageBeans.get(i).getImgPosition() != -1) {
                imageBeans[pickedImageBeans.get(i).getImgPosition()]
                        .setImgOrder(i + 1);
                notifyItemChanged(pickedImageBeans.get(i).getImgPosition());
            }
        }
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                pickerController.setRecyclerViewClickable(true);
            }
        }, 500);

    }

    private void setSize(Context context) {
        //가로길이
        width = context.getResources().getDisplayMetrics().widthPixels;

        //양옆 마진 빼기
        final float scale = context.getResources().getDisplayMetrics().density;
        float dip = 20.0f;  // 변환하고자하는 dip 치수
        int marginPixel = (int) (dip * scale + 0.5f);
        width = width / 2 - marginPixel;
        int thWidth = 50;
        int thHeight = 30;
        //세로길이 구하기
        height = width * thHeight / thWidth;
        //이미지 사이즈 변경
        params = new RelativeLayout.LayoutParams(width, height);
    }

}