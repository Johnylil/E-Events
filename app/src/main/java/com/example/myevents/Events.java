package com.example.myevents;


public class Events {
    private String address;
    private String photo;
    private String description;
    private String title;
    private String date;
    private String latitude;
    private String longitude;
    private String category;
    private int event_id;
    private String time;
    private String rating;
    private String comment;
    private String Image;
    private float event_average_rating;
    private String user_rates,last_event_id;

    public Events(String address, String photo, String description,
                  String title, String date, String latitude, String longitude, String category, String time) {
        this.address = address;
        this.photo = photo;
        this.description = description;
        this.title = title;
        this.date = date;
        this.time = time;
        this.latitude = latitude;
        this.longitude = longitude;
        this.category = category;
    }

    public Events(String Image)
    {
        this.Image = Image;
    }


    public Events(String rating, String comment)
    {
        this.rating = rating;
        this.comment = comment;
    }


    public String getAddress() {
        return address;
    }

    public String getPhoto() {
        return photo;
    }

    public String getDescription() {
        return description;
    }

    public String getTitle() {
        return title;
    }

    public String getDate() {
        return date;
    }

    public String getLatitude() {
        return latitude;
    }

    public String getLongitude() {
        return longitude;
    }

    public String getCategory(){ return category;}

    public int getEvent_id(){return event_id;}

    public String getTime(){return time;}

    public String getRating(){return rating;}

    public String getComment(){return comment;}

    public float getAver_rating(){return event_average_rating;}

    public String getUser_rated() {return user_rates;}

    public String getImage(){return Image;}

    public String get_Event_notification(){return last_event_id;}
}
