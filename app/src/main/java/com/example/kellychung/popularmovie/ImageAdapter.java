package com.example.kellychung.popularmovie;


import java.util.ArrayList;
import android.view.LayoutInflater;
import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;

/**
 * This is the custom adapter for the grid view of images
 **/


public class ImageAdapter extends BaseAdapter {

    private Context context;
    private java.util.ArrayList<movie> items;
    LayoutInflater inflater;

    public ImageAdapter(Context context, ArrayList<movie> items) {
        this.context = context;
        this.items = items;
        inflater = (LayoutInflater) this.context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

    }


    public int getCount() {
        return items.size();
    }


    public void add(movie object) {
        items.add(object);
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


    }


    // create a new ImageView for each item referenced by the Adapter
    public  View getView(int position, View convertView, ViewGroup parent) {

        ImageView imageView;
        if (convertView == null) {

            imageView = new ImageView(context);
            imageView.setAdjustViewBounds(true);

        } else {
            imageView = (ImageView) convertView;
        }
        com.squareup.picasso.Picasso.with(context)
                .load(items.get(position).getPosterUrl())
                .error(R.drawable.sample_6)
                .into(imageView);
        return imageView;
    }

}
