package com.example.myevents;

public class User {
    private String username;
    private String password;
    private String email;
    private String gender;
    private String profile_photo;

    public User(String name, String passwrd, String email, String gender) {
       this.username = name;
        this.password = passwrd;
        this.email = email;
        this.gender = gender;
    }

    public User(String profile_photo)
    {
        this.profile_photo = profile_photo;
    }

    public String getName() {
        return username;
    }

    public String getPassword() {
        return password;
    }

    public String getEmail() {
        return email;
    }

    public String getGender() {
        return gender;
    }

    public String getProfile_photo(){return profile_photo;}
}
