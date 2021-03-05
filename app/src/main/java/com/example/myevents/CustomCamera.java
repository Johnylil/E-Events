package com.example.myevents;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.os.Handler;
import android.util.Base64;
import android.util.Log;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.google.android.gms.vision.CameraSource;
import com.google.android.gms.vision.Detector;
import com.google.android.gms.vision.face.Face;
import com.google.android.gms.vision.face.FaceDetector;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class CustomCamera extends AppCompatActivity {

    SurfaceView surfaceView;
    CameraSource Camera;
    FaceDetector detector;
    Button sendButton;
    public static Bitmap loadedImage;
    public static String[] DataTaken = new String[4];
    public static Boolean wasON = false;
    private static final int MY_CAMERA_REQUEST_CODE = 100;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.layout_custom_camera);
        surfaceView = (SurfaceView) findViewById(R.id.MyCamera);
        detector = new FaceDetector.Builder(this).build();
        Camera = new CameraSource.Builder(this, detector)
                .setAutoFocusEnabled(true)
                .setRequestedPreviewSize(640, 480)
                .setFacing(CameraSource.CAMERA_FACING_BACK)
                .setRequestedFps(8.0f)
                .build();

        surfaceView.getHolder().addCallback(new SurfaceHolder.Callback() {
            @Override
            public void surfaceCreated(SurfaceHolder surfaceHolder) {
                try {
                    if (checkSelfPermission(Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
                        ActivityCompat.requestPermissions(CustomCamera.this, new String[] {Manifest.permission.CAMERA},MY_CAMERA_REQUEST_CODE);
                        return;
                    }
                    Camera.start(surfaceHolder);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }


            @Override
            public void surfaceChanged(SurfaceHolder surfaceHolder, int i, int i1, int i2) {

            }

            @Override
            public void surfaceDestroyed(SurfaceHolder surfaceHolder) {
                Camera.stop();
            }
        });
        detector.setProcessor(new Detector.Processor<Face>() {
            @Override
            public void release() {
            }

            @Override
            public void receiveDetections(Detector.Detections<Face> detections) {
            }
        });

        sendButton = (Button) findViewById(R.id.sendbutton);
        sendButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Camera.takePicture(null, new CameraSource.PictureCallback() {
                    @Override
                    public void onPictureTaken(byte[] bytes) {
                        loadedImage = BitmapFactory.decodeByteArray(bytes, 0,
                                bytes.length);
                       String myImage = bitmapToBase64(loadedImage);
                        Events events = new Events(myImage);
                        sendImage(events);
                        Toast.makeText(CustomCamera.this,"Περιμένετε...",Toast.LENGTH_LONG).show();
                        Log.e("TAG",myImage);
                        new Handler().postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                wasON = true;
                                Intent intent = new Intent(CustomCamera.this,Organize.class);
                                startActivity(intent);
                            }
                        },7000);
                    }
                });
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

    public void sendImage(Events events)
    {
        {
            HttpLoggingInterceptor loggingInterceptor = new HttpLoggingInterceptor();
            loggingInterceptor.setLevel(HttpLoggingInterceptor.Level.BODY);

            OkHttpClient okHttpClient = new OkHttpClient.Builder()
                    .addInterceptor(new Interceptor() {
                        @Override
                        public okhttp3.Response intercept(Chain chain) throws IOException {
                            Request originalRequest = chain.request();

                            Request newRequest = originalRequest.newBuilder()
                                    .header("Interceptor-Header", "xyz")
                                    .build();

                            return chain.proceed(newRequest);
                        }
                    })
                    .addInterceptor(loggingInterceptor)
                    .build();

            Retrofit sendimage = new Retrofit.Builder()
                    .baseUrl("https://api.e-events.drosatos.eu/android/")
                    .addConverterFactory(GsonConverterFactory.create())
                    .client(okHttpClient)
                    .build();

            FetchData sendIMAGE = sendimage.create(FetchData.class);

            Call<List<Events>> Uploadimage = sendIMAGE.Uploadimage(MainActivity.Token,events);


            Uploadimage.enqueue(new Callback<List<Events>>() {
                @Override
                public void onResponse(Call<List<Events>> call, Response<List<Events>> response) {

                    if (!response.isSuccessful()) {
                        Log.e("TAG",Integer.toString(response.code()));
                        return;
                    }
                    List<Events> events = response.body();
                    for (Events event : events) {
                        DataTaken[0] = event.getDescription();
                        DataTaken[1] = event.getTitle();
                        DataTaken[2] = event.getDate().split("00")[0];
                        DataTaken[3] = event.getTime();

                        Log.e("TAG", DataTaken[0] + " " + DataTaken[1]);
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
}
