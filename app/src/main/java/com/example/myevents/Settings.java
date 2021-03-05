package com.example.myevents;


import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;
import androidx.appcompat.app.AppCompatActivity;

import android.preference.PreferenceManager;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.Switch;


public class Settings extends AppCompatActivity {
    Switch simpleswitch1;
    Switch simpleswitch2;
    private Notification notification;
    NotificationManager manager;
    Notification myNotication;
    SharedPreferences prf;
    boolean enableSound = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);
        prf = PreferenceManager.getDefaultSharedPreferences(this);
        SharedPreferences.Editor editor = prf.edit();
        simpleswitch1 = (Switch) findViewById(R.id.simpleswitch1);
        simpleswitch2 = (Switch) findViewById(R.id.simpleswitch2);
        String notificationsenabled = prf.getString("Notifenabled", "");
        String soundenabled = prf.getString("Soundenabled","");
        if(notificationsenabled.equals("ON") || notificationsenabled.equals("Ενεργό"))
        {
            simpleswitch1.setChecked(true);
        }else if(notificationsenabled.equals("OFF") || notificationsenabled.equals("Ανενεργό"))
            {simpleswitch1.setChecked(false);}
        if(soundenabled.equals("ON") || soundenabled.equals("Ενεργό"))
        {
            simpleswitch2.setChecked(true);
        }else if (notificationsenabled.equals("OFF") || notificationsenabled.equals("Ανενεργό")){ simpleswitch2.setChecked(false);}


                simpleswitch1.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                    @TargetApi(Build.VERSION_CODES.P)
                    @Override
                    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                        if (isChecked && !(notificationsenabled.equals("ON") || notificationsenabled.equals("Ενεργό"))) {
                            editor.putString("Notifenabled",simpleswitch1.getTextOn().toString());
                            editor.apply();
                           Log.e("TAG",simpleswitch1.getTextOn().toString());}
                           else
                            editor.putString("Notifenabled","OFF");
                            editor.apply();
                        Log.e("TAG",simpleswitch1.getTextOn().toString());
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

                            Intent intent = new Intent(Settings.this, Visitor.class);
                            intent.putExtra("yourpackage.notifyId", notifyID);
                            PendingIntent pIntent = PendingIntent.getActivity(Settings.this, 0, intent,
                                    PendingIntent.FLAG_UPDATE_CURRENT);


                            // Select the correct notification channel
                            String selectedChannelId;
                            if (enableSound) {
                                selectedChannelId = CHANNEL_SOUND_ID;
                            } else {
                                selectedChannelId = CHANNEL_SILENT_ID;
                            }

                            // Create a notification and set the notification channel.
                            NotificationCompat.Builder notificationBuilder = new NotificationCompat.Builder(Settings.this, selectedChannelId);
                            notificationBuilder.setSmallIcon(R.drawable.cheers);
                            notificationBuilder.setContentTitle("e-Events");
                            notificationBuilder.setContentText("Υπάρχει μια νέα εκδήλωση κοντά σας!");
                            notificationBuilder.setContentIntent(pIntent);
                            notificationBuilder.setChannelId(selectedChannelId);

                            Notification notification = notificationBuilder.build();
                            mNotificationManager.notify(notifyID, notification);

                        }});
                simpleswitch2.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                    @Override
                    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                        if(!(soundenabled.equals("ON") || soundenabled.equals("Ενεργό")) && isChecked) {
                            editor.putString("Soundenabled", simpleswitch2.getTextOn().toString());
                            editor.apply();
                            enableSound = isChecked;
                        }else
                            editor.putString("Soundenabled", "OFF");
                            editor.apply();
                            enableSound = !(isChecked);
                    }
                });


            }


    }