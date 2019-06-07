package com.sangcomz.fishbun.adapter.view;

import android.net.Uri;
import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.viewpager.widget.PagerAdapter;
import androidx.viewpager.widget.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.sangcomz.fishbun.Fishton;
import com.sangcomz.fishbun.R;
import com.sangcomz.fishbun.util.TouchImageView;

/**
 * Created by sangcomz on 15/06/2017.
 */

public class DetailViewPagerAdapter extends PagerAdapter {

    private Fishton fishton;
    private LayoutInflater inflater;
    private Uri[] images;

    public DetailViewPagerAdapter(LayoutInflater inflater, Uri[] images) {
        this.inflater = inflater;
        this.images = images;
        fishton = Fishton.getInstance();
    }


    @NonNull
    @Override
    public Object instantiateItem(@NonNull ViewGroup container, int position) {

        View itemView = inflater.inflate(R.layout.detail_item, container, false);
        container.addView(itemView);

        TouchImageView imageView = itemView.findViewById(R.id.img_detail_image);

        if (imageView != null
                && images[position] != null)
            fishton
                    .imageAdapter
                    .loadDetailImage(imageView, images[position]);

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
