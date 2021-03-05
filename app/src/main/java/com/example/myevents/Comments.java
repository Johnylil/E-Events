package com.example.myevents;

import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager.widget.ViewPager;
import androidx.viewpager2.widget.ViewPager2;

import android.app.Activity;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.text.Html;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

public class Comments extends Activity {

    private ViewPager mSlideViewPager;
    private LinearLayout mDotLayout;
    private TextView[] mDots;
    private SliderAdapterComm sliderAdapterComm;
    Button mExitButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_comments);

        mExitButton=(Button) findViewById(R.id.exitcomments_button);
        mExitButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        mSlideViewPager=(ViewPager) findViewById(R.id.slideViewPager);
        mDotLayout=(LinearLayout) findViewById(R.id.dotsLayout);


        sliderAdapterComm=new SliderAdapterComm(this);
        mSlideViewPager.setAdapter(sliderAdapterComm);

        addDotsIndicator(0);
        mSlideViewPager.addOnPageChangeListener(viewListener);

        DisplayMetrics dm=new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(dm);

        int width=dm.widthPixels;
        int height=dm.heightPixels;

        getWindow().setLayout((int)(width*.9), (int)(height*.4));

        WindowManager.LayoutParams params=getWindow().getAttributes();
        params.gravity= Gravity.BOTTOM;
        params.x=0;
        params.y=-20;

        getWindow().setAttributes(params);
    }

    public void addDotsIndicator(int position){
        // μέσα στην παρένθεση του textview θα μπει ο αριθμός των σχολίων
        mDots=new TextView[EventDemo.Event_Comments.size()];
        Log.e("TAG",Integer.toString(EventDemo.Event_Comments.size()));
        mDotLayout.removeAllViews();

        for(int i=0; i<mDots.length; i++){

            mDots[i]=new TextView(this);
            mDots[i].setText(Html.fromHtml("&#8226;"));
            mDots[i].setTextSize(35);
            mDots[i].setTextColor(getResources().getColor(R.color.colorAccent));

            mDotLayout.addView(mDots[i]);

        }
        if(mDots.length>0){
            mDots[position].setTextColor(getResources().getColor(R.color.colorPrimaryDark));
        }
    }
    ViewPager.OnPageChangeListener viewListener= new ViewPager.OnPageChangeListener() {
        @Override
        public void onPageScrolled(int i, float v, int i1) {
        }

        @Override
        public void onPageSelected(int i) {
            addDotsIndicator(i);
        }

        @Override
        public void onPageScrollStateChanged(int i) {

        }
    };
}
