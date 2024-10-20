package edu.dsa.dennis.restclientandroid_v2;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import okhttp3.OkHttpClient;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;


public class DetalleTrackActivity extends AppCompatActivity {
    private ApiService apiService;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detalle_track);

        // Configurar Retrofit
        OkHttpClient client = new OkHttpClient.Builder().build();
        Gson gson = new GsonBuilder().create();
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("http://10.0.2.2:8080/dsaApp/") // Cambia esto si es necesario
                .addConverterFactory(GsonConverterFactory.create(gson))
                .client(client)
                .build();
        apiService = retrofit.create(ApiService.class);
        EditText IdTitleText = findViewById(R.id.IdTitleText);
        EditText IdSingerText = findViewById(R.id.IdSingerText);
        TextView titleTextView = findViewById(R.id.titleTextView);
        TextView singerTextView = findViewById(R.id.singerTextView);

        // Obtener los datos del Intent
        String id = getIntent().getStringExtra("id");
        String title = getIntent().getStringExtra("title");
        String singer = getIntent().getStringExtra("singer");

        // Mostrar los datos
        //idTextView.setText("ID: " + id);
        titleTextView.setText("Title: ");
        IdTitleText.setText(title);
        singerTextView.setText("Singer:");
        IdSingerText.setText(singer);


        Button buttonVolver = findViewById(R.id.buttonVolver);
        buttonVolver.setOnClickListener(v -> {
            // Finalizar la actividad actual y volver a la MainActivity
            finish();
        });
       // Button deleteTrack = findViewById(R.id.buttonBorrar);
        Button deleteTrackButton = findViewById(R.id.buttonBorrar);
        deleteTrackButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                deleteTrack(id);
            }

        });

        Button updateTrackButton = findViewById(R.id.buttonUpdate);
        updateTrackButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Track track = new Track(id,IdTitleText.getText().toString(),IdSingerText.getText().toString());
                updateTrack(track);
            }

        });

    }
    private void deleteTrack(String id){
        apiService.deleteTrack(id).enqueue(new Callback<Void>() {
            @Override
            public void onResponse(Call<Void> call, Response<Void> response) {
                Toast.makeText(DetalleTrackActivity.this, "Track deleted successfully", Toast.LENGTH_SHORT).show();
                // Aquí podrías actualizar la lista de tracks para reflejar la eliminación
            }

            @Override
            public void onFailure(Call<Void> call, Throwable t) {
                Toast.makeText(DetalleTrackActivity.this, "Error deleting track", Toast.LENGTH_SHORT).show();
            }

        });

    }
    private void updateTrack(Track track)
    {
        apiService.updateTrack(track).enqueue(new Callback<Track>() {
            @Override
            public void onResponse(Call<Track> call, Response<Track> response) {
                Toast.makeText(DetalleTrackActivity.this, "Track updated successfully", Toast.LENGTH_SHORT).show();
                // Aquí podrías actualizar la lista de tracks para reflejar la eliminación

            }

            @Override
            public void onFailure(Call<Track> call, Throwable t) {
                Toast.makeText(DetalleTrackActivity.this, "Track updated successfully", Toast.LENGTH_SHORT).show();

                //Toast.makeText(MainActivity.this, "Error updating track", Toast.LENGTH_SHORT).show();

            }
        });
    }
}

