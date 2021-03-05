package com.example.myevents;

import androidx.appcompat.app.AppCompatActivity;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.util.ArrayList;
import java.util.List;

public class EventDemo extends AppCompatActivity {

    RatingBar mRatingBar;
    TextView mRatingScale;
    EditText mFeedback;
    Button mSendFeedback;
    String[] demoevent = Visitor.Events.get(Visitor.ButtonPos);
    public Bitmap IMAGE = Visitor.FinalImage[Visitor.ButtonPos];
    public String TITLE = demoevent[3];
    public String DESCRIPTION = demoevent[2];
    public String comment;
    public String rating;
    public int id = Visitor.index[Visitor.ButtonPos];
    public static ArrayList<String> Event_Comments = new ArrayList<>();
    public static ArrayList<String> Event_Users = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_event_demo);
        ImageView Image = findViewById(R.id.imageView4);
        Image.setImageBitmap(IMAGE);
        TextView Title = findViewById(R.id.textView11);
        Title.setText(TITLE);
        TextView Description = findViewById(R.id.textView12);
        Description.setText(DESCRIPTION);

        mRatingBar = (RatingBar) findViewById(R.id.ratingBar);
        mRatingScale = (TextView) findViewById(R.id.tvRatingScale);
        mFeedback = (EditText) findViewById(R.id.etFeedback);
        mSendFeedback = (Button) findViewById(R.id.btnSubmit);

        TextView textView = (TextView) findViewById(R.id.averageRating);
        textView.setText("Μέση Βαθμολογία:" + " " + Float.toString(Visitor.Average_rating.get(id-1)));
        GetComments();

        mRatingBar.setOnRatingBarChangeListener(new RatingBar.OnRatingBarChangeListener() {
            @Override
            public void onRatingChanged(RatingBar ratingBar, float v, boolean b) {
                mRatingScale.setText(String.valueOf(v));
                switch ((int) ratingBar.getRating()) {
                    case 1:
                        mRatingScale.setText("Κακό");
                        break;
                    case 2:
                        mRatingScale.setText("Χρειάζεται βελτίωση");
                        break;
                    case 3:
                        mRatingScale.setText("Μέτριο");
                        break;
                    case 4:
                        mRatingScale.setText("Καλό");
                        break;
                    case 5:
                        mRatingScale.setText("Φανταστικό!");
                        break;
                    default:
                        mRatingScale.setText("");
                }
            }
        });


        mSendFeedback.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (mRatingBar.getRating() == 0.0) {
                    Toast.makeText(EventDemo.this, "Παρακαλώ καταχωρήστε μια βαθμολογία", Toast.LENGTH_LONG).show();
                } else {
                    //mFeedback.setText("");
                    Toast.makeText(EventDemo.this, "Ευχαριστούμε για τη βαθμολογία σας!", Toast.LENGTH_SHORT).show();
                    float rate = mRatingBar.getRating();
                    rating = Integer.toString((int)rate);
                    comment = mFeedback.getText().toString();
                }
                Log.e("TAG", comment + " " + rating);
                Events events = new Events(rating, comment);
                SendEvent(events);

            }
        });
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

    }

    public void SendEvent(Events events) {
        Retrofit sendrate = new Retrofit.Builder()
                .baseUrl("https://api.e-events.drosatos.eu/android/")
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        FetchData sendRate = sendrate.create(FetchData.class);

        Call<List<Events>> UploadRate = sendRate.RateEvent(id, MainActivity.Token, events);


        UploadRate.enqueue(new Callback<List<Events>>() {
            @Override
            public void onResponse(Call<List<Events>> call, Response<List<Events>> response) {

                if (!response.isSuccessful()) {
                    Log.e("TAG", Integer.toString(response.code()));
                    return;
                }
                Log.e("TAG", response.toString());
            }

            @Override
            public void onFailure(Call<List<Events>> call, Throwable t) {
                Log.e("TAG", t.getMessage());
            }
        });
    }

    public void startcomments(View view) {
        startActivity(new Intent(EventDemo.this, Comments.class));
    }

    public void GetComments() {
        Gson gson = new GsonBuilder()
                .setLenient()
                .create();


        Retrofit fetch = new Retrofit.Builder()
                .baseUrl("https://api.e-events.drosatos.eu/android/")
                .addConverterFactory(GsonConverterFactory.create(gson))
                .build();

        FetchData fetchData = fetch.create(FetchData.class);

        Call<List<Events>> call = fetchData.GetEventComments(id, MainActivity.Token);


        call.enqueue(new Callback<List<Events>>() {
            @Override
            public void onResponse(Call<List<Events>> call, Response<List<Events>> response) {

                if (!response.isSuccessful()) {
                    Log.e("TAG", Integer.toString(response.code()));
                    return;
                }

                Log.e("TAG", Integer.toString(response.code()));
                Event_Comments.clear();
                Event_Users.clear();
                List<Events> events = response.body();
                int i=0;
                for (Events event : events) {
                    Event_Comments.add(event.getComment());
                    Event_Users.add(event.getUser_rated());
                    i++;
                }
            }

            @Override
            public void onFailure(Call<List<Events>> call, Throwable t) {
                Log.e("TAG", t.getMessage());
            }
        });
    }
}
