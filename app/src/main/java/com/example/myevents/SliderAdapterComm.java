package com.example.myevents;

import android.app.Activity;
import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.util.ArrayList;

import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager.widget.PagerAdapter;

public class SliderAdapterComm extends PagerAdapter {

    Context context;
    LayoutInflater layoutInflater;


    public SliderAdapterComm(Context context){
        this.context=context;
    }


    public ArrayList<String> slide_username= EventDemo.Event_Users; //Username χρηστών



    public ArrayList<String> slide_comm = EventDemo.Event_Comments;  //Σχόλια Χρηστών


    @Override
    public int getCount(){
        return slide_username.size();
    }

    @Override
    public boolean isViewFromObject(View view, Object o){
        return view== (RelativeLayout) o;
    }

    @Override
    public  Object instantiateItem(ViewGroup container, int position){
        layoutInflater=(LayoutInflater) context.getSystemService(context.LAYOUT_INFLATER_SERVICE);
        View view=layoutInflater.inflate(R.layout.slidecomm_layout,container,false);

        TextView slideUsername=(TextView) view.findViewById(R.id.slide_username);
        TextView slideComment=(TextView) view.findViewById(R.id.slide_comm);

        slideUsername.setText(slide_username.get(position));
        slideComment.setText(slide_comm.get(position));
        Log.e("TAG","Username:" + slide_username.get(position) + " " + "Comment:" + slide_comm.get(position));
        container.addView(view);

        return view;
    }

    @Override
    public void destroyItem(ViewGroup container,int position,Object object){
        container.removeView((RelativeLayout)object);
    }
}
