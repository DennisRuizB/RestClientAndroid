package edu.dsa.dennis.restclientandroid_v2;

import android.os.Bundle;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import edu.dsa.dennis.restclientandroid_v2.Track;
import edu.dsa.dennis.restclientandroid_v2.ApiService;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.util.List;

import okhttp3.OkHttpClient;
import retrofit2.Call;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.Callback;
import retrofit2.Response;
public class MainActivity extends AppCompatActivity {

    private ApiService apiService;
    private TextView responseTextView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        EdgeToEdge.enable(this);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
        EditText nameTrackText = findViewById(R.id.nameTrackText);
        EditText singerText = findViewById(R.id.singerText);
        EditText idText = findViewById(R.id.idText);
        responseTextView = findViewById(R.id.responseTextView);

        // Configurar Retrofit
        OkHttpClient client = new OkHttpClient.Builder().build();
        Gson gson = new GsonBuilder().create();
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("http://10.0.2.2:8080/dsaApp/") // Cambia esto si es necesario
                .addConverterFactory(GsonConverterFactory.create(gson))
                .client(client)
                .build();

        apiService = retrofit.create(ApiService.class);
//        String title = nameTrackText.getText().toString();
//        String singer = singerText.getText().toString();
//        Track track1 = new Track(title,singer);
//        Track track2 = new Track("nN332847860","NoCalmen","Kaze");
       // Track track3 = new Track("noCalma","Kaze");

        Button getTracksButton = findViewById(R.id.getTracksButton);
        getTracksButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getTracks();
            }
        });
        Button getTrackButton = findViewById(R.id.getTrackButton);
        getTrackButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getTrack(idText.getText().toString());
            }
        });
        Button deleteTrackButton = findViewById(R.id.deleteTrackButton);
        deleteTrackButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                deleteTrack(idText.getText().toString());
            }
        });
        Button addTrackButton = findViewById(R.id.addTrackButton);
        addTrackButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String title = nameTrackText.getText().toString();
                String singer = singerText.getText().toString();
                Track track1 = new Track(title,singer);
                addTrack(track1);
            }
        });
        Button updateTrackButton = findViewById(R.id.updateTrackButton);
        updateTrackButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String title = nameTrackText.getText().toString();
                String singer = singerText.getText().toString();
                String id = idText.getText().toString();
                Track track2 = new Track(id,title,singer);
                updateTrack(track2);
            }
        });


    }
    private void updateTrack(Track track)
    {
        apiService.updateTrack(track).enqueue(new Callback<Track>() {
            @Override
            public void onResponse(Call<Track> call, Response<Track> response) {
                Toast.makeText(MainActivity.this, "Track updated successfully", Toast.LENGTH_SHORT).show();
                // Aquí podrías actualizar la lista de tracks para reflejar la eliminación
                getTracks(); // Llamar a getTracks para refrescar la lista
            }

            @Override
            public void onFailure(Call<Track> call, Throwable t) {
                //Toast.makeText(MainActivity.this, "Error updating track", Toast.LENGTH_SHORT).show();
                getTracks();
            }
        });
    }
    private  void  addTrack(Track track){
        apiService.addTrack(track).enqueue(new Callback<Track>() {
            @Override
            public void onResponse(Call<Track> call, Response<Track> response) {
                Toast.makeText(MainActivity.this, "Track added successfully", Toast.LENGTH_SHORT).show();
                // Aquí podrías actualizar la lista de tracks para reflejar la eliminación
                getTracks(); // Llamar a getTracks para refrescar la lista
            }

            @Override
            public void onFailure(Call<Track> call, Throwable t) {
                Toast.makeText(MainActivity.this, "Error adding track", Toast.LENGTH_SHORT).show();
            }
        });
    }
    private void deleteTrack(String id){

        apiService.deleteTrack(id).enqueue(new Callback<Void>() {
            @Override
            public void onResponse(Call<Void> call, Response<Void> response) {
                Toast.makeText(MainActivity.this, "Track deleted successfully", Toast.LENGTH_SHORT).show();
                // Aquí podrías actualizar la lista de tracks para reflejar la eliminación
                getTracks(); // Llamar a getTracks para refrescar la lista
            }

            @Override
            public void onFailure(Call<Void> call, Throwable t) {
                Toast.makeText(MainActivity.this, "Error deleting track", Toast.LENGTH_SHORT).show();

            }
        });
    }
    private void getTrack(String id){
        apiService.getTrack(id).enqueue(new Callback<Track>() {
            @Override
            public void onResponse(Call<Track> call, Response<Track> response) {
                if (response.isSuccessful() && response.body() != null) {
                    Track track = response.body();
                    StringBuilder trackList = new StringBuilder();

                        trackList.append("ID: ").append(track.getId()).append(", Title: ")
                                .append(track.getTitle()).append(", Singer: ")
                                .append(track.getSinger()).append("\n");

                    responseTextView.setText(trackList.toString());
                } else {
                    Toast.makeText(MainActivity.this, "Error fetching tracks", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<Track> call, Throwable t) {
                Toast.makeText(MainActivity.this, t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }
    private void getTracks() {
        apiService.getTracks().enqueue(new Callback<List<Track>>() {
            @Override
            public void onResponse(Call<List<Track>> call, Response<List<Track>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    List<Track> tracks = response.body();
                    StringBuilder trackList = new StringBuilder();
                    for (Track track : tracks) {
                        trackList.append("ID: ").append(track.getId()).append(", Title: ")
                                .append(track.getTitle()).append(", Singer: ")
                                .append(track.getSinger()).append("\n");
                    }
                    responseTextView.setText(trackList.toString());
                } else {
                    Toast.makeText(MainActivity.this, "Error fetching tracks", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<List<Track>> call, Throwable t) {
                Toast.makeText(MainActivity.this, t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }
}


