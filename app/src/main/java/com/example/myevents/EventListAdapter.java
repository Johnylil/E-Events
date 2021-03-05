package com.example.myevents;

import android.content.Context;

import androidx.annotation.NonNull;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.Image;
import android.net.Uri;
import android.provider.MediaStore;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ArrayAdapter;
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

import java.io.ByteArrayOutputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;


public class EventListAdapter extends BaseAdapter {

    private static final String TAG= "EventListAdapter";
    private Context mContext;
    private int mResource;
    private int lastPosition=-1;
    ArrayList<String[]> data = new ArrayList<>();
    public static Bitmap[] Images;

     static class ViewHolder {
        public TextView name;
        public  TextView address;
        public  TextView description;
        public ImageView img;
        public TextView date;

    }
    public EventListAdapter(Context context, int resource, ArrayList<String[]> objects){
        super();
        mContext=context;
        mResource= resource;
       data.addAll(objects);
    }

    @Override
    public int getCount() {
        return data.size();
    }

    @Override
    public Object getItem(int position) {
        return data.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @NonNull
    @Override
    public View getView(int position, View convertView, ViewGroup parent){
        setupImageLoader();
       Images = Visitor.FinalImage.clone();

       Log.e(TAG,Images.toString());

        //Create the view result for showing animation
        final View result;
        ViewHolder holder;

        if(convertView==null){

            LayoutInflater inflater=LayoutInflater.from(mContext);
        convertView=inflater.inflate(mResource, parent,false);
        holder = new ViewHolder();
        holder.name = (TextView) convertView.findViewById(R.id.textView1);
        holder.address = (TextView) convertView.findViewById(R.id.textView2);
        holder.description = (TextView) convertView.findViewById(R.id.textView3);
        holder.img=(ImageView) convertView.findViewById(R.id.image);
        holder.date = (TextView) convertView.findViewById(R.id.textView4);

        result=convertView;
        convertView.setTag(holder);}

        else
            {holder=(ViewHolder) convertView.getTag();
        result=convertView;}

        Animation animation= AnimationUtils.loadAnimation(mContext,
                (position>lastPosition)? R.anim.load_down_anim:R.anim.load_up_anim);
        result.startAnimation(animation);
        lastPosition=position;

        ImageLoader imageLoader = ImageLoader.getInstance();
        if(Visitor.MapsOn)
        {
            imageLoader.destroy();
            setupImageLoader();
            ImageLoader ImageLoader1 = ImageLoader.getInstance();
         //   ImageLoader1.displayImage(Visitor.URLs.get(position),holder.img);
            Bitmap[] newImages = Images.clone();
            holder.img.setImageBitmap(newImages[position]);
        }else{holder.img.setImageBitmap(Images[position]);}


        String[] event = data.get(position);
        holder.name.setText(event[3]);
        holder.address.setText(event[1]);
        holder.description.setText(event[2]);
        holder.date.setText(event[4] + " " + event[7]);

//        Log.e(TAG, event[7]);
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
                mContext)
                .defaultDisplayImageOptions(defaultOptions)
                .memoryCache(new WeakMemoryCache())
                .discCacheSize(100 * 1024 * 1024).build();

        ImageLoader.getInstance().init(config);
    }


}

