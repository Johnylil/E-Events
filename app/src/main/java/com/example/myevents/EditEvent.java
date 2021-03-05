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
import android.os.Bundle;
import android.provider.MediaStore;
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

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.PopupMenu;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

import static java.util.Calendar.DAY_OF_MONTH;
import static java.util.Calendar.MONTH;
import static java.util.Calendar.YEAR;
import static java.util.Calendar.getInstance;

public class EditEvent extends AppCompatActivity {
    private ImageView ImageView1;
    private EditText AreaEdit;
    private EditText DescriptionEdit;
    private EditText TitleEdit;
    public ArrayList<String[]> EditEvent = new ArrayList<>(OldUserEvents.OldEvents);
    int position;
    private Button send;
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
    TimePickerDialog picker;
    private EditText time,cat;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            position = extras.getInt("Position");
        }
        setContentView(R.layout.activity_editevent);
        Log.e("TAG",Integer.toString(position));
        ImageView1 = (ImageView) findViewById(R.id.imageViewEdit);
        AreaEdit = (EditText) findViewById(R.id.editTextEdit);
        DescriptionEdit = (EditText) findViewById(R.id.editTextEdit2);
        TitleEdit = (EditText) findViewById(R.id.editTextEdit3);
        time= (EditText) findViewById(R.id.timeedit);
        cat = (EditText)findViewById(R.id.categoryedit);
        String[] OldEvent = EditEvent.get(position);
      //  Bitmap photo = BitmapDecoder(OldEvent[1]);
        FinalImage = BitmapDecoder(OldEvent[0]);
        Log.e("TAG", OldEvent[1] + " " + OldEvent[2] + " " + OldEvent[3] + " " + OldEvent[4]);
        AreaEdit.append(OldEvent[1]);
        DescriptionEdit.append(OldEvent[2]);
        TitleEdit.append(OldEvent[3]);
        time.append(OldEvent[7]);
        cat.append(OldEvent[8]);
        ImageView1.setImageBitmap(FinalImage);
        send = (Button) findViewById(R.id.button5);
        send.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                boolean cancel = false;
                View focusView = null;
                if (TextUtils.isEmpty(TitleEdit.getText())) {
                    TitleEdit.setError("Υποχρεωτικό πεδίο!");
                    focusView = TitleEdit;
                    cancel = true;
                }

                if (TextUtils.isEmpty(AreaEdit.getText())) {
                    AreaEdit.setError("Υποχρεωτικό πεδίο!");
                    focusView = AreaEdit;
                    cancel = true;
                }

                if (TextUtils.isEmpty(DescriptionEdit.getText())) {
                    DescriptionEdit.setError("Υποχρεωτικό πεδίο!");
                    focusView = DescriptionEdit;
                    cancel = true;
                }

                if (cancel) {
                    // There was an error; don't attempt login and focus the first
                    // form field with an error.
                    focusView.requestFocus();
                }
                Area2Send = AreaEdit.getText().toString();
                Description2Send = DescriptionEdit.getText().toString();
                Text2Send = TitleEdit.getText().toString();
                Time2Send = time.getText().toString();
                Log.e("TAG", Area2Send + " " + Description2Send + " " + Text2Send);
                Log.e("TAG","Ready2Send");
                Events newEvent = new Events(Area2Send,bitmapToBase64(FinalImage),Description2Send,
                        Text2Send,Date2Send,Double.toString(Coordinates[0]),Double.toString(Coordinates[1]),Category2Send,Time2Send);
                SendEvent(newEvent);
                //  String2JSon();
                //  new Send2Server().execute("https://api.jsonbin.io/b/5e8cc918ff9c906bdf1d9c6a", Array.toString());
            }
        });

        Button camera = (Button) findViewById(R.id.button6);
        camera.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                selectImage(EditEvent.this);
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
                picker = new TimePickerDialog(EditEvent.this,
                        new TimePickerDialog.OnTimeSetListener() {
                            @Override
                            public void onTimeSet(TimePicker tp, int sHour, int sMinute) {
                                time.setText(sHour + ":" + sMinute + ":" + "00");
                            }
                        }, hour, minutes,true);
                picker.show();
            }
        });

        final Calendar myCalendar = getInstance();

        final EditText edittext = (EditText) findViewById(R.id.dateEdit);

        edittext.append(OldEvent[4]);

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
            }

        };

        edittext.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                new DatePickerDialog(EditEvent.this, date, myCalendar
                        .get(YEAR), myCalendar.get(MONTH),
                        myCalendar.get(DAY_OF_MONTH)).show();
                Coordinates = getAddressFromLocation(AreaEdit.getText().toString(),EditEvent.this);
            }
        });

        Date2Send = Integer.toString(myCalendar.get(YEAR)) + "-" + Integer.toString(myCalendar.get(MONTH)) + "-" + Integer.toString(myCalendar.get(DAY_OF_MONTH));

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
                        ImageView1.setImageBitmap(selectedImage);
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
                                ImageView1.setImageBitmap(BitmapFactory.decodeFile(picturePath));
                                cursor.close();
                                FinalImage = BitmapFactory.decodeFile(picturePath);
                            }
                        }

                    }
                    break;
            }
        }
    }

    private String bitmapToBase64(Bitmap bitmap) {
        Bitmap photo = Bitmap.createScaledBitmap(bitmap, 300, 500, true); //Ήταν 400x600
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        photo.compress(Bitmap.CompressFormat.PNG, 100, byteArrayOutputStream);
        byte[] byteArray = byteArrayOutputStream.toByteArray();
        return Base64.encodeToString(byteArray, Base64.DEFAULT);
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


    public double[] getAddressFromLocation(final String locationAddress,
                                           final Context context) {
        double[] Coords = new double[2];
        Thread thread = new Thread() {
            @Override
            public void run() {
                Geocoder geocoder = new Geocoder(context, Locale.getDefault());
                String result = null;
                try {
                    List addressList = geocoder.getFromLocationName(locationAddress, 1);
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

    public Bitmap BitmapDecoder(String stringPicture) {
        byte[] decodedString = Base64.decode(stringPicture, Base64.DEFAULT);
        Bitmap finalphoto = BitmapFactory.decodeByteArray(decodedString, 0, decodedString.length);
        return finalphoto;
    }

    public void SendEvent(Events events)
    {
        Retrofit sendevent = new Retrofit.Builder()
                .baseUrl("https://api.e-events.drosatos.eu/android/")
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        FetchData sendEvent = sendevent.create(FetchData.class);

        Call<List<Events>> Uploadevent = sendEvent.UpdateEvent(OldUserEvents.id,MainActivity.Token,events);


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