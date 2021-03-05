package com.example.myevents;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.ClipData;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.BitmapShader;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.RectF;
import android.graphics.Shader;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;

import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.material.bottomnavigation.BottomNavigationItemView;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.datepicker.CalendarConstraints;
import com.google.android.material.datepicker.MaterialDatePicker;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.navigation.NavigationView;
import com.google.android.material.snackbar.Snackbar;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import com.squareup.timessquare.CalendarPickerView;
import com.theartofdev.edmodo.cropper.CropImage;


import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.widget.PopupMenu;
import androidx.core.app.NotificationCompat;
import androidx.core.util.Pair;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

import android.os.Handler;
import android.os.Parcelable;
import android.preference.PreferenceManager;
import android.provider.MediaStore;
import android.util.Base64;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Serializable;
import java.lang.reflect.Array;
import java.lang.reflect.Type;
import java.net.HttpURLConnection;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.TimeZone;


import retrofit2.converter.gson.GsonConverterFactory;

import static android.graphics.Color.BLACK;
import static java.util.Calendar.DAY_OF_MONTH;
import static java.util.Calendar.MONTH;
import static java.util.Calendar.YEAR;
import static java.util.Calendar.getInstance;


public class Visitor extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    public static TextView mText;
    public static TextView mText2;
    public static Bitmap[] FinalImage = new Bitmap[70];
    public static Bitmap[] MapImage = new Bitmap[70];
    public String[] EventCategory = new String[70];
    private static final String TAG = "Visitor";
    private static final String PREFS = "Preferences";
    public static JSONArray NewArray = new JSONArray();
    public static int ButtonPos;
    public static ArrayList<String[]> Events = new ArrayList<>();
    public static boolean MapsOn = false;
    public static int index[] = new int[70];
    public static ArrayList<Float> Average_rating = new ArrayList<>();
    public String Category = null;
    public static String firstDate, lastDate;
    public static LatLngBounds city;
    public String notifON = "4";
    public String NotificatioN = "4";
    Dialog myDialog;
    SharedPreferences prf;
    public String nextDate;
    public String prev;
    private TextView mTextMessage;
    Uri cropImage;
    private ImageView photoprofile;

    @Override
    protected void onCreate(Bundle savedInstanceState) { //5e81c2fd58b91810000c4480
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_visitor);
        Log.e(TAG, "onCreate:Started.");

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        mTextMessage = (TextView) findViewById(R.id.message);
        BottomNavigationView navigation = (BottomNavigationView) findViewById(R.id.navigation);
        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);
        if(MainActivity.Token ==null)
        {
            prf = PreferenceManager.getDefaultSharedPreferences(this);
            MainActivity.Token = prf.getString("token","");
        }


        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();
        myDialog = new Dialog(this);
        prf = PreferenceManager.getDefaultSharedPreferences(this);
        GetEvents();
        if(NotificatioN.compareTo(notifON) !=0)
        {
            int notifyID = 1;
            String CHANNEL_SOUND_ID = "channel SOUND";// The id of the channel.
            CharSequence NAME_SOUND = "channel 1";// The user-visible name of the channel.
            String CHANNEL_SILENT_ID = "channel SILENT";// The id of the channel.
            CharSequence NAME_SILENT = "channel 2";// The user-visible name of the channel.
            int importance_sound = NotificationManager.IMPORTANCE_HIGH;
            int importance_silent = NotificationManager.IMPORTANCE_LOW;
            NotificationChannel mChannel_sound = new NotificationChannel(CHANNEL_SOUND_ID, NAME_SOUND, importance_sound);
            NotificationChannel mChannel_silent = new NotificationChannel(CHANNEL_SILENT_ID, NAME_SILENT, importance_silent);

            // Crete both notification channels
            NotificationManager mNotificationManager =
                    (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
            mNotificationManager.createNotificationChannel(mChannel_sound);
            mNotificationManager.createNotificationChannel(mChannel_silent);

            Intent intent = new Intent(Visitor.this, Visitor.class);
            intent.putExtra("yourpackage.notifyId", notifyID);
            PendingIntent pIntent = PendingIntent.getActivity(Visitor.this, 0, intent,
                    PendingIntent.FLAG_UPDATE_CURRENT);


            // Select the correct notification channel
            String selectedChannelId;
                selectedChannelId = CHANNEL_SOUND_ID;

            // Create a notification and set the notification channel.
            NotificationCompat.Builder notificationBuilder = new NotificationCompat.Builder(Visitor.this, selectedChannelId);
            notificationBuilder.setSmallIcon(R.drawable.cheers);
            notificationBuilder.setContentTitle("e-Events");
            notificationBuilder.setContentText("Υπάρχει μια νέα εκδήλωση κοντά σας!");
            notificationBuilder.setContentIntent(pIntent);
            notificationBuilder.setChannelId(selectedChannelId);

            Notification notification = notificationBuilder.build();
            mNotificationManager.notify(notifyID, notification);

            notifON = NotificatioN;
        }

    } //Εδώ τελειώνει η onCreate()

    @Override
    protected void onResume() {
        ListView mListView = (ListView) findViewById(R.id.listView);
        ArrayList<String[]> EVENTS = new ArrayList<>(Events);
        MapImage = CreateImagesforMap(FinalImage, EventCategory);
        EventListAdapter adapter = new EventListAdapter(Visitor.this, R.layout.adapter_visitor_layout, EVENTS);
        mListView.setAdapter(adapter);
        mListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                ButtonPos = parent.getPositionForView(view);
                Intent intent = new Intent(getApplicationContext(), EventDemo.class);
                startActivity(intent);
            }
        });
        super.onResume();
    }


    @Override
    protected void onPause() {
        MapsOn = true;
        Log.e(TAG, "cHANGED");
        super.onPause();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        Log.e(TAG, "dESTROYED");
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    public void ShowPopup(View v) {
        Button close;
        myDialog.setContentView(R.layout.activity_communication);
        close = (Button) myDialog.findViewById(R.id.close);
        //txtclose.setText("M");
        close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                myDialog.dismiss();
            }
        });
        myDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        myDialog.show();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.visitor, menu);
        mText = findViewById(R.id.UserName);
        String value = prf.getString("username", "");
        mText.setText(value);

        mText2 = findViewById(R.id.e_Mail);
        String value2 = prf.getString("email", "");
        mText2.setText(value2);

        MenuInflater menuInflater = getMenuInflater();

        photoprofile = (ImageView) findViewById(R.id.photoprofile);
        prf = PreferenceManager.getDefaultSharedPreferences(this);
        String photograph = prf.getString("Profilephoto","");
        if(!photograph.isEmpty())
        {
            Bitmap photo = BitmapDecoder(photograph);
            photoprofile.setImageBitmap(photo);
        }

        Button camera = (Button) findViewById(R.id.takepicture);
        camera.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                CropImage.activity().start(Visitor.this);
            }
        });

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        if (id == R.id.add) {
            Intent intent = new Intent(Visitor.this, Organize.class);
            startActivity(intent);
        }
        if (id == R.id.previous) {
            Intent intent = new Intent(Visitor.this, OldUserEvents.class);
            startActivity(intent);
        }
        if (id == R.id.callendar) {
            SetDate();
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    GetSelectedEventsDate(firstDate, lastDate);
                }
            }, 8000);

            Log.e("TAG", firstDate + "  " + lastDate);
        }
        if(id == R.id.refresh){
            GetEvents();
        }
        if(id == R.id.citychoose)
        {
           View v  = findViewById(id);
            ChooseArea(v);
        }
        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_camera) {
            // Handle the camera action
        } else if (id == R.id.nav_gallery) {

        } else if (id == R.id.nav_slideshow) {

        } else if (id == R.id.nav_share) {

        } else if (id == R.id.nav_send) {

        } else if (id == R.id.log_out) {
            AlertDialog.Builder builder = new AlertDialog.Builder(Visitor.this);
            builder.setMessage("Do you want to exit?");
            builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int id) {

                    finish();
                    SharedPreferences.Editor editor = prf.edit();
                    editor.clear();
                    editor.commit();
                    Intent MainIntent = new Intent(Visitor.this, MainActivity.class);
                    startActivity(MainIntent);
                    MainIntent.putExtra("finish", true);
                    MainIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP); // To clean up all activities
                    finish();

                }
            });

            builder.setNegativeButton("CANCEL", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int id) {
                    dialog.cancel();
                }
            });
            AlertDialog alert = builder.create();
            alert.show();
        }
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            = new BottomNavigationView.OnNavigationItemSelectedListener() {
        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            switch (item.getItemId()) {
                case R.id.help:
                    starthelp(null);
                    return true;
                case R.id.maps:
                    Intent MainIntent = new Intent(Visitor.this, MapsActivity.class);
                    MapsOn = true;
                    startActivity(MainIntent);
                    return true;
                case R.id.filter:
                    BottomNavigationItemView view = findViewById(R.id.filter);
                    show_popup(view);
                    return true;
                case R.id.comm:
                    ShowPopup(null);
                    return true;
                case R.id.set:
                    startset(null);
                    return true;
            }
            return false;
        }
    };

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode,resultCode,data);
        if (requestCode ==CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE){
            CropImage.ActivityResult result=CropImage.getActivityResult(data);
            if (resultCode == RESULT_OK){
                try {
                    cropImage=result.getUri();
                    Bitmap selectedImage = MediaStore.Images.Media.getBitmap(this.getContentResolver(), cropImage);
                    photoprofile.setImageBitmap(selectedImage);
                    String photo = bitmapToBase64(selectedImage);
                    SharedPreferences.Editor editor = prf.edit();
                    editor.putString("Profilephoto",photo);
                    editor.apply();
                    Toast.makeText(this,"Η φωτογραφία ανέβηκε επιτυχώς",Toast.LENGTH_SHORT).show();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            else if(resultCode==CropImage.CROP_IMAGE_ACTIVITY_RESULT_ERROR_CODE){
                Exception e = result.getError();
                Toast.makeText(this,"Πιθανό σφάλμα είναι:"+e,Toast.LENGTH_SHORT).show();
            }
        }

    }

    public Bitmap BitmapDecoder(String stringPicture) {
        byte[] decodedString = Base64.decode(stringPicture, Base64.DEFAULT);
        Bitmap finalphoto = BitmapFactory.decodeByteArray(decodedString, 0, decodedString.length);
        return finalphoto;
    }


    public Bitmap[] CreateImagesforMap(Bitmap[] bitmaps, String[] Categories) {
        Bitmap[] newImages = new Bitmap[bitmaps.length];
        for (int i = 0; i < bitmaps.length; i++) {
            newImages[i] = createUserBitmap(bitmaps[i], Categories[i]);
        }
        Log.e(TAG, "Images Ready");
        return newImages;
    }

    private Bitmap createUserBitmap(Bitmap bitmap, String cat) {
        Bitmap result = null;
        try {
            result = Bitmap.createBitmap(dp(62), dp(76), Bitmap.Config.ARGB_8888);
            result.eraseColor(Color.TRANSPARENT);
            Canvas canvas = new Canvas(result);
            Drawable drawable = null;
            switch (cat) {
                case "MUSIC":
                    drawable = getResources().getDrawable(R.drawable.custom_marker2);
                    break;
                case "GIG":
                    drawable = getResources().getDrawable(R.drawable.custom_marker3);
                    break;
                case "PARTY":
                    drawable = getResources().getDrawable(R.drawable.custom_marker4);
                    break;
                case "SOMETHING":
                    drawable = getResources().getDrawable(R.drawable.custom_marker5);
                    break;
                default:
                    break;
            }
            drawable.setBounds(0, 0, dp(62), dp(76));
            drawable.draw(canvas);

            Paint roundPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
            RectF bitmapRect = new RectF();
            canvas.save();

            if (bitmap != null) {
                BitmapShader shader = new BitmapShader(bitmap, Shader.TileMode.CLAMP, Shader.TileMode.CLAMP);
                Matrix matrix = new Matrix();
                float scale = dp(52) / (float) bitmap.getWidth();
                matrix.postTranslate(dp(5), dp(5));
                matrix.postScale(scale, scale);
                roundPaint.setShader(shader);
                shader.setLocalMatrix(matrix);
                bitmapRect.set(dp(5), dp(5), dp(52 + 5), dp(52 + 5));
                canvas.drawRoundRect(bitmapRect, dp(26), dp(26), roundPaint);
            }
            canvas.restore();
            try {
                canvas.setBitmap(null);
            } catch (Exception e) {
            }
        } catch (Throwable t) {
            t.printStackTrace();
        }
        return result;
    }

    public int dp(float value) {
        if (value == 0) {
            return 0;
        }
        return (int) Math.ceil(getResources().getDisplayMetrics().density * value);
    }

    public void StoreArraylist(Context context, ArrayList<String[]> EVENTS) {
        SharedPreferences sharedPref = context.getSharedPreferences("Get", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPref.edit();
        Gson gson = new Gson();
        String json = gson.toJson(EVENTS);
        editor.putString(PREFS, json);
        editor.commit();
    }

    public ArrayList<String[]> GetArraylist(Context context) {
        SharedPreferences GetsharedPref = context.getSharedPreferences("Get", Context.MODE_PRIVATE);
        Gson gson = new Gson();
        String json = GetsharedPref.getString(PREFS, "");
        Type type = new TypeToken<ArrayList<String[]>>() {
        }.getType();
        ArrayList<String[]> Getevents = gson.fromJson(json, type);
        return Getevents;
    }

    public void starthelp(View view) {
        startActivity(new Intent(Visitor.this, Help.class));
    }

    public void startset(View view) {
        startActivity(new Intent(Visitor.this, Settings.class));
    }

    public void show_popup(View v) {
        PopupMenu popup = new PopupMenu(this, v);
        popup.inflate(R.menu.popup_menu);
        popup.show();
        popup.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                String Path;
                switch (item.getItemId()) {
                    case R.id.music:
                        Category = "MUSIC";
                        GetSelectedEvents(Category);
                        Log.e("TAG", Category);
                        break;
                    case R.id.gig:
                        Category = "GIG";
                        GetSelectedEvents(Category);
                        Log.e("TAG", Category);
                        break;
                    case R.id.party:
                        Category = "PARTY";
                        GetSelectedEvents(Category);
                        Log.e("TAG", Category);
                        break;
                    case R.id.sth:
                        Category = "SOMETHING";
                        GetSelectedEvents(Category);
                        Log.e("TAG", Category);
                        break;
                    default:
                        Category = null;
                }

                return true;
            }
        });
    }


    public void GetEvents() {
        Gson gson = new GsonBuilder()
                .setLenient()
                .create();


        Retrofit fetch = new Retrofit.Builder()
                .baseUrl("https://api.e-events.drosatos.eu/android/")
                .addConverterFactory(GsonConverterFactory.create(gson))
                .build();

        FetchData fetchData = fetch.create(FetchData.class);

        Call<List<Events>> call = fetchData.getData(MainActivity.Token);


        call.enqueue(new Callback<List<Events>>() {
            @Override
            public void onResponse(Call<List<Events>> call, Response<List<Events>> response) {

                if (!response.isSuccessful()) {
                    Log.e(TAG, Integer.toString(response.code()));
                    return;
                }

                Log.e(TAG, Integer.toString(response.code()));
                List<Events> events = response.body();
                Bitmap FinalImage1;
                Events.clear();
                Bitmap[] FinalImages = new Bitmap[20];
                int i = 0;
                ListView mListView = (ListView) findViewById(R.id.listView);
                for (Events event : events) {
                    String[] FinalData = new String[8];
                    FinalData[0] = event.getPhoto();
                    FinalData[1] = event.getAddress();
                    FinalData[2] = event.getDescription();
                    FinalData[3] = event.getTitle();
                    FinalData[4] = event.getDate().split("00")[0];
                    FinalData[5] = event.getLatitude();
                    FinalData[6] = event.getLongitude();
                    FinalData[7] = event.getTime();
                    index[i] = event.getEvent_id();
                    NotificatioN = event.get_Event_notification();
                    Average_rating.add(event.getAver_rating());
                    Events.add(FinalData);
                    EventCategory[i] = event.getCategory();
                    FinalImage1 = BitmapDecoder(FinalData[0]);
                    FinalImage[i] = FinalImage1;
                    i++;
                }
                if (Events.size() == i) {
                    EventListAdapter adapter = new EventListAdapter(Visitor.this, R.layout.adapter_visitor_layout, Events);
                    mListView.setAdapter(adapter);
                }
                Log.e("TAG","Here" + NotificatioN);
                MapImage = CreateImagesforMap(FinalImage, EventCategory);
                mListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                        ButtonPos = parent.getPositionForView(view);
                        Intent intent = new Intent(getApplicationContext(), EventDemo.class);
                        startActivity(intent);
                    }
                });
            }

            @Override
            public void onFailure(Call<List<Events>> call, Throwable t) {
                Log.e(TAG, t.getMessage());
            }
        });

        StoreArraylist(Visitor.this, Events);
    }

    public void GetSelectedEvents(String category) {
        Gson gson = new GsonBuilder()
                .setLenient()
                .create();


        Log.e(TAG,category);

        Retrofit fetch = new Retrofit.Builder() //https://api.e-events.drosatos.eu/android/events/type/MUSIC
                .baseUrl("https://api.e-events.drosatos.eu/android/")
                .addConverterFactory(GsonConverterFactory.create(gson))
                .build();

        FetchData fetchData = fetch.create(FetchData.class);

        Call<List<Events>> call = fetchData.getSelectedData(MainActivity.Token, category);


        call.enqueue(new Callback<List<Events>>() {
            @Override
            public void onResponse(Call<List<Events>> call, Response<List<Events>> response) {

                if (!response.isSuccessful()) {
                    Log.e(TAG, Integer.toString(response.code()));
                    return;
                }

                Log.e(TAG, Integer.toString(response.code()));
                List<Events> events = response.body();
                Bitmap FinalImage1;
                Events.clear();
                Bitmap[] FinalImages = new Bitmap[20];
                int i = 0;
                ListView mListView = (ListView) findViewById(R.id.listView);
                for (Events event : events) {
                    String[] FinalData = new String[8];
                    FinalData[0] = event.getPhoto();
                    FinalData[1] = event.getAddress();
                    FinalData[2] = event.getDescription();
                    FinalData[3] = event.getTitle();
                    FinalData[4] = event.getDate().split("00")[0];
                    FinalData[5] = event.getLatitude();
                    FinalData[6] = event.getLongitude();
                    FinalData[7] = event.getTime();
                    index[i] = event.getEvent_id();
                    Average_rating.add(event.getAver_rating());
                    Events.add(FinalData);
                    EventCategory[i] = event.getCategory();
                    FinalImage1 = BitmapDecoder(FinalData[0]);
                    FinalImage[i] = FinalImage1;
                    i++;
                    //    SetPhoto(FinalImage1);

                }
                if (Events.size() == i) {
                    EventListAdapter adapter = new EventListAdapter(Visitor.this, R.layout.adapter_visitor_layout, Events);
                    mListView.setAdapter(adapter);
                }
                MapImage = CreateImagesforMap(FinalImage, EventCategory);
                mListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                        ButtonPos = parent.getPositionForView(view);
                        Intent intent = new Intent(getApplicationContext(), EventDemo.class);
                        startActivity(intent);
                    }
                });
            }

            @Override
            public void onFailure(Call<List<Events>> call, Throwable t) {
                Log.e(TAG, t.getMessage());
            }
        });

        StoreArraylist(Visitor.this, Events);
    }

    public void GetSelectedEventsDate(String PrevDate, String NextDate) {
        Gson gson = new GsonBuilder()
                .setLenient()
                .create();


        Retrofit fetch = new Retrofit.Builder()
                .baseUrl("https://api.e-events.drosatos.eu/android/")
                .addConverterFactory(GsonConverterFactory.create(gson))
                .build();

        FetchData fetchData = fetch.create(FetchData.class);

        Call<List<Events>> call = fetchData.getSelectedDataDate(MainActivity.Token, PrevDate, NextDate);


        Log.e("TAG", "Ok");
        Log.e("TAG", PrevDate + "  " + NextDate);
        call.enqueue(new Callback<List<Events>>() {
            @Override
            public void onResponse(Call<List<Events>> call, Response<List<Events>> response) {

                if (!response.isSuccessful()) {
                    Log.e(TAG, Integer.toString(response.code()));
                    return;
                }

                Log.e(TAG, Integer.toString(response.code()));
                List<Events> events = response.body();
                Bitmap FinalImage1;
                Events.clear();
                Bitmap[] FinalImages = new Bitmap[20];
                int i = 0;
                ListView mListView = (ListView) findViewById(R.id.listView);
                for (Events event : events) {
                    String[] FinalData = new String[8];
                    FinalData[0] = event.getPhoto();
                    FinalData[1] = event.getAddress();
                    FinalData[2] = event.getDescription();
                    FinalData[3] = event.getTitle();
                    FinalData[4] = event.getDate().split("00")[0];
                    FinalData[5] = event.getLatitude();
                    FinalData[6] = event.getLongitude();
                    FinalData[7] = event.getTime();
                    index[i] = event.getEvent_id();
                    Average_rating.add(event.getAver_rating());
                    Events.add(FinalData);
                    EventCategory[i] = event.getCategory();
                    FinalImage1 = BitmapDecoder(FinalData[0]);
                    FinalImage[i] = FinalImage1;
                    i++;
                    //    SetPhoto(FinalImage1);

                }
                if (Events.size() == i) {
                    EventListAdapter adapter = new EventListAdapter(Visitor.this, R.layout.adapter_visitor_layout, Events);
                    mListView.setAdapter(adapter);
                }
                MapImage = CreateImagesforMap(FinalImage, EventCategory);
                mListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                        ButtonPos = parent.getPositionForView(view);
                        Intent intent = new Intent(getApplicationContext(), EventDemo.class);
                        startActivity(intent);
                    }
                });
            }

            @Override
            public void onFailure(Call<List<Events>> call, Throwable t) {
                Log.e(TAG, t.getMessage());
            }
        });

        StoreArraylist(Visitor.this, Events);
    }

    private void SetDate() {
        final Calendar myCalendar = getInstance();
        Toast.makeText(Visitor.this, "Επιλέξτε αρχική ημερομηνία",Toast.LENGTH_SHORT).show();
        final DatePickerDialog.OnDateSetListener date = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int monthOfYear,
                                  int dayOfMonth) {
                myCalendar.set(YEAR, year);
                myCalendar.set(MONTH, monthOfYear);
                myCalendar.set(DAY_OF_MONTH, dayOfMonth);
                lastDate = Integer.toString(myCalendar.get(YEAR)) + "-" + Integer.toString(myCalendar.get(MONTH) + 1) + "-" + Integer.toString(myCalendar.get(DAY_OF_MONTH));
            Log.e(TAG,lastDate);
            }
        };
        new DatePickerDialog(Visitor.this, date, myCalendar
                .get(YEAR), myCalendar.get(MONTH),
                myCalendar.get(DAY_OF_MONTH)).show();



        final DatePickerDialog.OnDateSetListener date1 = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int monthOfYear,
                                  int dayOfMonth) {
                myCalendar.set(YEAR, year);
                myCalendar.set(MONTH, monthOfYear);
                myCalendar.set(DAY_OF_MONTH, dayOfMonth);
                firstDate = Integer.toString(myCalendar.get(YEAR)) + "-" + Integer.toString(myCalendar.get(MONTH) + 1) + "-" + Integer.toString(myCalendar.get(DAY_OF_MONTH));
            Log.e(TAG,firstDate);
            }
        };
        Toast.makeText(Visitor.this, "Επιλέξτε τελική ημερομηνία",Toast.LENGTH_SHORT).show();
        new DatePickerDialog(Visitor.this, date1, myCalendar
                .get(YEAR), myCalendar.get(MONTH),
                myCalendar.get(DAY_OF_MONTH)).show();
    }
    public void ChooseArea(View v){
        ArrayList<LatLngBounds> Cities = new ArrayList<>();
        Cities.add(0,new LatLngBounds(new LatLng(37.871673, 23.748917), new LatLng(38.072474,23.799418))); //Athens
        Cities.add(1,new LatLngBounds(new LatLng(40.663178, 22.898573), new LatLng(40.669754, 22.970753))); //Salonica
        Cities.add(2,new LatLngBounds(new LatLng(38.199304, 21.717113), new LatLng(38.294458, 21.791002))); //Patra
        Cities.add(3,new LatLngBounds(new LatLng(35.334315, 25.100269), new LatLng(35.337527, 25.164116))); //Heraklion
        Cities.add(4,new LatLngBounds(new LatLng(39.607381, 22.403889), new LatLng(39.652947, 22.408868))); //Larisa
        Cities.add(5,new LatLngBounds(new LatLng(39.363873, 22.914876), new LatLng(39.376215, 22.978047))); //Volos
        Cities.add(6,new LatLngBounds(new LatLng(39.662730, 20.823829), new LatLng(39.685853, 20.826930))); //Ioannina
        Cities.add(7,new LatLngBounds(new LatLng(39.531055, 21.767908), new LatLng(39.576056, 21.769968))); //Trikala
        Cities.add(8,new LatLngBounds(new LatLng(41.072147, 23.540330), new LatLng(41.100353, 23.552346))); //Serres
        Cities.add(9,new LatLngBounds(new LatLng(40.848355, 25.848531), new LatLng(40.855821, 25.897798))); //Alexandroupoli
        Cities.add(10,new LatLngBounds(new LatLng(41.138388, 24.875536), new LatLng(41.144035, 24.920161))); //Xanthi
        Cities.add(11,new LatLngBounds(new LatLng(40.917993, 24.377565), new LatLng(40.949460, 24.445597))); //Kavala
        Cities.add(12,new LatLngBounds(new LatLng(41.105744, 25.393196), new LatLng(41.132893, 25.413924))); //Komotini
        Cities.add(13,new LatLngBounds(new LatLng(40.286277, 21.776965), new LatLng(40.309778, 21.795161))); //Kozani
        Cities.add(14,new LatLngBounds(new LatLng(39.609556, 19.882376), new LatLng(39.630845, 19.883921))); //Kerkyra
        PopupMenu popup = new PopupMenu(this, v);
        popup.inflate(R.menu.city_menu);
        popup.show();
        popup.setOnMenuItemClickListener(item -> {
            switch (item.getItemId()) {
                case R.id.athens:
                     city = Cities.get(0);
                    Log.e("TAG", city.toString());
                    break;
                case R.id.salonica:
                    city = Cities.get(1);
                    Log.e("TAG", city.toString());
                    break;
                case R.id.patras:
                    city = Cities.get(2);
                    Log.e("TAG", city.toString());
                    break;
                case R.id.heraklion:
                    city = Cities.get(3);
                    Log.e("TAG", city.toString());
                    break;
                case R.id.larisa:
                    city = Cities.get(4);
                    Log.e("TAG", city.toString());
                    break;
                case R.id.volos:
                    city = Cities.get(5);
                    Log.e("TAG", city.toString());
                    break;
                case R.id.ioannina:
                    city = Cities.get(6);
                    Log.e("TAG", city.toString());
                    break;
                case R.id.trikala:
                    city = Cities.get(7);
                    Log.e("TAG", city.toString());
                    break;
                case R.id.serres:
                    city = Cities.get(8);
                    Log.e("TAG", city.toString());
                    break;
                case R.id.aleka:
                    city = Cities.get(9);
                    Log.e("TAG", city.toString());
                    break;
                case R.id.xanthi:
                    city = Cities.get(10);
                    Log.e("TAG", city.toString());
                    break;
                case R.id.kavala:
                    city = Cities.get(11);
                    Log.e("TAG", city.toString());
                    break;
                case R.id.komotini:
                    city = Cities.get(12);
                    Log.e("TAG", city.toString());
                    break;
                case R.id.kozani:
                    city = Cities.get(13);
                    Log.e("TAG", city.toString());
                    break;
                case R.id.kerkyra:
                    city = Cities.get(14);
                    Log.e("TAG", city.toString());
                    break;
                default:
                    city = null;
            }

            return true;
        });
    }
    private String bitmapToBase64(Bitmap bitmap) {
        Bitmap photo = Bitmap.createScaledBitmap(bitmap, 300, 500, true); //Ήταν 400x600
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        photo.compress(Bitmap.CompressFormat.PNG, 100, byteArrayOutputStream);
        byte[] byteArray = byteArrayOutputStream.toByteArray();
        return Base64.encodeToString(byteArray, Base64.DEFAULT);
    }
}
