package com.msbte.modelanswerpaper;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;

public class ImageAdapter extends BaseAdapter {
    private Context context;
    private final String[] mobileValues;

    public ImageAdapter(Context context, String[] mobileValues) {
        this.context = context;
        this.mobileValues = mobileValues;
    }

    public View getView(int position, View convertView, ViewGroup parent) {

        LayoutInflater inflater = (LayoutInflater) context
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        View gridView;

        if (convertView == null) {

            gridView = new View(context);

            // get layout from mobile.xml
            gridView = inflater.inflate(R.layout.mobile, null);

            // set value into textview
            TextView textView = (TextView) gridView.findViewById(R.id.grid_item_label);
            textView.setText(mobileValues[position]);

            // set image based on selected text
            ImageView imageView = (ImageView) gridView
                    .findViewById(R.id.grid_item_image);

            String mobile = mobileValues[position];

            if (mobile.equals("MSBTE Content")) {
                imageView.setImageResource(R.drawable.a);
            } else if (mobile.equals("Projects")) {
                imageView.setImageResource(R.drawable.b);
            } else if (mobile.equals("Question Paper")) {
                imageView.setImageResource(R.drawable.c);
            } else if (mobile.equals("Model Answer")) {
                imageView.setImageResource(R.drawable.d);
            } else if (mobile.equals("MSBTE Solution Pro")) {
                imageView.setImageResource(R.drawable.pro);
            }else if (mobile.equals("Video Lectures")) {
                imageView.setImageResource(R.drawable.f);
            }
            else {
                imageView.setImageResource(R.drawable.refresh);
            }

        } else {
            gridView = (View) convertView;
        }

        return gridView;
    }

    @Override
    public int getCount() {
        return mobileValues.length;
    }

    @Override
    public Object getItem(int position) {
        return null;
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

}