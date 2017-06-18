package com.sangcomz.fishbun.adapter;

import android.net.Uri;
import android.support.constraint.ConstraintLayout;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.sangcomz.fishbun.R;
import com.sangcomz.fishbun.util.TouchImageView;
import com.squareup.picasso.Picasso;

/**
 * Created by sangcomz on 15/06/2017.
 */

public class DetailViewPagerAdapter extends PagerAdapter {
    private LayoutInflater inflater;
    private Uri[] images;

    public DetailViewPagerAdapter(LayoutInflater inflater, Uri[] images) {
        this.inflater = inflater;
        this.images = images;
    }


    @Override
    public Object instantiateItem(ViewGroup container, int position) {

        View itemView = inflater.inflate(R.layout.detail_item, container, false);
        container.addView(itemView);

        TouchImageView imageView = (TouchImageView) itemView.findViewById(R.id.img_detail_image);

        Picasso.with(itemView.getContext())
                .load(images[position])
                .fit()
                .centerInside()
                .into(imageView);
        return itemView;
    }

    @Override
    public int getCount() {
        return images.length;
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        if (container instanceof ViewPager) {
            container.removeView((ConstraintLayout) object);
        }
    }

    @Override
    public boolean isViewFromObject(View view, Object object) {
        return view == object;
    }
}
