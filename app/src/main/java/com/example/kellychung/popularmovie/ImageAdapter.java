package com.example.kellychung.popularmovie;


import java.util.ArrayList;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

/**
 * This is the custom adapter for the grid view of images
 **/


public class ImageAdapter extends android.widget.BaseAdapter {

    private android.content.Context context;
    private java.util.ArrayList<String> items;
    android.view.LayoutInflater inflater;

    public ImageAdapter(Context context, ArrayList<String> items) {
        this.context = context;
        this.items = items;
        inflater = (android.view.LayoutInflater) this.context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

    }


    public int getCount() {
        return items.size();
    }


    public void add(String object) {
        items.add(object);
        //Log.e("Add", items.size() + "");
        super.notifyDataSetChanged();
    }


    public Object getItem(int position) {
        return null;
    }

    public long getItemId(int position) {
        return 0;
    }


    public void clear() {
        items.clear();
        super.notifyDataSetChanged();

        //String m = getCount() + "";
        // Log.e("Image Loader", m);

    }


    // create a new ImageView for each item referenced by the Adapter
    public android.view.View getView(int position, View convertView, ViewGroup parent) {

        ImageView imageView;
        if (convertView == null) {

            imageView = new ImageView(context);
            imageView.setAdjustViewBounds(true);

        } else {
            imageView = (ImageView) convertView;
        }
        com.squareup.picasso.Picasso.with(context)
                .load(items.get(position))
                .into(imageView);
        return imageView;
    }

}
