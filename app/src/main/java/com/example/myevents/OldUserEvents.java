package com.example.myevents;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.util.Base64;
import android.util.Log;
import android.view.ContextMenu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.util.ArrayList;
import java.util.List;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;


public class OldUserEvents extends AppCompatActivity {
    private static final String TAG = "OldUserEvents";
    public static ArrayList<String[]> OldEvents = new ArrayList<>();
    public Bitmap[] PrevImage = new Bitmap[70];
    public static int id = 0;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_previous__events);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        Gson gson = new GsonBuilder()
                .setLenient()
                .create();




        Retrofit fetch = new Retrofit.Builder()
                .baseUrl("https://api.e-events.drosatos.eu/android/")
                .addConverterFactory(GsonConverterFactory.create(gson))
                .build();

        FetchData fetchData = fetch.create(FetchData.class);

        Call<List<Events>> call = fetchData.getMyEvent(MainActivity.Token);


        call.enqueue(new Callback<List<Events>>() {
            @Override
            public void onResponse(Call<List<Events>> call, Response<List<Events>> response) {

                if (!response.isSuccessful()) {
                    Log.e(TAG, Integer.toString(response.code()));
                    return;
                }

                Log.e(TAG, Integer.toString(response.code()));
                OldEvents.clear();
                List<Events> events = response.body();
                Bitmap FinalImage1;
                Bitmap[] FinalImages = new Bitmap[20];
                int i = 0;
                ListView mListView = (ListView) findViewById(R.id.listView_prev_events);
                for (Events event : events) {
                    String[] FinalData = new String[9];
                    FinalData[0] = event.getPhoto();
                    FinalData[1] = event.getAddress();
                    FinalData[2] = event.getDescription();
                    FinalData[3] = event.getTitle();
                    FinalData[4] = event.getDate().split("00")[0];
                    FinalData[5] = event.getLatitude();
                    FinalData[6] = event.getLongitude();
                    FinalData[7] = event.getTime();
                    FinalData[8] = event.getCategory();
                    id = event.getEvent_id();
                    Log.e(TAG,FinalData[2] + " " + FinalData[3]);
                    OldEvents.add(FinalData);
                    FinalImage1 = BitmapDecoder(FinalData[0]);
                    PrevImage[i] = FinalImage1;
                    i++;
                    //    SetPhoto(FinalImage1);
                }
                Log.e(TAG,Integer.toString(i) + "  " + Integer.toString(OldEvents.size()));
                if (OldEvents.size() == i) {
                    Previous_Events adapter = new Previous_Events(OldUserEvents.this, R.layout.adapter_prev_events_layout, OldEvents, PrevImage);
                    Log.e(TAG,Integer.toString(OldEvents.size()) + " " + Integer.toString(adapter.getCount()));
                    mListView.setAdapter(adapter);
                    registerForContextMenu(mListView);
                }
            }

            @Override
            public void onFailure(Call<List<Events>> call, Throwable t) {
                Log.e(TAG, t.getMessage());
            }
        });
    }

    public Bitmap BitmapDecoder(String stringPicture) {
        byte[] decodedString = Base64.decode(stringPicture, Base64.DEFAULT);
        Bitmap finalphoto = BitmapFactory.decodeByteArray(decodedString, 0, decodedString.length);
        return finalphoto;
    }

    @Override
    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
        super.onCreateContextMenu(menu, v, menuInfo);
        if (v.getId()==R.id.listView_prev_events) {
            MenuInflater inflater = getMenuInflater();
            inflater.inflate(R.menu.menu_list, menu);
        }
    }

    @Override
    public boolean onContextItemSelected(MenuItem item) {
        AdapterView.AdapterContextMenuInfo info = (AdapterView.AdapterContextMenuInfo) item.getMenuInfo();
        switch(item.getItemId()) {
            case R.id.edit:
                Intent intent = new Intent(getApplicationContext(), EditEvent.class).putExtra("Position",info.position);
                startActivity(intent);
                return true;
            case R.id.delete:
                DeleteEvent();
                return true;
            default:
                return super.onContextItemSelected(item);
        }
    }
    private void DeleteEvent()
    {
        Retrofit delete = new Retrofit.Builder()
                .baseUrl("https://api.e-events.drosatos.eu/android/")
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        FetchData deleteEvent = delete.create(FetchData.class);

        Call<List<Events>> Delete = deleteEvent.DeleteData(id,MainActivity.Token);


        Delete.enqueue(new Callback<List<Events>>() {
            @Override
            public void onResponse(Call<List<Events>> call, Response<List<Events>> response) {

                if (!response.isSuccessful()) {
                    Log.e("TAG",Integer.toString(response.code()));
                    return;
                }
                Log.e("TAG",response.toString());
            }

            @Override
            public void onFailure(Call<List<Events>> call, Throwable t) {
                Log.e("TAG",t.getMessage());
            }
        });
    }

    }
