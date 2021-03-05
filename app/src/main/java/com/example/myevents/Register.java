package com.example.myevents;

import java.lang.*;

import android.content.Intent;

import androidx.appcompat.app.AppCompatActivity;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import android.widget.RadioGroup;
import java.util.List;


public class Register extends AppCompatActivity {

    private static final String TAG = "RegisterActivity";



    private EditText signupInputName, signupInputEmail, signupInputPassword ; //Πλαίσια εγγραφής
    private RadioGroup genderRadioGroup;
    private static String Name;
    private static String Password;
    private static String Email;
    SharedPreferences pref;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);  //Ένωση της κλάσης με το αντίστοιχο xml αρχείο που περιέχει το view της κλάσης
        pref = PreferenceManager.getDefaultSharedPreferences(this);
        final Button btnLogin = (Button) findViewById(R.id.btnLogin2);
        btnLogin.setOnClickListener(new OnClickListener() {

            public void onClick(View view) {
                signupInputName = (EditText) findViewById(R.id.signup_input_name);  //Αρχικοποίσηση EditTexts βάσει του διαθέσιμου view
                signupInputEmail = (EditText) findViewById(R.id.signup_input_email);
                signupInputPassword = (EditText) findViewById(R.id.signup_input_password);
                genderRadioGroup = (RadioGroup) findViewById(R.id.gender_radio_group);
                btnLogin.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Name = signupInputName.getText().toString();  //Λήψη κειμένου από τα EditTexts και αποθήκευσή του σε μορφή String
                        Password = signupInputPassword.getText().toString();
                        Email = signupInputEmail.getText().toString();
                        SharedPreferences.Editor editor = pref.edit();
                        editor.putString("email",Email);
                        editor.apply();
                        if (Name.isEmpty() || Password.isEmpty() || Email.isEmpty()) { //Σε περίπτωση που ο χρήστης έχει ξεχάσει να συμπληρώσει κάποιο πεδίο
                            Toast.makeText(Register.this, "Παρακαλώ συμπληρώστε τα κενά πεδία.", Toast.LENGTH_SHORT).show();
                        } else {
                            User newUser = new User(Name,Password,Email,submitForm());  //Δημιουργία Αντικειμένου User
                            SendUser(newUser); //Αποστολή User
                            Intent intent = new Intent(getApplicationContext(), MainActivity.class); //Μεταφορά στην αρχική οθόνη
                            startActivity(intent);

                            Toast.makeText(Register.this, "Επιτυχής Δημιουργία Λογαριασμού.", Toast.LENGTH_SHORT).show();
                        }
                    }
                });

            }

            private String submitForm() { //Επιλογή Φύλου και αποθήκευση στη μεταβλητή Gender

                String Gender = "";
                int selectedId = genderRadioGroup.getCheckedRadioButtonId();
                if (selectedId == R.id.female_radio_btn)
                    Gender = "Female";
                else
                    Gender = "Male";

                return Gender;
            }
        });
    }


    private void SendUser(User user)          //Συνάρτηση upload user
    {
        Retrofit send = new Retrofit.Builder()
                .baseUrl("https://api.e-events.drosatos.eu/android/") //URL στο οποίο θα σταλούν τα δεδομένα
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        FetchData sendUser = send.create(FetchData.class);

        Call<List<User>> Upload = sendUser.UploadUser(user);  //Σύνδεση με τη κλάση FetchData


        Upload.enqueue(new Callback<List<User>>() {
            @Override
            public void onResponse(Call<List<User>> call, Response<List<User>> response) {

                if (!response.isSuccessful()) {
                    Log.e(TAG,Integer.toString(response.code()));
                    return;
                }
                Log.e(TAG,response.toString());
                }

            @Override
            public void onFailure(Call<List<User>> call, Throwable t) {
                Log.e(TAG,t.getMessage());
            }
        });
    }
    }
