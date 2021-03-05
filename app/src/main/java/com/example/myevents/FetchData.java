package com.example.myevents;

import java.util.List;
import java.util.Map;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.DELETE;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.HeaderMap;
import retrofit2.http.Headers;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Path;
import retrofit2.http.Query;
import retrofit2.http.QueryMap;

public interface FetchData {
//5e83622f862c46101ac164c8
 //5e9230c2cc62be4369c30aba/1
   @GET("events") //Λήψη events
    Call<List<Events>> getData(@Header("x-access-token") String token);

   @GET("events/type/{Category}") //Λήψη events βάσει Category
    Call<List<Events>> getSelectedData(@Header("x-access-token") String token,@Path("Category") String cat);

    @GET("events/date/{Prev}/{Next}") //Λήψη events βάσει Date
    Call<List<Events>> getSelectedDataDate(@Header("x-access-token") String token,@Path("Prev") String prev,@Path("Next") String next);

    @GET("my_events") //Λήψη events
    Call<List<Events>> getMyEvent(@Header("x-access-token") String token);

    @Headers("Content-Type: application/json")
    @POST("users") //Εγγραφή user
    Call<List<User>> UploadUser(@Body User newUser);

    @DELETE("events/{id}") //Διαγραφή event
    Call<List<Events>> DeleteData(@Path("id") int Id, @Header("x-access-token") String token);

    @Headers("Content-Type: application/json")
    @POST("events/{id}") //Επεξεργασία Event
    Call<List<Events>> UpdateEvent(@Path("id") int Id,@Header("x-access-token") String token,@Body Events newEvent);

  @Headers("Content-Type: application/json")
  @POST("events") // Δημιουργία Event
  Call<List<Events>> UploadEvent(@Header("x-access-token") String token,@Body Events newEvent);

    @GET("login") // Για login και λήψη token
    Call<List<String>> GetToken(@Header("Authorization") String header);

    @Headers("Content-Type: application/json")
    @POST("events/{id}/rating") //Για Post Rate Event
    Call<List<Events>> RateEvent(@Path("id") int Id,@Header("x-access-token") String token,@Body Events newEvent);

    @Headers("Content-Type: application/json")
    @POST("backend_ai/photo") //Για Post Image
    Call<List<Events>> Uploadimage(@Header("x-access-token") String token,@Body Events Event);

    @GET("events/{id}/rating") //Για Rate event
    Call<List<Events>> GetEventComments(@Path("id") int Id,@Header("x-access-token") String token);
}