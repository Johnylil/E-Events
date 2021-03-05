package com.example.myevents;

import androidx.fragment.app.FragmentActivity;

import android.graphics.Bitmap;
import android.graphics.Matrix;
import android.os.Bundle;
import android.util.Log;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.MarkerOptions;

import java.util.ArrayList;

public class MapsActivity extends FragmentActivity implements OnMapReadyCallback {

    private GoogleMap Map;
    public ArrayList<String[]> NewEvents = new ArrayList<>();
    public  Bitmap[] Images;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);
        NewEvents.addAll(Visitor.Events);
        Images = Visitor.MapImage.clone();
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

    }


    @Override
    public void onMapReady(GoogleMap googleMap) {
        Map = googleMap;
     if(Visitor.city !=null)
     {
         Map.moveCamera((CameraUpdateFactory.newLatLngZoom(Visitor.city.getCenter(),12)));
     }else {
         LatLngBounds Greece = new LatLngBounds(
                 new LatLng(35.01186, 19.91975), new LatLng(41.50306, 28.2225));
         Map.moveCamera(CameraUpdateFactory.newLatLngZoom(Greece.getCenter(), 6));
     }

        Log.e("TAG",Map.getCameraPosition().target.toString());

        for (int i = 0; i <= NewEvents.size()-1; i++) {
            String[] NewEvent = NewEvents.get(i);
            Bitmap NewImage = getScaledDownBitmap(Images[i], 250, false);
            LatLng loc = new LatLng(Double.valueOf(NewEvent[5]), Double.valueOf(NewEvent[6]));
            Map.addMarker(new MarkerOptions().position(loc)
                    .title(NewEvent[3]).snippet(NewEvent[2])
                    .icon(BitmapDescriptorFactory.fromBitmap(NewImage)));
            Log.e("TAG", "Marker Added");
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    public static Bitmap getScaledDownBitmap(Bitmap bitmap, int threshold, boolean isNecessaryToKeepOrig){
        int width = bitmap.getWidth();
        int height = bitmap.getHeight();
        int newWidth = width;
        int newHeight = height;

        if(width > height && width > threshold){
            newWidth = threshold;
            newHeight = (int)(height * (float)newWidth/width);
        }

        if(width > height && width <= threshold){
            return bitmap;
        }

        if(width < height && height > threshold){
            newHeight = threshold;
            newWidth = (int)(width * (float)newHeight/height);
        }

        if(width < height && height <= threshold){
            return bitmap;
        }

        if(width == height && width > threshold){
            newWidth = threshold;
            newHeight = newWidth;
        }

        if(width == height && width <= threshold){
            return bitmap;
        }

        return getResizedBitmap(bitmap, newWidth, newHeight, isNecessaryToKeepOrig);
    }

    private static Bitmap getResizedBitmap(Bitmap bm, int newWidth, int newHeight, boolean isNecessaryToKeepOrig) {
        int width = bm.getWidth();
        int height = bm.getHeight();
        float scaleWidth = ((float) newWidth) / width;
        float scaleHeight = ((float) newHeight) / height;
        Matrix matrix = new Matrix();
        matrix.postScale(scaleWidth, scaleHeight);
        Bitmap resizedBitmap = Bitmap.createBitmap(bm, 0, 0, width, height, matrix, false);
        if(!isNecessaryToKeepOrig){
            bm.recycle();
        }
        return resizedBitmap;
    }
}
