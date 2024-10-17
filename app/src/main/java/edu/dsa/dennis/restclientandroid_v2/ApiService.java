package edu.dsa.dennis.restclientandroid_v2;
import edu.dsa.dennis.restclientandroid_v2.Track;


import java.util.List;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.DELETE;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Path;

public interface ApiService {
    @GET("tracks/")
    Call<List<Track>> getTracks();

    @GET("tracks/{id}")
    Call<Track> getTrack(@Path("id") String id);

    @POST("tracks/")
    Call<Track> addTrack(@Body Track track);

    @PUT("tracks/")
    Call<Track> updateTrack(@Body Track track);

    @DELETE("tracks/{id}")
    Call<Void> deleteTrack(@Path("id") String id);
}
