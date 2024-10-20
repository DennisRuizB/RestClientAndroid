package edu.dsa.dennis.restclientandroid_v2;

import android.content.Intent;
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
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import edu.dsa.dennis.restclientandroid_v2.Track;
import edu.dsa.dennis.restclientandroid_v2.ApiService;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.util.ArrayList;
import java.util.List;

import okhttp3.OkHttpClient;
import retrofit2.Call;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.Callback;
import retrofit2.Response;
public class MainActivity extends AppCompatActivity {

    ArrayList<String> listDatos = new ArrayList<>();
    RecyclerView recycler;
    private ApiService apiService;
    private TextView responseTextView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        EdgeToEdge.enable(this);
        recycler = findViewById(R.id.RecyclerId);
        recycler.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
        EditText nameTrackText = findViewById(R.id.nameTrackText);
        EditText singerText = findViewById(R.id.singerText);

        // Configurar Retrofit
        OkHttpClient client = new OkHttpClient.Builder().build();
        Gson gson = new GsonBuilder().create();
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("http://10.0.2.2:8080/dsaApp/") // Cambia esto si es necesario
                .addConverterFactory(GsonConverterFactory.create(gson))
                .client(client)
                .build();

        apiService = retrofit.create(ApiService.class);
        Button getTracksButton = findViewById(R.id.getTracksButton);
        getTracksButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getTracks();
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

    private void getTracks() {
        apiService.getTracks().enqueue(new Callback<List<Track>>() {
            @Override
            public void onResponse(Call<List<Track>> call, Response<List<Track>> response) {
                listDatos.clear();
                if (response.isSuccessful() && response.body() != null) {
                    List<Track> tracks = response.body();
                    for (Track track : tracks) {
                        listDatos.add("id: " + track.getId() + "\n" + " Cantante: " + track.getSinger() + "\n" + " Cancion: " + track.getTitle() + "\n");
                    }
                    AdapterDatos adapter = new AdapterDatos(listDatos, new AdapterDatos.OnItemClickListener() {
                        @Override
                        public void onItemClick(Track track) {
                            // Abrir nueva actividad cuando se hace clic en un track
                            Intent intent = new Intent(MainActivity.this, DetalleTrackActivity.class);
                            intent.putExtra("id", track.getId());
                            intent.putExtra("title", track.getTitle());
                            intent.putExtra("singer", track.getSinger());
                            startActivity(intent);
                        }
                    });
                    recycler.setAdapter(adapter);
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


