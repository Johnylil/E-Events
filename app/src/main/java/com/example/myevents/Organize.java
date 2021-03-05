package com.example.myevents;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.location.Address;
import android.location.Geocoder;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Handler;
import android.provider.MediaStore;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.PopupMenu;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

import android.os.Bundle;
import android.text.InputType;
import android.text.TextUtils;
import android.util.Base64;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.MultiAutoCompleteTextView;
import android.widget.TimePicker;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;

import static java.util.Calendar.DAY_OF_MONTH;
import static java.util.Calendar.MONTH;
import static java.util.Calendar.YEAR;
import static java.util.Calendar.getInstance;


public class Organize extends AppCompatActivity {
    private ImageView imageView;
    private EditText Area;
    private EditText Description;
    private EditText Title;
    private Button send,reject;
    private MultiAutoCompleteTextView mDescriptionView;
    public String User = MainActivity.UserName;
    public Bitmap FinalImage;
    public String Area2Send;
    public String Description2Send;
    public String Text2Send;
    public String Date2Send;
    public String Time2Send;
    public String Category2Send;
    public static double[] Coordinates = new double[2];
    public JSONArray Array = Visitor.NewArray;
    public JSONObject Object2Send = new JSONObject();
    TimePickerDialog picker;
    private EditText time;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_organize);

        imageView = (ImageView) findViewById(R.id.imageView);
        Area = (EditText) findViewById(R.id.editText);
        Description = (EditText) findViewById(R.id.editText2);
        Title = (EditText) findViewById(R.id.editText3);
        time= (EditText) findViewById(R.id.time);
        final EditText edittext = (EditText) findViewById(R.id.date);
        getSupportActionBar().setDefaultDisplayHomeAsUpEnabled(true);

        reject = (Button) findViewById(R.id.cancel);
        reject.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Title.getText().clear();
                Area.getText().clear();
                Description.getText().clear();
                time.getText().clear();
                edittext.getText().clear();
                CustomCamera.wasON = false;
                Intent intent = new Intent(Organize.this,Organize.class);
                startActivity(intent);
            }
        });


     if(!CustomCamera.wasON) {
         AlertDialog.Builder builder = new AlertDialog.Builder(Organize.this);
         builder.setMessage("Θέλετε τα πεδία να συμπληρωθούν αυτόματα;");
         builder.setPositiveButton("YES", new DialogInterface.OnClickListener() {
             @Override
             public void onClick(DialogInterface dialog, int id) {
                 //  finish();
                 Log.e("TAG", "Opened");
                 Intent intent = new Intent(Organize.this, CustomCamera.class);
                 startActivity(intent);
             }
         });
         builder.setNegativeButton("NO", new DialogInterface.OnClickListener() {
             @Override
             public void onClick(DialogInterface dialog, int id) {
                 dialog.cancel();
             }
         });
         AlertDialog alert = builder.create();
         alert.show();
     }

        if(CustomCamera.wasON)
        {
            if(CustomCamera.DataTaken[0] !=null ) {
                Description.append(CustomCamera.DataTaken[0]);
            }
            if(CustomCamera.DataTaken[1] !=null) {
                Title.append(CustomCamera.DataTaken[1]);
            }
            if(CustomCamera.DataTaken[3] !=null) {
                time.append(CustomCamera.DataTaken[3]);
            }
            if(CustomCamera.DataTaken[2] !=null) {
                edittext.append(CustomCamera.DataTaken[2]);
            }
            imageView.setImageBitmap(CustomCamera.loadedImage);
            imageView.setRotation(90);
        }

        send = (Button) findViewById(R.id.button5);
        send.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                boolean cancel = false;
                View focusView = null;
                if (TextUtils.isEmpty(Title.getText())) {
                    Title.setError("Υποχρεωτικό πεδίο!");
                    focusView = Title;
                    cancel = true;
                }

                if (TextUtils.isEmpty(Area.getText())) {
                    Area.setError("Υποχρεωτικό πεδίο!");
                    focusView = Area;
                    cancel = true;
                }

                if (TextUtils.isEmpty(Description.getText())) {
                    Description.setError("Υποχρεωτικό πεδίο!");
                    focusView = Description;
                    cancel = true;
                }

                if(imageView.getDrawable() == null || FinalImage ==null){
                    Toast.makeText(Organize.this,"Παρακαλούμε προσθέστε μια φωτογραφία",Toast.LENGTH_SHORT).show();
                    cancel= true;
                }

                if (cancel) {
                    // There was an error; don't attempt login and focus the first
                    // form field with an error.
                    if (focusView != null) {
                        focusView.requestFocus();
                    }
                }
                Area2Send = Area.getText().toString();
                Description2Send = Description.getText().toString();
                Text2Send = Title.getText().toString();
                Log.e("TAG","Ready2Send");
                if(FinalImage !=null){
                Events newEvent = new Events(Area2Send,bitmapToBase64(FinalImage),Description2Send,
                        Text2Send,Date2Send,Double.toString(Coordinates[0]),Double.toString(Coordinates[1]),Category2Send,Time2Send);
                SendEvent(newEvent);
            }}
        });

        Button camera = (Button) findViewById(R.id.button6);
        camera.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                selectImage(Organize.this);
            }
        });

        time.setInputType(InputType.TYPE_NULL);
        time.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final Calendar cldr = Calendar.getInstance();
                int hour = cldr.get(Calendar.HOUR_OF_DAY);
                int minutes = cldr.get(Calendar.MINUTE);
                // time picker dialog
                picker = new TimePickerDialog(Organize.this,
                        new TimePickerDialog.OnTimeSetListener() {
                            @Override
                            public void onTimeSet(TimePicker tp, int sHour, int sMinute) {
                                time.setText(sHour + ":" + sMinute + ":" + "00");
                                Time2Send = sHour + ":" + sMinute + ":" + "00";
                                Log.e("TAG",Time2Send);
                            }
                        }, hour, minutes,true);
                picker.show();
            }
        });

        final Calendar myCalendar = getInstance();
        final DatePickerDialog.OnDateSetListener date = new DatePickerDialog.OnDateSetListener() {
            private void updateLabel() {

                String myFormat = "yy/MM/dd"; //In which you need put here
                SimpleDateFormat sdf = new SimpleDateFormat(myFormat, Locale.ENGLISH);

                edittext.setText(sdf.format(myCalendar.getTime()));
            }
            @Override
            public void onDateSet(DatePicker view, int year, int monthOfYear,
                                  int dayOfMonth) {
                myCalendar.set(YEAR, year);
                myCalendar.set(MONTH, monthOfYear);
                myCalendar.set(DAY_OF_MONTH, dayOfMonth);
                updateLabel();
                Date2Send = Integer.toString(myCalendar.get(YEAR)) + "-" + Integer.toString(myCalendar.get(MONTH) + 1) + "-" + Integer.toString(myCalendar.get(DAY_OF_MONTH));
                Log.e("TAG",Date2Send);
            }
        };

        edittext.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                new DatePickerDialog(Organize.this, date, myCalendar
                        .get(YEAR), myCalendar.get(MONTH),
                        myCalendar.get(DAY_OF_MONTH)).show();
                Coordinates = getAddressFromLocation(Area.getText().toString(),Organize.this);
            }
        });


    } //Τέλος OnCreate

    private void selectImage(Context context) {
        final CharSequence[] options = {"Take Photo", "Choose from Gallery", "Cancel"};

        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setTitle("Choose your profile picture");

        builder.setItems(options, new DialogInterface.OnClickListener() {

            @Override
            public void onClick(DialogInterface dialog, int item) {

                if (options[item].equals("Take Photo")) {
                    Intent takePicture = new Intent(android.provider.MediaStore.ACTION_IMAGE_CAPTURE);
                    startActivityForResult(takePicture, 0);

                } else if (options[item].equals("Choose from Gallery")) {
                    Intent pickPhoto = new Intent(Intent.ACTION_PICK, android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                    startActivityForResult(pickPhoto, 1);

                } else if (options[item].equals("Cancel")) {
                    dialog.dismiss();
                }
            }
        });
        builder.show();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode != RESULT_CANCELED) {
            switch (requestCode) {
                case 0:
                    if (resultCode == RESULT_OK && data != null) {
                        Bitmap selectedImage = (Bitmap) data.getExtras().get("data");
                        imageView.setImageBitmap(selectedImage);
                        FinalImage = selectedImage;
                    }

                    break;
                case 1:
                    if (resultCode == RESULT_OK && data != null) {
                        Uri selectedImage = data.getData();
                        String[] filePathColumn = {MediaStore.Images.Media.DATA};
                        if (selectedImage != null) {
                            Cursor cursor = getContentResolver().query(selectedImage,
                                    filePathColumn, null, null, null);
                            if (cursor != null) {
                                cursor.moveToFirst();

                                int columnIndex = cursor.getColumnIndex(filePathColumn[0]);
                                String picturePath = cursor.getString(columnIndex);
                                imageView.setImageBitmap(BitmapFactory.decodeFile(picturePath));
                                cursor.close();
                                FinalImage = BitmapFactory.decodeFile(picturePath);
                            }
                        }

                    }
                    break;
            }
        }
    }

    public void show_popup(View v) {
        EditText cat = findViewById(R.id.Category);
        PopupMenu popup = new PopupMenu(this, v);
        popup.inflate(R.menu.popup_menu);
        popup.show();
        popup.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                String Path;
                switch (item.getItemId()) {
                    case R.id.music:
                        Category2Send = "MUSIC";
                        Log.e("TAG", Category2Send);
                        break;
                    case R.id.gig:
                        Category2Send = "GIG";
                        Log.e("TAG", Category2Send);
                        break;
                    case R.id.party:
                        Category2Send = "PARTY";
                        Log.e("TAG", Category2Send);
                        break;
                    case R.id.sth:
                        Category2Send = "SOMETHING";
                        Log.e("TAG", Category2Send);
                        break;
                    default:
                        Category2Send = null;
                }
                cat.append(Category2Send);
                return true;
            }
        });
    }

    private String bitmapToBase64(Bitmap bitmap) {
        Bitmap photo = Bitmap.createScaledBitmap(bitmap, 300, 500, true); //Ήταν 400x600
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        photo.compress(Bitmap.CompressFormat.PNG, 100, byteArrayOutputStream);
        byte[] byteArray = byteArrayOutputStream.toByteArray();
        return Base64.encodeToString(byteArray, Base64.DEFAULT);
    }


    public double[] getAddressFromLocation(final String locationAddress,
                                              final Context context) {
        double[] Coords = new double[2];
        Thread thread = new Thread() {
            @Override
            public void run() {
                Geocoder geocoder = new Geocoder(context, Locale.getDefault());
                String result = null;
                try {
                    List
                            addressList = geocoder.getFromLocationName(locationAddress, 1);
                    if (addressList != null && addressList.size() > 0) {
                        Address address = (Address) addressList.get(0);
                        Coords[0] = address.getLatitude();
                        Coords[1] = address.getLongitude();
                        StringBuilder sb = new StringBuilder();
                        sb.append(address.getLatitude()).append("\n");
                        sb.append(address.getLongitude()).append("\n");
                        result = sb.toString();
                        Log.e("TAG",result);
                    }
                } catch (IOException e) {
                    Log.e("TAG", "Unable to connect to Geocoder", e);
                }

            }
        };
        thread.start();
        return Coords;
    }

    public void SendEvent(Events events)
    {
        Retrofit sendevent = new Retrofit.Builder()
                .baseUrl("https://api.e-events.drosatos.eu/android/")
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        FetchData sendEvent = sendevent.create(FetchData.class);

        Call<List<Events>> Uploadevent = sendEvent.UploadEvent(MainActivity.Token,events);


        Uploadevent.enqueue(new Callback<List<Events>>() {
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