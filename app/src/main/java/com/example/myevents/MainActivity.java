package com.example.myevents;

import android.Manifest;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.Paint;
import android.os.AsyncTask;

import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

import android.os.Bundle;
import android.os.Handler;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import java.io.IOException;
import java.util.Base64;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


public class MainActivity extends AppCompatActivity {
    private int CAMERA_PERMISSION_CODE = 1;

    public static String Password;
    public static String UserName;
    public static String Token;
    private EditText mUserNameView;
    private EditText mPasswordView;
    private View mProgressView;
    private View mLoginFormView;
    public static Map<String,String> Visitor = new HashMap<>();
    public static Map<String,String> VisitorMail = new HashMap<>();
    SharedPreferences pref;
    Intent intent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        findViewById(R.id.loadingPanel).setVisibility(View.GONE);
        pref = PreferenceManager.getDefaultSharedPreferences(this);//getSharedPreferences("user_details",MODE_PRIVATE);
        if(pref.contains("token")){
            intent = new Intent(MainActivity.this,Visitor.class);
            startActivity(intent);
        }
        if (ContextCompat.checkSelfPermission(MainActivity.this,
                Manifest.permission.CAMERA) == PackageManager.PERMISSION_GRANTED) {
            Toast.makeText(MainActivity.this, "You have already granted this permission!",
                    Toast.LENGTH_SHORT).show();
        } else {
            cameraPermission();
        }

        // Set up the login form.
        mUserNameView = (EditText) findViewById(R.id.txtUserName);
        mPasswordView = (EditText) findViewById(R.id.password);

        // mTextViewResult = findViewById(R.id.text_view_result); //Για επαλήθευση

        TextView signin = findViewById(R.id.textView8);
        signin.setPaintFlags(signin.getPaintFlags() | Paint.UNDERLINE_TEXT_FLAG);



        Button btnLogin = (Button) findViewById(R.id.btnLogin);
        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                findViewById(R.id.loadingPanel).setVisibility(View.VISIBLE);
                UserName=mUserNameView.getText().toString();
                Password=mPasswordView.getText().toString();
                String TextSend = UserName + ":" + Password;
                byte[] encodedBytes = Base64.getEncoder().encode(TextSend.getBytes());
                String Basedtext = new String(encodedBytes);
                Log.e("TAG",Basedtext);
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
                Retrofit retrieve = new Retrofit.Builder()
                        .baseUrl("https://api.e-events.drosatos.eu/android/")
                        .client(okHttpClient)
                        .addConverterFactory(GsonConverterFactory.create())
                        .build();

                FetchData getToken = retrieve.create(FetchData.class);
                String header2 = "application/json";
                Log.e("TAG","Welcome");
                String NewHeader = "Basic" + " " + Basedtext;
                Call<List<String>> load = getToken.GetToken(NewHeader);


                load.enqueue(new Callback<List<String>>() {
                    @Override
                    public void onResponse(Call<List<String>> call, Response<List<String>> response) {

                        if (!response.isSuccessful()) {
                            Log.e("TAG", Integer.toString(response.code()));
                            return;
                        }

                        List<String> Response= response.body();
                        if (Response != null) {
                            for (String ResponseNEW : Response) {
                                Token = ResponseNEW;
                                Log.e("TAG", response.headers().toString());
                                Log.e("TAG", "This is the " + Token);
                            }
                        }
                    }

                    @Override
                    public void onFailure(Call<List<String>> call, Throwable t) {
                        Log.e("TAG", t.getMessage());
                        Log.e("TAG","SKATA");
                    }
                });
                String Pswd=mPasswordView.getText().toString();
                new Handler().postDelayed(() -> {
                    if (!Token.isEmpty() || !pref.getString("token","").isEmpty()){ //Authentication με Map
                        SharedPreferences.Editor editor = pref.edit();
                        editor.putString("username",UserName);
                        editor.putString("token", Token);
                        editor.apply();
                        Intent MainIntent= new Intent(MainActivity.this , Visitor.class);
                        startActivity(MainIntent);
                        findViewById(R.id.loadingPanel).setVisibility(View.GONE);
                        Toast.makeText(MainActivity.this, "Συνδεθήκατε επιτυχώς.", Toast.LENGTH_SHORT).show();

                    }
                    else {
                        Toast.makeText(MainActivity.this, "Το UserName ή ο κωδικός είναι λάθος.", Toast.LENGTH_SHORT).show();
                    }
                },4000);
           }
        });

        mLoginFormView = findViewById(R.id.login_form);
        mProgressView = findViewById(R.id.login_progress);
    }

    private void cameraPermission() {
        if (ActivityCompat.shouldShowRequestPermissionRationale(this,
                Manifest.permission.CAMERA)) {

            new AlertDialog.Builder(this)
                    .setTitle("Permission needed")
                    .setMessage("This permission is needed because of this and that")
                    .setPositiveButton("ok", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            ActivityCompat.requestPermissions(MainActivity.this,
                                    new String[] {Manifest.permission.CAMERA}, CAMERA_PERMISSION_CODE);
                        }
                    })
                    .setNegativeButton("cancel", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.dismiss();
                        }
                    })
                    .create().show();

        } else {
            ActivityCompat.requestPermissions(this,
                    new String[] {Manifest.permission.READ_EXTERNAL_STORAGE}, CAMERA_PERMISSION_CODE);
        }
    }



    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if (requestCode == CAMERA_PERMISSION_CODE)  {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                Toast.makeText(this, "Permission GRANTED", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(this, "Permission DENIED", Toast.LENGTH_SHORT).show();
            }
        }
    }


    public void register(View view){
        startActivity(new Intent(this, Register.class));
    }


}
