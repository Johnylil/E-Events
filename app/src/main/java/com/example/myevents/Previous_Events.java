package com.example.myevents;

import androidx.annotation.NonNull;

import android.content.Context;
import android.graphics.Bitmap;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.nostra13.universalimageloader.cache.memory.impl.WeakMemoryCache;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.nostra13.universalimageloader.core.assist.ImageScaleType;
import com.nostra13.universalimageloader.core.display.FadeInBitmapDisplayer;

import java.util.ArrayList;

public class Previous_Events extends BaseAdapter {

    private static final String TAG= "EventListAdapter";
    private Context myContext;
    private int myResource;
    public static int CurPos;
    private int lastPosition=-1;
    public ArrayList<String[]> previous_data = new ArrayList<>();
    public Bitmap[] prevImages;

    static class ViewEventHolder {
        public TextView name;
        public  TextView address;
        public  TextView description;
        public ImageView img;
        public TextView date;

    }
    public Previous_Events(Context context, int resource, ArrayList<String[]> objects, Bitmap[] PrevImages){
        super();
        myContext=context;
        myResource= resource;
        previous_data.addAll(objects);
        prevImages = PrevImages.clone();
    }

    @Override
    public int getCount() {
        return previous_data.size();
    }

    @Override
    public Object getItem(int position) {
        return previous_data.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @NonNull
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        setupImageLoader();
        String[] prev_event = previous_data.get(position);
        CurPos = position;

            //Create the view result for showing animation
            final View result;
            Previous_Events.ViewEventHolder holder;

        if (prev_event.length == 0) {
            Toast.makeText(myContext, "You haven't uploaded any events", Toast.LENGTH_SHORT).show();
            result = convertView;
            Log.e("TAG","true");
        } else {

            Log.e("TAG", "false");


            if (convertView == null) {

                LayoutInflater inflater = LayoutInflater.from(myContext);
                convertView = inflater.inflate(myResource, parent, false);
                holder = new Previous_Events.ViewEventHolder();
                holder.name = (TextView) convertView.findViewById(R.id.textViewPrev1);
                holder.address = (TextView) convertView.findViewById(R.id.textViewPrev2);
                holder.description = (TextView) convertView.findViewById(R.id.textViewPrev3);
                holder.img = (ImageView) convertView.findViewById(R.id.imagePrev);
                holder.date = (TextView) convertView.findViewById(R.id.textViewPrev4);

                result = convertView;
                convertView.setTag(holder);
            } else {
                holder = (Previous_Events.ViewEventHolder) convertView.getTag();
                result = convertView;
            }

            Animation animation = AnimationUtils.loadAnimation(myContext,
                    (position > lastPosition) ? R.anim.load_down_anim : R.anim.load_up_anim);
            result.startAnimation(animation);
            lastPosition = position;


            holder.img.setImageBitmap(prevImages[position]);
            holder.name.setText(prev_event[3]);
            holder.address.setText(prev_event[1]);
            holder.description.setText(prev_event[2]);
            holder.date.setText(prev_event[4] + " " + prev_event[7]);
        }
        return convertView;
    }

    @Override
    public int getViewTypeCount() {
        return super.getViewTypeCount();
    }

    private void setupImageLoader (){
        // UNIVERSAL IMAGE LOADER SETUP
        DisplayImageOptions defaultOptions = new DisplayImageOptions.Builder()
                .cacheOnDisc(true).cacheInMemory(true)
                .imageScaleType(ImageScaleType.EXACTLY)
                .displayer(new FadeInBitmapDisplayer(300)).build();

        ImageLoaderConfiguration config = new ImageLoaderConfiguration.Builder(
                myContext)
                .defaultDisplayImageOptions(defaultOptions)
                .memoryCache(new WeakMemoryCache())
                .discCacheSize(100 * 1024 * 1024).build();

        ImageLoader.getInstance().init(config);
    }
}